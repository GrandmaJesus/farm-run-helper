package com.farmrunhelper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.runelite.api.gameval.ItemID;

/**
 * Builds an import string for the Inventory Setups plugin from one trip.
 */
class SetupExporter
{
	private static final int INVENTORY_SIZE = 28;
	private static final int EQUIPMENT_SIZE = 14;
	private static final int BANK_COLUMNS = 8;
	private static final int ROWS = 7;

	// Where each equipment slot sits in the left half of the bank layout, laid out like the equipment screen
	private static final java.util.Map<Integer, Integer> EQUIPMENT_GRID = java.util.Map.ofEntries(
		java.util.Map.entry(1, 0), java.util.Map.entry(4, 1), java.util.Map.entry(5, 2), java.util.Map.entry(6, 13),
		java.util.Map.entry(8, 3), java.util.Map.entry(9, 4), java.util.Map.entry(10, 5), java.util.Map.entry(13, 7),
		java.util.Map.entry(16, 9), java.util.Map.entry(17, 10), java.util.Map.entry(18, 12));

	private static final int[] TOOL_ORDER = {
		ItemID.SPADE, ItemID.RAKE, ItemID.DIBBER, ItemID.FAIRY_ENCHANTED_SECATEURS, ItemID.SECATEURS,
		ItemID.BOTTOMLESS_COMPOST_BUCKET_FILLED, ItemID.DRAMEN_STAFF, ItemID.COINS, ItemID.POH_TABLET_TELEPORTTOHOUSE
	};

	private final Gson gson;

	SetupExporter(Gson gson)
	{
		this.gson = gson;
	}

	/**
	 * @return the import string, and whether some items had to be left out because they didn't fit
	 */
	ExportResult export(Route route, Trip trip)
	{
		List<Map.Entry<Integer, Integer>> ordered = new ArrayList<>(trip.getItems().entrySet());
		ordered.sort(Comparator.comparingInt(e -> priority(e.getKey())));
		List<int[]> slots = new ArrayList<>();
		for (Map.Entry<Integer, Integer> entry : ordered)
		{
			int id = entry.getKey();
			int quantity = entry.getValue();
			if (trip.isStackable(id))
			{
				slots.add(new int[]{id, quantity});
			}
			else
			{
				for (int i = 0; i < quantity; i++)
				{
					slots.add(new int[]{id, 1});
				}
			}
		}
		boolean truncated = slots.size() > INVENTORY_SIZE;

		JsonArray inventory = new JsonArray();
		int[] inventoryIds = new int[INVENTORY_SIZE];
		for (int i = 0; i < INVENTORY_SIZE; i++)
		{
			if (i < slots.size())
			{
				JsonObject item = new JsonObject();
				item.addProperty("id", slots.get(i)[0]);
				if (slots.get(i)[1] != 1)
				{
					item.addProperty("q", slots.get(i)[1]);
				}
				inventory.add(item);
				inventoryIds[i] = slots.get(i)[0];
			}
			else
			{
				inventory.add(JsonNull.INSTANCE);
				inventoryIds[i] = -1;
			}
		}

		JsonArray equipment = new JsonArray();
		for (int i = 0; i < EQUIPMENT_SIZE; i++)
		{
			Integer worn = trip.getWorn().get(i);
			if (worn != null)
			{
				JsonObject item = new JsonObject();
				item.addProperty("id", worn);
				equipment.add(item);
			}
			else
			{
				equipment.add(JsonNull.INSTANCE);
			}
		}

		StringBuilder notes = new StringBuilder();
		for (RouteStep step : route.getSteps())
		{
			if (step.getTripIndex() != trip.getIndex())
			{
				continue;
			}
			notes.append(step.getTitle()).append('\n');
			for (String line : step.getLines())
			{
				notes.append("  ").append(line).append('\n');
			}
		}

		JsonObject setup = new JsonObject();
		setup.add("inv", inventory);
		setup.add("eq", equipment);
		setup.addProperty("name", route.getRunType().getLabel() + " trip " + trip.getIndex() + "/" + route.getTrips().size());
		setup.addProperty("notes", notes.toString().trim());
		setup.addProperty("hc", "#FFFF0000");
		setup.addProperty("hd", true);
		setup.addProperty("fb", true);

		// Bank layout: left half mirrors the equipment screen (left empty), right half mirrors the inventory.
		JsonArray layout = new JsonArray();
		for (int row = 0; row < ROWS; row++)
		{
			for (int col = 0; col < BANK_COLUMNS; col++)
			{
				if (col < 4)
				{
					Integer worn = EQUIPMENT_GRID.get(row * 4 + col) == null ? null : trip.getWorn().get(EQUIPMENT_GRID.get(row * 4 + col));
					layout.add(worn != null ? worn : -1);
				}
				else
				{
					layout.add(inventoryIds[row * 4 + (col - 4)]);
				}
			}
		}

		JsonObject root = new JsonObject();
		root.add("setup", setup);
		root.add("layout", layout);
		return new ExportResult(gson.toJson(root), truncated);
	}

	/**
	 * Tools and teleports go first so the setup reads like a normal farm run inventory.
	 */
	private static int priority(int itemId)
	{
		for (int i = 0; i < TOOL_ORDER.length; i++)
		{
			if (TOOL_ORDER[i] == itemId)
			{
				return i;
			}
		}
		return TOOL_ORDER.length;
	}

	static class ExportResult
	{
		private final String json;
		private final boolean truncated;

		ExportResult(String json, boolean truncated)
		{
			this.json = json;
			this.truncated = truncated;
		}

		String getJson()
		{
			return json;
		}

		boolean isTruncated()
		{
			return truncated;
		}
	}
}

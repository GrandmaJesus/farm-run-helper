package com.farmrunhelper;

import java.util.HashMap;
import java.util.Map;
import net.runelite.api.gameval.ItemID;

/**
 * Runes for the teleport spells the routes use (rune costs from the OSRS Wiki).
 */
final class SpellRunes
{
	private static final Map<String, int[]> RUNES = new HashMap<>();

	static
	{
		// pairs of rune item id, amount
		RUNES.put("Camelot Teleport", new int[]{ItemID.AIRRUNE, 5, ItemID.LAWRUNE, 1});
		RUNES.put("Falador Teleport", new int[]{ItemID.AIRRUNE, 3, ItemID.WATERRUNE, 1, ItemID.LAWRUNE, 1});
		RUNES.put("Ardougne Teleport", new int[]{ItemID.WATERRUNE, 2, ItemID.LAWRUNE, 2});
		RUNES.put("Varrock Teleport", new int[]{ItemID.AIRRUNE, 3, ItemID.FIRERUNE, 1, ItemID.LAWRUNE, 1});
		RUNES.put("Lumbridge Teleport", new int[]{ItemID.AIRRUNE, 3, ItemID.EARTHRUNE, 1, ItemID.LAWRUNE, 1});
		RUNES.put("Trollheim Teleport", new int[]{ItemID.FIRERUNE, 2, ItemID.LAWRUNE, 2});
		RUNES.put("Civitas illa Fortis Teleport", new int[]{ItemID.EARTHRUNE, 1, ItemID.FIRERUNE, 1, ItemID.LAWRUNE, 2});
		RUNES.put("Kharyrll Teleport", new int[]{ItemID.BLOODRUNE, 1, ItemID.LAWRUNE, 2});
		RUNES.put("Catherby Teleport", new int[]{ItemID.WATERRUNE, 10, ItemID.ASTRALRUNE, 3, ItemID.LAWRUNE, 3});
		RUNES.put("Battlefront Teleport", new int[]{ItemID.EARTHRUNE, 1, ItemID.FIRERUNE, 1, ItemID.LAWRUNE, 1});
		RUNES.put("Draynor Manor Teleport", new int[]{ItemID.EARTHRUNE, 1, ItemID.WATERRUNE, 1, ItemID.LAWRUNE, 1});
		RUNES.put("Harmony Island Teleport", new int[]{ItemID.LAWRUNE, 1, ItemID.NATURERUNE, 1, ItemID.SOULRUNE, 1});
	}

	private SpellRunes()
	{
	}

	/**
	 * Rune id and amount pairs for a route that starts with a teleport spell, or null.
	 */
	static int[] forInstruction(String instruction)
	{
		for (Map.Entry<String, int[]> entry : RUNES.entrySet())
		{
			if (instruction.startsWith(entry.getKey()))
			{
				return entry.getValue();
			}
		}
		return null;
	}
}

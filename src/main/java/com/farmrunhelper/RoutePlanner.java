package com.farmrunhelper;

import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.runelite.api.gameval.ItemID;

/**
 * Turns the player's settings into an ordered list of steps, split into trips.
 */
class RoutePlanner
{
	private static final int COINS_PER_TREE = 200;
	private static final int SEEDS_PER_BIRD_HOUSE = 10;

	private final FarmRunHelperConfig config;
	private final int farmingLevel;
	private final int hunterLevel;
	private final Set<Integer> ownedItems;

	/**
	 * @param farmingLevel the player's real Farming level, or 0 if unknown (not logged in)
	 * @param hunterLevel  the player's real Hunter level, or 0 if unknown
	 * @param ownedItems   items seen in the bank, seed vault, inventory and equipment, or empty if never seen
	 */
	RoutePlanner(FarmRunHelperConfig config, int farmingLevel, int hunterLevel, Set<Integer> ownedItems)
	{
		this(config, farmingLevel, hunterLevel, ownedItems, Collections.emptyMap());
	}

	/**
	 * @param stillGrowing how many patches of each type at each stop are still growing (from Time Tracking),
	 *                     which are left out of the run so you don't bring seeds for them
	 */
	RoutePlanner(FarmRunHelperConfig config, int farmingLevel, int hunterLevel, Set<Integer> ownedItems,
		Map<FarmLocation, Map<PatchType, Integer>> stillGrowing)
	{
		this.config = config;
		this.farmingLevel = farmingLevel;
		this.hunterLevel = hunterLevel;
		this.ownedItems = ownedItems;
		this.stillGrowing = stillGrowing;
	}

	private final Map<FarmLocation, Map<PatchType, Integer>> stillGrowing;

	private RunType planning;

	private static final class PlannedStop
	{
		final FarmLocation location;
		final Map<PatchType, Integer> todo;
		final TeleportOption teleport;

		PlannedStop(FarmLocation location, Map<PatchType, Integer> todo, TeleportOption teleport)
		{
			this.location = location;
			this.todo = todo;
			this.teleport = teleport;
		}
	}

	private static boolean isFossilIsland(FarmLocation location)
	{
		return location == FarmLocation.FOSSIL_ISLAND || location == FarmLocation.BIRD_HOUSES || location == FarmLocation.SEAWEED;
	}

	Route plan(RunType runType)
	{
		planning = runType;
		List<RouteStep> steps = new ArrayList<>();
		List<Trip> trips = new ArrayList<>();
		List<String> warnings = new ArrayList<>();

		Set<PatchType> types = runType.patchTypes(config);
		Map<PatchType, SeedOption> seeds = new EnumMap<>(PatchType.class);
		for (PatchType type : types)
		{
			SeedOption seed = seedFor(type);
			if (seed == null)
			{
				continue;
			}
			if (type == PatchType.BIRDHOUSE && hunterLevel > 0 && hunterLevel < seed.getLevel())
			{
				warnings.add("Bird houses skipped: " + seed.getLabel() + " bird houses need " + seed.getLevel() + " Hunter.");
				continue;
			}
			if (type != PatchType.BIRDHOUSE && farmingLevel > 0 && farmingLevel < seed.getLevel())
			{
				warnings.add(type.getLabel() + " skipped: " + seed.getLabel() + " needs " + seed.getLevel() + " Farming.");
				continue;
			}
			seeds.put(type, seed);
		}

		Trip trip = startTrip(trips, steps, true);
		int capacity = config.tripCapacity();
		int leftOutGrowing = 0;

		List<PlannedStop> stops = new ArrayList<>();
		for (FarmLocation location : FarmLocation.values())
		{
			Map<PatchType, Integer> todo = new EnumMap<>(PatchType.class);
			for (Map.Entry<PatchType, Integer> patch : location.getPatches().entrySet())
			{
				PatchType type = patch.getKey();
				if (!seeds.containsKey(type))
				{
					continue;
				}
				if (location.isFarmingGuild() && farmingLevel > 0 && farmingLevel < type.getGuildLevel())
				{
					continue;
				}
				int growing = stillGrowing.getOrDefault(location, Collections.emptyMap()).getOrDefault(type, 0);
				if (growing > 0)
				{
					leftOutGrowing += Math.min(growing, patch.getValue());
				}
				if (patch.getValue() - growing > 0)
				{
					todo.put(type, patch.getValue() - growing);
				}
			}
			if (todo.isEmpty())
			{
				continue;
			}

			Req missing = firstUnmet(location.getRequirements());
			if (missing != null)
			{
				warnings.add(location.getDisplayName() + " skipped: needs " + missing.getDescription() + ".");
				continue;
			}

			TeleportOption teleport = resolveTeleport(location, warnings);
			if (teleport == null)
			{
				continue;
			}
			stops.add(new PlannedStop(location, todo, teleport));
		}

		// Do the second part of a split stop first when the teleport lands nearer to it
		for (int i = 0; i + 1 < stops.size(); i++)
		{
			if (StopParts.parentOf(stops.get(i + 1).location) == stops.get(i).location
				&& StopParts.secondPartFirst(stops.get(i).location, stops.get(i).teleport))
			{
				stops.add(i, stops.remove(i + 1));
				i++;
			}
		}

		// Two or more of the Fossil Island stops (hardwood, bird houses, seaweed) get a trip of their own,
		// banking before and after, so everything there is done in one go without leaving the island
		int fossilStops = 0;
		for (PlannedStop stop : stops)
		{
			fossilStops += isFossilIsland(stop.location) ? 1 : 0;
		}
		boolean fossilTrip = fossilStops >= 2;
		for (int i = 0; i < stops.size(); i++)
		{
			FarmLocation location = stops.get(i).location;
			Map<PatchType, Integer> todo = stops.get(i).todo;
			TeleportOption teleport = stops.get(i).teleport;
			boolean fossil = fossilTrip && isFossilIsland(location);
			boolean afterFossil = fossilTrip && i > 0 && isFossilIsland(stops.get(i - 1).location);
			if (trip.getSlotsUsed() > 0 && fossil != afterFossil)
			{
				// Starting or leaving the Fossil Island trip
				finishTrip(trip);
				trip = startTrip(trips, steps, false);
			}

			int teleportItem = pickTeleportItem(teleport);
			freeTravel = alreadyThere(steps, trip, location, teleport);
			int cost = slotCost(trip, todo, seeds, teleport, teleportItem);
			// The second part of a split stop stays in the same trip as the first, so it needs no teleport
			boolean followsOn = (freeTravel && i > 0 && StopParts.sameStop(stops.get(i - 1).location, location)) || (fossil && afterFossil);
			if (trip.getSlotsUsed() > 0 && trip.getSlotsUsed() + cost > capacity && !followsOn)
			{
				finishTrip(trip);
				trip = startTrip(trips, steps, false);
				freeTravel = false;
				cost = slotCost(trip, todo, seeds, teleport, teleportItem);
			}
			trip.addSlots(cost);
			steps.add(buildLocationStep(trip, location, todo, seeds, teleport, teleportItem));
		}

		finishTrip(trip);
		if (leftOutGrowing > 0)
		{
			warnings.add(leftOutGrowing + " patch" + (leftOutGrowing == 1 ? " is" : "es are")
				+ " left out because Time Tracking says they're still growing.");
		}

		RouteStep finish = new RouteStep(RouteStep.Kind.FINISH, trip.getIndex(), "Finish", null);
		finish.addLine(config.bankStop().getInstruction() + ".");
		steps.add(finish);

		if (steps.size() <= 2)
		{
			warnings.add("No patches match this run. Check the run contents, your unlocks and your seeds.");
		}
		return new Route(runType, steps, trips, warnings);
	}

	private Trip startTrip(List<Trip> trips, List<RouteStep> steps, boolean first)
	{
		Trip trip = new Trip(trips.size() + 1);
		trips.add(trip);
		RouteStep bank = new RouteStep(RouteStep.Kind.BANK, trip.getIndex(),
			first ? "Start: withdraw trip 1 items" : "Bank: withdraw trip " + trip.getIndex() + " items", null);
		if (!first)
		{
			bank.addLine(config.bankStop().getInstruction() + ".");
		}
		bank.addLine("Withdraw the items listed for this trip.");
		trip.setBankStep(bank);
		steps.add(bank);
		return trip;
	}

	private void finishTrip(Trip trip)
	{
		// The farmer's outfit only helps with patches: bird houses give Hunter experience
		Map<Integer, Integer> outfit = FarmerOutfit.owned(ownedItems);
		if (trip.farmsPatches() && !outfit.isEmpty())
		{
			outfit.forEach(trip::wear);
			trip.getBankStep().addLine("Wear your farmer's outfit for bonus Farming experience.");
		}

		Map<Integer, Integer> tools = new LinkedHashMap<>();
		if (trip.farmsPatches())
		{
			tools.put(ItemID.SPADE, 1);
			if (!config.autoweed())
			{
				tools.put(ItemID.RAKE, 1);
			}
		}
		if (trip.plantsSeeds())
		{
			tools.put(ItemID.DIBBER, 1);
		}
		if (trip.needsSecateurs())
		{
			if (config.secateurs() == SecateursChoice.MAGIC)
			{
				tools.put(ItemID.FAIRY_ENCHANTED_SECATEURS, 1);
			}
			else if (config.secateurs() == SecateursChoice.NORMAL)
			{
				tools.put(ItemID.SECATEURS, 1);
			}
		}
		CompostChoice compost = config.compost();
		if (trip.getPatchesToCompost() > 0 && compost != CompostChoice.NONE)
		{
			tools.put(compost.getItemId(), compost.isOnePerPatch() ? trip.getPatchesToCompost() : 1);
		}
		if (trip.usesFairyRing() && config.fairyRingAccess() == FairyRingAccess.DRAMEN_STAFF)
		{
			tools.put(ItemID.DRAMEN_STAFF, 1);
		}
		for (Map.Entry<Integer, Integer> tool : tools.entrySet())
		{
			trip.add(tool.getKey(), tool.getValue(), false);
		}
		if (trip.getHouseTeleports() > 0)
		{
			trip.add(ItemID.POH_TABLET_TELEPORTTOHOUSE, trip.getHouseTeleports() + 1, true);
		}
	}

	private boolean reqsMet(TeleportOption option)
	{
		for (Req req : option.getRequirements())
		{
			if (!req.isMet(config))
			{
				return false;
			}
		}
		return true;
	}

	// Set while planning a stop you'll already be at: no teleport needed
	private boolean freeTravel;

	/**
	 * Whether the stop before this one in the same trip leaves you where this route's teleport would go,
	 * like the Fossil Island bird houses straight after the hardwood patches.
	 */
	private static boolean alreadyThere(List<RouteStep> steps, Trip trip, FarmLocation location, TeleportOption teleport)
	{
		if (steps.isEmpty())
		{
			return false;
		}
		RouteStep last = steps.get(steps.size() - 1);
		if (last.getKind() == RouteStep.Kind.LOCATION && last.getTripIndex() == trip.getIndex()
			&& StopParts.sameStop(last.getLocation(), location))
		{
			// Straight after the other part of the same stop
			return true;
		}
		if (!teleport.getInstruction().contains("(skip if already there)"))
		{
			return false;
		}
		RouteStep previous = steps.get(steps.size() - 1);
		if (previous.getKind() != RouteStep.Kind.LOCATION || previous.getTripIndex() != trip.getIndex() || previous.getLocation() == null)
		{
			return false;
		}
		// Straight after the first part of the same stop
		for (int region : previous.getLocation().getRegionIds())
		{
			if (location.containsRegion(region))
			{
				return true;
			}
		}
		for (int region : previous.getLocation().getRegionIds())
		{
			if (FarmLocation.FOSSIL_ISLAND.containsRegion(region))
			{
				return true;
			}
		}
		return false;
	}

	private RouteStep buildLocationStep(Trip trip, FarmLocation location, Map<PatchType, Integer> todo,
		Map<PatchType, SeedOption> seeds, TeleportOption teleport, int teleportItem)
	{
		RouteStep step = new RouteStep(RouteStep.Kind.LOCATION, trip.getIndex(), location.getDisplayName(), location);
		step.addLine("Travel: " + (freeTravel && StopParts.parentOf(location) == null && StopParts.secondPartFirst(location, teleport)
			? StopParts.walkBack(location) : travelText(teleport)));
		step.setTravel(teleport);
		if (!freeTravel && !TravelKind.usesHouseObject(teleport) && TravelKind.usesHouse(teleport, config))
		{
			step.addLine("If you aren't near one, teleport to your house and use its "
				+ (config.houseSpiritTree() && TravelKind.usesSpiritTree(teleport) ? "spirit tree." : "fairy ring."));
		}

		boolean onlyBirdHouses = todo.size() == 1 && todo.containsKey(PatchType.BIRDHOUSE);
		boolean single = todo.size() == 1;
		StringBuilder patches = new StringBuilder(onlyBirdHouses ? ""
			: single ? "Plant: " : "Plant (nearest first): ");
		int order = 0;
		for (Map.Entry<PatchType, Integer> entry : todo.entrySet())
		{
			PatchType type = entry.getKey();
			int count = entry.getValue();
			SeedOption seed = seeds.get(type);
			step.addPatchType(type);
			step.addSeedItem(type, seed.getItemId(), seedName(type, seed));
			if (type == PatchType.BIRDHOUSE)
			{
				step.addSeedItem(type, config.birdHouseSeed().getItemId(), seedName(type, seed));
			}
			if (order > 0)
			{
				patches.append(", ");
			}
			order++;
			if (type == PatchType.BIRDHOUSE)
			{
				// Bird houses are built and filled with seeds, not planted
				String house = seedName(type, seed).toLowerCase() + (count == 1 ? "" : "s");
				String text = "set up " + count + " " + house + " with " + config.birdHouseSeed().getLabel().toLowerCase() + " seeds";
				patches.append(onlyBirdHouses ? Character.toUpperCase(text.charAt(0)) + text.substring(1) + "." : text);
			}
			else
			{
				patches.append(type.getLabel().toLowerCase()).append(" x").append(count)
					.append(" (").append(seed.getLabel()).append(")");
			}
			addPatchItems(trip, type, count, seed);
		}
		step.addLine(patches.toString());
		if (config.compost() != CompostChoice.NONE && todo.keySet().stream().anyMatch(PatchType::takesCompost))
		{
			step.addLine("After planting each patch, use your " + config.compost().toString().toLowerCase() + " on it."
				+ (todo.containsKey(PatchType.ANIMA) ? " Not the anima plant: it can't take compost." : ""));
		}
		if (!location.getExtraNote().isEmpty())
		{
			step.addLine(location.getExtraNote());
		}

		addTravelItems(trip, teleport, teleportItem);
		return step;
	}

	private void addTravelItems(Trip trip, TeleportOption teleport, int teleportItem)
	{
		if (teleportItem > 0 && !freeTravel && !tripHasAny(trip, teleport.getItemIds()))
		{
			trip.add(teleportItem, 1, false);
		}
		if (TravelKind.usesHouse(teleport, config) && !freeTravel)
		{
			trip.addHouseTeleport();
		}
		int[] runes = freeTravel ? null : SpellRunes.forInstruction(teleport.getInstruction());
		if (runes != null)
		{
			for (int i = 0; i < runes.length; i += 2)
			{
				trip.add(runes[i], runes[i + 1], true);
			}
		}
		if (requires(teleport, Req.FAIRY_RING))
		{
			trip.setUsesFairyRing();
		}
	}

	private void addPatchItems(Trip trip, PatchType type, int count, SeedOption seed)
	{
		if (type != PatchType.BIRDHOUSE)
		{
			trip.setFarmsPatches();
		}
		if (type == PatchType.CELASTRUS && !trip.contains(ItemID.BRONZE_AXE))
		{
			// Harvesting celastrus bark needs an axe; any axe counts
			trip.add(ItemID.BRONZE_AXE, 1, false);
		}
		if (type == PatchType.BELLADONNA && !trip.contains(ItemID.LEATHER_GLOVES))
		{
			// Picking nightshade needs gloves; any worn gloves count
			trip.add(ItemID.LEATHER_GLOVES, 1, false);
		}
		switch (type)
		{
			case BIRDHOUSE:
				trip.add(seed.getItemId(), count, false);
				trip.add(config.birdHouseSeed().getItemId(), count * SEEDS_PER_BIRD_HOUSE, true);
				return;
			case SEAWEED:
				trip.add(seed.getItemId(), count, true);
				if (!trip.contains(ItemID.HUNDRED_PIRATE_DIVING_HELMET))
				{
					trip.add(ItemID.HUNDRED_PIRATE_DIVING_HELMET, 1, false);
					trip.add(ItemID.HUNDRED_PIRATE_DIVING_BACKPACK, 1, false);
				}
				trip.setPlantsSeeds();
				trip.addPatchesToCompost(count);
				return;
			default:
				break;
		}

		if (type.isTree())
		{
			trip.add(seed.getItemId(), count, false);
			trip.add(ItemID.COINS, count * COINS_PER_TREE, true);
		}
		else
		{
			trip.add(seed.getItemId(), count * type.getSeedsPerPatch(), true);
			trip.setPlantsSeeds();
		}
		if (type == PatchType.HERB || type == PatchType.ALLOTMENT)
		{
			trip.setNeedsSecateurs();
		}
		if (type.takesCompost())
		{
			trip.addPatchesToCompost(count);
		}
	}

	/**
	 * Inventory slots this location adds to the trip, not counting tools.
	 */
	private int slotCost(Trip trip, Map<PatchType, Integer> todo, Map<PatchType, SeedOption> seeds,
		TeleportOption teleport, int teleportItem)
	{
		int slots = 0;
		for (Map.Entry<PatchType, Integer> entry : todo.entrySet())
		{
			PatchType type = entry.getKey();
			int count = entry.getValue();
			int itemId = seeds.get(type).getItemId();
			if (type.isTree() || type == PatchType.BIRDHOUSE)
			{
				slots += count;
				if (type.isTree() && !trip.contains(ItemID.COINS))
				{
					slots++;
				}
				if (type == PatchType.BIRDHOUSE && !trip.contains(config.birdHouseSeed().getItemId()))
				{
					slots++;
				}
			}
			else if (!trip.contains(itemId))
			{
				slots++;
			}
			if (type == PatchType.SEAWEED && !trip.contains(ItemID.HUNDRED_PIRATE_DIVING_HELMET))
			{
				slots += 2;
			}
			if (type.takesCompost() && config.compost().isOnePerPatch())
			{
				// Buckets of compost don't stack, so each patch needs its own slot.
				slots += count;
			}
		}
		if (teleportItem > 0 && !freeTravel && !tripHasAny(trip, teleport.getItemIds()))
		{
			slots++;
		}
		if (TravelKind.usesHouse(teleport, config) && !freeTravel && trip.getHouseTeleports() == 0)
		{
			slots++;
		}
		int[] runes = freeTravel ? null : SpellRunes.forInstruction(teleport.getInstruction());
		if (runes != null)
		{
			for (int i = 0; i < runes.length; i += 2)
			{
				slots += trip.contains(runes[i]) ? 0 : 1;
			}
		}
		return slots;
	}

	private TeleportOption resolveTeleport(FarmLocation location, List<String> warnings)
	{
		TeleportOption selected = location.getSelectedTeleport(config);
		FarmLocation parent = StopParts.parentOf(location);
		if (parent != null)
		{
			// The second part of a split stop goes the same way as the first
			TeleportOption chosen = parent.getSelectedTeleport(config);
			selected = chosen;
			if (chosen != null && !chosen.isAuto() && !chosen.isSkip())
			{
				for (TeleportOption option : location.getTeleportOptions())
				{
					if (option.name().equals(chosen.name()))
					{
						selected = option;
					}
				}
			}
		}
		if (selected == null || selected.isSkip())
		{
			return null;
		}
		if (!selected.isAuto())
		{
			Req missing = firstUnmet(selected.getRequirements());
			if (missing != null)
			{
				warnings.add(location.getDisplayName() + ": " + selected.getLabel() + " needs "
					+ missing.getDescription() + " (check your settings).");
			}
			return selected;
		}

		TeleportOption fallback = null;
		for (TeleportOption option : location.getTeleportOptions())
		{
			if (option.isAuto() || option.isSkip() || firstUnmet(option.getRequirements()) != null)
			{
				continue;
			}
			if (option.getItemIds().length == 0 || ownedItems.isEmpty() || ownsAny(option.getItemIds()))
			{
				return option;
			}
			if (fallback == null)
			{
				fallback = option;
			}
		}
		if (fallback != null)
		{
			return fallback;
		}
		for (TeleportOption option : location.getTeleportOptions())
		{
			if (!option.isAuto() && !option.isSkip())
			{
				warnings.add(location.getDisplayName() + ": no route is unlocked in your settings, so "
					+ option.getLabel() + " is shown. You can change it in Teleport settings.");
				return option;
			}
		}
		return null;
	}

	private int pickTeleportItem(TeleportOption teleport)
	{
		int[] ids = teleport.getItemIds();
		if (ids.length == 0)
		{
			return -1;
		}
		for (int id : ids)
		{
			if (ownedItems.contains(id))
			{
				return id;
			}
		}
		return ids[0];
	}

	private boolean ownsAny(int[] ids)
	{
		for (int id : ids)
		{
			if (ownedItems.contains(id))
			{
				return true;
			}
		}
		return false;
	}

	private static boolean tripHasAny(Trip trip, int[] ids)
	{
		for (int id : ids)
		{
			if (trip.contains(id))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * The route's instruction, saying "portal nexus" for house teleports you've set to use the nexus.
	 */
	private String travelText(TeleportOption teleport)
	{
		String text = teleport.getInstruction();
		for (Req req : teleport.getRequirements())
		{
			if (req.name().startsWith("POH_PORTAL_") && FarmRunHelperPlugin.housePortal(config, req) == HousePortal.NEXUS)
			{
				return text.replaceFirst("House -> (.+?) portal", "House -> portal nexus -> $1");
			}
		}
		return text;
	}

	private static boolean requires(TeleportOption teleport, Req req)
	{
		for (Req r : teleport.getRequirements())
		{
			if (r == req)
			{
				return true;
			}
		}
		return false;
	}

	private Req firstUnmet(Req[] reqs)
	{
		for (Req req : reqs)
		{
			if (!req.isMet(config))
			{
				return req;
			}
		}
		return null;
	}

	private static String seedName(PatchType type, SeedOption seed)
	{
		if (type == PatchType.BIRDHOUSE)
		{
			return seed.getLabel().toLowerCase().contains("bird house") ? seed.getLabel() : seed.getLabel() + " bird house";
		}
		if (seed instanceof Seeds.Fixed)
		{
			return seed.getLabel();
		}
		return seed.getLabel() + (type.isTree() ? " sapling" : " seed");
	}

	private SeedOption seedFor(PatchType type)
	{
		switch (type)
		{
			case HERB:
				return config.herbSeed();
			case FLOWER:
				return config.flowerSeed();
			case ALLOTMENT:
				return config.allotmentSeed();
			case BUSH:
				return config.bushSeed();
			case CACTUS:
				return config.cactusSeed();
			case TREE:
				return config.treeSapling();
			case FRUIT_TREE:
				return config.fruitSapling();
			case HARDWOOD:
				return config.hardwoodSapling();
			case ANIMA:
				return config.animaSeed();
			case BIRDHOUSE:
				return config.birdHouse();
			case BELLADONNA:
				return Seeds.Fixed.BELLADONNA;
			case MUSHROOM:
				return Seeds.Fixed.MUSHROOM;
			case SEAWEED:
				return Seeds.Fixed.SEAWEED;
			case CALQUAT:
				return Seeds.Fixed.CALQUAT;
			case CELASTRUS:
				return Seeds.Fixed.CELASTRUS;
			case REDWOOD:
				return Seeds.Fixed.REDWOOD;
			case HESPORI:
				return Seeds.Fixed.HESPORI;
			default:
				return null;
		}
	}
}

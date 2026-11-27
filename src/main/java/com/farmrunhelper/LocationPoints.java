package com.farmrunhelper;

import java.util.EnumMap;
import java.util.Map;
import net.runelite.api.coords.WorldPoint;

/**
 * A walkable tile at each farming stop, and where every patch there is.
 * Most come from Quest Helper's herb and tree runs; stops without one use the middle of their map area.
 */
final class LocationPoints
{
	private static final Map<FarmLocation, WorldPoint> POINTS = new EnumMap<>(FarmLocation.class);

	static
	{
		POINTS.put(FarmLocation.FARMING_GUILD, new WorldPoint(1238, 3726, 0));
		POINTS.put(FarmLocation.ORTUS_FARM, new WorldPoint(1582, 3094, 0));
		POINTS.put(FarmLocation.AUBURNVALE, new WorldPoint(1367, 3322, 0));
		POINTS.put(FarmLocation.KASTORI, new WorldPoint(1350, 3057, 0));
		POINTS.put(FarmLocation.LOCUS_OASIS, new WorldPoint(1687, 2972, 0));
		POINTS.put(FarmLocation.HOSIDIUS, new WorldPoint(1738, 3550, 0));
		POINTS.put(FarmLocation.GREAT_CONCH, new WorldPoint(3129, 2406, 0));
		POINTS.put(FarmLocation.TAVERLEY, new WorldPoint(2936, 3438, 0));
		POINTS.put(FarmLocation.GNOME_STRONGHOLD, new WorldPoint(2475, 3445, 0));
		POINTS.put(FarmLocation.GNOME_STRONGHOLD_TREE, new WorldPoint(2436, 3415, 0));
		POINTS.put(FarmLocation.CATHERBY_FRUIT, new WorldPoint(2860, 3433, 0));
		POINTS.put(FarmLocation.AUBURNVALE_BELLADONNA, new WorldPoint(1450, 3354, 0));
		POINTS.put(FarmLocation.TREE_GNOME_VILLAGE, new WorldPoint(2490, 3180, 0));
		POINTS.put(FarmLocation.LLETYA, new WorldPoint(2347, 3162, 0));
		POINTS.put(FarmLocation.CATHERBY, new WorldPoint(2813, 3463, 0));
		POINTS.put(FarmLocation.ARDOUGNE_FARM, new WorldPoint(2670, 3374, 0));
		POINTS.put(FarmLocation.TROLL_STRONGHOLD, new WorldPoint(2826, 3694, 0));
		POINTS.put(FarmLocation.WEISS, new WorldPoint(2848, 3934, 0));
		POINTS.put(FarmLocation.FALADOR_FARM, new WorldPoint(3058, 3311, 0));
		POINTS.put(FarmLocation.FALADOR_TREE, new WorldPoint(3004, 3373, 0));
		POINTS.put(FarmLocation.LUMBRIDGE, new WorldPoint(3193, 3231, 0));
		POINTS.put(FarmLocation.VARROCK, new WorldPoint(3229, 3459, 0));
		POINTS.put(FarmLocation.PORT_PHASMATYS, new WorldPoint(3605, 3529, 0));
		POINTS.put(FarmLocation.HARMONY, new WorldPoint(3797, 2838, 0));
		POINTS.put(FarmLocation.BRIMHAVEN, new WorldPoint(2765, 3213, 0));
		POINTS.put(FarmLocation.TAI_BWO_WANNAI, new WorldPoint(2795, 3102, 0));
		POINTS.put(FarmLocation.FOSSIL_ISLAND, new WorldPoint(3708, 3833, 0));
		POINTS.put(FarmLocation.ANGLERS_RETREAT, new WorldPoint(2470, 2704, 0));
		// The tool leprechaun's spot next to the patch, or the patch itself
		POINTS.put(FarmLocation.ARDOUGNE_BUSH, new WorldPoint(2613, 3226, 0));
		POINTS.put(FarmLocation.RIMMINGTON, new WorldPoint(2940, 3225, 0));
		POINTS.put(FarmLocation.DRAYNOR_MANOR, new WorldPoint(3086, 3354, 0));
		POINTS.put(FarmLocation.AL_KHARID, new WorldPoint(3319, 3204, 0));
		POINTS.put(FarmLocation.CHAMPIONS_GUILD, new WorldPoint(3181, 3353, 0));
		POINTS.put(FarmLocation.CANIFIS_MUSHROOM, new WorldPoint(3451, 3477, 0));
		POINTS.put(FarmLocation.ETCETERIA, new WorldPoint(2591, 3863, 0));
		POINTS.put(FarmLocation.PRIFDDINAS, new WorldPoint(3295, 6105, 0));
	}

	private LocationPoints()
	{
	}

	/**
	 * The known spot for a stop, or null if there isn't one (the plugin learns those on a visit).
	 */
	static WorldPoint known(FarmLocation location)
	{
		return POINTS.get(location);
	}

	static WorldPoint of(FarmLocation location)
	{
		WorldPoint point = POINTS.get(location);
		if (point != null)
		{
			return point;
		}
		// Middle of the stop's first map region
		int region = location.getRegionIds()[0];
		return new WorldPoint(((region >> 8) << 6) + 32, ((region & 0xFF) << 6) + 32, 0);
	}

	/**
	 * Where a patch is: its type and one tile of it. Bird house spaces are in {@link BirdHouses}.
	 */
	static final class Spot
	{
		final PatchType type;
		final WorldPoint point;

		Spot(PatchType type, WorldPoint point)
		{
			this.type = type;
			this.point = point;
		}
	}

	// Every patch at every stop, from survey runs
	private static final Map<FarmLocation, java.util.List<Spot>> SPOTS = new EnumMap<>(FarmLocation.class);

	static
	{
		spot(FarmLocation.FARMING_GUILD, PatchType.TREE, 1232, 3736, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.HERB, 1238, 3726, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.BUSH, 1260, 3733, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.FLOWER, 1260, 3725, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.ALLOTMENT, 1267, 3723, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.ALLOTMENT, 1267, 3732, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.CACTUS, 1264, 3747, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.FRUIT_TREE, 1242, 3758, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.ANIMA, 1232, 3723, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.CELASTRUS, 1244, 3750, 0);
		spot(FarmLocation.FARMING_GUILD, PatchType.REDWOOD, 1226, 3752, 0);
		spot(FarmLocation.ORTUS_FARM, PatchType.HERB, 1581, 3094, 0);
		spot(FarmLocation.ORTUS_FARM, PatchType.FLOWER, 1585, 3098, 0);
		spot(FarmLocation.ORTUS_FARM, PatchType.ALLOTMENT, 1581, 3098, 0);
		spot(FarmLocation.ORTUS_FARM, PatchType.ALLOTMENT, 1581, 3103, 0);
		spot(FarmLocation.AUBURNVALE, PatchType.TREE, 1366, 3321, 0);
		spot(FarmLocation.AUBURNVALE_BELLADONNA, PatchType.BELLADONNA, 1450, 3354, 0);
		spot(FarmLocation.KASTORI, PatchType.CALQUAT, 1367, 3032, 0);
		spot(FarmLocation.KASTORI, PatchType.FRUIT_TREE, 1349, 3056, 0);
		spot(FarmLocation.KASTORI, PatchType.FLOWER, 1352, 3022, 0);
		spot(FarmLocation.LOCUS_OASIS, PatchType.HARDWOOD, 1687, 2972, 0);
		spot(FarmLocation.HOSIDIUS, PatchType.HERB, 1738, 3550, 0);
		spot(FarmLocation.HOSIDIUS, PatchType.FLOWER, 1734, 3554, 0);
		spot(FarmLocation.HOSIDIUS, PatchType.ALLOTMENT, 1730, 3550, 0);
		spot(FarmLocation.HOSIDIUS, PatchType.ALLOTMENT, 1730, 3555, 0);
		spot(FarmLocation.GREAT_CONCH, PatchType.CALQUAT, 3128, 2405, 0);
		spot(FarmLocation.TAVERLEY, PatchType.TREE, 2936, 3438, 0);
		spot(FarmLocation.GNOME_STRONGHOLD_TREE, PatchType.TREE, 2436, 3415, 0);
		spot(FarmLocation.GNOME_STRONGHOLD, PatchType.FRUIT_TREE, 2475, 3445, 0);
		spot(FarmLocation.TREE_GNOME_VILLAGE, PatchType.FRUIT_TREE, 2489, 3179, 0);
		spot(FarmLocation.PRIFDDINAS, PatchType.ALLOTMENT, 3288, 6095, 0);
		spot(FarmLocation.PRIFDDINAS, PatchType.ALLOTMENT, 3288, 6101, 0);
		spot(FarmLocation.PRIFDDINAS, PatchType.FLOWER, 3292, 6099, 0);
		spot(FarmLocation.LLETYA, PatchType.FRUIT_TREE, 2346, 3161, 0);
		spot(FarmLocation.CATHERBY, PatchType.HERB, 2813, 3463, 0);
		spot(FarmLocation.CATHERBY, PatchType.FLOWER, 2809, 3463, 0);
		spot(FarmLocation.CATHERBY, PatchType.ALLOTMENT, 2805, 3459, 0);
		spot(FarmLocation.CATHERBY, PatchType.ALLOTMENT, 2805, 3466, 0);
		spot(FarmLocation.CATHERBY_FRUIT, PatchType.FRUIT_TREE, 2860, 3433, 0);
		spot(FarmLocation.ARDOUGNE_FARM, PatchType.HERB, 2670, 3374, 0);
		spot(FarmLocation.ARDOUGNE_FARM, PatchType.FLOWER, 2666, 3374, 0);
		spot(FarmLocation.ARDOUGNE_FARM, PatchType.ALLOTMENT, 2662, 3370, 0);
		spot(FarmLocation.ARDOUGNE_FARM, PatchType.ALLOTMENT, 2662, 3377, 0);
		spot(FarmLocation.ARDOUGNE_BUSH, PatchType.BUSH, 2617, 3225, 0);
		spot(FarmLocation.TROLL_STRONGHOLD, PatchType.HERB, 2826, 3694, 0);
		spot(FarmLocation.WEISS, PatchType.HERB, 2848, 3934, 0);
		spot(FarmLocation.ETCETERIA, PatchType.BUSH, 2591, 3863, 0);
		spot(FarmLocation.FALADOR_FARM, PatchType.HERB, 3058, 3311, 0);
		spot(FarmLocation.FALADOR_FARM, PatchType.FLOWER, 3054, 3307, 0);
		spot(FarmLocation.FALADOR_FARM, PatchType.ALLOTMENT, 3055, 3303, 0);
		spot(FarmLocation.FALADOR_FARM, PatchType.ALLOTMENT, 3050, 3307, 0);
		spot(FarmLocation.FALADOR_TREE, PatchType.TREE, 3004, 3373, 0);
		spot(FarmLocation.RIMMINGTON, PatchType.BUSH, 2940, 3221, 0);
		spot(FarmLocation.DRAYNOR_MANOR, PatchType.BELLADONNA, 3086, 3354, 0);
		spot(FarmLocation.LUMBRIDGE, PatchType.TREE, 3193, 3231, 0);
		spot(FarmLocation.AL_KHARID, PatchType.CACTUS, 3315, 3202, 0);
		spot(FarmLocation.CHAMPIONS_GUILD, PatchType.BUSH, 3181, 3357, 0);
		spot(FarmLocation.VARROCK, PatchType.TREE, 3229, 3459, 0);
		spot(FarmLocation.PORT_PHASMATYS, PatchType.HERB, 3605, 3529, 0);
		spot(FarmLocation.PORT_PHASMATYS, PatchType.FLOWER, 3601, 3525, 0);
		spot(FarmLocation.PORT_PHASMATYS, PatchType.ALLOTMENT, 3602, 3521, 0);
		spot(FarmLocation.PORT_PHASMATYS, PatchType.ALLOTMENT, 3597, 3525, 0);
		spot(FarmLocation.CANIFIS_MUSHROOM, PatchType.MUSHROOM, 3451, 3472, 0);
		spot(FarmLocation.HARMONY, PatchType.HERB, 3789, 2837, 0);
		spot(FarmLocation.HARMONY, PatchType.ALLOTMENT, 3794, 2833, 0);
		spot(FarmLocation.BRIMHAVEN, PatchType.FRUIT_TREE, 2764, 3212, 0);
		spot(FarmLocation.TAI_BWO_WANNAI, PatchType.CALQUAT, 2796, 3101, 0);
		spot(FarmLocation.FOSSIL_ISLAND, PatchType.HARDWOOD, 3715, 3835, 0);
		spot(FarmLocation.FOSSIL_ISLAND, PatchType.HARDWOOD, 3702, 3837, 0);
		spot(FarmLocation.FOSSIL_ISLAND, PatchType.HARDWOOD, 3708, 3833, 0);
		spot(FarmLocation.SEAWEED, PatchType.SEAWEED, 3733, 10267, 1);
		spot(FarmLocation.SEAWEED, PatchType.SEAWEED, 3733, 10273, 1);
		spot(FarmLocation.ANGLERS_RETREAT, PatchType.HARDWOOD, 2471, 2703, 0);
	}

	private static void spot(FarmLocation location, PatchType type, int x, int y, int plane)
	{
		SPOTS.computeIfAbsent(location, l -> new java.util.ArrayList<>()).add(new Spot(type, new WorldPoint(x, y, plane)));
	}

	/**
	 * Where each patch at a stop is.
	 */
	static java.util.List<Spot> spots(FarmLocation location)
	{
		return SPOTS.getOrDefault(location, java.util.Collections.emptyList());
	}
}

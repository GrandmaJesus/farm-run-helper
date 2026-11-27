package com.farmrunhelper;

import net.runelite.api.gameval.VarPlayerID;

/**
 * The four bird house spaces on Fossil Island, in the order to do them, read the same way as
 * RuneLite's Time Tracking plugin: each space's value is 0 when empty, a multiple of 3 once seeded,
 * and anything else when built but not seeded yet.
 */
final class BirdHouses
{
	// Verdant Valley first (the first mushtree stop from the House on the Hill), then Mushroom Meadow.
	// Spaces in the part of the island you're already in come first, nearest first
	static final int[] SPACES = {
		VarPlayerID.BIRDHOUSE_TRANSMIT_C,
		VarPlayerID.BIRDHOUSE_TRANSMIT_D,
		VarPlayerID.BIRDHOUSE_TRANSMIT_A,
		VarPlayerID.BIRDHOUSE_TRANSMIT_B,
	};

	static final long FILL_MILLIS = 50L * 60_000L;

	private BirdHouses()
	{
	}

	static boolean isSpace(int varp)
	{
		for (int space : SPACES)
		{
			if (space == varp)
			{
				return true;
			}
		}
		return false;
	}

	static boolean isSeeded(int value)
	{
		return value > 0 && value % 3 == 0;
	}

	/**
	 * How to walk to a space from the other one in its area.
	 */
	static String walkHint(int varp)
	{
		if (varp == VarPlayerID.BIRDHOUSE_TRANSMIT_B)
		{
			return "walk south to the bird house at the Tar Swamp entrance";
		}
		if (varp == VarPlayerID.BIRDHOUSE_TRANSMIT_A)
		{
			return "walk north to the bird house in the north of the Mushroom Forest";
		}
		return "walk to the other bird house in Verdant Valley";
	}

	// Rough spots of each space, used until the plugin has seen the real one
	private static final int[][] DEFAULT_SPOTS = {{3768, 3761}, {3763, 3755}, {3677, 3882}, {3679, 3815}};

	static net.runelite.api.coords.WorldPoint defaultSpot(int varp)
	{
		for (int i = 0; i < SPACES.length; i++)
		{
			if (SPACES[i] == varp)
			{
				return new net.runelite.api.coords.WorldPoint(DEFAULT_SPOTS[i][0], DEFAULT_SPOTS[i][1], 0);
			}
		}
		return null;
	}

	/**
	 * Which magic mushtree's part of Fossil Island a spot is in: "Mushroom Meadow" (the Mushroom Forest,
	 * the hardwood patches, the Tar Swamp entrance and the rowboat to the seaweed), "Verdant Valley",
	 * "Sticky Swamp" or "House on the Hill". Null when not on Fossil Island.
	 */
	static String zone(net.runelite.api.coords.WorldPoint point)
	{
		if (point == null || !FarmLocation.FOSSIL_ISLAND.containsRegion(point.getRegionID()))
		{
			return null;
		}
		if (point.getPlane() != 0 || (point.getX() >= 3745 && point.getY() >= 3800))
		{
			return "House on the Hill";
		}
		if (point.getY() >= 3800)
		{
			return "Mushroom Meadow";
		}
		return point.getX() >= 3720 ? "Verdant Valley" : "Sticky Swamp";
	}

	/**
	 * The magic mushtree destination nearest this space.
	 */
	static String area(int varp)
	{
		return varp == VarPlayerID.BIRDHOUSE_TRANSMIT_C || varp == VarPlayerID.BIRDHOUSE_TRANSMIT_D
			? "Verdant Valley" : "Mushroom Meadow";
	}
}

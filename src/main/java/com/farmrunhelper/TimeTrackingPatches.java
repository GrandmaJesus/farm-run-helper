package com.farmrunhelper;

import net.runelite.api.gameval.VarbitID;

/**
 * Every farming patch RuneLite's Time Tracking plugin saves, as map region, farming varbit and patch type.
 * Taken from Time Tracking's FarmingWorld (BSD 2-Clause), keeping only the patch types this plugin plants.
 * Time Tracking saves each one to your profile as "region.varbit" = "value:epoch seconds".
 */
final class TimeTrackingPatches
{
	static final class Entry
	{
		final int region;
		final int varbit;
		final PatchType type;

		Entry(int region, int varbit, PatchType type)
		{
			this.region = region;
			this.varbit = varbit;
			this.type = type;
		}
	}

	static final Entry[] ALL = {
		new Entry(13106, VarbitID.FARMING_TRANSMIT_A, PatchType.CACTUS),
		new Entry(9770, VarbitID.FARMING_TRANSMIT_A, PatchType.HARDWOOD),
		new Entry(10290, VarbitID.FARMING_TRANSMIT_A, PatchType.BUSH),
		new Entry(10548, VarbitID.FARMING_TRANSMIT_A, PatchType.ALLOTMENT),
		new Entry(10548, VarbitID.FARMING_TRANSMIT_B, PatchType.ALLOTMENT),
		new Entry(10548, VarbitID.FARMING_TRANSMIT_C, PatchType.FLOWER),
		new Entry(10548, VarbitID.FARMING_TRANSMIT_D, PatchType.HERB),
		new Entry(5427, VarbitID.FARMING_TRANSMIT_A, PatchType.TREE),
		new Entry(5427, VarbitID.FARMING_TRANSMIT_B, PatchType.BELLADONNA),
		new Entry(6702, VarbitID.FARMING_TRANSMIT_A, PatchType.HARDWOOD),
		new Entry(11058, VarbitID.FARMING_TRANSMIT_A, PatchType.FRUIT_TREE),
		new Entry(11062, VarbitID.FARMING_TRANSMIT_A, PatchType.ALLOTMENT),
		new Entry(11062, VarbitID.FARMING_TRANSMIT_B, PatchType.ALLOTMENT),
		new Entry(11062, VarbitID.FARMING_TRANSMIT_C, PatchType.FLOWER),
		new Entry(11062, VarbitID.FARMING_TRANSMIT_D, PatchType.HERB),
		new Entry(11317, VarbitID.FARMING_TRANSMIT_A, PatchType.FRUIT_TREE),
		new Entry(6192, VarbitID.FARMING_TRANSMIT_A, PatchType.ALLOTMENT),
		new Entry(6192, VarbitID.FARMING_TRANSMIT_B, PatchType.ALLOTMENT),
		new Entry(6192, VarbitID.FARMING_TRANSMIT_C, PatchType.FLOWER),
		new Entry(6192, VarbitID.FARMING_TRANSMIT_D, PatchType.HERB),
		new Entry(12596, VarbitID.FARMING_TRANSMIT_A, PatchType.BUSH),
		new Entry(12340, VarbitID.FARMING_TRANSMIT_A, PatchType.BELLADONNA),
		new Entry(10300, VarbitID.FARMING_TRANSMIT_A, PatchType.BUSH),
		new Entry(11828, VarbitID.FARMING_TRANSMIT_A, PatchType.TREE),
		new Entry(12083, VarbitID.FARMING_TRANSMIT_A, PatchType.ALLOTMENT),
		new Entry(12083, VarbitID.FARMING_TRANSMIT_B, PatchType.ALLOTMENT),
		new Entry(12083, VarbitID.FARMING_TRANSMIT_C, PatchType.FLOWER),
		new Entry(12083, VarbitID.FARMING_TRANSMIT_D, PatchType.HERB),
		new Entry(14651, VarbitID.FARMING_TRANSMIT_A, PatchType.HARDWOOD),
		new Entry(14651, VarbitID.FARMING_TRANSMIT_B, PatchType.HARDWOOD),
		new Entry(14651, VarbitID.FARMING_TRANSMIT_C, PatchType.HARDWOOD),
		new Entry(15008, VarbitID.FARMING_TRANSMIT_A, PatchType.SEAWEED),
		new Entry(15008, VarbitID.FARMING_TRANSMIT_B, PatchType.SEAWEED),
		new Entry(9781, VarbitID.FARMING_TRANSMIT_A, PatchType.TREE),
		new Entry(9781, VarbitID.FARMING_TRANSMIT_B, PatchType.FRUIT_TREE),
		new Entry(12581, VarbitID.FARMING_TRANSMIT_C, PatchType.CALQUAT),
		new Entry(15148, VarbitID.FARMING_TRANSMIT_A, PatchType.ALLOTMENT),
		new Entry(15148, VarbitID.FARMING_TRANSMIT_B, PatchType.HERB),
		new Entry(5423, VarbitID.FARMING_TRANSMIT_A, PatchType.CALQUAT),
		new Entry(5423, VarbitID.FARMING_TRANSMIT_B, PatchType.FRUIT_TREE),
		new Entry(5423, VarbitID.FARMING_TRANSMIT_C, PatchType.FLOWER),
		new Entry(6967, VarbitID.FARMING_TRANSMIT_A, PatchType.ALLOTMENT),
		new Entry(6967, VarbitID.FARMING_TRANSMIT_B, PatchType.ALLOTMENT),
		new Entry(6967, VarbitID.FARMING_TRANSMIT_C, PatchType.FLOWER),
		new Entry(6967, VarbitID.FARMING_TRANSMIT_D, PatchType.HERB),
		new Entry(9265, VarbitID.FARMING_TRANSMIT_A, PatchType.FRUIT_TREE),
		new Entry(12594, VarbitID.FARMING_TRANSMIT_A, PatchType.TREE),
		new Entry(13622, VarbitID.FARMING_TRANSMIT_A, PatchType.MUSHROOM),
		new Entry(14391, VarbitID.FARMING_TRANSMIT_A, PatchType.ALLOTMENT),
		new Entry(14391, VarbitID.FARMING_TRANSMIT_B, PatchType.ALLOTMENT),
		new Entry(14391, VarbitID.FARMING_TRANSMIT_C, PatchType.FLOWER),
		new Entry(14391, VarbitID.FARMING_TRANSMIT_D, PatchType.HERB),
		new Entry(11570, VarbitID.FARMING_TRANSMIT_A, PatchType.BUSH),
		new Entry(11056, VarbitID.FARMING_TRANSMIT_A, PatchType.CALQUAT),
		new Entry(11573, VarbitID.FARMING_TRANSMIT_A, PatchType.TREE),
		new Entry(9777, VarbitID.FARMING_TRANSMIT_A, PatchType.FRUIT_TREE),
		new Entry(11321, VarbitID.FARMING_TRANSMIT_A, PatchType.HERB),
		new Entry(12854, VarbitID.FARMING_TRANSMIT_A, PatchType.TREE),
		new Entry(11325, VarbitID.FARMING_TRANSMIT_A, PatchType.HERB),
		new Entry(5021, VarbitID.FARMING_TRANSMIT_J, PatchType.HESPORI),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_G, PatchType.TREE),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_E, PatchType.HERB),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_B, PatchType.BUSH),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_H, PatchType.FLOWER),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_C, PatchType.ALLOTMENT),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_D, PatchType.ALLOTMENT),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_F, PatchType.CACTUS),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_K, PatchType.FRUIT_TREE),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_M, PatchType.ANIMA),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_L, PatchType.CELASTRUS),
		new Entry(4922, VarbitID.FARMING_TRANSMIT_I, PatchType.REDWOOD),
		new Entry(13151, VarbitID.FARMING_TRANSMIT_A, PatchType.ALLOTMENT),
		new Entry(13151, VarbitID.FARMING_TRANSMIT_B, PatchType.ALLOTMENT),
		new Entry(13151, VarbitID.FARMING_TRANSMIT_C, PatchType.FLOWER),
	};

	private TimeTrackingPatches()
	{
	}

	/**
	 * The kind of patch with this farming varbit in this map square (or a stop's other squares), or null if it isn't listed.
	 */
	static PatchType typeAt(int region, int varbit)
	{
		if (varbit == -1)
		{
			return null;
		}
		for (Entry entry : ALL)
		{
			if (entry.varbit == varbit && entry.region == region)
			{
				return entry.type;
			}
		}
		// In one of a stop's other squares: only trust it if every match agrees (Catherby has two kinds of
		// patch on the same varbit in different squares)
		PatchType found = null;
		for (Entry entry : ALL)
		{
			if (entry.varbit != varbit)
			{
				continue;
			}
			for (FarmLocation location : FarmLocation.values())
			{
				if (location.containsRegion(region) && location.containsRegion(entry.region))
				{
					if (found != null && found != entry.type)
					{
						return null;
					}
					found = entry.type;
				}
			}
		}
		return found;
	}

	/**
	 * Whether this varbit is one that farming patches use.
	 */
	static boolean isFarmingVarbit(int varbit)
	{
		for (Entry entry : ALL)
		{
			if (entry.varbit == varbit)
			{
				return true;
			}
		}
		return false;
	}
}

package com.farmrunhelper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.runelite.api.gameval.ItemID;

/**
 * The farmer's outfit from the Tithe Farm, which gives bonus Farming experience when worn.
 * Each piece has a male and a female version.
 */
final class FarmerOutfit
{
	// Equipment slots used by Inventory Setups
	static final int HEAD = 0;
	static final int BODY = 4;
	static final int LEGS = 7;
	static final int BOOTS = 10;

	private static final int[][] PIECES = {
		{HEAD, ItemID.TITHE_REWARD_HAT_MALE, ItemID.TITHE_REWARD_HAT_FEMALE},
		{BODY, ItemID.TITHE_REWARD_TORSO_MALE, ItemID.TITHE_REWARD_TORSO_FEMALE},
		{LEGS, ItemID.TITHE_REWARD_LEGS_MALE, ItemID.TITHE_REWARD_LEGS_FEMALE},
		{BOOTS, ItemID.TITHE_REWARD_FEET_MALE, ItemID.TITHE_REWARD_FEET_FEMALE},
	};

	private FarmerOutfit()
	{
	}

	/**
	 * Equipment slot to item id for each piece you own.
	 */
	static Map<Integer, Integer> owned(Set<Integer> ownedItems)
	{
		Map<Integer, Integer> worn = new LinkedHashMap<>();
		for (int[] piece : PIECES)
		{
			for (int i = 1; i < piece.length; i++)
			{
				if (ownedItems.contains(piece[i]))
				{
					worn.put(piece[0], piece[i]);
					break;
				}
			}
		}
		return worn;
	}

	static String name(int itemId)
	{
		if (itemId == ItemID.TITHE_REWARD_HAT_MALE || itemId == ItemID.TITHE_REWARD_HAT_FEMALE)
		{
			return "Farmer's strawhat";
		}
		if (itemId == ItemID.TITHE_REWARD_TORSO_MALE)
		{
			return "Farmer's jacket";
		}
		if (itemId == ItemID.TITHE_REWARD_TORSO_FEMALE)
		{
			return "Farmer's shirt";
		}
		if (itemId == ItemID.TITHE_REWARD_LEGS_MALE || itemId == ItemID.TITHE_REWARD_LEGS_FEMALE)
		{
			return "Farmer's boro trousers";
		}
		return "Farmer's boots";
	}
}

package com.farmrunhelper;

import java.util.HashMap;
import java.util.Map;
import net.runelite.api.gameval.ItemID;

/**
 * Things that count as elemental runes for a teleport spell: combination runes, which count as
 * both of their elements, and staves and tomes, which give an unlimited supply of their elements.
 */
final class RuneSources
{
	// Enough to cover any spell, for an unlimited supply
	private static final int UNLIMITED = 100_000;

	private static final Map<Integer, int[]> COMBINATION = new HashMap<>();
	private static final Map<Integer, int[]> STAVES = new HashMap<>();

	static
	{
		COMBINATION.put(ItemID.DUSTRUNE, new int[]{ItemID.AIRRUNE, ItemID.EARTHRUNE});
		COMBINATION.put(ItemID.MISTRUNE, new int[]{ItemID.AIRRUNE, ItemID.WATERRUNE});
		COMBINATION.put(ItemID.SMOKERUNE, new int[]{ItemID.AIRRUNE, ItemID.FIRERUNE});
		COMBINATION.put(ItemID.MUDRUNE, new int[]{ItemID.WATERRUNE, ItemID.EARTHRUNE});
		COMBINATION.put(ItemID.STEAMRUNE, new int[]{ItemID.WATERRUNE, ItemID.FIRERUNE});
		COMBINATION.put(ItemID.LAVARUNE, new int[]{ItemID.EARTHRUNE, ItemID.FIRERUNE});

		int[] air = {ItemID.AIRRUNE};
		int[] water = {ItemID.WATERRUNE};
		int[] earth = {ItemID.EARTHRUNE};
		int[] fire = {ItemID.FIRERUNE};
		for (int id : new int[]{ItemID.STAFF_OF_AIR, ItemID.AIR_BATTLESTAFF, ItemID.MYSTIC_AIR_STAFF, ItemID.DRAMEN_STAFF_AIR})
		{
			STAVES.put(id, air);
		}
		for (int id : new int[]{ItemID.STAFF_OF_WATER, ItemID.WATER_BATTLESTAFF, ItemID.MYSTIC_WATER_STAFF, ItemID.DRAMEN_STAFF_WATER,
			ItemID.KODAI_WAND, ItemID.TOXIC_SOTD, ItemID.TOXIC_SOTD_CHARGED, ItemID.TOME_OF_WATER})
		{
			STAVES.put(id, water);
		}
		for (int id : new int[]{ItemID.STAFF_OF_EARTH, ItemID.EARTH_BATTLESTAFF, ItemID.MYSTIC_EARTH_STAFF, ItemID.TOME_OF_EARTH})
		{
			STAVES.put(id, earth);
		}
		for (int id : new int[]{ItemID.STAFF_OF_FIRE, ItemID.FIRE_BATTLESTAFF, ItemID.MYSTIC_FIRE_STAFF, ItemID.DRAMEN_STAFF_FIRE,
			ItemID.TOME_OF_FIRE})
		{
			STAVES.put(id, fire);
		}
		STAVES.put(ItemID.DUST_BATTLESTAFF, new int[]{ItemID.AIRRUNE, ItemID.EARTHRUNE});
		STAVES.put(ItemID.MIST_BATTLESTAFF, new int[]{ItemID.AIRRUNE, ItemID.WATERRUNE});
		STAVES.put(ItemID.SMOKE_BATTLESTAFF, new int[]{ItemID.AIRRUNE, ItemID.FIRERUNE});
		STAVES.put(ItemID.MUD_BATTLESTAFF, new int[]{ItemID.WATERRUNE, ItemID.EARTHRUNE});
		STAVES.put(ItemID.MYSTIC_MUD_STAFF, new int[]{ItemID.WATERRUNE, ItemID.EARTHRUNE});
		STAVES.put(ItemID.STEAM_BATTLESTAFF, new int[]{ItemID.WATERRUNE, ItemID.FIRERUNE});
		STAVES.put(ItemID.STEAM_BATTLESTAFF_PRETTY, new int[]{ItemID.WATERRUNE, ItemID.FIRERUNE});
		STAVES.put(ItemID.TWINFLAME_STAFF, new int[]{ItemID.WATERRUNE, ItemID.FIRERUNE});
		STAVES.put(ItemID.LAVA_BATTLESTAFF, new int[]{ItemID.EARTHRUNE, ItemID.FIRERUNE});
		STAVES.put(ItemID.LAVA_BATTLESTAFF_PRETTY, new int[]{ItemID.EARTHRUNE, ItemID.FIRERUNE});
		STAVES.put(ItemID.MYSTIC_LAVA_STAFF, new int[]{ItemID.EARTHRUNE, ItemID.FIRERUNE});
		STAVES.put(ItemID.MYSTIC_LAVA_STAFF_PRETTY, new int[]{ItemID.EARTHRUNE, ItemID.FIRERUNE});
	}

	private RuneSources()
	{
	}

	/**
	 * Adds the elemental runes that combination runes in what you carry, and the staff or tome you're wielding, stand for.
	 */
	static void addElements(Map<Integer, Integer> carried, Map<Integer, Integer> worn)
	{
		Map<Integer, Integer> extra = new HashMap<>();
		carried.forEach((id, qty) ->
		{
			int[] elements = COMBINATION.get(id);
			if (elements != null)
			{
				for (int element : elements)
				{
					extra.merge(element, qty, Integer::sum);
				}
			}
		});
		worn.keySet().forEach(id ->
		{
			int[] elements = STAVES.get(id);
			if (elements != null)
			{
				for (int element : elements)
				{
					extra.put(element, UNLIMITED);
				}
			}
		});
		extra.forEach((id, qty) -> carried.merge(id, qty, (a, b) -> Math.min(UNLIMITED, a + b)));
	}
}

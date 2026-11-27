package com.farmrunhelper;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * The items needed for one trip away from the bank.
 */
public class Trip
{
	private final int index;
	private final Map<Integer, Integer> items = new LinkedHashMap<>();
	private final Set<Integer> stackable = new HashSet<>();
	private final Map<Integer, Integer> worn = new LinkedHashMap<>();
	private boolean farmsPatches;
	private RouteStep bankStep;
	private int slotsUsed;
	private int houseTeleports;
	private boolean usesFairyRing;
	private boolean plantsSeeds;
	private boolean needsSecateurs;
	private int patchesToCompost;

	Trip(int index)
	{
		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}

	/**
	 * Item id to quantity, in the order they were added.
	 */
	void setBankStep(RouteStep bankStep)
	{
		this.bankStep = bankStep;
	}

	RouteStep getBankStep()
	{
		return bankStep;
	}

	void setFarmsPatches()
	{
		farmsPatches = true;
	}

	/**
	 * Whether this trip visits any farming patches (not just bird houses), so it needs a spade and rake.
	 */
	boolean farmsPatches()
	{
		return farmsPatches;
	}

	void wear(int slot, int itemId)
	{
		worn.put(slot, itemId);
	}

	/**
	 * Equipment slot to item id for things to wear on this trip.
	 */
	public Map<Integer, Integer> getWorn()
	{
		return Collections.unmodifiableMap(worn);
	}

	public Map<Integer, Integer> getItems()
	{
		return Collections.unmodifiableMap(items);
	}

	public boolean isStackable(int itemId)
	{
		return stackable.contains(itemId);
	}

	boolean contains(int itemId)
	{
		return items.containsKey(itemId);
	}

	void add(int itemId, int quantity, boolean isStackable)
	{
		items.merge(itemId, quantity, Integer::sum);
		if (isStackable)
		{
			stackable.add(itemId);
		}
	}

	int getSlotsUsed()
	{
		return slotsUsed;
	}

	void addSlots(int slots)
	{
		slotsUsed += slots;
	}

	int getHouseTeleports()
	{
		return houseTeleports;
	}

	void addHouseTeleport()
	{
		houseTeleports++;
	}

	boolean usesFairyRing()
	{
		return usesFairyRing;
	}

	void setUsesFairyRing()
	{
		usesFairyRing = true;
	}

	boolean plantsSeeds()
	{
		return plantsSeeds;
	}

	void setPlantsSeeds()
	{
		plantsSeeds = true;
	}

	boolean needsSecateurs()
	{
		return needsSecateurs;
	}

	void setNeedsSecateurs()
	{
		needsSecateurs = true;
	}

	int getPatchesToCompost()
	{
		return patchesToCompost;
	}

	void addPatchesToCompost(int patches)
	{
		patchesToCompost += patches;
	}
}

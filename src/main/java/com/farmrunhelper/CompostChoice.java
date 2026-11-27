package com.farmrunhelper;

import net.runelite.api.gameval.ItemID;

public enum CompostChoice
{
	BOTTOMLESS_BUCKET("Bottomless bucket", ItemID.BOTTOMLESS_COMPOST_BUCKET_FILLED, false),
	ULTRACOMPOST("Ultracompost", ItemID.BUCKET_ULTRACOMPOST, true),
	SUPERCOMPOST("Supercompost", ItemID.BUCKET_SUPERCOMPOST, true),
	COMPOST("Compost", ItemID.BUCKET_COMPOST, true),
	NONE("No compost", -1, false);

	private final String label;
	private final int itemId;
	private final boolean onePerPatch;

	CompostChoice(String label, int itemId, boolean onePerPatch)
	{
		this.label = label;
		this.itemId = itemId;
		this.onePerPatch = onePerPatch;
	}

	public int getItemId()
	{
		return itemId;
	}

	/**
	 * Buckets of compost are used up, one per patch. The bottomless bucket is a single item.
	 */
	public boolean isOnePerPatch()
	{
		return onePerPatch;
	}

	@Override
	public String toString()
	{
		return label;
	}
}

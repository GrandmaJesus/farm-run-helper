package com.farmrunhelper;

public enum FairyRingAccess
{
	NONE("No access"),
	DRAMEN_STAFF("With a dramen/lunar staff"),
	STAFFLESS("Staffless (Lumbridge elite)");

	private final String label;

	FairyRingAccess(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}

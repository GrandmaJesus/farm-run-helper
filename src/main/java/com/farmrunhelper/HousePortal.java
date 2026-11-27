package com.farmrunhelper;

/**
 * How your house teleports to a destination: a portal space in the portal chamber, or the portal nexus.
 */
public enum HousePortal
{
	NONE("Not in house"),
	PORTAL("Portal Space"),
	NEXUS("Nexus");

	private final String label;

	HousePortal(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}

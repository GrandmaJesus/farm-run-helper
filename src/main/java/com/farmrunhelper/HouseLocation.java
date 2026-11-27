package com.farmrunhelper;

public enum HouseLocation
{
	NONE("No house / not listed"),
	RIMMINGTON("Rimmington"),
	TAVERLEY("Taverley"),
	POLLNIVNEACH("Pollnivneach"),
	HOSIDIUS("Hosidius"),
	RELLEKKA("Rellekka"),
	BRIMHAVEN("Brimhaven"),
	YANILLE("Yanille"),
	PRIFDDINAS("Prifddinas"),
	ALDARIN("Aldarin");

	private final String label;

	HouseLocation(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}

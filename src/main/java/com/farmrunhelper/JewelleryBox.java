package com.farmrunhelper;

public enum JewelleryBox
{
	NONE("None"),
	BASIC("Basic"),
	FANCY("Fancy"),
	ORNATE("Ornate");

	private final String label;

	JewelleryBox(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}

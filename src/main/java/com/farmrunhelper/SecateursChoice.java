package com.farmrunhelper;

public enum SecateursChoice
{
	MAGIC("Magic secateurs"),
	NORMAL("Secateurs"),
	NONE("None");

	private final String label;

	SecateursChoice(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}

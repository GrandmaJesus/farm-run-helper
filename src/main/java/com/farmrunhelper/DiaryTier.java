package com.farmrunhelper;

public enum DiaryTier
{
	NONE("None"),
	EASY("Easy"),
	MEDIUM("Medium"),
	HARD("Hard"),
	ELITE("Elite");

	private final String label;

	DiaryTier(String label)
	{
		this.label = label;
	}

	public boolean atLeast(DiaryTier other)
	{
		return ordinal() >= other.ordinal();
	}

	@Override
	public String toString()
	{
		return label;
	}
}

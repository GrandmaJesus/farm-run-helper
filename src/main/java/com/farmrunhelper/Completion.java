package com.farmrunhelper;

public enum Completion
{
	NOT_DONE("Not done"),
	DONE("Done");

	private final String label;

	Completion(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}

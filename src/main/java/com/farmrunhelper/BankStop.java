package com.farmrunhelper;

public enum BankStop
{
	FARMING_GUILD("Farming Guild", "Teleport to the Farming Guild and use the bank chest"),
	GRAND_EXCHANGE("Grand Exchange", "Teleport to the Grand Exchange and bank"),
	CASTLE_WARS("Castle Wars", "Ring of dueling -> Castle Wars and bank"),
	NEAREST("Nearest bank", "Go to your nearest bank");

	private final String label;
	private final String instruction;

	BankStop(String label, String instruction)
	{
		this.label = label;
		this.instruction = instruction;
	}

	public String getInstruction()
	{
		return instruction;
	}

	@Override
	public String toString()
	{
		return label;
	}
}

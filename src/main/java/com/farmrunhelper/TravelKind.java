package com.farmrunhelper;

/**
 * What a route to a farming stop involves: spirit trees, fairy rings, or a trip through your house.
 */
final class TravelKind
{
	private TravelKind()
	{
	}

	static boolean usesSpiritTree(TeleportOption option)
	{
		for (Req req : option.getRequirements())
		{
			if (req == Req.SPIRIT_TREES || req.name().startsWith("SPIRIT_TREE_"))
			{
				return true;
			}
		}
		return option.getInstruction().toLowerCase().contains("spirit tree");
	}

	static boolean usesFairyRing(TeleportOption option)
	{
		for (Req req : option.getRequirements())
		{
			if (req == Req.FAIRY_RING)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Whether the route needs a mounted item, portal or jewellery box in your house.
	 */
	static boolean usesHouseObject(TeleportOption option)
	{
		for (Req req : option.getRequirements())
		{
			if (req.name().startsWith("POH_"))
			{
				return true;
			}
		}
		String instruction = option.getInstruction();
		return instruction.startsWith("House") || instruction.startsWith("Teleport to house");
	}

	/**
	 * Whether the route goes through your house, including spirit tree and fairy ring routes when
	 * you've said your house has one.
	 */
	static boolean usesHouse(TeleportOption option, FarmRunHelperConfig config)
	{
		return usesHouseObject(option)
			|| (config.houseSpiritTree() && usesSpiritTree(option))
			|| (config.houseFairyRing() && usesFairyRing(option));
	}
}

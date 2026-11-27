package com.farmrunhelper;

/**
 * Growth cycle lengths and stage counts from the OSRS Wiki, used to estimate when a crop
 * planted during a run will be ready. Patches grow on fixed clock ticks, so these are estimates.
 */
final class GrowthTimes
{
	private GrowthTimes()
	{
	}

	/**
	 * Minutes per growth stage and number of stages for what the run plants in this patch type,
	 * or null when there's no estimate.
	 */
	static int[] cycle(PatchType type, FarmRunHelperConfig config)
	{
		switch (type)
		{
			case HERB:
				return new int[]{20, 4};
			case FLOWER:
				return new int[]{5, 4};
			case ALLOTMENT:
				switch (config.allotmentSeed())
				{
					case SWEETCORN:
					case STRAWBERRY:
						return new int[]{10, 6};
					case WATERMELON:
						return new int[]{10, 8};
					case SNAPE_GRASS:
						return new int[]{10, 7};
					default:
						return new int[]{10, 4};
				}
			case BUSH:
				switch (config.bushSeed())
				{
					case REDBERRY:
						return new int[]{20, 5};
					case CADAVABERRY:
						return new int[]{20, 6};
					case DWELLBERRY:
						return new int[]{20, 7};
					default:
						return new int[]{20, 8};
				}
			case CACTUS:
				return config.cactusSeed() == Seeds.CactusSeed.POTATO_CACTUS ? new int[]{10, 7} : new int[]{80, 7};
			case MUSHROOM:
				return new int[]{40, 6};
			case SEAWEED:
				return new int[]{10, 4};
			case BELLADONNA:
				return new int[]{80, 4};
			case TREE:
				switch (config.treeSapling())
				{
					case OAK:
						return new int[]{40, 4};
					case WILLOW:
						return new int[]{40, 6};
					case MAPLE:
						return new int[]{40, 8};
					case YEW:
						return new int[]{40, 10};
					default:
						return new int[]{40, 12};
				}
			case FRUIT_TREE:
				return new int[]{160, 6};
			case CALQUAT:
				return new int[]{160, 8};
			case CELASTRUS:
				return new int[]{160, 5};
			case REDWOOD:
				return new int[]{640, 10};
			case HARDWOOD:
				switch (config.hardwoodSapling())
				{
					case TEAK:
						return new int[]{640, 7};
					case ROSEWOOD:
						return new int[]{640, 9};
					default:
						return new int[]{640, 8};
				}
			case ANIMA:
				return new int[]{640, 8};
			case HESPORI:
				return new int[]{640, 3};
			default:
				return null;
		}
	}

	/**
	 * Estimated time (epoch millis) the crop planted at {@code plantedAt} will be fully grown.
	 * Growth advances on clock-aligned ticks, so the first stage only takes until the next tick.
	 */
	static long readyAt(PatchType type, FarmRunHelperConfig config, long plantedAt)
	{
		if (type == PatchType.BIRDHOUSE)
		{
			return plantedAt + 50L * 60_000L;
		}
		int[] cycle = cycle(type, config);
		if (cycle == null)
		{
			return -1;
		}
		long tick = cycle[0] * 60_000L;
		long nextTick = (plantedAt / tick + 1) * tick;
		return nextTick + (cycle[1] - 1) * tick;
	}

	static String format(long millisLeft)
	{
		if (millisLeft <= 0)
		{
			return "Ready";
		}
		long minutes = (millisLeft + 59_999L) / 60_000L;
		if (minutes >= 60)
		{
			long hours = minutes / 60;
			if (hours >= 24)
			{
				return "~" + (hours / 24) + "d " + (hours % 24) + "h";
			}
			return "~" + hours + "h " + (minutes % 60) + "m";
		}
		return "~" + minutes + "m";
	}
}

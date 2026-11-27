package com.farmrunhelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * A seed or sapling choice shown in the side panel, where options the player can't use yet are greyed out.
 */
final class SeedSetting
{
	static final List<SeedSetting> ALL = Collections.unmodifiableList(Arrays.asList(
		new SeedSetting("Herb seed", "herbSeed", Seeds.HerbSeed.values(), FarmRunHelperConfig::herbSeed),
		new SeedSetting("Flower seed", "flowerSeed", Seeds.FlowerSeed.values(), FarmRunHelperConfig::flowerSeed),
		new SeedSetting("Allotment seed (3 per patch)", "allotmentSeed", Seeds.AllotmentSeed.values(), FarmRunHelperConfig::allotmentSeed),
		new SeedSetting("Bush seed", "bushSeed", Seeds.BushSeed.values(), FarmRunHelperConfig::bushSeed),
		new SeedSetting("Cactus seed", "cactusSeed", Seeds.CactusSeed.values(), FarmRunHelperConfig::cactusSeed),
		new SeedSetting("Tree sapling", "treeSapling", Seeds.TreeSapling.values(), FarmRunHelperConfig::treeSapling),
		new SeedSetting("Fruit tree sapling", "fruitSapling", Seeds.FruitSapling.values(), FarmRunHelperConfig::fruitSapling),
		new SeedSetting("Hardwood sapling", "hardwoodSapling", Seeds.HardwoodSapling.values(), FarmRunHelperConfig::hardwoodSapling),
		new SeedSetting("Anima seed", "animaSeed", Seeds.AnimaSeed.values(), FarmRunHelperConfig::animaSeed),
		new SeedSetting("Bird house", "birdHouse", Seeds.BirdHouse.values(), FarmRunHelperConfig::birdHouse),
		new SeedSetting("Bird house seeds (10 per house)", "birdHouseSeed", Seeds.BirdHouseSeed.values(), FarmRunHelperConfig::birdHouseSeed)
	));

	private final String label;
	private final String configKey;
	private final SeedOption[] options;
	private final Function<FarmRunHelperConfig, SeedOption> getter;

	private SeedSetting(String label, String configKey, SeedOption[] options, Function<FarmRunHelperConfig, SeedOption> getter)
	{
		this.label = label;
		this.configKey = configKey;
		this.options = options;
		this.getter = getter;
	}

	String getLabel()
	{
		return label;
	}

	String getConfigKey()
	{
		return configKey;
	}

	SeedOption[] getOptions()
	{
		return options;
	}

	SeedOption getSelected(FarmRunHelperConfig config)
	{
		return getter.apply(config);
	}

	/**
	 * Crafting level needed to build each bird house.
	 */
	static int craftingLevel(Seeds.BirdHouse house)
	{
		switch (house)
		{
			case OAK:
				return 15;
			case WILLOW:
				return 25;
			case TEAK:
				return 35;
			case MAPLE:
				return 45;
			case MAHOGANY:
				return 50;
			case YEW:
				return 60;
			case MAGIC:
				return 75;
			case REDWOOD:
				return 90;
			default:
				return 5;
		}
	}

	/**
	 * Text shown next to an option, naming the skills its levels are for.
	 */
	static String levelText(SeedOption option)
	{
		if (option instanceof Seeds.BirdHouseSeed)
		{
			return "";
		}
		if (option instanceof Seeds.BirdHouse)
		{
			return " (" + option.getLevel() + " Hunter, " + craftingLevel((Seeds.BirdHouse) option) + " Crafting)";
		}
		return " (" + option.getLevel() + " Farming)";
	}

	/**
	 * Why the player can't use this option yet, or null if they can. Levels of 0 mean "unknown", which never blocks.
	 */
	static String blockedReason(SeedOption option, int farming, int crafting, int hunter)
	{
		if (option instanceof Seeds.BirdHouseSeed)
		{
			return null;
		}
		if (option instanceof Seeds.BirdHouse)
		{
			boolean lowHunter = hunter > 0 && hunter < option.getLevel();
			int craft = craftingLevel((Seeds.BirdHouse) option);
			boolean lowCrafting = crafting > 0 && crafting < craft;
			if (lowHunter && lowCrafting)
			{
				return option.getLevel() + " Hunter and " + craft + " Crafting";
			}
			if (lowHunter)
			{
				return option.getLevel() + " Hunter";
			}
			if (lowCrafting)
			{
				return craft + " Crafting";
			}
			return null;
		}
		if (farming > 0 && farming < option.getLevel())
		{
			return option.getLevel() + " Farming";
		}
		return null;
	}
}

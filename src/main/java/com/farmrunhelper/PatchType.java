package com.farmrunhelper;

public enum PatchType
{
	HERB("Herbs", 1, 65, false),
	FLOWER("Flowers", 1, 45, false),
	ALLOTMENT("Allotments", 3, 45, false),
	BUSH("Bushes", 1, 45, false),
	CACTUS("Cacti", 1, 45, false),
	BELLADONNA("Belladonna", 1, 0, false),
	MUSHROOM("Mushrooms", 1, 0, false),
	SEAWEED("Seaweed", 1, 0, false),
	ANIMA("Anima", 1, 76, false),
	HESPORI("Hespori", 1, 65, false),
	TREE("Trees", 1, 65, true),
	FRUIT_TREE("Fruit trees", 1, 85, true),
	HARDWOOD("Hardwood trees", 1, 0, true),
	CALQUAT("Calquat", 1, 0, true),
	CELASTRUS("Celastrus", 1, 85, true),
	REDWOOD("Redwood", 1, 90, true),
	BIRDHOUSE("Bird houses", 1, 0, false);

	private final String label;
	private final int seedsPerPatch;
	private final int guildLevel;
	private final boolean tree;

	PatchType(String label, int seedsPerPatch, int guildLevel, boolean tree)
	{
		this.label = label;
		this.seedsPerPatch = seedsPerPatch;
		this.guildLevel = guildLevel;
		this.tree = tree;
	}

	public String getLabel()
	{
		return label;
	}

	public int getSeedsPerPatch()
	{
		return seedsPerPatch;
	}

	/**
	 * Farming level needed to reach this patch inside the Farming Guild.
	 */
	public int getGuildLevel()
	{
		return guildLevel;
	}

	/**
	 * Trees are planted from (non-stackable) saplings and may need clearing first.
	 */
	public boolean isTree()
	{
		return tree;
	}

	/**
	 * Whether compost can be used on this patch. Anima patches and bird houses can't take it.
	 */
	public boolean takesCompost()
	{
		return this != ANIMA && this != BIRDHOUSE;
	}

	public boolean isCustomEnabled(FarmRunHelperConfig c)
	{
		switch (this)
		{
			case HERB:
				return c.customHerb();
			case FLOWER:
				return c.customFlower();
			case ALLOTMENT:
				return c.customAllotment();
			case BUSH:
				return c.customBush();
			case CACTUS:
				return c.customCactus();
			case BELLADONNA:
				return c.customBelladonna();
			case MUSHROOM:
				return c.customMushroom();
			case SEAWEED:
				return c.customSeaweed();
			case ANIMA:
				return c.customAnima();
			case HESPORI:
				return c.customHespori();
			case TREE:
				return c.customTree();
			case FRUIT_TREE:
				return c.customFruitTree();
			case HARDWOOD:
				return c.customHardwood();
			case CALQUAT:
				return c.customCalquat();
			case CELASTRUS:
				return c.customCelastrus();
			case REDWOOD:
				return c.customRedwood();
			case BIRDHOUSE:
				return c.customBirdhouse();
			default:
				return false;
		}
	}
}

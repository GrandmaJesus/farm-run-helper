package com.farmrunhelper;

import net.runelite.api.gameval.ItemID;

public final class Seeds
{
	private Seeds()
	{
	}

	public enum HerbSeed implements SeedOption
	{
		GUAM("Guam", 9, ItemID.GUAM_SEED),
		MARRENTILL("Marrentill", 14, ItemID.MARRENTILL_SEED),
		TARROMIN("Tarromin", 19, ItemID.TARROMIN_SEED),
		HARRALANDER("Harralander", 26, ItemID.HARRALANDER_SEED),
		RANARR("Ranarr", 32, ItemID.RANARR_SEED),
		TOADFLAX("Toadflax", 38, ItemID.TOADFLAX_SEED),
		IRIT("Irit", 44, ItemID.IRIT_SEED),
		AVANTOE("Avantoe", 50, ItemID.AVANTOE_SEED),
		KWUARM("Kwuarm", 56, ItemID.KWUARM_SEED),
		SNAPDRAGON("Snapdragon", 62, ItemID.SNAPDRAGON_SEED),
		HUASCA("Huasca", 65, ItemID.HUASCA_SEED),
		CADANTINE("Cadantine", 67, ItemID.CADANTINE_SEED),
		LANTADYME("Lantadyme", 73, ItemID.LANTADYME_SEED),
		DWARF_WEED("Dwarf weed", 79, ItemID.DWARF_WEED_SEED),
		TORSTOL("Torstol", 85, ItemID.TORSTOL_SEED);

		private final String label;
		private final int level;
		private final int itemId;

		HerbSeed(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum FlowerSeed implements SeedOption
	{
		MARIGOLD("Marigold", 2, ItemID.MARIGOLD_SEED),
		ROSEMARY("Rosemary", 11, ItemID.ROSEMARY_SEED),
		NASTURTIUM("Nasturtium", 24, ItemID.NASTURTIUM_SEED),
		WOAD("Woad", 25, ItemID.WOAD_SEED),
		LIMPWURT("Limpwurt", 26, ItemID.LIMPWURT_SEED),
		WHITE_LILY("White lily", 58, ItemID.WHITE_LILY_SEED);

		private final String label;
		private final int level;
		private final int itemId;

		FlowerSeed(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum AllotmentSeed implements SeedOption
	{
		POTATO("Potato", 1, ItemID.POTATO_SEED),
		ONION("Onion", 5, ItemID.ONION_SEED),
		CABBAGE("Cabbage", 7, ItemID.CABBAGE_SEED),
		TOMATO("Tomato", 12, ItemID.TOMATO_SEED),
		SWEETCORN("Sweetcorn", 20, ItemID.SWEETCORN_SEED),
		STRAWBERRY("Strawberry", 31, ItemID.STRAWBERRY_SEED),
		WATERMELON("Watermelon", 47, ItemID.WATERMELON_SEED),
		SNAPE_GRASS("Snape grass", 61, ItemID.SNAPE_GRASS_SEED);

		private final String label;
		private final int level;
		private final int itemId;

		AllotmentSeed(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum BushSeed implements SeedOption
	{
		REDBERRY("Redberry", 10, ItemID.REDBERRY_BUSH_SEED),
		CADAVABERRY("Cadavaberry", 22, ItemID.CADAVABERRY_BUSH_SEED),
		DWELLBERRY("Dwellberry", 36, ItemID.DWELLBERRY_BUSH_SEED),
		JANGERBERRY("Jangerberry", 48, ItemID.JANGERBERRY_BUSH_SEED),
		WHITEBERRY("Whiteberry", 59, ItemID.WHITEBERRY_BUSH_SEED),
		POISON_IVY("Poison ivy", 70, ItemID.POISONIVY_BUSH_SEED);

		private final String label;
		private final int level;
		private final int itemId;

		BushSeed(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum CactusSeed implements SeedOption
	{
		CACTUS("Cactus", 55, ItemID.CACTUS_SEED),
		POTATO_CACTUS("Potato cactus", 64, ItemID.POTATO_CACTUS_SEED);

		private final String label;
		private final int level;
		private final int itemId;

		CactusSeed(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum TreeSapling implements SeedOption
	{
		OAK("Oak", 15, ItemID.PLANTPOT_OAK_SAPLING),
		WILLOW("Willow", 30, ItemID.PLANTPOT_WILLOW_SAPLING),
		MAPLE("Maple", 45, ItemID.PLANTPOT_MAPLE_SAPLING),
		YEW("Yew", 60, ItemID.PLANTPOT_YEW_SAPLING),
		MAGIC("Magic", 75, ItemID.PLANTPOT_MAGIC_TREE_SAPLING);

		private final String label;
		private final int level;
		private final int itemId;

		TreeSapling(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum FruitSapling implements SeedOption
	{
		APPLE("Apple", 27, ItemID.PLANTPOT_APPLE_SAPLING),
		BANANA("Banana", 33, ItemID.PLANTPOT_BANANA_SAPLING),
		ORANGE("Orange", 39, ItemID.PLANTPOT_ORANGE_SAPLING),
		CURRY("Curry", 42, ItemID.PLANTPOT_CURRY_SAPLING),
		PINEAPPLE("Pineapple", 51, ItemID.PLANTPOT_PINEAPPLE_SAPLING),
		PAPAYA("Papaya", 57, ItemID.PLANTPOT_PAPAYA_SAPLING),
		PALM("Palm", 68, ItemID.PLANTPOT_PALM_SAPLING),
		DRAGONFRUIT("Dragonfruit", 81, ItemID.PLANTPOT_DRAGONFRUIT_SAPLING);

		private final String label;
		private final int level;
		private final int itemId;

		FruitSapling(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum HardwoodSapling implements SeedOption
	{
		TEAK("Teak", 35, ItemID.PLANTPOT_TEAK_SAPLING),
		MAHOGANY("Mahogany", 55, ItemID.PLANTPOT_MAHOGANY_SAPLING),
		CAMPHOR("Camphor", 66, ItemID.PLANTPOT_CAMPHOR_SAPLING),
		IRONWOOD("Ironwood", 80, ItemID.PLANTPOT_IRONWOOD_SAPLING),
		ROSEWOOD("Rosewood", 92, ItemID.PLANTPOT_ROSEWOOD_SAPLING);

		private final String label;
		private final int level;
		private final int itemId;

		HardwoodSapling(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum AnimaSeed implements SeedOption
	{
		ATTAS("Attas", 76, ItemID.ATTAS_SEED),
		IASOR("Iasor", 76, ItemID.IASOR_SEED),
		KRONOS("Kronos", 76, ItemID.KRONOS_SEED);

		private final String label;
		private final int level;
		private final int itemId;

		AnimaSeed(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum BirdHouse implements SeedOption
	{
		REGULAR("Bird house", 5, ItemID.BIRDHOUSE_NORMAL),
		OAK("Oak", 14, ItemID.BIRDHOUSE_OAK),
		WILLOW("Willow", 24, ItemID.BIRDHOUSE_WILLOW),
		TEAK("Teak", 34, ItemID.BIRDHOUSE_TEAK),
		MAPLE("Maple", 44, ItemID.BIRDHOUSE_MAPLE),
		MAHOGANY("Mahogany", 49, ItemID.BIRDHOUSE_MAHOGANY),
		YEW("Yew", 59, ItemID.BIRDHOUSE_YEW),
		MAGIC("Magic", 74, ItemID.BIRDHOUSE_MAGIC),
		REDWOOD("Redwood", 89, ItemID.BIRDHOUSE_REDWOOD);

		private final String label;
		private final int level;
		private final int itemId;

		BirdHouse(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum BirdHouseSeed implements SeedOption
	{
		BARLEY("Barley", 0, ItemID.BARLEY_SEED),
		HAMMERSTONE("Hammerstone", 0, ItemID.HAMMERSTONE_HOP_SEED),
		ASGARNIAN("Asgarnian", 0, ItemID.ASGARNIAN_HOP_SEED),
		JUTE("Jute", 0, ItemID.JUTE_SEED),
		YANILLIAN("Yanillian", 0, ItemID.YANILLIAN_HOP_SEED),
		KRANDORIAN("Krandorian", 0, ItemID.KRANDORIAN_HOP_SEED),
		WILDBLOOD("Wildblood", 0, ItemID.WILDBLOOD_HOP_SEED);

		private final String label;
		private final int level;
		private final int itemId;

		BirdHouseSeed(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Fixed implements SeedOption
	{
		BELLADONNA("Belladonna seed", 63, ItemID.BELLADONNA_SEED),
		MUSHROOM("Mushroom spore", 53, ItemID.MUSHROOM_SEED),
		SEAWEED("Seaweed spore", 23, ItemID.SEAWEED_SEED),
		CALQUAT("Calquat sapling", 72, ItemID.PLANTPOT_CALQUAT_SAPLING),
		CELASTRUS("Celastrus sapling", 85, ItemID.PLANTPOT_CELASTRUS_TREE_SAPLING),
		REDWOOD("Redwood sapling", 90, ItemID.PLANTPOT_REDWOOD_TREE_SAPLING),
		HESPORI("Hespori seed", 65, ItemID.HESPORI_SEED);

		private final String label;
		private final int level;
		private final int itemId;

		Fixed(String label, int level, int itemId)
		{
			this.label = label;
			this.level = level;
			this.itemId = itemId;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getItemId()
		{
			return itemId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}
}

package com.farmrunhelper;

import net.runelite.api.gameval.ItemID;

public final class Teleports
{
	private Teleports()
	{
	}

	public enum FarmingGuild implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		SKILLS_NECKLACE("Skills necklace", "Skills necklace -> Farming Guild", new Req[]{}, new int[]{ItemID.JEWL_NECKLACE_OF_SKILLS_1, ItemID.JEWL_NECKLACE_OF_SKILLS_2, ItemID.JEWL_NECKLACE_OF_SKILLS_3, ItemID.JEWL_NECKLACE_OF_SKILLS_4, ItemID.JEWL_NECKLACE_OF_SKILLS_5, ItemID.JEWL_NECKLACE_OF_SKILLS_6}),
		FARMING_CAPE("Farming cape", "Farming cape teleport", new Req[]{}, new int[]{ItemID.SKILLCAPE_FARMING, ItemID.SKILLCAPE_FARMING_TRIMMED}),
		SPIRIT_TREE("Spirit tree (Farming Guild)", "Spirit tree -> Farming Guild", new Req[]{Req.SPIRIT_TREE_FARMING_GUILD}, new int[]{}),
		FAIRY_RING("Fairy ring CIR", "Fairy ring CIR, run south-west", new Req[]{Req.FAIRY_RING}, new int[]{}),
		BATTLEFRONT("Battlefront tele", "Battlefront Teleport, run west", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		FarmingGuild(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum OrtusFarm implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		QUETZAL_WHISTLE("Quetzal whistle", "Quetzal whistle -> Hunter Guild, run north", new Req[]{}, new int[]{ItemID.HG_QUETZALWHISTLE_BASIC, ItemID.HG_QUETZALWHISTLE_ENHANCED, ItemID.HG_QUETZALWHISTLE_PERFECTED}),
		CIVITAS("Civitas teleport", "Civitas illa Fortis Teleport, run to Ortus Farm", new Req[]{}, new int[]{}),
		FAIRY_RING("Fairy ring AJP", "Fairy ring AJP, run to Ortus Farm", new Req[]{Req.FAIRY_RING}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		OrtusFarm(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Auburnvale implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		QUETZAL_WHISTLE("Quetzal whistle", "Quetzal whistle -> Auburnvale", new Req[]{}, new int[]{ItemID.HG_QUETZALWHISTLE_BASIC, ItemID.HG_QUETZALWHISTLE_ENHANCED, ItemID.HG_QUETZALWHISTLE_PERFECTED}),
		FAIRY_RING("Fairy ring AIS", "Fairy ring AIS, run north", new Req[]{Req.FAIRY_RING}, new int[]{}),
		PENDANT_OF_ATES("Pendant of Ates", "Pendant of Ates, run to Auburnvale", new Req[]{}, new int[]{ItemID.PENDANT_OF_ATES});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Auburnvale(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum AuburnvaleBelladonna implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		QUETZAL_WHISTLE("Quetzal whistle", "Quetzal whistle -> Auburnvale (skip if already there), then walk east to the belladonna patch", new Req[]{}, new int[]{ItemID.HG_QUETZALWHISTLE_BASIC, ItemID.HG_QUETZALWHISTLE_ENHANCED, ItemID.HG_QUETZALWHISTLE_PERFECTED}),
		FAIRY_RING("Fairy ring AIS", "Fairy ring AIS (skip if already there), then walk north-east to the belladonna patch", new Req[]{Req.FAIRY_RING}, new int[]{}),
		PENDANT_OF_ATES("Pendant of Ates", "Pendant of Ates (skip if already there), then walk east to the belladonna patch", new Req[]{}, new int[]{ItemID.PENDANT_OF_ATES});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		AuburnvaleBelladonna(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Kastori implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		QUETZAL_WHISTLE("Quetzal whistle", "Quetzal whistle -> Kastori", new Req[]{}, new int[]{ItemID.HG_QUETZALWHISTLE_BASIC, ItemID.HG_QUETZALWHISTLE_ENHANCED, ItemID.HG_QUETZALWHISTLE_PERFECTED}),
		PENDANT_OF_ATES("Pendant of Ates", "Pendant of Ates -> Kastori, run south", new Req[]{}, new int[]{ItemID.PENDANT_OF_ATES});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Kastori(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum LocusOasis implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		QUETZAL_WHISTLE("Quetzal whistle", "Quetzal whistle -> Colossal Wyrm Remains, run north", new Req[]{}, new int[]{ItemID.HG_QUETZALWHISTLE_BASIC, ItemID.HG_QUETZALWHISTLE_ENHANCED, ItemID.HG_QUETZALWHISTLE_PERFECTED}),
		FAIRY_RING("Fairy ring AJP", "Fairy ring AJP, run south-east", new Req[]{Req.FAIRY_RING}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		LocusOasis(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Hosidius implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		BOOK_OF_THE_DEAD("Book of the Dead", "Book of the Dead (or Kharedst's memoirs) -> Hosidius", new Req[]{}, new int[]{ItemID.BOOK_OF_THE_DEAD, ItemID.VEOS_KHAREDSTS_MEMOIRS}),
		XERICS_TALISMAN("Xeric's talisman", "Xeric's talisman -> Xeric's Glade, run to the farm", new Req[]{}, new int[]{ItemID.XERIC_TALISMAN}),
		POH_XERIC("Xeric's (house)", "House -> mounted Xeric's talisman -> Xeric's Glade, run to the farm", new Req[]{Req.POH_XERIC}, new int[]{}),
		POH("House portal Hosidius", "Teleport to house (house in Hosidius)", new Req[]{Req.POH_HOSIDIUS}, new int[]{}),
		SPIRIT_TREE("Spirit tree (Hosidius)", "Spirit tree -> Hosidius", new Req[]{Req.SPIRIT_TREE_HOSIDIUS}, new int[]{}),
		FAIRY_RING("Fairy ring AKR", "Fairy ring AKR, run to the farm", new Req[]{Req.FAIRY_RING}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Hosidius(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum GreatConch implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		FAIRY_RING("Fairy ring CJQ", "Fairy ring CJQ, run south-west", new Req[]{Req.FAIRY_RING}, new int[]{}),
		SAIL("Sail there", "Sail or travel to the Great Conch, Summer Shore patch", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		GreatConch(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Taverley implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH("House portal Taverley", "Teleport to house, walk out (house in Taverley)", new Req[]{Req.POH_TAVERLEY}, new int[]{}),
		GAMES_NECKLACE("Games necklace", "Games necklace -> Burthorpe, run south", new Req[]{}, new int[]{ItemID.NECKLACE_OF_MINIGAMES_1, ItemID.NECKLACE_OF_MINIGAMES_2, ItemID.NECKLACE_OF_MINIGAMES_3, ItemID.NECKLACE_OF_MINIGAMES_4, ItemID.NECKLACE_OF_MINIGAMES_5, ItemID.NECKLACE_OF_MINIGAMES_6, ItemID.NECKLACE_OF_MINIGAMES_7, ItemID.NECKLACE_OF_MINIGAMES_8}),
		FALADOR("Falador tele", "Falador Teleport, take the wall shortcut to Taverley", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Taverley(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum GnomeStronghold implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		SPIRIT_TREE("Spirit tree (Gnome Stronghold)", "Spirit tree -> Gnome Stronghold, walk east to the fruit tree", new Req[]{Req.SPIRIT_TREES}, new int[]{}),
		ROYAL_SEED_POD("Royal seed pod", "Royal seed pod -> Grand Tree, walk east to the fruit tree", new Req[]{}, new int[]{ItemID.MM2_ROYAL_SEED_POD}),
		SLAYER_RING("Slayer ring", "Slayer ring -> Stronghold Slayer Cave, run north-east to the fruit tree", new Req[]{}, new int[]{ItemID.SLAYER_RING_1, ItemID.SLAYER_RING_2, ItemID.SLAYER_RING_3, ItemID.SLAYER_RING_4, ItemID.SLAYER_RING_5, ItemID.SLAYER_RING_6, ItemID.SLAYER_RING_7, ItemID.SLAYER_RING_8, ItemID.SLAYER_RING_ETERNAL});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		GnomeStronghold(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum GnomeStrongholdTree implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		SPIRIT_TREE("Spirit tree (Gnome Stronghold)", "Spirit tree -> Gnome Stronghold (skip if already there), then walk south-west to the tree patch", new Req[]{Req.SPIRIT_TREES}, new int[]{}),
		ROYAL_SEED_POD("Royal seed pod", "Royal seed pod -> Grand Tree (skip if already there), then walk south-west to the tree patch", new Req[]{}, new int[]{ItemID.MM2_ROYAL_SEED_POD}),
		SLAYER_RING("Slayer ring", "Slayer ring -> Stronghold Slayer Cave (skip if already there), run north to the tree patch", new Req[]{}, new int[]{ItemID.SLAYER_RING_1, ItemID.SLAYER_RING_2, ItemID.SLAYER_RING_3, ItemID.SLAYER_RING_4, ItemID.SLAYER_RING_5, ItemID.SLAYER_RING_6, ItemID.SLAYER_RING_7, ItemID.SLAYER_RING_8, ItemID.SLAYER_RING_ETERNAL});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		GnomeStrongholdTree(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum TreeGnomeVillage implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		SPIRIT_TREE("Spirit tree (Gnome Village)", "Spirit tree -> Tree Gnome Village, follow Elkoy out of the maze", new Req[]{Req.SPIRIT_TREES}, new int[]{}),
		FAIRY_RING("Fairy ring CIQ", "Fairy ring CIQ", new Req[]{Req.FAIRY_RING}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		TreeGnomeVillage(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Prifddinas implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		SPIRIT_TREE("Spirit tree (Prifddinas)", "Spirit tree -> Prifddinas", new Req[]{Req.SPIRIT_TREES}, new int[]{}),
		ETERNAL_CRYSTAL("Eternal crystal", "Eternal teleport crystal -> Prifddinas", new Req[]{}, new int[]{ItemID.PRIF_TELEPORT_CRYSTAL}),
		POH("House portal Prifddinas", "Teleport to house (house in Prifddinas)", new Req[]{Req.POH_PRIFDDINAS}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Prifddinas(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Lletya implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		TELEPORT_CRYSTAL("Teleport crystal", "Teleport crystal -> Lletya", new Req[]{}, new int[]{ItemID.MOURNING_TELEPORT_CRYSTAL_1, ItemID.MOURNING_TELEPORT_CRYSTAL_2, ItemID.MOURNING_TELEPORT_CRYSTAL_3, ItemID.MOURNING_TELEPORT_CRYSTAL_4, ItemID.MOURNING_TELEPORT_CRYSTAL_5}),
		ETERNAL_CRYSTAL("Eternal crystal", "Eternal teleport crystal -> Lletya", new Req[]{}, new int[]{ItemID.PRIF_TELEPORT_CRYSTAL}),
		RUN_FROM_PRIFDDINAS("Run from Prifddinas", "Run to Lletya from Prifddinas (spirit tree -> Prifddinas if you aren't there already)", new Req[]{Req.Q_SONG_OF_THE_ELVES}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Lletya(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Catherby implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_CAMELOT("Camelot (house)", "House -> Camelot portal, run east", new Req[]{Req.POH_PORTAL_CAMELOT}, new int[]{}),
		CATHERBY_TELEPORT("Catherby tele", "Catherby Teleport (Lunar spell or tab)", new Req[]{}, new int[]{}),
		CAMELOT("Camelot tele", "Camelot Teleport, run east", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Catherby(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum CatherbyFruit implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_CAMELOT("Camelot (house)", "House -> Camelot portal (skip if already there), then walk south-east along the beach to the fruit tree", new Req[]{Req.POH_PORTAL_CAMELOT}, new int[]{}),
		CATHERBY_TELEPORT("Catherby tele", "Catherby Teleport (skip if already there), then walk south-east along the beach to the fruit tree", new Req[]{}, new int[]{}),
		CAMELOT("Camelot tele", "Camelot Teleport (skip if already there), then walk south-east along the beach to the fruit tree", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		CatherbyFruit(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum ArdougneFarm implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		ARDOUGNE_CLOAK("Ardougne cloak", "Ardougne cloak -> Farm teleport", new Req[]{Req.DIARY_ARDOUGNE_MEDIUM}, new int[]{ItemID.ARDY_CAPE_MEDIUM, ItemID.ARDY_CAPE_HARD, ItemID.ARDY_CAPE_ELITE}),
		SKILLS_NECKLACE("Skills necklace", "Skills necklace -> Fishing Guild, run north", new Req[]{}, new int[]{ItemID.JEWL_NECKLACE_OF_SKILLS_1, ItemID.JEWL_NECKLACE_OF_SKILLS_2, ItemID.JEWL_NECKLACE_OF_SKILLS_3, ItemID.JEWL_NECKLACE_OF_SKILLS_4, ItemID.JEWL_NECKLACE_OF_SKILLS_5, ItemID.JEWL_NECKLACE_OF_SKILLS_6}),
		POH_ARDOUGNE("Ardougne (house)", "House -> Ardougne portal, run north", new Req[]{Req.POH_PORTAL_ARDOUGNE}, new int[]{}),
		ARDOUGNE("Ardougne tele", "Ardougne Teleport, run north", new Req[]{}, new int[]{}),
		FAIRY_RING("Fairy ring BLR", "Fairy ring BLR, run to the farm", new Req[]{Req.FAIRY_RING}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		ArdougneFarm(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum ArdougneBush implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		ARDOUGNE_CLOAK("Ardougne cloak", "Ardougne cloak -> Monastery, run to the bush", new Req[]{}, new int[]{ItemID.ARDY_CAPE_EASY, ItemID.ARDY_CAPE_MEDIUM, ItemID.ARDY_CAPE_HARD, ItemID.ARDY_CAPE_ELITE}),
		FAIRY_RING("Fairy ring DJP", "Fairy ring DJP, run to the bush", new Req[]{Req.FAIRY_RING}, new int[]{}),
		SPIRIT_TREE("Spirit tree (Battlefield)", "Spirit tree -> Battlefield of Khazard, run to the bush", new Req[]{Req.SPIRIT_TREES}, new int[]{}),
		ARDOUGNE("Ardougne tele", "Ardougne Teleport, run south", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		ArdougneBush(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum TrollStronghold implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_TROLL("Troll (house)", "House -> Troll Stronghold portal, herb on the roof", new Req[]{Req.POH_PORTAL_TROLL_STRONGHOLD}, new int[]{}),
		STONY_BASALT("Stony basalt", "Stony basalt -> Troll Stronghold roof", new Req[]{}, new int[]{ItemID.STRONGHOLD_TELEPORT_BASALT}),
		TROLL_TELEPORT("Troll tele", "Troll Stronghold Teleport (spell/tab)", new Req[]{}, new int[]{}),
		TROLLHEIM("Trollheim tele", "Trollheim Teleport, walk west to the Stronghold", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		TrollStronghold(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Weiss implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		ICY_BASALT("Icy basalt", "Icy basalt -> Weiss", new Req[]{}, new int[]{ItemID.WEISS_TELEPORT_BASALT}),
		FAIRY_RING("Fairy ring DKS", "Fairy ring DKS, then Larry's boat", new Req[]{Req.FAIRY_RING}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Weiss(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Etceteria implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		RING_OF_WEALTH("Ring of wealth", "Ring of wealth -> Miscellania, walk to Etceteria", new Req[]{}, new int[]{ItemID.RING_OF_WEALTH_1, ItemID.RING_OF_WEALTH_2, ItemID.RING_OF_WEALTH_3, ItemID.RING_OF_WEALTH_4, ItemID.RING_OF_WEALTH_5}),
		FAIRY_RING("Fairy ring CIP", "Fairy ring CIP, walk to Etceteria", new Req[]{Req.FAIRY_RING}, new int[]{}),
		SPIRIT_TREE("Spirit tree (Etceteria)", "Spirit tree -> Etceteria", new Req[]{Req.SPIRIT_TREE_ETCETERIA}, new int[]{}),
		SEA_BOOTS("Sea boots", "Fremennik sea boots -> Rellekka, boat to Miscellania", new Req[]{}, new int[]{ItemID.FREMENNIK_BOOTS_MEDIUM, ItemID.FREMENNIK_BOOTS_HARD, ItemID.FREMENNIK_BOOTS_ELITE});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Etceteria(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum FaladorFarm implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		EXPLORERS_RING("Explorer's ring", "Explorer's ring -> cabbage patch, run north", new Req[]{Req.DIARY_LUMBRIDGE_MEDIUM}, new int[]{ItemID.LUMBRIDGE_RING_MEDIUM, ItemID.LUMBRIDGE_RING_HARD, ItemID.LUMBRIDGE_RING_ELITE}),
		SPIRIT_TREE("Spirit tree (Port Sarim)", "Spirit tree -> Port Sarim, run north", new Req[]{Req.SPIRIT_TREE_PORT_SARIM}, new int[]{}),
		DRAYNOR_MANOR("Draynor Manor tele", "Draynor Manor Teleport, run south-west", new Req[]{}, new int[]{}),
		FALADOR("Falador tele", "Falador Teleport, run south", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		FaladorFarm(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum FaladorTree implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		RING_OF_WEALTH("Ring of wealth", "Ring of wealth -> Falador Park", new Req[]{}, new int[]{ItemID.RING_OF_WEALTH_1, ItemID.RING_OF_WEALTH_2, ItemID.RING_OF_WEALTH_3, ItemID.RING_OF_WEALTH_4, ItemID.RING_OF_WEALTH_5}),
		POH_FALADOR("Falador (house)", "House -> Falador portal, run to Falador Park", new Req[]{Req.POH_PORTAL_FALADOR}, new int[]{}),
		FALADOR("Falador tele", "Falador Teleport, run to Falador Park", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		FaladorTree(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Rimmington implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH("House portal Rimmington", "Teleport to house, walk out (house in Rimmington)", new Req[]{Req.POH_RIMMINGTON}, new int[]{}),
		SPIRIT_TREE("Spirit tree (Port Sarim)", "Spirit tree -> Port Sarim, walk west", new Req[]{Req.SPIRIT_TREE_PORT_SARIM}, new int[]{}),
		SKILLS_NECKLACE("Skills necklace", "Skills necklace -> Crafting Guild, run south", new Req[]{}, new int[]{ItemID.JEWL_NECKLACE_OF_SKILLS_1, ItemID.JEWL_NECKLACE_OF_SKILLS_2, ItemID.JEWL_NECKLACE_OF_SKILLS_3, ItemID.JEWL_NECKLACE_OF_SKILLS_4, ItemID.JEWL_NECKLACE_OF_SKILLS_5, ItemID.JEWL_NECKLACE_OF_SKILLS_6});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Rimmington(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum DraynorManor implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_GLORY("Glory (house)", "House -> mounted glory -> Draynor Village, walk north", new Req[]{Req.POH_GLORY}, new int[]{}),
		GLORY("Amulet of glory", "Amulet of glory -> Draynor Village, walk north", new Req[]{}, new int[]{ItemID.AMULET_OF_GLORY_1, ItemID.AMULET_OF_GLORY_2, ItemID.AMULET_OF_GLORY_3, ItemID.AMULET_OF_GLORY_4, ItemID.AMULET_OF_GLORY_5, ItemID.AMULET_OF_GLORY_6, ItemID.AMULET_OF_GLORY_INF}),
		DRAYNOR_MANOR("Draynor Manor tele", "Draynor Manor Teleport (Arceuus spell/tab)", new Req[]{}, new int[]{}),
		EXPLORERS_RING("Explorer's ring", "Explorer's ring -> cabbage patch, run east", new Req[]{Req.DIARY_LUMBRIDGE_MEDIUM}, new int[]{ItemID.LUMBRIDGE_RING_MEDIUM, ItemID.LUMBRIDGE_RING_HARD, ItemID.LUMBRIDGE_RING_ELITE});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		DraynorManor(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Lumbridge implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_LUMBRIDGE("Lumbridge (house)", "House -> Lumbridge portal, tree west of the castle", new Req[]{Req.POH_PORTAL_LUMBRIDGE}, new int[]{}),
		LUMBRIDGE("Lumbridge tele", "Lumbridge Teleport, tree west of the castle", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Lumbridge(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum AlKharid implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_GLORY("Glory (house)", "House -> mounted glory -> Al Kharid", new Req[]{Req.POH_GLORY}, new int[]{}),
		GLORY("Amulet of glory", "Amulet of glory -> Al Kharid", new Req[]{}, new int[]{ItemID.AMULET_OF_GLORY_1, ItemID.AMULET_OF_GLORY_2, ItemID.AMULET_OF_GLORY_3, ItemID.AMULET_OF_GLORY_4, ItemID.AMULET_OF_GLORY_5, ItemID.AMULET_OF_GLORY_6, ItemID.AMULET_OF_GLORY_INF}),
		JEWELLERY_BOX("Jewellery box (house)", "House -> jewellery box -> Ring of dueling: Emir's Arena", new Req[]{Req.POH_JEWELLERY_BOX}, new int[]{}),
		RING_OF_DUELING("Ring of dueling", "Ring of dueling -> Emir's Arena", new Req[]{}, new int[]{ItemID.RING_OF_DUELING_1, ItemID.RING_OF_DUELING_2, ItemID.RING_OF_DUELING_3, ItemID.RING_OF_DUELING_4, ItemID.RING_OF_DUELING_5, ItemID.RING_OF_DUELING_6, ItemID.RING_OF_DUELING_7, ItemID.RING_OF_DUELING_8});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		AlKharid(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum ChampionsGuild implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_VARROCK("Varrock (house)", "House -> Varrock portal, walk south-west", new Req[]{Req.POH_PORTAL_VARROCK}, new int[]{}),
		COMBAT_BRACELET("Combat bracelet", "Combat bracelet -> Champions' Guild", new Req[]{}, new int[]{ItemID.JEWL_BRACELET_OF_COMBAT_1, ItemID.JEWL_BRACELET_OF_COMBAT_2, ItemID.JEWL_BRACELET_OF_COMBAT_3, ItemID.JEWL_BRACELET_OF_COMBAT_4, ItemID.JEWL_BRACELET_OF_COMBAT_5, ItemID.JEWL_BRACELET_OF_COMBAT_6}),
		CHRONICLE("Chronicle", "Chronicle -> Champions' Guild", new Req[]{}, new int[]{ItemID.CHRONICLE}),
		VARROCK("Varrock tele", "Varrock Teleport, walk south-west", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		ChampionsGuild(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Varrock implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_VARROCK("Varrock (house)", "House -> Varrock portal, castle courtyard", new Req[]{Req.POH_PORTAL_VARROCK}, new int[]{}),
		VARROCK("Varrock tele", "Varrock Teleport, castle courtyard", new Req[]{}, new int[]{}),
		RING_OF_WEALTH("Ring of wealth", "Ring of wealth -> Grand Exchange, run east", new Req[]{}, new int[]{ItemID.RING_OF_WEALTH_1, ItemID.RING_OF_WEALTH_2, ItemID.RING_OF_WEALTH_3, ItemID.RING_OF_WEALTH_4, ItemID.RING_OF_WEALTH_5});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Varrock(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum PortPhasmatys implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		ECTOPHIAL("Ectophial", "Ectophial, run west", new Req[]{Req.Q_GHOSTS_AHOY}, new int[]{ItemID.ECTOPHIAL}),
		POH_KHARYRLL("Kharyrll (house)", "House -> Kharyrll portal, run east", new Req[]{Req.POH_PORTAL_KHARYRLL}, new int[]{}),
		KHARYRLL("Kharyrll tele", "Kharyrll Teleport (spell/tab), run east", new Req[]{}, new int[]{}),
		FAIRY_RING("Fairy ring ALQ", "Fairy ring ALQ, run to the farm", new Req[]{Req.FAIRY_RING}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		PortPhasmatys(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum CanifisMushroom implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		FAIRY_RING("Fairy ring CKS", "Fairy ring CKS, run to the mushroom patch", new Req[]{Req.FAIRY_RING}, new int[]{}),
		POH_KHARYRLL("Kharyrll (house)", "House -> Kharyrll portal, run to the mushroom patch", new Req[]{Req.POH_PORTAL_KHARYRLL}, new int[]{}),
		KHARYRLL("Kharyrll tele", "Kharyrll Teleport, run to the mushroom patch", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		CanifisMushroom(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Harmony implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		HARMONY_TELEPORT("Harmony tele", "Harmony Island Teleport", new Req[]{}, new int[]{ItemID.TELETAB_HARMONY}),
		MOS_LE_HARMLESS("Mos Le'Harmless", "Mos Le'Harmless teleport, boat to Harmony", new Req[]{}, new int[]{ItemID.TELEPORTSCROLL_MOSLES});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Harmony(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Brimhaven implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		SPIRIT_TREE("Spirit tree (Brimhaven)", "Spirit tree -> Brimhaven, run north", new Req[]{Req.SPIRIT_TREE_BRIMHAVEN}, new int[]{}),
		POH("House portal Brimhaven", "Teleport to house, run north (house in Brimhaven)", new Req[]{Req.POH_BRIMHAVEN}, new int[]{}),
		KARAMJA_GLOVES("Karamja gloves", "Karamja gloves -> Shilo Village gem mine, climb the ladder out, then Vigroy's cart to Brimhaven", new Req[]{Req.DIARY_KARAMJA_HARD}, new int[]{ItemID.ATJUN_GLOVES_HARD, ItemID.ATJUN_GLOVES_ELITE}),
		ARDOUGNE_BOAT("Ardougne boat", "Ardougne Teleport, talk to Captain Barnaby and pick Brimhaven (30gp), cross the gangplank, run south-west", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Brimhaven(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum TaiBwoWannai implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		TAB("Tai Bwo tab", "Tai Bwo Wannai teleport, run north", new Req[]{}, new int[]{ItemID.TELEPORTSCROLL_TAIBWO}),
		SPIRIT_TREE("Spirit tree (Brimhaven)", "Spirit tree -> Brimhaven, run south-east to the Tai Bwo Wannai calquat patch", new Req[]{Req.SPIRIT_TREE_BRIMHAVEN}, new int[]{}),
		FAIRY_RING("Fairy ring CKR", "Fairy ring CKR, run north", new Req[]{Req.FAIRY_RING}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		TaiBwoWannai(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum BirdHouses implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_DIGSITE("Digsite (house)", "House -> mounted digsite pendant -> Fossil Island (skip if already there)", new Req[]{Req.POH_DIGSITE}, new int[]{}),
		DIGSITE_PENDANT("Digsite pendant", "Digsite pendant -> Fossil Island (skip if already there)", new Req[]{}, new int[]{ItemID.NECKLACE_OF_DIGSITE_1, ItemID.NECKLACE_OF_DIGSITE_2, ItemID.NECKLACE_OF_DIGSITE_3, ItemID.NECKLACE_OF_DIGSITE_4, ItemID.NECKLACE_OF_DIGSITE_5});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		BirdHouses(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum FossilIsland implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		POH_DIGSITE("Digsite (house)", "House -> mounted digsite pendant -> Fossil Island (skip if already there), magic mushtree -> Mushroom Meadow (skip if you're already there, like after the bird houses), walk south-east to the hardwood patches", new Req[]{Req.POH_DIGSITE}, new int[]{}),
		DIGSITE_PENDANT("Digsite pendant", "Digsite pendant -> Fossil Island (skip if already there), magic mushtree -> Mushroom Meadow (skip if you're already there, like after the bird houses), walk south-east to the hardwood patches", new Req[]{}, new int[]{ItemID.NECKLACE_OF_DIGSITE_1, ItemID.NECKLACE_OF_DIGSITE_2, ItemID.NECKLACE_OF_DIGSITE_3, ItemID.NECKLACE_OF_DIGSITE_4, ItemID.NECKLACE_OF_DIGSITE_5});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		FossilIsland(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum Seaweed implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		ROWBOAT("Mushtree + rowboat", "Digsite pendant -> Fossil Island (skip if already there), magic mushtree -> Mushroom Meadow (skip if you're already there, like after the hardwood patches), run north-east to the rowboat on the north coast and pick 'Row out to sea' (press 3, quickly: the rock crabs close the menu), then right-click the rowboat on the island and pick 'Dive' (wear fishbowl helmet + diving apparatus)", new Req[]{}, new int[]{}),
		ZIPLINE("Zipline (74 Agi)", "Zipline shortcut from Fossil Island (74 Agility), dive", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		Seaweed(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public enum AnglersRetreat implements TeleportOption
	{
		AUTO("Auto", "", new Req[]{}, new int[]{}),
		SKIP("Skip", "", new Req[]{}, new int[]{}),
		MYTHS_GUILD("Spirit tree (Myths' Guild)", "Spirit tree -> Feldip Hills (Myths' Guild), walk to Corsair Cove, rowboat south-west", new Req[]{Req.SPIRIT_TREES}, new int[]{}),
		CORSAIR_COVE("Corsair rowboat", "Travel to Corsair Cove, rowboat south-west", new Req[]{}, new int[]{});

		private final String label;
		private final String instruction;
		private final Req[] requirements;
		private final int[] itemIds;

		AnglersRetreat(String label, String instruction, Req[] requirements, int[] itemIds)
		{
			this.label = label;
			this.instruction = instruction;
			this.requirements = requirements;
			this.itemIds = itemIds;
		}

		@Override
		public String getLabel()
		{
			return label;
		}

		@Override
		public String getInstruction()
		{
			return instruction;
		}

		@Override
		public Req[] getRequirements()
		{
			return requirements;
		}

		@Override
		public int[] getItemIds()
		{
			return itemIds;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}
}

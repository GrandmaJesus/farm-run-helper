package com.farmrunhelper;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public enum FarmLocation
{
	FARMING_GUILD("Farming Guild", new int[]{4922, 5021, 5177, 5178, 5179, 4921, 4923, 4665, 4666, 4667}, new Req[]{}, true, "",
		patches(PatchType.TREE, 1, PatchType.HERB, 1, PatchType.BUSH, 1, PatchType.FLOWER, 1, PatchType.ALLOTMENT, 2, PatchType.CACTUS, 1, PatchType.FRUIT_TREE, 1, PatchType.ANIMA, 1, PatchType.CELASTRUS, 1, PatchType.REDWOOD, 1, PatchType.HESPORI, 1)),
	ORTUS_FARM("Ortus Farm", new int[]{6192, 6447, 6448, 6449, 6191, 6193}, new Req[]{Req.Q_CHILDREN_OF_THE_SUN}, false, "",
		patches(PatchType.HERB, 1, PatchType.FLOWER, 1, PatchType.ALLOTMENT, 2)),
	AUBURNVALE("Auburnvale", new int[]{5427, 5428, 5684}, new Req[]{Req.Q_CHILDREN_OF_THE_SUN}, false, "",
		patches(PatchType.TREE, 1)),
	AUBURNVALE_BELLADONNA("Auburnvale belladonna", new int[]{5427, 5428, 5684}, new Req[]{Req.Q_CHILDREN_OF_THE_SUN}, false, "",
		patches(PatchType.BELLADONNA, 1)),
	KASTORI("Kastori", new int[]{5423, 5167, 5424}, new Req[]{Req.Q_CHILDREN_OF_THE_SUN}, false, "",
		patches(PatchType.CALQUAT, 1, PatchType.FRUIT_TREE, 1, PatchType.FLOWER, 1)),
	LOCUS_OASIS("Locus Oasis", new int[]{6702, 6446}, new Req[]{Req.Q_CHILDREN_OF_THE_SUN, Req.Q_RIBBITING_TALE}, false, "",
		patches(PatchType.HARDWOOD, 1)),
	HOSIDIUS("Hosidius", new int[]{6967, 6711}, new Req[]{}, false, "",
		patches(PatchType.HERB, 1, PatchType.FLOWER, 1, PatchType.ALLOTMENT, 2)),
	GREAT_CONCH("Great Conch", new int[]{12581, 12325, 12326, 12327, 12580, 12582, 12583, 12836, 12837, 12838, 12839, 13092, 13093, 13194}, new Req[]{Req.Q_TROUBLED_TORTUGANS}, false, "",
		patches(PatchType.CALQUAT, 1)),
	TAVERLEY("Taverley", new int[]{11573, 11829}, new Req[]{}, false, "",
		patches(PatchType.TREE, 1)),
	GNOME_STRONGHOLD("Gnome Stronghold", new int[]{9781, 9782, 9526, 9525}, new Req[]{}, false, "",
		patches(PatchType.FRUIT_TREE, 1)),
	GNOME_STRONGHOLD_TREE("Gnome Stronghold tree", new int[]{9781, 9782, 9526, 9525}, new Req[]{}, false, "",
		patches(PatchType.TREE, 1)),
	TREE_GNOME_VILLAGE("Gnome Village", new int[]{9777, 10033}, new Req[]{}, false, "",
		patches(PatchType.FRUIT_TREE, 1)),
	PRIFDDINAS("Prifddinas", new int[]{13151}, new Req[]{Req.Q_SONG_OF_THE_ELVES}, false, "",
		patches(PatchType.ALLOTMENT, 2, PatchType.FLOWER, 1)),
	LLETYA("Lletya", new int[]{9265, 11103}, new Req[]{Req.Q_REGICIDE}, false, "",
		patches(PatchType.FRUIT_TREE, 1)),
	CATHERBY("Catherby", new int[]{11062, 11061, 11318, 11317}, new Req[]{}, false, "",
		patches(PatchType.HERB, 1, PatchType.FLOWER, 1, PatchType.ALLOTMENT, 2)),
	CATHERBY_FRUIT("Catherby fruit tree", new int[]{11062, 11061, 11318, 11317}, new Req[]{}, false, "",
		patches(PatchType.FRUIT_TREE, 1)),
	ARDOUGNE_FARM("Ardougne farm", new int[]{10548}, new Req[]{}, false, "",
		patches(PatchType.HERB, 1, PatchType.FLOWER, 1, PatchType.ALLOTMENT, 2)),
	ARDOUGNE_BUSH("Ardougne bush", new int[]{10290, 10546}, new Req[]{}, false, "",
		patches(PatchType.BUSH, 1)),
	TROLL_STRONGHOLD("Troll Stronghold", new int[]{11321}, new Req[]{Req.Q_MY_ARMS_BIG_ADVENTURE}, false, "",
		patches(PatchType.HERB, 1)),
	WEISS("Weiss", new int[]{11325}, new Req[]{Req.Q_MAKING_FRIENDS_WITH_MY_ARM}, false, "",
		patches(PatchType.HERB, 1)),
	ETCETERIA("Etceteria", new int[]{10300}, new Req[]{}, false, "",
		patches(PatchType.BUSH, 1)),
	FALADOR_FARM("Falador farm", new int[]{12083}, new Req[]{}, false, "",
		patches(PatchType.HERB, 1, PatchType.FLOWER, 1, PatchType.ALLOTMENT, 2)),
	FALADOR_TREE("Falador Park", new int[]{11828, 12084}, new Req[]{}, false, "",
		patches(PatchType.TREE, 1)),
	RIMMINGTON("Rimmington", new int[]{11570, 11826}, new Req[]{}, false, "",
		patches(PatchType.BUSH, 1)),
	DRAYNOR_MANOR("Draynor Manor", new int[]{12340}, new Req[]{}, false, "",
		patches(PatchType.BELLADONNA, 1)),
	LUMBRIDGE("Lumbridge", new int[]{12594, 12850}, new Req[]{}, false, "",
		patches(PatchType.TREE, 1)),
	AL_KHARID("Al Kharid", new int[]{13106, 13362, 13105}, new Req[]{}, false, "",
		patches(PatchType.CACTUS, 1)),
	CHAMPIONS_GUILD("Champions' Guild", new int[]{12596}, new Req[]{}, false, "",
		patches(PatchType.BUSH, 1)),
	VARROCK("Varrock", new int[]{12854, 12853}, new Req[]{}, false, "",
		patches(PatchType.TREE, 1)),
	PORT_PHASMATYS("Port Phasmatys", new int[]{14391, 14390}, new Req[]{}, false, "",
		patches(PatchType.HERB, 1, PatchType.FLOWER, 1, PatchType.ALLOTMENT, 2)),
	CANIFIS_MUSHROOM("Canifis mushroom", new int[]{13622, 13878}, new Req[]{}, false, "",
		patches(PatchType.MUSHROOM, 1)),
	HARMONY("Harmony Island", new int[]{15148}, new Req[]{Req.DIARY_MORYTANIA_ELITE}, false, "",
		patches(PatchType.HERB, 1, PatchType.ALLOTMENT, 1)),
	BRIMHAVEN("Brimhaven", new int[]{11058, 11057}, new Req[]{}, false, "",
		patches(PatchType.FRUIT_TREE, 1)),
	TAI_BWO_WANNAI("Tai Bwo Wannai", new int[]{11056}, new Req[]{}, false, "",
		patches(PatchType.CALQUAT, 1)),
	BIRD_HOUSES("Fossil Island bird houses", new int[]{14651, 14907, 14908, 15164, 14652, 14906, 14650, 15162, 15163}, new Req[]{Req.Q_BONE_VOYAGE}, false, "Bird houses: the ones in the part of the island you're in first. From the House on the Hill: magic mushtree -> Verdant Valley (2), then magic mushtree -> Mushroom Meadow (1), then walk south to the Tar Swamp entrance (1).",
		patches(PatchType.BIRDHOUSE, 4)),
	FOSSIL_ISLAND("Fossil Island", new int[]{14651, 14907, 14908, 15164, 14652, 14906, 14650, 15162, 15163}, new Req[]{Req.Q_BONE_VOYAGE}, false, "After checking each tree, pay the squirrel next to it to remove it before planting.",
		patches(PatchType.HARDWOOD, 3)),
	SEAWEED("Seaweed", new int[]{15008}, new Req[]{Req.Q_BONE_VOYAGE}, false, "",
		patches(PatchType.SEAWEED, 2)),
	ANGLERS_RETREAT("Anglers' Retreat", new int[]{9770}, new Req[]{Req.ANGLERS_ROWBOAT}, false, "",
		patches(PatchType.HARDWOOD, 1));

	private final String displayName;
	private final int[] regionIds;
	private final Req[] requirements;
	private final boolean farmingGuild;
	private final String extraNote;
	private final Map<PatchType, Integer> patches;

	FarmLocation(String displayName, int[] regionIds, Req[] requirements, boolean farmingGuild, String extraNote, Map<PatchType, Integer> patches)
	{
		this.displayName = displayName;
		this.regionIds = regionIds;
		this.requirements = requirements;
		this.farmingGuild = farmingGuild;
		this.extraNote = extraNote;
		this.patches = patches;
	}

	private static Map<PatchType, Integer> patches(Object... pairs)
	{
		Map<PatchType, Integer> map = new EnumMap<>(PatchType.class);
		for (int i = 0; i < pairs.length; i += 2)
		{
			map.put((PatchType) pairs[i], (Integer) pairs[i + 1]);
		}
		return Collections.unmodifiableMap(map);
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public int[] getRegionIds()
	{
		return regionIds;
	}

	public Req[] getRequirements()
	{
		return requirements;
	}

	public boolean isFarmingGuild()
	{
		return farmingGuild;
	}

	public String getExtraNote()
	{
		return extraNote;
	}

	public Map<PatchType, Integer> getPatches()
	{
		return patches;
	}

	public boolean containsRegion(int regionId)
	{
		for (int r : regionIds)
		{
			if (r == regionId)
			{
				return true;
			}
		}
		return false;
	}

	public TeleportOption getSelectedTeleport(FarmRunHelperConfig c)
	{
		switch (this)
		{
			case FARMING_GUILD:
				return c.teleportFarmingGuild();
			case ORTUS_FARM:
				return c.teleportOrtusFarm();
			case AUBURNVALE:
				return c.teleportAuburnvale();
			case AUBURNVALE_BELLADONNA:
				return c.teleportAuburnvaleBelladonna();
			case KASTORI:
				return c.teleportKastori();
			case LOCUS_OASIS:
				return c.teleportLocusOasis();
			case HOSIDIUS:
				return c.teleportHosidius();
			case GREAT_CONCH:
				return c.teleportGreatConch();
			case TAVERLEY:
				return c.teleportTaverley();
			case GNOME_STRONGHOLD:
				return c.teleportGnomeStronghold();
			case GNOME_STRONGHOLD_TREE:
				return c.teleportGnomeStrongholdTree();
			case TREE_GNOME_VILLAGE:
				return c.teleportTreeGnomeVillage();
			case PRIFDDINAS:
				return c.teleportPrifddinas();
			case LLETYA:
				return c.teleportLletya();
			case CATHERBY:
				return c.teleportCatherby();
			case CATHERBY_FRUIT:
				return c.teleportCatherbyFruit();
			case ARDOUGNE_FARM:
				return c.teleportArdougneFarm();
			case ARDOUGNE_BUSH:
				return c.teleportArdougneBush();
			case TROLL_STRONGHOLD:
				return c.teleportTrollStronghold();
			case WEISS:
				return c.teleportWeiss();
			case ETCETERIA:
				return c.teleportEtceteria();
			case FALADOR_FARM:
				return c.teleportFaladorFarm();
			case FALADOR_TREE:
				return c.teleportFaladorTree();
			case RIMMINGTON:
				return c.teleportRimmington();
			case DRAYNOR_MANOR:
				return c.teleportDraynorManor();
			case LUMBRIDGE:
				return c.teleportLumbridge();
			case AL_KHARID:
				return c.teleportAlKharid();
			case CHAMPIONS_GUILD:
				return c.teleportChampionsGuild();
			case VARROCK:
				return c.teleportVarrock();
			case PORT_PHASMATYS:
				return c.teleportPortPhasmatys();
			case CANIFIS_MUSHROOM:
				return c.teleportCanifisMushroom();
			case HARMONY:
				return c.teleportHarmony();
			case BRIMHAVEN:
				return c.teleportBrimhaven();
			case TAI_BWO_WANNAI:
				return c.teleportTaiBwoWannai();
			case BIRD_HOUSES:
				return c.teleportBirdHouses();
			case FOSSIL_ISLAND:
				return c.teleportFossilIsland();
			case SEAWEED:
				return c.teleportSeaweed();
			case ANGLERS_RETREAT:
				return c.teleportAnglersRetreat();
			default:
				return null;
		}
	}

	/**
	 * Config key that stores the teleport choice for this location.
	 */
	public String getTeleportConfigKey()
	{
		switch (this)
		{
			case FARMING_GUILD:
				return "teleportFarmingGuild";
			case ORTUS_FARM:
				return "teleportOrtusFarm";
			case AUBURNVALE:
				return "teleportAuburnvale";
			case AUBURNVALE_BELLADONNA:
				return "teleportAuburnvaleBelladonna";
			case KASTORI:
				return "teleportKastori";
			case LOCUS_OASIS:
				return "teleportLocusOasis";
			case HOSIDIUS:
				return "teleportHosidius";
			case GREAT_CONCH:
				return "teleportGreatConch";
			case TAVERLEY:
				return "teleportTaverley";
			case GNOME_STRONGHOLD:
				return "teleportGnomeStronghold";
			case GNOME_STRONGHOLD_TREE:
				return "teleportGnomeStrongholdTree";
			case TREE_GNOME_VILLAGE:
				return "teleportTreeGnomeVillage";
			case PRIFDDINAS:
				return "teleportPrifddinas";
			case LLETYA:
				return "teleportLletya";
			case CATHERBY:
				return "teleportCatherby";
			case CATHERBY_FRUIT:
				return "teleportCatherbyFruit";
			case ARDOUGNE_FARM:
				return "teleportArdougneFarm";
			case ARDOUGNE_BUSH:
				return "teleportArdougneBush";
			case TROLL_STRONGHOLD:
				return "teleportTrollStronghold";
			case WEISS:
				return "teleportWeiss";
			case ETCETERIA:
				return "teleportEtceteria";
			case FALADOR_FARM:
				return "teleportFaladorFarm";
			case FALADOR_TREE:
				return "teleportFaladorTree";
			case RIMMINGTON:
				return "teleportRimmington";
			case DRAYNOR_MANOR:
				return "teleportDraynorManor";
			case LUMBRIDGE:
				return "teleportLumbridge";
			case AL_KHARID:
				return "teleportAlKharid";
			case CHAMPIONS_GUILD:
				return "teleportChampionsGuild";
			case VARROCK:
				return "teleportVarrock";
			case PORT_PHASMATYS:
				return "teleportPortPhasmatys";
			case CANIFIS_MUSHROOM:
				return "teleportCanifisMushroom";
			case HARMONY:
				return "teleportHarmony";
			case BRIMHAVEN:
				return "teleportBrimhaven";
			case TAI_BWO_WANNAI:
				return "teleportTaiBwoWannai";
			case BIRD_HOUSES:
				return "teleportBirdHouses";
			case FOSSIL_ISLAND:
				return "teleportFossilIsland";
			case SEAWEED:
				return "teleportSeaweed";
			case ANGLERS_RETREAT:
				return "teleportAnglersRetreat";
			default:
				return null;
		}
	}

	public TeleportOption[] getTeleportOptions()
	{
		switch (this)
		{
			case FARMING_GUILD:
				return Teleports.FarmingGuild.values();
			case ORTUS_FARM:
				return Teleports.OrtusFarm.values();
			case AUBURNVALE:
				return Teleports.Auburnvale.values();
			case AUBURNVALE_BELLADONNA:
				return Teleports.AuburnvaleBelladonna.values();
			case KASTORI:
				return Teleports.Kastori.values();
			case LOCUS_OASIS:
				return Teleports.LocusOasis.values();
			case HOSIDIUS:
				return Teleports.Hosidius.values();
			case GREAT_CONCH:
				return Teleports.GreatConch.values();
			case TAVERLEY:
				return Teleports.Taverley.values();
			case GNOME_STRONGHOLD:
				return Teleports.GnomeStronghold.values();
			case GNOME_STRONGHOLD_TREE:
				return Teleports.GnomeStrongholdTree.values();
			case TREE_GNOME_VILLAGE:
				return Teleports.TreeGnomeVillage.values();
			case PRIFDDINAS:
				return Teleports.Prifddinas.values();
			case LLETYA:
				return Teleports.Lletya.values();
			case CATHERBY:
				return Teleports.Catherby.values();
			case CATHERBY_FRUIT:
				return Teleports.CatherbyFruit.values();
			case ARDOUGNE_FARM:
				return Teleports.ArdougneFarm.values();
			case ARDOUGNE_BUSH:
				return Teleports.ArdougneBush.values();
			case TROLL_STRONGHOLD:
				return Teleports.TrollStronghold.values();
			case WEISS:
				return Teleports.Weiss.values();
			case ETCETERIA:
				return Teleports.Etceteria.values();
			case FALADOR_FARM:
				return Teleports.FaladorFarm.values();
			case FALADOR_TREE:
				return Teleports.FaladorTree.values();
			case RIMMINGTON:
				return Teleports.Rimmington.values();
			case DRAYNOR_MANOR:
				return Teleports.DraynorManor.values();
			case LUMBRIDGE:
				return Teleports.Lumbridge.values();
			case AL_KHARID:
				return Teleports.AlKharid.values();
			case CHAMPIONS_GUILD:
				return Teleports.ChampionsGuild.values();
			case VARROCK:
				return Teleports.Varrock.values();
			case PORT_PHASMATYS:
				return Teleports.PortPhasmatys.values();
			case CANIFIS_MUSHROOM:
				return Teleports.CanifisMushroom.values();
			case HARMONY:
				return Teleports.Harmony.values();
			case BRIMHAVEN:
				return Teleports.Brimhaven.values();
			case TAI_BWO_WANNAI:
				return Teleports.TaiBwoWannai.values();
			case BIRD_HOUSES:
				return Teleports.BirdHouses.values();
			case FOSSIL_ISLAND:
				return Teleports.FossilIsland.values();
			case SEAWEED:
				return Teleports.Seaweed.values();
			case ANGLERS_RETREAT:
				return Teleports.AnglersRetreat.values();
			default:
				return new TeleportOption[0];
		}
	}
}

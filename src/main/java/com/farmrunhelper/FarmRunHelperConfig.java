package com.farmrunhelper;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(FarmRunHelperConfig.GROUP)
public interface FarmRunHelperConfig extends Config
{
	String GROUP = "farmrunhelper";

	@ConfigSection(name = "Custom run contents", description = "Patch types included when you start a Custom run", position = 0)
	String customRunSection = "customRun";

	@ConfigSection(name = "Quests", description = "Quests that unlock patches and teleports", position = 1, closedByDefault = true)
	String questsSection = "quests";

	@ConfigSection(name = "Diaries & unlocks", description = "Achievement diaries and other unlocks", position = 2, closedByDefault = true)
	String diariesSection = "diaries";

	@ConfigSection(name = "Travel", description = "Fairy rings, spirit trees and your house", position = 3, closedByDefault = true)
	String travelSection = "travel";

	@ConfigSection(name = "Trips & items", description = "How runs are split into trips", position = 4)
	String tripsSection = "trips";

	@ConfigSection(name = "Display", description = "Panel and overlay options", position = 5)
	String displaySection = "display";

	@ConfigItem(keyName = "customHerb", name = "Herbs", description = "Include herbs in a Custom run", position = 1, section = customRunSection)
	default boolean customHerb()
	{
		return true;
	}

	@ConfigItem(keyName = "customFlower", name = "Flowers", description = "Include flowers in a Custom run", position = 2, section = customRunSection)
	default boolean customFlower()
	{
		return true;
	}

	@ConfigItem(keyName = "customAllotment", name = "Allotments", description = "Include allotments in a Custom run", position = 3, section = customRunSection)
	default boolean customAllotment()
	{
		return true;
	}

	@ConfigItem(keyName = "customBush", name = "Bushes", description = "Include bushes in a Custom run", position = 4, section = customRunSection)
	default boolean customBush()
	{
		return true;
	}

	@ConfigItem(keyName = "customCactus", name = "Cacti", description = "Include cacti in a Custom run", position = 5, section = customRunSection)
	default boolean customCactus()
	{
		return true;
	}

	@ConfigItem(keyName = "customBelladonna", name = "Belladonna", description = "Include belladonna in a Custom run", position = 6, section = customRunSection)
	default boolean customBelladonna()
	{
		return true;
	}

	@ConfigItem(keyName = "customMushroom", name = "Mushrooms", description = "Include mushrooms in a Custom run", position = 7, section = customRunSection)
	default boolean customMushroom()
	{
		return true;
	}

	@ConfigItem(keyName = "customSeaweed", name = "Seaweed", description = "Include seaweed in a Custom run", position = 8, section = customRunSection)
	default boolean customSeaweed()
	{
		return true;
	}

	@ConfigItem(keyName = "customAnima", name = "Anima", description = "Include anima in a Custom run", position = 9, section = customRunSection)
	default boolean customAnima()
	{
		return true;
	}

	@ConfigItem(keyName = "customHespori", name = "Hespori", description = "Include hespori in a Custom run", position = 10, section = customRunSection)
	default boolean customHespori()
	{
		return false;
	}

	@ConfigItem(keyName = "customTree", name = "Trees", description = "Include trees in a Custom run", position = 11, section = customRunSection)
	default boolean customTree()
	{
		return true;
	}

	@ConfigItem(keyName = "customFruitTree", name = "Fruit trees", description = "Include fruit trees in a Custom run", position = 12, section = customRunSection)
	default boolean customFruitTree()
	{
		return true;
	}

	@ConfigItem(keyName = "customHardwood", name = "Hardwood trees", description = "Include hardwood trees in a Custom run", position = 13, section = customRunSection)
	default boolean customHardwood()
	{
		return true;
	}

	@ConfigItem(keyName = "customCalquat", name = "Calquat", description = "Include calquat in a Custom run", position = 14, section = customRunSection)
	default boolean customCalquat()
	{
		return true;
	}

	@ConfigItem(keyName = "customCelastrus", name = "Celastrus", description = "Include celastrus in a Custom run", position = 15, section = customRunSection)
	default boolean customCelastrus()
	{
		return true;
	}

	@ConfigItem(keyName = "customRedwood", name = "Redwood", description = "Include redwood in a Custom run", position = 16, section = customRunSection)
	default boolean customRedwood()
	{
		return true;
	}

	@ConfigItem(keyName = "customBirdhouse", name = "Bird houses", description = "Include bird houses in a Custom run", position = 17, section = customRunSection)
	default boolean customBirdhouse()
	{
		return true;
	}

	@ConfigItem(keyName = "herbSeed", name = "Herb seed", description = "Seed to plant in herb patches", position = 18, hidden = true)
	default Seeds.HerbSeed herbSeed()
	{
		return Seeds.HerbSeed.GUAM;
	}

	@ConfigItem(keyName = "flowerSeed", name = "Flower seed", description = "Seed to plant in flower patches", position = 19, hidden = true)
	default Seeds.FlowerSeed flowerSeed()
	{
		return Seeds.FlowerSeed.MARIGOLD;
	}

	@ConfigItem(keyName = "allotmentSeed", name = "Allotment seed", description = "Seed to plant in allotments (3 per patch)", position = 20, hidden = true)
	default Seeds.AllotmentSeed allotmentSeed()
	{
		return Seeds.AllotmentSeed.POTATO;
	}

	@ConfigItem(keyName = "bushSeed", name = "Bush seed", description = "Seed to plant in bush patches", position = 21, hidden = true)
	default Seeds.BushSeed bushSeed()
	{
		return Seeds.BushSeed.REDBERRY;
	}

	@ConfigItem(keyName = "cactusSeed", name = "Cactus seed", description = "Seed to plant in cactus patches", position = 22, hidden = true)
	default Seeds.CactusSeed cactusSeed()
	{
		return Seeds.CactusSeed.CACTUS;
	}

	@ConfigItem(keyName = "treeSapling", name = "Tree sapling", description = "Sapling to plant in tree patches", position = 23, hidden = true)
	default Seeds.TreeSapling treeSapling()
	{
		return Seeds.TreeSapling.OAK;
	}

	@ConfigItem(keyName = "fruitSapling", name = "Fruit sapling", description = "Sapling to plant in fruit tree patches", position = 24, hidden = true)
	default Seeds.FruitSapling fruitSapling()
	{
		return Seeds.FruitSapling.APPLE;
	}

	@ConfigItem(keyName = "hardwoodSapling", name = "Hardwood", description = "Sapling to plant in hardwood patches", position = 25, hidden = true)
	default Seeds.HardwoodSapling hardwoodSapling()
	{
		return Seeds.HardwoodSapling.TEAK;
	}

	@ConfigItem(keyName = "animaSeed", name = "Anima seed", description = "Seed to plant in the anima patch", position = 26, hidden = true)
	default Seeds.AnimaSeed animaSeed()
	{
		return Seeds.AnimaSeed.ATTAS;
	}

	@ConfigItem(keyName = "birdHouse", name = "Bird house", description = "Bird house to set up on Fossil Island", position = 27, hidden = true)
	default Seeds.BirdHouse birdHouse()
	{
		return Seeds.BirdHouse.REGULAR;
	}

	@ConfigItem(keyName = "birdHouseSeed", name = "House seeds", description = "Seeds to fill each bird house with (10 per house)", position = 28, hidden = true)
	default Seeds.BirdHouseSeed birdHouseSeed()
	{
		return Seeds.BirdHouseSeed.BARLEY;
	}

	@ConfigItem(keyName = "questTreeGnomeVillage", name = "Gnome Village", description = "Tree Gnome Village: unlocks the spirit tree network", position = 29, section = questsSection)
	default Completion questTreeGnomeVillage()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "questChildrenOfTheSun", name = "Children of Sun", description = "Children of the Sun: Varlamore patches", position = 30, section = questsSection)
	default Completion questChildrenOfTheSun()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "questRibbitingTale", name = "Ribbiting Tale", description = "The Ribbiting Tale of a Lily Pad Labour Dispute: Locus Oasis hardwood patch", position = 31, section = questsSection)
	default Completion questRibbitingTale()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "questTroubledTortugans", name = "Tortugans", description = "Troubled Tortugans: Great Conch calquat patch", position = 32, section = questsSection)
	default Completion questTroubledTortugans()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "questSongOfTheElves", name = "Song of Elves", description = "Song of the Elves: Prifddinas patches", position = 33, section = questsSection)
	default Completion questSongOfTheElves()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "questRegicide", name = "Regicide", description = "Regicide: Lletya fruit tree patch", position = 34, section = questsSection)
	default Completion questRegicide()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "questMyArmsBigAdventure", name = "My Arm's Adv.", description = "My Arm's Big Adventure: Troll Stronghold herb patch", position = 35, section = questsSection)
	default Completion questMyArmsBigAdventure()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "questMakingFriendsWithMyArm", name = "Making Friends", description = "Making Friends with My Arm: Weiss herb patch", position = 36, section = questsSection)
	default Completion questMakingFriendsWithMyArm()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "questBoneVoyage", name = "Bone Voyage", description = "Bone Voyage: Fossil Island hardwoods, bird houses and seaweed", position = 37, section = questsSection)
	default Completion questBoneVoyage()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "questGhostsAhoy", name = "Ghosts Ahoy", description = "Ghosts Ahoy: Ectophial", position = 38, section = questsSection)
	default Completion questGhostsAhoy()
	{
		return Completion.NOT_DONE;
	}

	@ConfigItem(keyName = "diaryArdougne", name = "Ardougne", description = "Ardougne diary. Medium: Ardougne cloak farm teleport", position = 39, section = diariesSection)
	default DiaryTier diaryArdougne()
	{
		return DiaryTier.NONE;
	}

	@ConfigItem(keyName = "diaryLumbridge", name = "Lumbridge", description = "Lumbridge & Draynor diary. Medium: Explorer's ring cabbage teleport", position = 40, section = diariesSection)
	default DiaryTier diaryLumbridge()
	{
		return DiaryTier.NONE;
	}

	@ConfigItem(keyName = "diaryKaramja", name = "Karamja", description = "Karamja diary. Hard: Karamja gloves Shilo teleport", position = 41, section = diariesSection)
	default DiaryTier diaryKaramja()
	{
		return DiaryTier.NONE;
	}

	@ConfigItem(keyName = "diaryMorytania", name = "Morytania", description = "Morytania diary. Elite: Harmony Island patches", position = 42, section = diariesSection)
	default DiaryTier diaryMorytania()
	{
		return DiaryTier.NONE;
	}

	@ConfigItem(keyName = "autoweed", name = "Autoweed", description = "Autoweed from the Tithe Farm: patches stay clear of weeds, so no rake is needed", position = 43, section = diariesSection)
	default boolean autoweed()
	{
		return false;
	}

	@ConfigItem(keyName = "anglersRowboat", name = "Rowboat built", description = "Corsair Cove rowboat built: unlocks the Anglers' Retreat hardwood patch", position = 44, section = diariesSection)
	default boolean anglersRowboat()
	{
		return false;
	}

	@ConfigItem(keyName = "fairyRingAccess", name = "Fairy rings", description = "Whether and how you can use fairy rings", position = 45, section = travelSection)
	default FairyRingAccess fairyRingAccess()
	{
		return FairyRingAccess.NONE;
	}

	@ConfigItem(keyName = "spiritTreeFarmingGuild", name = "Spirit: Guild", description = "You have a spirit tree planted in Farming Guild", position = 46, section = travelSection)
	default boolean spiritTreeFarmingGuild()
	{
		return false;
	}

	@ConfigItem(keyName = "spiritTreeHosidius", name = "Spirit: Hosidius", description = "You have a spirit tree planted in Hosidius", position = 47, section = travelSection)
	default boolean spiritTreeHosidius()
	{
		return false;
	}

	@ConfigItem(keyName = "spiritTreePortSarim", name = "Spirit: Sarim", description = "You have a spirit tree planted in Port Sarim", position = 48, section = travelSection)
	default boolean spiritTreePortSarim()
	{
		return false;
	}

	@ConfigItem(keyName = "spiritTreeEtceteria", name = "Spirit: Etceteria", description = "You have a spirit tree planted in Etceteria", position = 49, section = travelSection)
	default boolean spiritTreeEtceteria()
	{
		return false;
	}

	@ConfigItem(keyName = "spiritTreeBrimhaven", name = "Spirit: Brimhaven", description = "You have a spirit tree planted in Brimhaven", position = 50, section = travelSection)
	default boolean spiritTreeBrimhaven()
	{
		return false;
	}

	@ConfigItem(keyName = "houseLocation", name = "House", description = "Where your player-owned house is", position = 51, section = travelSection)
	default HouseLocation houseLocation()
	{
		return HouseLocation.NONE;
	}

	@ConfigItem(keyName = "mountedGlory", name = "Mounted glory", description = "Your house has a mounted Glory", position = 52, section = travelSection)
	default boolean mountedGlory()
	{
		return false;
	}

	@ConfigItem(keyName = "mountedDigsite", name = "Mounted digsite", description = "Your house has a mounted Digsite pendant", position = 53, section = travelSection)
	default boolean mountedDigsite()
	{
		return false;
	}

	@ConfigItem(keyName = "mountedXeric", name = "Mounted xeric's", description = "Your house has a mounted Xeric's talisman", position = 54, section = travelSection)
	default boolean mountedXeric()
	{
		return false;
	}

	@ConfigItem(keyName = "houseSpiritTree", name = "House spirit tree", description = "Your house has a spirit tree (or a spiritual fairy tree). Spirit tree routes then start with a house teleport", position = 55, section = travelSection)
	default boolean houseSpiritTree()
	{
		return false;
	}

	@ConfigItem(keyName = "houseFairyRing", name = "House fairy ring", description = "Your house has a fairy ring (or a spiritual fairy tree). Fairy ring routes then start with a house teleport", position = 56, section = travelSection)
	default boolean houseFairyRing()
	{
		return false;
	}

	@ConfigItem(keyName = "jewelleryBox", name = "Jewellery box", description = "The jewellery box in your house (basic has the ring of dueling)", position = 57, section = travelSection)
	default JewelleryBox jewelleryBox()
	{
		return JewelleryBox.NONE;
	}

	@ConfigItem(keyName = "housePortalVarrock", name = "Varrock (house)", description = "How your house teleports to Varrock: a portal space in your portal chamber, or your nexus", position = 58, section = travelSection)
	default HousePortal housePortalVarrock()
	{
		return HousePortal.NONE;
	}

	@ConfigItem(keyName = "housePortalLumbridge", name = "Lumbridge (house)", description = "How your house teleports to Lumbridge: a portal space in your portal chamber, or your nexus", position = 59, section = travelSection)
	default HousePortal housePortalLumbridge()
	{
		return HousePortal.NONE;
	}

	@ConfigItem(keyName = "housePortalFalador", name = "Falador (house)", description = "How your house teleports to Falador: a portal space in your portal chamber, or your nexus", position = 60, section = travelSection)
	default HousePortal housePortalFalador()
	{
		return HousePortal.NONE;
	}

	@ConfigItem(keyName = "housePortalCamelot", name = "Camelot (house)", description = "How your house teleports to Camelot: a portal space in your portal chamber, or your nexus", position = 61, section = travelSection)
	default HousePortal housePortalCamelot()
	{
		return HousePortal.NONE;
	}

	@ConfigItem(keyName = "housePortalArdougne", name = "Ardougne (house)", description = "How your house teleports to Ardougne: a portal space in your portal chamber, or your nexus", position = 62, section = travelSection)
	default HousePortal housePortalArdougne()
	{
		return HousePortal.NONE;
	}

	@ConfigItem(keyName = "housePortalKharyrll", name = "Kharyrll (house)", description = "How your house teleports to Kharyrll: a portal space in your portal chamber, or your nexus", position = 63, section = travelSection)
	default HousePortal housePortalKharyrll()
	{
		return HousePortal.NONE;
	}

	@ConfigItem(keyName = "housePortalTrollStronghold", name = "Troll (house)", description = "How your house teleports to Troll Stronghold: a portal space in your portal chamber, or your nexus", position = 64, section = travelSection)
	default HousePortal housePortalTrollStronghold()
	{
		return HousePortal.NONE;
	}

	@ConfigItem(keyName = "teleportFarmingGuild", name = "Farming Guild", description = "How you get to Farming Guild", position = 65, hidden = true)
	default Teleports.FarmingGuild teleportFarmingGuild()
	{
		return Teleports.FarmingGuild.AUTO;
	}

	@ConfigItem(keyName = "teleportOrtusFarm", name = "Ortus Farm", description = "How you get to Ortus Farm", position = 66, hidden = true)
	default Teleports.OrtusFarm teleportOrtusFarm()
	{
		return Teleports.OrtusFarm.AUTO;
	}

	@ConfigItem(keyName = "teleportAuburnvale", name = "Auburnvale", description = "How you get to Auburnvale", position = 67, hidden = true)
	default Teleports.Auburnvale teleportAuburnvale()
	{
		return Teleports.Auburnvale.AUTO;
	}

	@ConfigItem(keyName = "teleportAuburnvaleBelladonna", name = "Auburnvale belladonna", description = "How you get to Auburnvale belladonna", position = 68, hidden = true)
	default Teleports.AuburnvaleBelladonna teleportAuburnvaleBelladonna()
	{
		return Teleports.AuburnvaleBelladonna.AUTO;
	}

	@ConfigItem(keyName = "teleportKastori", name = "Kastori", description = "How you get to Kastori", position = 69, hidden = true)
	default Teleports.Kastori teleportKastori()
	{
		return Teleports.Kastori.AUTO;
	}

	@ConfigItem(keyName = "teleportLocusOasis", name = "Locus Oasis", description = "How you get to Locus Oasis", position = 70, hidden = true)
	default Teleports.LocusOasis teleportLocusOasis()
	{
		return Teleports.LocusOasis.AUTO;
	}

	@ConfigItem(keyName = "teleportHosidius", name = "Hosidius", description = "How you get to Hosidius", position = 71, hidden = true)
	default Teleports.Hosidius teleportHosidius()
	{
		return Teleports.Hosidius.AUTO;
	}

	@ConfigItem(keyName = "teleportGreatConch", name = "Great Conch", description = "How you get to Great Conch", position = 72, hidden = true)
	default Teleports.GreatConch teleportGreatConch()
	{
		return Teleports.GreatConch.AUTO;
	}

	@ConfigItem(keyName = "teleportTaverley", name = "Taverley", description = "How you get to Taverley", position = 73, hidden = true)
	default Teleports.Taverley teleportTaverley()
	{
		return Teleports.Taverley.AUTO;
	}

	@ConfigItem(keyName = "teleportGnomeStronghold", name = "Gnome Stronghold", description = "How you get to Gnome Stronghold", position = 74, hidden = true)
	default Teleports.GnomeStronghold teleportGnomeStronghold()
	{
		return Teleports.GnomeStronghold.AUTO;
	}

	@ConfigItem(keyName = "teleportGnomeStrongholdTree", name = "Gnome Stronghold tree", description = "How you get to Gnome Stronghold tree", position = 75, hidden = true)
	default Teleports.GnomeStrongholdTree teleportGnomeStrongholdTree()
	{
		return Teleports.GnomeStrongholdTree.AUTO;
	}

	@ConfigItem(keyName = "teleportTreeGnomeVillage", name = "Gnome Village", description = "How you get to Gnome Village", position = 76, hidden = true)
	default Teleports.TreeGnomeVillage teleportTreeGnomeVillage()
	{
		return Teleports.TreeGnomeVillage.AUTO;
	}

	@ConfigItem(keyName = "teleportPrifddinas", name = "Prifddinas", description = "How you get to Prifddinas", position = 77, hidden = true)
	default Teleports.Prifddinas teleportPrifddinas()
	{
		return Teleports.Prifddinas.AUTO;
	}

	@ConfigItem(keyName = "teleportLletya", name = "Lletya", description = "How you get to Lletya", position = 78, hidden = true)
	default Teleports.Lletya teleportLletya()
	{
		return Teleports.Lletya.AUTO;
	}

	@ConfigItem(keyName = "teleportCatherby", name = "Catherby", description = "How you get to Catherby", position = 79, hidden = true)
	default Teleports.Catherby teleportCatherby()
	{
		return Teleports.Catherby.AUTO;
	}

	@ConfigItem(keyName = "teleportCatherbyFruit", name = "Catherby fruit tree", description = "How you get to Catherby fruit tree", position = 80, hidden = true)
	default Teleports.CatherbyFruit teleportCatherbyFruit()
	{
		return Teleports.CatherbyFruit.AUTO;
	}

	@ConfigItem(keyName = "teleportArdougneFarm", name = "Ardougne farm", description = "How you get to Ardougne farm", position = 81, hidden = true)
	default Teleports.ArdougneFarm teleportArdougneFarm()
	{
		return Teleports.ArdougneFarm.AUTO;
	}

	@ConfigItem(keyName = "teleportArdougneBush", name = "Ardougne bush", description = "How you get to Ardougne bush", position = 82, hidden = true)
	default Teleports.ArdougneBush teleportArdougneBush()
	{
		return Teleports.ArdougneBush.AUTO;
	}

	@ConfigItem(keyName = "teleportTrollStronghold", name = "Troll Stronghold", description = "How you get to Troll Stronghold", position = 83, hidden = true)
	default Teleports.TrollStronghold teleportTrollStronghold()
	{
		return Teleports.TrollStronghold.AUTO;
	}

	@ConfigItem(keyName = "teleportWeiss", name = "Weiss", description = "How you get to Weiss", position = 84, hidden = true)
	default Teleports.Weiss teleportWeiss()
	{
		return Teleports.Weiss.AUTO;
	}

	@ConfigItem(keyName = "teleportEtceteria", name = "Etceteria", description = "How you get to Etceteria", position = 85, hidden = true)
	default Teleports.Etceteria teleportEtceteria()
	{
		return Teleports.Etceteria.AUTO;
	}

	@ConfigItem(keyName = "teleportFaladorFarm", name = "Falador farm", description = "How you get to Falador farm", position = 86, hidden = true)
	default Teleports.FaladorFarm teleportFaladorFarm()
	{
		return Teleports.FaladorFarm.AUTO;
	}

	@ConfigItem(keyName = "teleportFaladorTree", name = "Falador Park", description = "How you get to Falador Park", position = 87, hidden = true)
	default Teleports.FaladorTree teleportFaladorTree()
	{
		return Teleports.FaladorTree.AUTO;
	}

	@ConfigItem(keyName = "teleportRimmington", name = "Rimmington", description = "How you get to Rimmington", position = 88, hidden = true)
	default Teleports.Rimmington teleportRimmington()
	{
		return Teleports.Rimmington.AUTO;
	}

	@ConfigItem(keyName = "teleportDraynorManor", name = "Draynor Manor", description = "How you get to Draynor Manor", position = 89, hidden = true)
	default Teleports.DraynorManor teleportDraynorManor()
	{
		return Teleports.DraynorManor.AUTO;
	}

	@ConfigItem(keyName = "teleportLumbridge", name = "Lumbridge", description = "How you get to Lumbridge", position = 90, hidden = true)
	default Teleports.Lumbridge teleportLumbridge()
	{
		return Teleports.Lumbridge.AUTO;
	}

	@ConfigItem(keyName = "teleportAlKharid", name = "Al Kharid", description = "How you get to Al Kharid", position = 91, hidden = true)
	default Teleports.AlKharid teleportAlKharid()
	{
		return Teleports.AlKharid.AUTO;
	}

	@ConfigItem(keyName = "teleportChampionsGuild", name = "Champions' Guild", description = "How you get to Champions' Guild", position = 92, hidden = true)
	default Teleports.ChampionsGuild teleportChampionsGuild()
	{
		return Teleports.ChampionsGuild.AUTO;
	}

	@ConfigItem(keyName = "teleportVarrock", name = "Varrock", description = "How you get to Varrock", position = 93, hidden = true)
	default Teleports.Varrock teleportVarrock()
	{
		return Teleports.Varrock.AUTO;
	}

	@ConfigItem(keyName = "teleportPortPhasmatys", name = "Port Phasmatys", description = "How you get to Port Phasmatys", position = 94, hidden = true)
	default Teleports.PortPhasmatys teleportPortPhasmatys()
	{
		return Teleports.PortPhasmatys.AUTO;
	}

	@ConfigItem(keyName = "teleportCanifisMushroom", name = "Canifis mushroom", description = "How you get to Canifis mushroom", position = 95, hidden = true)
	default Teleports.CanifisMushroom teleportCanifisMushroom()
	{
		return Teleports.CanifisMushroom.AUTO;
	}

	@ConfigItem(keyName = "teleportHarmony", name = "Harmony Island", description = "How you get to Harmony Island", position = 96, hidden = true)
	default Teleports.Harmony teleportHarmony()
	{
		return Teleports.Harmony.AUTO;
	}

	@ConfigItem(keyName = "teleportBrimhaven", name = "Brimhaven", description = "How you get to Brimhaven", position = 97, hidden = true)
	default Teleports.Brimhaven teleportBrimhaven()
	{
		return Teleports.Brimhaven.AUTO;
	}

	@ConfigItem(keyName = "teleportTaiBwoWannai", name = "Tai Bwo Wannai", description = "How you get to Tai Bwo Wannai", position = 98, hidden = true)
	default Teleports.TaiBwoWannai teleportTaiBwoWannai()
	{
		return Teleports.TaiBwoWannai.AUTO;
	}

	@ConfigItem(keyName = "teleportBirdHouses", name = "Fossil Island bird houses", description = "How you get to Fossil Island bird houses", position = 99, hidden = true)
	default Teleports.BirdHouses teleportBirdHouses()
	{
		return Teleports.BirdHouses.AUTO;
	}

	@ConfigItem(keyName = "teleportFossilIsland", name = "Fossil Island", description = "How you get to Fossil Island", position = 100, hidden = true)
	default Teleports.FossilIsland teleportFossilIsland()
	{
		return Teleports.FossilIsland.AUTO;
	}

	@ConfigItem(keyName = "teleportSeaweed", name = "Seaweed", description = "How you get to Seaweed", position = 101, hidden = true)
	default Teleports.Seaweed teleportSeaweed()
	{
		return Teleports.Seaweed.AUTO;
	}

	@ConfigItem(keyName = "teleportAnglersRetreat", name = "Anglers' Retreat", description = "How you get to Anglers' Retreat", position = 102, hidden = true)
	default Teleports.AnglersRetreat teleportAnglersRetreat()
	{
		return Teleports.AnglersRetreat.AUTO;
	}

	@ConfigItem(keyName = "tripCapacity", name = "Trip slots", description = "Inventory slots per trip: slots for seeds, saplings and teleports before the run is split into another trip", position = 103, section = tripsSection)
	@Range(min = 5, max = 26)
	default int tripCapacity()
	{
		return 18;
	}

	@ConfigItem(keyName = "bankStop", name = "Bank stop", description = "Where you bank between trips", position = 104, section = tripsSection)
	default BankStop bankStop()
	{
		return BankStop.NEAREST;
	}

	@ConfigItem(keyName = "compost", name = "Compost", description = "Compost to bring", position = 105, section = tripsSection)
	default CompostChoice compost()
	{
		return CompostChoice.SUPERCOMPOST;
	}

	@ConfigItem(keyName = "secateurs", name = "Secateurs", description = "Secateurs to bring for herbs and allotments", position = 106, section = tripsSection)
	default SecateursChoice secateurs()
	{
		return SecateursChoice.NONE;
	}

	@ConfigItem(keyName = "autoAdvance", name = "Auto-advance steps", description = "Move to the next step when you arrive at a later location", position = 107, section = displaySection)
	default boolean autoAdvance()
	{
		return true;
	}

	@ConfigItem(keyName = "highlightPatches", name = "Highlight patches", description = "Highlight farming patches at your current step", position = 108, section = displaySection)
	default boolean highlightPatches()
	{
		return true;
	}

	@ConfigItem(keyName = "highlightColor", name = "Highlight colour", description = "Colour of the patch highlight", position = 109, section = displaySection)
	default Color highlightColor()
	{
		return new Color(0, 255, 128);
	}

	@ConfigItem(keyName = "growthTimers", name = "Patch timers", description = "During a run, show time left above growing patches, and Ready, Diseased or Dead where a patch needs you", position = 110, section = displaySection)
	default boolean growthTimers()
	{
		return true;
	}

	@ConfigItem(keyName = "showPath", name = "Path to next click", description = "Highlight the tiles from you to the next patch, spirit tree, fairy ring or house object you need to click", position = 111, section = displaySection)
	default boolean showPath()
	{
		return true;
	}

	@ConfigItem(keyName = "pathColor", name = "Path colour", description = "Colour of the walking path tiles", position = 112, section = displaySection)
	default Color pathColor()
	{
		return new Color(0, 200, 255);
	}

	@ConfigItem(keyName = "spaceWarning", name = "Low space warning", description = "Warn you to note produce at the tool leprechaun when this many inventory slots or fewer are free and patches are left at your location (0 = off)", position = 113, section = displaySection)
	@Range(min = 0, max = 10)
	default int spaceWarning()
	{
		return 2;
	}

	@ConfigItem(keyName = "ownedBankItems", name = "", description = "", hidden = true)
	default String ownedBankItems()
	{
		return "";
	}

	@ConfigItem(keyName = "ownedVaultItems", name = "", description = "", hidden = true)
	default String ownedVaultItems()
	{
		return "";
	}
}

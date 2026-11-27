package com.farmrunhelper;

public enum Req
{
	FAIRY_RING("fairy ring access"),
	SPIRIT_TREES("Tree Gnome Village (spirit trees)"),
	SPIRIT_TREE_FARMING_GUILD("a spirit tree planted at the Farming Guild"),
	SPIRIT_TREE_HOSIDIUS("a spirit tree planted in Hosidius"),
	SPIRIT_TREE_PORT_SARIM("a spirit tree planted in Port Sarim"),
	SPIRIT_TREE_ETCETERIA("a spirit tree planted in Etceteria"),
	SPIRIT_TREE_BRIMHAVEN("a spirit tree planted in Brimhaven"),
	POH_TAVERLEY("house in Taverley"),
	POH_RIMMINGTON("house in Rimmington"),
	POH_HOSIDIUS("house in Hosidius"),
	POH_BRIMHAVEN("house in Brimhaven"),
	POH_PRIFDDINAS("house in Prifddinas"),
	POH_GLORY("a mounted glory"),
	POH_JEWELLERY_BOX("a jewellery box in your house"),
	POH_DIGSITE("a mounted digsite pendant"),
	POH_XERIC("a mounted Xeric's talisman"),
	POH_PORTAL_VARROCK("Varrock in your house (portal or nexus)"),
	POH_PORTAL_LUMBRIDGE("Lumbridge in your house (portal or nexus)"),
	POH_PORTAL_FALADOR("Falador in your house (portal or nexus)"),
	POH_PORTAL_CAMELOT("Camelot in your house (portal or nexus)"),
	POH_PORTAL_ARDOUGNE("Ardougne in your house (portal or nexus)"),
	POH_PORTAL_KHARYRLL("Kharyrll in your house (portal or nexus)"),
	POH_PORTAL_TROLL_STRONGHOLD("Troll Stronghold in your house (portal or nexus)"),
	Q_CHILDREN_OF_THE_SUN("Children of the Sun"),
	Q_RIBBITING_TALE("The Ribbiting Tale of a Lily Pad Labour Dispute"),
	Q_TROUBLED_TORTUGANS("Troubled Tortugans"),
	Q_SONG_OF_THE_ELVES("Song of the Elves"),
	Q_REGICIDE("Regicide"),
	Q_MY_ARMS_BIG_ADVENTURE("My Arm's Big Adventure"),
	Q_MAKING_FRIENDS_WITH_MY_ARM("Making Friends with My Arm"),
	Q_BONE_VOYAGE("Bone Voyage"),
	Q_GHOSTS_AHOY("Ghosts Ahoy"),
	DIARY_ARDOUGNE_MEDIUM("the medium Ardougne diary"),
	DIARY_LUMBRIDGE_MEDIUM("the medium Lumbridge & Draynor diary"),
	DIARY_KARAMJA_HARD("the hard Karamja diary"),
	DIARY_MORYTANIA_ELITE("the elite Morytania diary"),
	ANGLERS_ROWBOAT("the Corsair Cove rowboat");

	private final String description;

	Req(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}

	public boolean isMet(FarmRunHelperConfig c)
	{
		switch (this)
		{
			case FAIRY_RING:
				return c.fairyRingAccess() != FairyRingAccess.NONE;
			case SPIRIT_TREES:
				return c.questTreeGnomeVillage() == Completion.DONE;
			case SPIRIT_TREE_FARMING_GUILD:
				return c.spiritTreeFarmingGuild();
			case SPIRIT_TREE_HOSIDIUS:
				return c.spiritTreeHosidius();
			case SPIRIT_TREE_PORT_SARIM:
				return c.spiritTreePortSarim();
			case SPIRIT_TREE_ETCETERIA:
				return c.spiritTreeEtceteria();
			case SPIRIT_TREE_BRIMHAVEN:
				return c.spiritTreeBrimhaven();
			case POH_TAVERLEY:
				return c.houseLocation() == HouseLocation.TAVERLEY;
			case POH_RIMMINGTON:
				return c.houseLocation() == HouseLocation.RIMMINGTON;
			case POH_HOSIDIUS:
				return c.houseLocation() == HouseLocation.HOSIDIUS;
			case POH_BRIMHAVEN:
				return c.houseLocation() == HouseLocation.BRIMHAVEN;
			case POH_PRIFDDINAS:
				return c.houseLocation() == HouseLocation.PRIFDDINAS;
			case POH_GLORY:
				return c.mountedGlory();
			case POH_JEWELLERY_BOX:
				return c.jewelleryBox() != JewelleryBox.NONE;
			case POH_DIGSITE:
				return c.mountedDigsite();
			case POH_XERIC:
				return c.mountedXeric();
			case POH_PORTAL_VARROCK:
				return c.housePortalVarrock() != HousePortal.NONE;
			case POH_PORTAL_LUMBRIDGE:
				return c.housePortalLumbridge() != HousePortal.NONE;
			case POH_PORTAL_FALADOR:
				return c.housePortalFalador() != HousePortal.NONE;
			case POH_PORTAL_CAMELOT:
				return c.housePortalCamelot() != HousePortal.NONE;
			case POH_PORTAL_ARDOUGNE:
				return c.housePortalArdougne() != HousePortal.NONE;
			case POH_PORTAL_KHARYRLL:
				return c.housePortalKharyrll() != HousePortal.NONE;
			case POH_PORTAL_TROLL_STRONGHOLD:
				return c.housePortalTrollStronghold() != HousePortal.NONE;
			case Q_CHILDREN_OF_THE_SUN:
				return c.questChildrenOfTheSun() == Completion.DONE;
			case Q_RIBBITING_TALE:
				return c.questRibbitingTale() == Completion.DONE;
			case Q_TROUBLED_TORTUGANS:
				return c.questTroubledTortugans() == Completion.DONE;
			case Q_SONG_OF_THE_ELVES:
				return c.questSongOfTheElves() == Completion.DONE;
			case Q_REGICIDE:
				return c.questRegicide() == Completion.DONE;
			case Q_MY_ARMS_BIG_ADVENTURE:
				return c.questMyArmsBigAdventure() == Completion.DONE;
			case Q_MAKING_FRIENDS_WITH_MY_ARM:
				return c.questMakingFriendsWithMyArm() == Completion.DONE;
			case Q_BONE_VOYAGE:
				return c.questBoneVoyage() == Completion.DONE;
			case Q_GHOSTS_AHOY:
				return c.questGhostsAhoy() == Completion.DONE;
			case DIARY_ARDOUGNE_MEDIUM:
				return c.diaryArdougne().atLeast(DiaryTier.MEDIUM);
			case DIARY_LUMBRIDGE_MEDIUM:
				return c.diaryLumbridge().atLeast(DiaryTier.MEDIUM);
			case DIARY_KARAMJA_HARD:
				return c.diaryKaramja().atLeast(DiaryTier.HARD);
			case DIARY_MORYTANIA_ELITE:
				return c.diaryMorytania().atLeast(DiaryTier.ELITE);
			case ANGLERS_ROWBOAT:
				return c.anglersRowboat();
			default:
				return false;
		}
	}
}

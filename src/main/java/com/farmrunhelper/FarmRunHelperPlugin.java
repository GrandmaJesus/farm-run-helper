package com.farmrunhelper;

import com.google.gson.Gson;
import com.google.inject.Provides;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.EnumID;
import net.runelite.api.Constants;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.api.ItemComposition;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Player;
import net.runelite.api.ScriptID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.PostClientTick;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
	name = "Farm Run Helper",
	description = "Step-by-step herb, tree and custom farm runs built from your unlocks and teleports",
	tags = {"farming", "herb", "tree", "run", "birdhouse", "seaweed", "guide"}
)
public class FarmRunHelperPlugin extends Plugin
{
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FarmRunHelperPlugin.class);

	@Inject
	private Client client;

	@Inject
	private FarmRunHelperConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PatchHighlightOverlay overlay;

	@Inject
	private InventorySeedOverlay seedOverlay;

	@Inject
	private MenuHighlightOverlay menuOverlay;

	@Inject
	private LeprechaunOverlay leprechaunOverlay;

	@Inject
	private StorageItemOverlay storageOverlay;

	@Inject
	private Gson gson;

	// Treating a patch, casting Fertile Soil, being told it's already treated, or that it can't take compost
	// at all (like an anima patch): any of these means there's no compost left to do there
	private static final Pattern COMPOST_MESSAGE = Pattern.compile(
		"(You treat the .+ with (ultra|super|)compost)|(has been treated with (ultra|super|)compost)|(has already been (treated|fertilised) with)"
			+ "|(won't benefit from that kind of treatment)", Pattern.CASE_INSENSITIVE);

	private static final Set<Integer> HOUSE_TELEPORT_ITEMS = Set.of(
		ItemID.POH_TABLET_TELEPORTTOHOUSE, ItemID.SKILLCAPE_CONSTRUCTION, ItemID.SKILLCAPE_CONSTRUCTION_TRIMMED);

	private FarmRunPanel panel;
	private NavigationButton navButton;

	private final Map<GameObject, PatchType> patchObjects = new ConcurrentHashMap<>();
	// Other tiles of patches made of several objects (like allotments), kept in case the one in use goes away
	private final Map<GameObject, PatchType> spareTiles = new ConcurrentHashMap<>();
	private final Map<GameObject, Integer> patchSpawnTicks = new ConcurrentHashMap<>();
	private int gameTicks;
	private int lastModalTick;
	private boolean farmingVarbitsReady;

	/**
	 * Whether farming states can be trusted this tick: no modal interface open, and at least two ticks since
	 * one closed (the same rule as Time Tracking).
	 */
	private void updateFarmingVarbitsReady()
	{
		boolean modal = false;
		for (net.runelite.api.WidgetNode node : client.getComponentTable())
		{
			if (node.getModalMode() != net.runelite.api.widgets.WidgetModalMode.NON_MODAL)
			{
				modal = true;
				break;
			}
		}
		if (modal)
		{
			lastModalTick = gameTicks;
		}
		farmingVarbitsReady = !modal && gameTicks - lastModalTick > 1;
	}

	/**
	 * Whether you're standing in the stop this patch belongs to.
	 */
	private boolean inSameStop(WorldPoint patch)
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return false;
		}
		int here = player.getWorldLocation().getRegionID();
		for (FarmLocation location : FarmLocation.values())
		{
			if (location.containsRegion(patch.getRegionID()) && location.containsRegion(here))
			{
				return true;
			}
		}
		return false;
	}
	private final Map<TileObject, String> houseObjects = new ConcurrentHashMap<>();
	private volatile boolean inHouse;
	private final Map<TileObject, String> travelObjects = new ConcurrentHashMap<>();
	private final Map<NPC, String> travelNpcs = new ConcurrentHashMap<>();
	// The squirrels who look after the Fossil Island hardwood trees, and remove them for a fee
	private final Set<NPC> squirrels = ConcurrentHashMap.newKeySet();
	private volatile int legsDone = -1;
	private int nearLeg = -1;
	private final Set<Integer> seededThisRun = ConcurrentHashMap.newKeySet();
	private final Set<Integer> skippedSpaces = ConcurrentHashMap.newKeySet();
	private final Map<Integer, Integer> birdHouseValues = new ConcurrentHashMap<>();
	// When each bird house space last changed, saved to your profile like Time Tracking does (0 = unknown)
	private final Map<Integer, Long> birdHouseChanged = new ConcurrentHashMap<>();
	// When each space was found already changed on arriving at Fossil Island, this session
	private final Map<Integer, Long> birdHouseSeenAt = new ConcurrentHashMap<>();
	private boolean onFossilIsland;
	private int fossilTicks;
	private volatile long fossilArrivedAt;
	private volatile String mushtreeDestination;
	private volatile String birdHouseHint;
	private final Set<Integer> reopenedSteps = ConcurrentHashMap.newKeySet();
	private final Map<Integer, WorldPoint> spaceLocations = new ConcurrentHashMap<>();
	// The bird house area you were last working in, so the next one there is walked to, not reached by mushtree
	private int legsStep = -1;
	private WorldPoint lastPosition;
	private volatile List<int[]> path = Collections.emptyList();
	private final Set<Integer> bankItems = new HashSet<>();
	private final Set<Integer> vaultItems = new HashSet<>();
	private volatile Map<Integer, Integer> carriedItems = Collections.emptyMap();
	private Map<Integer, Integer> inventoryItems = Collections.emptyMap();
	private Map<Integer, Integer> wornItems = Collections.emptyMap();
	private volatile int freeSlots = 28;

	// Planting detection: whether each patch looked empty last tick, and what was planted this run
	private final Map<WorldPoint, Boolean> patchWasEmpty = new HashMap<>();
	private final Map<WorldPoint, Long> plantedPatches = new ConcurrentHashMap<>();
	private final Map<WorldPoint, PatchStatus> patchStatus = new ConcurrentHashMap<>();

	/**
	 * Every patch seen this run, one per farming state ("region:varbit"), with its last known state. Kept when
	 * the patch goes out of view, so a stop spread out like Catherby (the fruit tree is a walk from the
	 * allotments) can tell everything's done without having it all in view at once.
	 */
	private static final class SeenPatch
	{
		volatile WorldPoint point;
		final PatchType type;
		volatile PatchStatus status;

		SeenPatch(WorldPoint point, PatchType type)
		{
			this.point = point;
			this.type = type;
		}
	}

	private final Map<String, SeenPatch> seenPatches = new ConcurrentHashMap<>();
	private volatile String spaceWarningText;
	private final Set<WorldPoint> skippedPatches = ConcurrentHashMap.newKeySet();
	private volatile GameObject nextPatch;
	private volatile PatchType nextPatchType;
	private volatile boolean nextIsCompost;
	private final Set<WorldPoint> plantedThisRun = ConcurrentHashMap.newKeySet();
	private final Set<WorldPoint> compostedPatches = ConcurrentHashMap.newKeySet();

	private volatile Route route;
	private volatile int currentStep;
	private volatile int farmingLevel;
	private volatile int craftingLevel;
	private volatile int hunterLevel;
	private volatile String panelMessage;
	private int lastRegion = -1;

	@Provides
	FarmRunHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FarmRunHelperConfig.class);
	}

	@Override
	protected void startUp()
	{
		readIds(config.ownedBankItems(), bankItems);
		readIds(config.ownedVaultItems(), vaultItems);

		panel = new FarmRunPanel(this);
		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "icon.png");
		navButton = NavigationButton.builder()
			.tooltip("Farm Run Helper")
			.icon(icon)
			.priority(7)
			.panel(panel)
			.build();
		clientToolbar.addNavigation(navButton);
		overlayManager.add(overlay);
		overlayManager.add(seedOverlay);
		overlayManager.add(menuOverlay);
		overlayManager.add(leprechaunOverlay);
		overlayManager.add(storageOverlay);
	}

	@Override
	protected void shutDown()
	{
		clientToolbar.removeNavigation(navButton);
		overlayManager.remove(overlay);
		overlayManager.remove(seedOverlay);
		overlayManager.remove(menuOverlay);
		overlayManager.remove(leprechaunOverlay);
		overlayManager.remove(storageOverlay);
		patchObjects.clear();
		spareTiles.clear();
		patchWasEmpty.clear();
		patchStatus.clear();
		plantedPatches.clear();
		skippedPatches.clear();
		plantedThisRun.clear();
		seenPatches.clear();
		legsDone = -1;
		legsStep = -1;
		reopenedSteps.clear();
		seededThisRun.clear();
		skippedSpaces.clear();
		mushtreeDestination = null;
		birdHouseHint = null;
		compostedPatches.clear();
		nextIsCompost = false;
		nextPatch = null;
		nextPatchType = null;
		route = null;
		panel = null;
	}

	// ---- Run control (called from the Swing thread)

	void startRun(RunType type)
	{
		planGrowing = stillGrowingPatches();
		route = new RoutePlanner(config, farmingLevel, hunterLevel, ownedItems(), planGrowing).plan(type);
		currentStep = 0;
		panelMessage = null;
		lastRegion = -1;
		plantedPatches.clear();
		skippedPatches.clear();
		plantedThisRun.clear();
		seenPatches.clear();
		legsDone = -1;
		legsStep = -1;
		reopenedSteps.clear();
		seededThisRun.clear();
		skippedSpaces.clear();
		mushtreeDestination = null;
		birdHouseHint = null;
		compostedPatches.clear();
		nextIsCompost = false;
		nextPatch = null;
		nextPatchType = null;
		spaceWarningText = null;
		refreshPanel();
	}

	void nextStep()
	{
		Route r = route;
		if (r == null)
		{
			return;
		}
		List<RouteStep> steps = r.getSteps();
		if (currentStep < steps.size())
		{
			steps.get(currentStep).setDone(true);
		}
		if (currentStep < steps.size() - 1)
		{
			currentStep++;
		}
		refreshPanel();
	}

	void previousStep()
	{
		if (route == null || currentStep == 0)
		{
			return;
		}
		currentStep--;
		route.getSteps().get(currentStep).setDone(false);
		reopenedSteps.add(currentStep);
		refreshPanel();
	}

	void setStepDone(int index, boolean done)
	{
		Route r = route;
		if (r == null || index < 0 || index >= r.getSteps().size())
		{
			return;
		}
		if (done && r.getSteps().get(index).getKind() == RouteStep.Kind.FINISH)
		{
			// Ticking Finish closes the run
			resetRun();
			return;
		}
		r.getSteps().get(index).setDone(done);
		if (done && index == currentStep && currentStep < r.getSteps().size() - 1)
		{
			currentStep++;
		}
		else if (!done && index < currentStep)
		{
			currentStep = index;
		}
		if (!done)
		{
			reopenedSteps.add(index);
			r.getSteps().get(index).setNote(null);
		}
		refreshPanel();
	}

	void resetRun()
	{
		route = null;
		path = Collections.emptyList();
		currentStep = 0;
		panelMessage = null;
		plantedPatches.clear();
		skippedPatches.clear();
		plantedThisRun.clear();
		seenPatches.clear();
		legsDone = -1;
		legsStep = -1;
		reopenedSteps.clear();
		seededThisRun.clear();
		skippedSpaces.clear();
		mushtreeDestination = null;
		birdHouseHint = null;
		compostedPatches.clear();
		nextIsCompost = false;
		nextPatch = null;
		nextPatchType = null;
		spaceWarningText = null;
		refreshPanel();
	}

	void copyCurrentTripSetup()
	{
		Route r = route;
		RouteStep step = getCurrentStep();
		if (r == null || step == null)
		{
			return;
		}
		Trip trip = r.getTrip(step.getTripIndex());
		SetupExporter.ExportResult result = new SetupExporter(gson).export(r, trip);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(result.getJson()), null);
		panelMessage = "Trip " + trip.getIndex() + " setup copied. Import it in Inventory Setups."
			+ (result.isTruncated() ? " Some items didn't fit in 28 slots, so lower \"Inventory slots per trip\"." : "");
		refreshPanel();
	}

	void setSeed(SeedSetting setting, SeedOption option)
	{
		configManager.setConfiguration(FarmRunHelperConfig.GROUP, setting.getConfigKey(), option.name());
	}

	void setTeleport(FarmLocation location, TeleportOption option)
	{
		configManager.setConfiguration(FarmRunHelperConfig.GROUP, location.getTeleportConfigKey(), option.name());
	}

	// ---- State for the panel and overlay

	FarmRunHelperConfig getConfig()
	{
		return config;
	}

	/**
	 * Whether the bank or seed vault has been seen, so item ownership can be trusted.
	 */
	boolean hasSeenStorage()
	{
		return !bankItems.isEmpty() || !vaultItems.isEmpty();
	}

	boolean ownsItem(int itemId)
	{
		return bankItems.contains(itemId) || vaultItems.contains(itemId) || carriedItems.containsKey(itemId)
			|| leprechaunItems.containsKey(itemId);
	}

	/**
	 * Where you keep an item: "bank", "seed vault" and/or "tool leprechaun", or null if it isn't known to be in any.
	 */
	String getItemSources(int itemId)
	{
		List<String> sources = new ArrayList<>();
		if (bankItems.contains(itemId) || (itemId == ItemID.BOTTOMLESS_COMPOST_BUCKET_FILLED && bankItems.contains(ItemID.BOTTOMLESS_COMPOST_BUCKET)))
		{
			sources.add("bank");
		}
		if (pouchContents.containsKey(itemId))
		{
			boolean pouchInBank = false;
			for (int pouch : RUNE_POUCHES)
			{
				pouchInBank |= bankItems.contains(pouch) && !inventoryItems.containsKey(pouch);
			}
			if (pouchInBank)
			{
				sources.add("rune pouch (bank)");
			}
		}
		if (itemId == ItemID.BRONZE_AXE && !sources.contains("bank") && bankHasAxe)
		{
			sources.add("bank");
		}
		if (vaultItems.contains(itemId))
		{
			sources.add("seed vault");
		}
		if (leprechaunItems.containsKey(itemId))
		{
			sources.add("tool leprechaun");
		}
		return sources.isEmpty() ? null : String.join(", ", sources);
	}

	// ---- Tool leprechaun storage (the game keeps it in varbits, so it's known without opening the store)

	private volatile Map<Integer, Integer> leprechaunItems = Collections.emptyMap();

	private void updateLeprechaunItems()
	{
		Map<Integer, Integer> items = new HashMap<>();
		putIfStored(items, ItemID.RAKE, client.getVarbitValue(VarbitID.FARMING_TOOLS_RAKE) + client.getVarbitValue(VarbitID.FARMING_TOOLS_EXTRARAKES));
		putIfStored(items, ItemID.DIBBER, client.getVarbitValue(VarbitID.FARMING_TOOLS_DIBBER) + client.getVarbitValue(VarbitID.FARMING_TOOLS_EXTRADIBBERS));
		putIfStored(items, ItemID.SPADE, client.getVarbitValue(VarbitID.FARMING_TOOLS_SPADE) + client.getVarbitValue(VarbitID.FARMING_TOOLS_EXTRASPADES));
		int secateurs = client.getVarbitValue(VarbitID.FARMING_TOOLS_SECATEURS) + client.getVarbitValue(VarbitID.FARMING_TOOLS_EXTRASECATEURS);
		putIfStored(items, client.getVarbitValue(VarbitID.FARMING_TOOLS_FAIRYSECATEURS) > 0 ? ItemID.FAIRY_ENCHANTED_SECATEURS : ItemID.SECATEURS, secateurs);
		putIfStored(items, ItemID.BUCKET_COMPOST, client.getVarbitValue(VarbitID.FARMING_TOOLS_COMPOST)
			+ 256 * client.getVarbitValue(VarbitID.FARMING_TOOLS_EXTRACOMPOST));
		putIfStored(items, ItemID.BUCKET_SUPERCOMPOST, client.getVarbitValue(VarbitID.FARMING_TOOLS_SUPERCOMPOST)
			+ 256 * client.getVarbitValue(VarbitID.FARMING_TOOLS_EXTRASUPERCOMPOST));
		putIfStored(items, ItemID.BUCKET_ULTRACOMPOST, client.getVarbitValue(VarbitID.FARMING_TOOLS_ULTRACOMPOST));
		putIfStored(items, ItemID.BOTTOMLESS_COMPOST_BUCKET_FILLED, client.getVarbitValue(VarbitID.FARMING_TOOLS_BOTTOMLESS_BUCKET_TYPE));
		if (!items.equals(leprechaunItems))
		{
			leprechaunItems = items;
			if (route != null)
			{
				refreshPanel();
			}
		}
	}

	private static void putIfStored(Map<Integer, Integer> items, int itemId, int amount)
	{
		if (amount > 0)
		{
			items.put(itemId, amount);
		}
	}

	// ---- Bottomless compost bucket: what's in it, from the message when you check it

	// Loose on purpose: any message about the bucket with an amount and a kind of compost, wherever it's shown
	private static final Pattern BUCKET_CHECK = Pattern.compile(
		"bottomless compost bucket.*?\\b(a single|one|[\\d,]+)\\b.*?\\b(ultra|super|)compost", Pattern.CASE_INSENSITIVE);
	// "Your compost bucket is currently empty."
	private static final Pattern BUCKET_EMPTY = Pattern.compile("compost bucket.*\\bempty\\b", Pattern.CASE_INSENSITIVE);
	private static final Pattern BUCKET_TOTAL = Pattern.compile("compost bucket now contains a total of ([\\d,]+) uses?", Pattern.CASE_INSENSITIVE);
	private static final Pattern BUCKET_KIND = Pattern.compile("\\b(ultra|super|)compost\\b(?! bucket)", Pattern.CASE_INSENSITIVE);
	private String lastBucketDialog;

	/**
	 * Checking the bucket can show its contents in a dialog box (with the bucket's picture) rather than in chat.
	 */
	private void readBucketDialog()
	{
		String text = null;
		for (int id : new int[]{InterfaceID.Objectbox.TEXT, InterfaceID.Messagebox.TEXT})
		{
			Widget widget = client.getWidget(id);
			if (widget != null && !widget.isHidden() && widget.getText() != null
				&& widget.getText().toLowerCase().contains("compost bucket"))
			{
				text = widget.getText().replace("<br>", " ");
			}
		}
		if (text != null && !text.equals(lastBucketDialog))
		{
			readBucketMessage(text);
		}
		lastBucketDialog = text;
	}
	private volatile String bucketType;
	private volatile int bucketUses = -1;

	/**
	 * Short text for the bottomless bucket in your inventory, like "U 23" for 23 uses of ultracompost, once you've checked it.
	 */
	String getBucketLabel()
	{
		if (bucketUses < 0)
		{
			String saved = configManager.getRSProfileConfiguration(FarmRunHelperConfig.GROUP, "bottomlessbucket");
			if (saved == null)
			{
				return null;
			}
			String[] parts = saved.split(":");
			try
			{
				bucketType = parts[0];
				bucketUses = Integer.parseInt(parts[1]);
			}
			catch (RuntimeException e)
			{
				return null;
			}
		}
		return bucketUses == 0 ? "Empty" : bucketType + " " + bucketUses;
	}

	private void setBucket(String type, int uses)
	{
		bucketType = type;
		bucketUses = uses;
		configManager.setRSProfileConfiguration(FarmRunHelperConfig.GROUP, "bottomlessbucket", type + ":" + uses);
	}

	private void readBucketMessage(String message)
	{
		String plain = message.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ");
		// Filling it: "You fill your bottomless compost bucket with a single bucket of ultracompost.
		// Your bottomless compost bucket now contains a total of 4 uses."
		Matcher total = BUCKET_TOTAL.matcher(plain);
		if (total.find())
		{
			Matcher kind = BUCKET_KIND.matcher(plain);
			String type = kind.find() ? (kind.group(1).isEmpty() ? "C" : kind.group(1).equalsIgnoreCase("super") ? "S" : "U")
				: (bucketType != null && !bucketType.isEmpty() ? bucketType : "?");
			setBucket(type, Integer.parseInt(total.group(1).replace(",", "")));
			return;
		}
		Matcher check = BUCKET_CHECK.matcher(plain);
		if (check.find())
		{
			String amount = check.group(1).toLowerCase();
			int uses = amount.startsWith("a") || amount.equals("one") ? 1 : Integer.parseInt(amount.replace(",", ""));
			String type = check.group(2).isEmpty() ? "C" : check.group(2).equalsIgnoreCase("super") ? "S" : "U";
			setBucket(type, uses);
			return;
		}
		if (BUCKET_EMPTY.matcher(plain).find())
		{
			setBucket("", 0);
			return;
		}
		// Treating a patch from the bucket uses one
		Matcher treat = Pattern.compile("You treat the .+ with (ultra|super|)compost").matcher(plain);
		if (treat.find() && getBucketLabel() != null && bucketUses > 0
			&& inventoryItems.containsKey(ItemID.BOTTOMLESS_COMPOST_BUCKET_FILLED) && config.compost() == CompostChoice.BOTTOMLESS_BUCKET)
		{
			setBucket(bucketType, bucketUses - 1);
		}
	}

	int getFarmingLevel()
	{
		return farmingLevel;
	}

	int getCraftingLevel()
	{
		return craftingLevel;
	}

	int getHunterLevel()
	{
		return hunterLevel;
	}

	Route getRoute()
	{
		return route;
	}

	int getCurrentStepIndex()
	{
		return currentStep;
	}

	RouteStep getCurrentStep()
	{
		Route r = route;
		if (r == null || currentStep < 0 || currentStep >= r.getSteps().size())
		{
			return null;
		}
		return r.getSteps().get(currentStep);
	}

	Map<Integer, Integer> getCarriedItems()
	{
		return carriedItems;
	}

	Map<GameObject, PatchType> getPatchObjects()
	{
		return patchObjects;
	}

	String getPanelMessage()
	{
		return panelMessage;
	}

	String getSpaceWarning()
	{
		return spaceWarningText;
	}

	/**
	 * The patch's current state, or null if it hasn't been read yet.
	 */
	PatchStatus getPatchStatus(WorldPoint point)
	{
		return patchStatus.get(point);
	}

	// ---- Travel highlights

	/**
	 * Whether you're on your way to the current step's location (a farming stop you haven't reached yet).
	 */
	/**
	 * Whether you're inside a player-owned house: a mounted item, nexus or teleport portal is loaded,
	 * or you're in an instanced area with an exit portal.
	 */
	private boolean isInHouse()
	{
		// A house is always an instanced area, so the entrance portal outside never counts
		WorldView view = client.getTopLevelWorldView();
		return view != null && view.isInstance() && (inHouse || houseObjects.containsValue("portal"));
	}

	/**
	 * Whether your house reaches a teleport portal's destination by its own portal or the portal nexus.
	 */
	static HousePortal housePortal(FarmRunHelperConfig config, Req req)
	{
		switch (req)
		{
			case POH_PORTAL_VARROCK:
				return config.housePortalVarrock();
			case POH_PORTAL_LUMBRIDGE:
				return config.housePortalLumbridge();
			case POH_PORTAL_FALADOR:
				return config.housePortalFalador();
			case POH_PORTAL_CAMELOT:
				return config.housePortalCamelot();
			case POH_PORTAL_ARDOUGNE:
				return config.housePortalArdougne();
			case POH_PORTAL_KHARYRLL:
				return config.housePortalKharyrll();
			case POH_PORTAL_TROLL_STRONGHOLD:
				return config.housePortalTrollStronghold();
			default:
				return HousePortal.NONE;
		}
	}

	private boolean isTravelling()
	{
		RouteStep step = getCurrentStep();
		Player player = client.getLocalPlayer();
		if (step == null || step.getKind() != RouteStep.Kind.LOCATION || step.getTravel() == null || player == null)
		{
			return false;
		}
		if (!step.getLocation().containsRegion(player.getWorldLocation().getRegionID()))
		{
			return true;
		}
		// In the stop's map area but not there yet: part of the route is still to do, like the cave at Weiss
		// after Larry's boat drops you on the other side of the mountain
		List<String> legs = TravelLegs.legs(step.getLocation(), step.getTravel(), config);
		int next = legsDone + 1;
		return legsDone >= 0 && legsStep == currentStep && next < legs.size() && !TravelLegs.isTeleportLeg(legs.get(next))
			&& !loadedLeg(legs.get(next)).isEmpty();
	}

	/**
	 * Items to outline in the inventory and equipment: the teleport for the next stop while travelling
	 * (a house teleport if the route goes through your house), otherwise the seed or compost for the next patch.
	 */
	Set<Integer> getHighlightItems()
	{
		RouteStep step = getCurrentStep();
		if (isTravelling())
		{
			TeleportOption travel = step.getTravel();
			if (alreadyThere(travel))
			{
				return Collections.emptySet();
			}
			if (TravelKind.usesHouseObject(travel))
			{
				return isInHouse() ? Collections.emptySet() : HOUSE_TELEPORT_ITEMS;
			}
			Set<Integer> ids = new HashSet<>();
			for (int id : travel.getItemIds())
			{
				ids.add(id);
			}
			// Spirit tree or fairy ring in your house: teleport there unless one is already nearby
			if (TravelKind.usesHouse(travel, config) && !isInHouse() && getTravelTargets().isEmpty())
			{
				ids.addAll(HOUSE_TELEPORT_ITEMS);
			}
			return ids;
		}
		return getNextSeedItems();
	}

	/**
	 * Whether the route's first teleport can be skipped because you're already where it goes: routes like
	 * "Digsite pendant -> Fossil Island (skip if already there)" when you're on Fossil Island.
	 */
	private boolean alreadyThere(TeleportOption travel)
	{
		Player player = client.getLocalPlayer();
		if (!travel.getInstruction().contains("(skip if already there)") || player == null)
		{
			return false;
		}
		RouteStep step = getCurrentStep();
		WorldPoint here = player.getWorldLocation();
		return BirdHouses.zone(here) != null
			|| (step != null && step.getLocation() != null && step.getLocation().containsRegion(here.getRegionID()));
	}

	/**
	 * Objects to highlight inside your house: the mounted item or portal the route uses, or the exit portal.
	 */
	List<TileObject> getHouseTargets()
	{
		if (!isInHouse() || !isTravelling())
		{
			return Collections.emptyList();
		}
		TeleportOption travel = getCurrentStep().getTravel();
		if (!TravelKind.usesHouseObject(travel))
		{
			// Spirit tree and fairy ring routes highlight the tree or ring itself
			return Collections.emptyList();
		}
		String keyword = houseKeyword(travel);
		if (keyword == null)
		{
			return Collections.emptyList();
		}
		List<TileObject> targets = new ArrayList<>();
		for (Map.Entry<TileObject, String> entry : houseObjects.entrySet())
		{
			String name = entry.getValue();
			boolean match;
			if (keyword.equals("portal"))
			{
				match = name.equals("portal");
			}
			else if (keyword.endsWith(" portal"))
			{
				// A portal in the portal chamber: its name has the destination and "portal", e.g. "Camelot Portal"
				match = name.contains(keyword.substring(0, keyword.length() - " portal".length()))
					&& name.contains("portal") && !name.contains("nexus");
			}
			else
			{
				match = name.contains(keyword);
			}
			if (match)
			{
				targets.add(entry.getKey());
			}
		}
		return targets;
	}

	/**
	 * The next thing to click on the way to the next stop: a spirit tree, fairy ring, mushtree, boat or
	 * the person who sails it. Later parts of the route win when several are in view, and anything you've
	 * just teleported from is skipped, so arriving at a fairy ring moves the highlight on to what's next.
	 */
	List<Object> getTravelTargets()
	{
		if (!isTravelling())
		{
			return Collections.emptyList();
		}
		RouteStep step = getCurrentStep();
		List<String> legs = TravelLegs.legs(step.getLocation(), step.getTravel(), config);
		int leg = currentLeg(legs);
		return leg < legs.size() ? loadedLeg(legs.get(leg)) : Collections.emptyList();
	}

	/**
	 * Which part of the route is next: the latest one in view that you haven't done, or else the one after the last done.
	 */
	private int currentLeg(List<String> legs)
	{
		// Legs done only count for the step they were done on, and never past the end of this route
		int done = legsStep == currentStep ? Math.min(legsDone, legs.size() - 1) : -1;
		// Don't jump past a magic mushtree you still need to take: the rowboat to the seaweed is in view
		// from where the digsite pendant lands, but the mushtree is the way there
		int mushtree = legs.subList(done + 1, legs.size()).indexOf(TravelLegs.MUSHTREE);
		int last = mushtree >= 0 ? done + 1 + mushtree : legs.size() - 1;
		Player me = client.getLocalPlayer();
		WorldPoint here = me != null ? me.getWorldLocation() : null;
		for (int i = last; i > done; i--)
		{
			// Only jump ahead to something specific (a named person or object). Loose matches like any cave
			// entrance only count once everything before them is done, so unrelated ones nearby are ignored
			if (legs.get(i).startsWith("~") && i != done + 1)
			{
				continue;
			}
			// Something at a known spot only counts once you're there, not just when it's in view: the seaweed
			// rowboat out at sea can be seen from the shore rowboat you need first
			WorldPoint at = TravelLegs.position(legs.get(i));
			if (i != done + 1 && at != null && (here == null || here.distanceTo2D(at) > 15))
			{
				continue;
			}
			if (!loadedLeg(legs.get(i)).isEmpty())
			{
				return i;
			}
		}
		// Something at a known spot far from you (the cave at Weiss while you're still in Rellekka) can't be
		// next: you haven't taken the boat that gets you there yet
		int next = done + 1;
		Player player = client.getLocalPlayer();
		WorldPoint spot = next < legs.size() ? TravelLegs.position(legs.get(next)) : null;
		if (spot != null && next > 0 && player != null && player.getWorldLocation().distanceTo2D(spot) > 100)
		{
			return next - 1;
		}
		return next;
	}

	private static final Pattern FAIRY_CODE = Pattern.compile("Fairy ring ([A-Z]{3})");
	private static final Pattern SPELL = Pattern.compile("^([A-Z][A-Za-z' ]*? Teleport)\\b");
	private static final Pattern ARROW = Pattern.compile("-> ([^,()>-]+)");

	/**
	 * The spell to cast, from routes that start like "Camelot Teleport, run east".
	 */
	private static String spellName(String instruction)
	{
		Matcher matcher = SPELL.matcher(instruction);
		return matcher.find() ? matcher.group(1) : null;
	}

	/**
	 * The destination to pick in a teleport menu, from routes like "Skills necklace -> Farming Guild",
	 * "House -> mounted glory -> Draynor Village" or "House -> Camelot portal" with a portal nexus.
	 * Null when the route has no menu to pick from.
	 */
	private String teleportDestination(TeleportOption travel)
	{
		String instruction = travel.getInstruction();
		if (instruction.startsWith("Fairy ring") || instruction.startsWith("Quetzal whistle"))
		{
			return null;
		}
		// Only the first part of the route ("Digsite pendant -> Fossil Island, magic mushtree -> ...")
		int comma = instruction.indexOf(',');
		String first = comma >= 0 ? instruction.substring(0, comma) : instruction;
		String last = null;
		Matcher matcher = ARROW.matcher(first);
		while (matcher.find())
		{
			last = matcher.group(1).trim();
		}
		if (last == null)
		{
			return null;
		}
		int condition = last.indexOf(" if ");
		if (condition >= 0)
		{
			last = last.substring(0, condition);
		}
		if (last.toLowerCase().endsWith(" portal"))
		{
			// A portal space is clicked directly; the nexus has a list to pick the destination from
			for (Req req : travel.getRequirements())
			{
				if (req.name().startsWith("POH_PORTAL_") && housePortal(config, req) == HousePortal.NEXUS)
				{
					return last.substring(0, last.length() - " portal".length());
				}
			}
			return null;
		}
		if (last.startsWith("Ring of dueling: "))
		{
			last = last.substring("Ring of dueling: ".length());
		}
		return last;
	}
	private static final Pattern MUSHTREE_TO = Pattern.compile("magic mushtree -> ([A-Za-z' ]+?)\\s*(\\(|,|$)");
	private static final Pattern QUETZAL_TO = Pattern.compile("Quetzal whistle -> ([A-Za-z' ]+?)\\s*(\\(|,|$)");

	/**
	 * Options to highlight in game menus for the next part of the route.
	 */
	List<MenuTarget> getMenuTargets()
	{
		List<MenuTarget> targets = new ArrayList<>();
		String mushtree = mushtreeDestination;
		if (mushtree != null)
		{
			targets.add(new MenuTarget(mushtree, false, InterfaceID.FOSSIL_MUSHTREES));
		}
		if (!isTravelling())
		{
			return targets;
		}
		RouteStep step = getCurrentStep();
		TeleportOption travel = step.getTravel();
		String instruction = travel.getInstruction();
		List<String> legs = TravelLegs.legs(step.getLocation(), travel, config);
		int index = currentLeg(legs);
		String leg = index < legs.size() ? legs.get(index) : null;

		// The first teleport: the spell in your spellbook, or the destination in the item's or house
		// object's menu (jewellery, Xeric's talisman, jewellery box, portal nexus, spirit tree)
		if (legsDone < 0 && !alreadyThere(travel))
		{
			String spell = spellName(instruction);
			if (spell != null)
			{
				targets.add(new MenuTarget(spell, false, InterfaceID.MAGIC_SPELLBOOK));
			}
			if (TravelKind.usesHouse(travel, config) && !isInHouse())
			{
				targets.add(new MenuTarget("Teleport to House", false, InterfaceID.MAGIC_SPELLBOOK));
			}
			String destination = teleportDestination(travel);
			if (destination != null)
			{
				targets.add(new MenuTarget(destination, false, InterfaceID.CHATMENU, InterfaceID.MENU,
					InterfaceID.POH_JEWELLERY_BOX, InterfaceID.TELENEXUS_TELEPORT));
			}
		}

		Matcher quetzal = QUETZAL_TO.matcher(instruction);
		if (quetzal.find() && legsDone < 0)
		{
			targets.add(new MenuTarget(quetzal.group(1).trim(), false, InterfaceID.QUETZALWHISTLE_MENU, InterfaceID.QUETZAL_MENU));
		}
		Matcher fairy = FAIRY_CODE.matcher(instruction);
		if (fairy.find() && (leg == null || leg.equals(TravelLegs.FAIRY_RING)))
		{
			targets.add(new MenuTarget(fairy.group(1), true, InterfaceID.FAIRYRINGS_LOG, InterfaceID.FAIRYRINGS));
		}
		Matcher mush = MUSHTREE_TO.matcher(instruction);
		if (mush.find() && TravelLegs.MUSHTREE.equals(leg))
		{
			targets.add(new MenuTarget(mush.group(1).trim(), false, InterfaceID.FOSSIL_MUSHTREES));
		}
		if (leg != null)
		{
			for (String option : TravelLegs.dialogueOptions(leg))
			{
				targets.add(new MenuTarget(option, false, InterfaceID.CHATMENU));
			}
		}
		// A boat trip can count as the leg being done before its last dialogue (like "Dive" after
		// rowing out to sea), so keep the step's last leg's options highlighted once the legs are done
		if (legsDone >= 0 && !legs.isEmpty() && !legs.get(legs.size() - 1).equals(leg))
		{
			for (String option : TravelLegs.dialogueOptions(legs.get(legs.size() - 1)))
			{
				targets.add(new MenuTarget(option, false, InterfaceID.CHATMENU));
			}
		}
		return targets;
	}

	/**
	 * Items for the current trip you still need to take out of the bank or seed vault.
	 */
	Set<Integer> getWithdrawItems()
	{
		Route r = route;
		RouteStep step = getCurrentStep();
		if (r == null || step == null || step.getKind() == RouteStep.Kind.FINISH)
		{
			return Collections.emptySet();
		}
		Trip trip = r.getTrip(step.getTripIndex());
		if (trip == null)
		{
			return Collections.emptySet();
		}
		Map<Integer, Integer> carried = carriedItems;
		Set<Integer> ids = new HashSet<>();
		trip.getItems().forEach((id, need) ->
		{
			if (carried.getOrDefault(id, 0) < need)
			{
				ids.add(id);
			}
		});
		for (int id : trip.getWorn().values())
		{
			if (!carried.containsKey(id))
			{
				ids.add(id);
			}
		}
		// Runes you still need that are in your rune pouch: take the pouch
		Map<Integer, Integer> pouch = pouchContents;
		for (int id : new ArrayList<>(ids))
		{
			if (pouch.containsKey(id))
			{
				for (int pouchId : RUNE_POUCHES)
				{
					ids.add(pouchId);
				}
				break;
			}
		}
		return ids;
	}

	// ---- Stops that are still growing (from Time Tracking's saved patches)

	/**
	 * Before you get to a stop, skips it if Time Tracking says everything there is still growing.
	 * Returns whether the current step changed.
	 */
	private boolean skipGrowingStops(Route r)
	{
		boolean skipped = false;
		List<RouteStep> steps = r.getSteps();
		while (currentStep < steps.size() - 1)
		{
			RouteStep step = steps.get(currentStep);
			// Once you're in a farming stop's map area (even with part of the route still to do, like the cave
			// at Weiss), what you can see there counts, not Time Tracking's guess, which can be out of date or a
			// tick out. Only bird houses are skipped from there, since their state comes straight from the game
			Player me = client.getLocalPlayer();
			boolean there = me != null && step.getLocation() != null && step.getLocation().containsRegion(me.getWorldLocation().getRegionID());
			if (reopenedSteps.contains(currentStep) || step.getKind() != RouteStep.Kind.LOCATION
				|| (there && (workedAt(step) || !step.getPatchTypes().equals(EnumSet.of(PatchType.BIRDHOUSE)))))
			{
				// You went back to this stop yourself, so don't skip it again
				break;
			}
			long readyAt = stillGrowingUntil(step);
			if (readyAt <= 0)
			{
				break;
			}
			Player me2 = client.getLocalPlayer();
			log.debug("Farm Run Helper: {} skipped as still growing (Time Tracking) at {}, ready at {}", step.getTitle(),
				me2 != null ? me2.getWorldLocation() : null, new java.util.Date(readyAt));
			step.setDone(true);
			step.setNote("Skipped: still growing, ready in " + GrowthTimes.format(readyAt - System.currentTimeMillis()).replace("~", "about "));
			currentStep++;
			skipped = true;
		}
		if (skipped)
		{
			nextPatch = null;
			nextPatchType = null;
			nextIsCompost = false;
			refreshPanel();
		}
		return skipped;
	}

	// What was left out as still growing when the run was planned, kept for replanning after a settings change
	private volatile Map<FarmLocation, Map<PatchType, Integer>> planGrowing = Collections.emptyMap();

	/**
	 * How many patches of each type at each stop Time Tracking says are still growing (and bird houses still filling).
	 */
	private Map<FarmLocation, Map<PatchType, Integer>> stillGrowingPatches()
	{
		Map<FarmLocation, Map<PatchType, Integer>> result = new HashMap<>();
		long now = System.currentTimeMillis();
		int offset = farmTickOffset();
		for (FarmLocation location : FarmLocation.values())
		{
			for (TimeTrackingPatches.Entry entry : TimeTrackingPatches.ALL)
			{
				if (!location.getPatches().containsKey(entry.type) || !containsRegion(location, entry.region))
				{
					continue;
				}
				String saved = configManager.getRSProfileConfiguration("timetracking", entry.region + "." + entry.varbit);
				if (saved == null)
				{
					continue;
				}
				try
				{
					String[] parts = saved.split(":");
					PatchStatus status = PatchStatus.predict(entry.type, Integer.parseInt(parts[0]), Long.parseLong(parts[1]), now, offset);
					if (status != null && status.getKind() == PatchStatus.Kind.GROWING && status.getReadyAt() > now)
					{
						result.computeIfAbsent(location, l -> new EnumMap<>(PatchType.class)).merge(entry.type, 1, Integer::sum);
					}
				}
				catch (RuntimeException ignored)
				{
					// bad saved value: treat as unknown
				}
			}
			if (location.getPatches().containsKey(PatchType.BIRDHOUSE))
			{
				int filling = 0;
				for (int space : BirdHouses.SPACES)
				{
					loadBirdHouse(space);
					if (BirdHouses.isSeeded(birdHouseValues.getOrDefault(space, 0)) && birdHouseFullAt(space) > now)
					{
						filling++;
					}
				}
				// Bird houses are done as a set: only leave them out if all four are filling
				if (filling == BirdHouses.SPACES.length)
				{
					result.computeIfAbsent(location, l -> new EnumMap<>(PatchType.class)).put(PatchType.BIRDHOUSE, filling);
				}
			}
		}
		return result;
	}

	/**
	 * Time Tracking can't see a patch die, get diseased or be picked while you're away, so something it
	 * guessed was still growing may need doing after all. Once you're at the stop and can see it, put
	 * any of those back into the step.
	 */
	private void addBackWrongGuesses(Player player)
	{
		RouteStep step = getCurrentStep();
		if (step == null || step.getKind() != RouteStep.Kind.LOCATION || step.getLocation() == null
			|| !step.getLocation().containsRegion(player.getWorldLocation().getRegionID()))
		{
			return;
		}
		Map<PatchType, Integer> leftOut = planGrowing.getOrDefault(step.getLocation(), Collections.emptyMap());
		if (leftOut.isEmpty())
		{
			return;
		}
		for (Map.Entry<GameObject, PatchType> entry : patchObjects.entrySet())
		{
			PatchType type = entry.getValue();
			WorldPoint point = entry.getKey().getWorldLocation();
			if (!leftOut.containsKey(type) || step.getPatchTypes().contains(type) || type == PatchType.BIRDHOUSE
				|| !step.getLocation().containsRegion(point.getRegionID()))
			{
				continue;
			}
			PatchStatus status = patchStatus.get(point);
			if (status != null && status.getKind() != PatchStatus.Kind.GROWING)
			{
				step.addPatchType(type);
				step.addLine(type.getLabel() + ": Time Tracking thought this was still growing, but it needs doing"
					+ (status.getKind() == PatchStatus.Kind.READY ? " (ready)" : status.getKind() == PatchStatus.Kind.DEAD ? " (dead)"
					: status.getKind() == PatchStatus.Kind.DISEASED ? " (diseased)" : " (empty)") + ". Its seeds weren't in the trip.");
				refreshPanel();
			}
		}
	}

	private String describeSeen(RouteStep step)
	{
		StringBuilder out = new StringBuilder();
		for (Map.Entry<String, SeenPatch> entry : seenPatches.entrySet())
		{
			SeenPatch seen = entry.getValue();
			if (step.getLocation() != null && step.getLocation().containsRegion(seen.point.getRegionID()))
			{
				out.append(seen.type).append('@').append(seen.point).append('=')
					.append(seen.status != null ? seen.status.getKind() : null).append(" (").append(entry.getKey()).append(") ");
			}
		}
		return out.toString();
	}

	private boolean workedAt(RouteStep step)
	{
		for (WorldPoint point : plantedThisRun)
		{
			if (step.getLocation().containsRegion(point.getRegionID()))
			{
				return true;
			}
		}
		return step.getPatchTypes().contains(PatchType.BIRDHOUSE) && !seededThisRun.isEmpty();
	}

	/**
	 * When the last patch at this stop finishes growing, if every patch there is still growing according
	 * to Time Tracking's saved data (and bird houses are all filling), or 0 if anything needs doing or isn't known.
	 */
	long stillGrowingUntil(RouteStep step)
	{
		if (step.getKind() != RouteStep.Kind.LOCATION || step.getLocation() == null)
		{
			return 0;
		}
		long now = System.currentTimeMillis();
		int offset = farmTickOffset();
		long latest = 0;
		int found = 0;
		for (TimeTrackingPatches.Entry entry : TimeTrackingPatches.ALL)
		{
			if (!step.getPatchTypes().contains(entry.type) || !containsRegion(step.getLocation(), entry.region))
			{
				continue;
			}
			String saved = configManager.getRSProfileConfiguration("timetracking", entry.region + "." + entry.varbit);
			if (saved == null)
			{
				return 0;
			}
			PatchStatus status;
			try
			{
				String[] parts = saved.split(":");
				status = PatchStatus.predict(entry.type, Integer.parseInt(parts[0]), Long.parseLong(parts[1]), now, offset);
			}
			catch (RuntimeException e)
			{
				return 0;
			}
			// Within a few minutes of being ready counts as ready: growth ticks can come a little earlier
			// than predicted, and it's better to stop by than to skip a patch that's done
			if (status == null || status.getKind() != PatchStatus.Kind.GROWING || status.getReadyAt() <= now + 5 * 60_000L)
			{
				return 0;
			}
			latest = Math.max(latest, status.getReadyAt());
			found++;
		}
		if (step.getPatchTypes().contains(PatchType.BIRDHOUSE))
		{
			for (int space : BirdHouses.SPACES)
			{
				loadBirdHouse(space);
				long fullAt = birdHouseFullAt(space);
				if (!BirdHouses.isSeeded(birdHouseValues.getOrDefault(space, 0)) || fullAt <= now)
				{
					return 0;
				}
				latest = Math.max(latest, fullAt);
				found++;
			}
		}
		return found > 0 ? latest : 0;
	}

	private static boolean containsRegion(FarmLocation location, int region)
	{
		for (int id : location.getRegionIds())
		{
			if (id == region)
			{
				return true;
			}
		}
		return false;
	}

	private volatile boolean stepTeleported;
	private int stepTeleportedFor = -1;

	private WorldPoint closest(List<Object> objects)
	{
		Player player = client.getLocalPlayer();
		WorldPoint best = null;
		for (Object object : objects)
		{
			WorldPoint point = object instanceof NPC ? ((NPC) object).getWorldLocation() : ((TileObject) object).getWorldLocation();
			if (best == null || (player != null && point.distanceTo(player.getWorldLocation()) < best.distanceTo(player.getWorldLocation())))
			{
				best = point;
			}
		}
		return best;
	}

	/**
	 * Where a bird house space is, once it's been seen (saved to your profile).
	 */
	private WorldPoint spaceLocation(int space)
	{
		WorldPoint known = spaceLocations.get(space);
		if (known == null)
		{
			String saved = configManager.getRSProfileConfiguration(FarmRunHelperConfig.GROUP, "birdhousepos." + space);
			if (saved != null)
			{
				String[] parts = saved.split(",");
				try
				{
					known = new WorldPoint(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
					spaceLocations.put(space, known);
				}
				catch (RuntimeException ignored)
				{
					// bad saved value
				}
			}
		}
		return known;
	}

	// ---- Places learned in-game, saved to your profile: where the people and objects on routes are, so
	// the path can head there before they're in view

	private final Map<String, WorldPoint> learnedPoints = new ConcurrentHashMap<>();

	WorldPoint learnedPoint(String key)
	{
		WorldPoint known = learnedPoints.get(key);
		if (known == null)
		{
			String saved = configManager.getRSProfileConfiguration(FarmRunHelperConfig.GROUP, "pos." + key);
			if (saved != null)
			{
				try
				{
					String[] parts = saved.split(",");
					known = new WorldPoint(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
					learnedPoints.put(key, known);
				}
				catch (RuntimeException ignored)
				{
					// bad saved value
				}
			}
		}
		return known;
	}

	private void rememberPoint(String key, WorldPoint point)
	{
		if (point == null || point.equals(learnedPoint(key)))
		{
			return;
		}
		learnedPoints.put(key, point);
		configManager.setRSProfileConfiguration(FarmRunHelperConfig.GROUP, "pos." + key,
			point.getX() + "," + point.getY() + "," + point.getPlane());
	}

	private void learnPlaces(RouteStep step)
	{
		if (step == null || step.getLocation() == null)
		{
			return;
		}
		GameObject patch = nextPatch;
		if (patch != null && !isTravelling() && LocationPoints.known(step.getLocation()) == null)
		{
			WorldPoint learned = learnedPoint("stop." + step.getLocation().name());
			if (learned == null)
			{
				rememberPoint("stop." + step.getLocation().name(), patch.getWorldLocation());
			}
		}
		if (step.getTravel() != null)
		{
			for (String leg : TravelLegs.legs(step.getLocation(), step.getTravel(), config))
			{
				if (TravelLegs.isNpc(leg) || leg.startsWith("~"))
				{
					List<Object> seen = loadedLeg(leg);
					if (!seen.isEmpty() && learnedPoint("leg." + leg) == null)
					{
						rememberPoint("leg." + leg, closest(seen));
					}
				}
			}
		}
	}

	private void rememberSpaceLocation(int space, WorldPoint point)
	{
		if (point.equals(spaceLocations.get(space)))
		{
			return;
		}
		spaceLocations.put(space, point);
		configManager.setRSProfileConfiguration(FarmRunHelperConfig.GROUP, "birdhousepos." + space,
			point.getX() + "," + point.getY() + "," + point.getPlane());
	}

	/**
	 * Magic mushtrees to highlight when the next bird house is in another part of Fossil Island.
	 */
	List<Object> getMushtreeTargets()
	{
		return mushtreeDestination != null ? loadedLeg(TravelLegs.MUSHTREE) : Collections.emptyList();
	}

	/**
	 * The latest part of the route you're standing next to (within 5 tiles), or -1.
	 */
	private int legNear(List<String> legs, WorldPoint here)
	{
		for (int i = legs.size() - 1; i > legsDone; i--)
		{
			for (Object match : loadedLeg(legs.get(i)))
			{
				WorldPoint point = match instanceof NPC ? ((NPC) match).getWorldLocation() : ((TileObject) match).getWorldLocation();
				if (point != null && point.distanceTo(here) <= 5)
				{
					return i;
				}
			}
		}
		return -1;
	}

	private List<Object> loadedLeg(String leg)
	{
		List<Object> matches = new ArrayList<>();
		List<Object> withOption = new ArrayList<>();
		String option = TravelLegs.dialogueOption(leg);
		WorldPoint at = TravelLegs.position(leg);
		Map<?, String> source = TravelLegs.isNpc(leg) ? travelNpcs : travelObjects;
		for (Map.Entry<?, String> entry : source.entrySet())
		{
			if (at != null && entry.getKey() instanceof TileObject
				&& ((TileObject) entry.getKey()).getWorldLocation().distanceTo(at) > 5)
			{
				// Several share this name: only the one at this spot
				continue;
			}
			if (TravelLegs.matches(leg, entry.getValue()))
			{
				matches.add(entry.getKey());
				if (option != null && entry.getKey() instanceof TileObject && hasAction((TileObject) entry.getKey(), option))
				{
					withOption.add(entry.getKey());
				}
			}
		}
		// When several share a name (like rowboats), the one with the right option is the one to use. An
		// object leg with an option but no spot (the rowboat you dive from) only counts with that option
		if (option != null && at == null && !TravelLegs.isNpc(leg))
		{
			return withOption;
		}
		return withOption.isEmpty() ? matches : withOption;
	}

	private boolean hasAction(TileObject object, String option)
	{
		ObjectComposition composition = client.getObjectDefinition(object.getId());
		if (composition != null && composition.getImpostorIds() != null && composition.getImpostor() != null)
		{
			composition = composition.getImpostor();
		}
		String[] actions = composition != null ? composition.getActions() : null;
		if (actions != null)
		{
			for (String action : actions)
			{
				if (action != null && action.equalsIgnoreCase(option))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Notices when you've teleported by a spirit tree, fairy ring or mushtree, so that part of the route counts as done.
	 */
	private void updateLegs(Player player)
	{
		if (legsStep != currentStep)
		{
			legsStep = currentStep;
			legsDone = -1;
		}
		WorldPoint here = player.getWorldLocation();
		WorldPoint before = lastPosition;
		lastPosition = here;
		RouteStep step = getCurrentStep();
		if (step == null || step.getTravel() == null)
		{
			return;
		}
		List<String> legs = TravelLegs.legs(step.getLocation(), step.getTravel(), config);
		// Already in the part of Fossil Island the mushtree would take you to (like walking from the
		// hardwood patches to the seaweed rowboat): that leg's done
		Matcher mush = MUSHTREE_TO.matcher(step.getTravel().getInstruction());
		int mushLeg = legs.indexOf(TravelLegs.MUSHTREE);
		if (mushLeg > legsDone && mush.find() && mush.group(1).trim().equals(BirdHouses.zone(here)))
		{
			legsDone = mushLeg;
		}
		// Close to something at a known spot further along the route (the cave by Weiss's landing): everything
		// before it is done, even if we missed you using it (say rock crabs pulled you away from Larry just
		// before his boat left)
		for (int i = legs.size() - 1; i > legsDone + 1; i--)
		{
			WorldPoint spot = TravelLegs.position(legs.get(i));
			// (15 tiles: the seaweed rowboat on the shore is 29 tiles from the one out at sea)
			if (spot != null && here.distanceTo2D(spot) <= 15)
			{
				legsDone = i - 1;
				break;
			}
		}
		int wasNear = nearLeg;
		nearLeg = legNear(legs, here);
		if (stepTeleportedFor != currentStep)
		{
			stepTeleportedFor = currentStep;
			stepTeleported = false;
		}
		if (before == null || here.distanceTo(before) <= 15)
		{
			return;
		}
		stepTeleported = true;
		// Moved far in one go: a boat or person you were standing next to took you there
		if (wasNear > legsDone && !TravelLegs.isTeleportLeg(legs.get(wasNear)))
		{
			legsDone = wasNear;
		}
		for (int i = legs.size() - 1; i > legsDone; i--)
		{
			String leg = legs.get(i);
			if (!TravelLegs.isTeleportLeg(leg))
			{
				continue;
			}
			for (Object match : loadedLeg(leg))
			{
				WorldPoint point = ((TileObject) match).getWorldLocation();
				if (point.distanceTo(here) <= 3)
				{
					legsDone = i;
					return;
				}
			}
		}
	}

	/**
	 * The walking path to whatever you need to click next, as scene tiles.
	 */
	List<int[]> getPath()
	{
		return path;
	}

	// The fixed path being shown and the place it leads to
	private WorldPoint shownGoal;
	private List<WorldPoint> shownPath = Collections.emptyList();
	private WorldPoint lastPathSpot;
	private String shownStage;

	private void updatePath(Player player)
	{
		path = Collections.emptyList();
		LocalPoint here = player.getLocalLocation();
		WorldPoint me = player.getWorldLocation();
		if (!config.showPath() || here == null || me == null)
		{
			return;
		}
		List<Object> targets = new ArrayList<>();
		if (isTravelling())
		{
			targets.addAll(getHouseTargets());
			targets.addAll(getTravelTargets());
		}
		else if (mushtreeDestination != null)
		{
			targets.addAll(getMushtreeTargets());
		}
		else if (nextPatch != null)
		{
			targets.add(nextPatch);
		}
		// Keep the path until you get where it leads, or the step or leg of the journey changes: as you walk,
		// which patch or spot is nearest changes, and that mustn't swap the path for another
		FixedPaths fixed = FixedPaths.get();
		String stage = currentStep + "|" + isTravelling() + "|" + legsDone + "|" + mushtreeDestination;
		boolean arrived = shownGoal != null && shownGoal.getPlane() == me.getPlane() && shownGoal.distanceTo2D(me) <= 2;
		boolean jumped = lastPathSpot != null && (lastPathSpot.getPlane() != me.getPlane() || lastPathSpot.distanceTo2D(me) > 15);
		if (shownGoal != null && stage.equals(shownStage) && !arrived && !jumped && !shownPath.isEmpty())
		{
			lastPathSpot = me;
			drawPath();
			return;
		}
		shownStage = stage;
		// Where to walk: the thing to click the path already leads to, else the nearest one, else where the
		// next one is known to be
		WorldPoint target = null;
		for (Object object : targets)
		{
			WorldPoint at = object instanceof NPC ? ((NPC) object).getWorldLocation()
				: object instanceof TileObject ? ((TileObject) object).getWorldLocation() : null;
			if (at == null)
			{
				continue;
			}
			if (shownGoal != null && shownGoal.equals(fixed.goalFor(at)))
			{
				target = at;
				break;
			}
			if (target == null || at.distanceTo2D(me) < target.distanceTo2D(me))
			{
				target = at;
			}
		}
		if (target == null && targets.isEmpty())
		{
			target = pathGoal();
		}
		WorldPoint goal = target == null || isInHouse() ? null : fixed.goalFor(target);
		if (target != null && shownGoal != null && shownGoal.getPlane() == target.getPlane() && shownGoal.distanceTo2D(target) <= 4)
		{
			// Still the same place (a patch seen up close is a tile or two from where it was known to be)
			goal = shownGoal;
		}
		if (goal == null)
		{
			shownGoal = null;
			shownPath = Collections.emptyList();
			return;
		}
		boolean moved = lastPathSpot != null && (lastPathSpot.getPlane() != me.getPlane() || lastPathSpot.distanceTo2D(me) > 15);
		lastPathSpot = me;
		if (!goal.equals(shownGoal) || shownPath.isEmpty() || moved)
		{
			// (A teleport, boat or stairs on the way starts a new path from where you land)
			// Like the Shortest Path plugin: the path is set once, from where you are, and stays as it is
			// (passed parts too) until you get there and the next one starts
			shownPath = fixed.path(target, me);
			shownGoal = shownPath.isEmpty() ? null : goal;
		}
		drawPath();
	}

	private void drawPath()
	{
		WorldPoint me = client.getLocalPlayer() != null ? client.getLocalPlayer().getWorldLocation() : null;
		if (me == null)
		{
			return;
		}
		// Placed by the scene's own corner, not by you: while you run, where you're drawn is between tiles
		WorldView view = client.getTopLevelWorldView();
		if (view == null)
		{
			return;
		}
		List<int[]> tiles = new ArrayList<>();
		for (WorldPoint point : shownPath)
		{
			int x = point.getX() - view.getBaseX();
			int y = point.getY() - view.getBaseY();
			if (point.getPlane() == me.getPlane() && x >= 0 && y >= 0 && x < Constants.SCENE_SIZE && y < Constants.SCENE_SIZE)
			{
				tiles.add(new int[]{x, y});
			}
		}
		path = tiles;
	}

	/**
	 * Where the next thing to click is, when it isn't in view: the next person or object on the route,
	 * or the next bird house. Null if it isn't known, or there's no need to walk.
	 */
	private WorldPoint pathGoal()
	{
		RouteStep step = getCurrentStep();
		if (step == null || step.getKind() != RouteStep.Kind.LOCATION)
		{
			return null;
		}
		if (isTravelling())
		{
			if (isInHouse())
			{
				return null;
			}
			List<String> legs = TravelLegs.legs(step.getLocation(), step.getTravel(), config);
			int leg = currentLeg(legs);
			boolean teleported = legsDone >= 0 || stepTeleported || alreadyThere(step.getTravel());
			if (leg >= legs.size())
			{
				// Nothing left to use on the way: once you've teleported, walk the rest (like the run north
				// from the Colossal Wyrm Remains to the Locus Oasis patch)
				return teleported ? nearestSpotOnTheWay(step) : null;
			}
			// Only once the step's teleport is used (or skipped), so it doesn't lead away from the teleport
			if ((legsDone < 0 && !stepTeleported && !alreadyThere(step.getTravel())) || TravelLegs.isTeleportLeg(legs.get(leg)))
			{
				return null;
			}
			WorldPoint seen = learnedPoint("leg." + legs.get(leg));
			return seen != null ? seen : TravelLegs.knownPoint(legs.get(leg));
		}
		if (mushtreeDestination == null && birdHouseHint != null)
		{
			int space = nextBirdHouseSpace();
			return space != -1 ? spaceSpot(space) : null;
		}
		return remoteSpot;
	}

	/**
	 * The nearest patch of this stop's, if it's a walk away (within 150 tiles), for after the teleport.
	 */
	private WorldPoint nearestSpotOnTheWay(RouteStep step)
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return null;
		}
		WorldPoint here = player.getWorldLocation();
		WorldPoint best = null;
		for (LocationPoints.Spot spot : LocationPoints.spots(step.getLocation()))
		{
			if (step.getPatchTypes().contains(spot.type) && (best == null || spot.point.distanceTo2D(here) < best.distanceTo2D(here)))
			{
				best = spot.point;
			}
		}
		if (best == null)
		{
			best = LocationPoints.known(step.getLocation());
		}
		return best != null && best.distanceTo2D(here) <= 150 ? best : null;
	}

	/**
	 * Lower-case name of the house object to use for this route, or "portal" to leave by the exit portal.
	 */
	private String houseKeyword(TeleportOption travel)
	{
		for (Req req : travel.getRequirements())
		{
			switch (req)
			{
				case POH_XERIC:
					return "xeric's talisman";
				case POH_GLORY:
					return "glory";
				case POH_DIGSITE:
					return "digsite pendant";
				case POH_JEWELLERY_BOX:
					return "jewellery box";
				default:
					break;
			}
			String name = req.name();
			if (name.startsWith("POH_PORTAL_"))
			{
				return housePortal(config, req) == HousePortal.NEXUS
					? "portal nexus"
					: name.substring("POH_PORTAL_".length()).replace('_', ' ').toLowerCase() + " portal";
			}
		}
		// A route to your house's own location: walk out through the exit portal
		return "portal";
	}

	// Destinations of the house portals the routes use
	private static final Set<String> HOUSE_PORTALS = Set.of("varrock portal", "lumbridge portal", "falador portal",
		"camelot portal", "ardougne portal", "kharyrll portal", "troll stronghold portal");

	private static boolean isHouseObjectName(String n)
	{
		return n.equals("portal") || HOUSE_PORTALS.contains(n) || (n.endsWith(" portal") && !n.contains("exit")) || n.contains("portal nexus") || n.contains("xeric's talisman")
			|| n.contains("amulet of glory") || n.contains("digsite pendant") || n.contains("jewellery box");
	}

	/**
	 * Whether everything at the current stop is planted (and composted) or skipped, with all of its patches loaded.
	 */
	private boolean isStopComplete(RouteStep step, Player player)
	{
		if (step.getKind() != RouteStep.Kind.LOCATION || step.getLocation() == null || nextPatch != null
			|| !step.getLocation().containsRegion(player.getWorldLocation().getRegionID()))
		{
			return false;
		}
		boolean didSomething = false;
		if (mushtreeDestination != null)
		{
			return false;
		}
		for (PatchType type : step.getPatchTypes())
		{
			if (type == PatchType.BIRDHOUSE)
			{
				if (nextBirdHouseSpace() != -1)
				{
					return false;
				}
				// All four filling: nothing left to do here, even if they were filled before this run started
				didSomething = true;
				continue;
			}
			int expected = step.getLocation().getPatches().getOrDefault(type, 0);
			int loaded = 0;
			for (SeenPatch seen : seenPatches.values())
			{
				WorldPoint point = seen.point;
				if (seen.type != type || !step.getLocation().containsRegion(point.getRegionID()))
				{
					continue;
				}
				PatchStatus status = seen.status;
				if (status == null)
				{
					return false;
				}
				if (!(status.isPlanted() || skippedPatches.contains(point)) || (status.isPlanted() && plantedThisRun.contains(point) && config.compost() != CompostChoice.NONE
					&& type.takesCompost() && !compostedPatches.contains(point)))
				{
					// Something still to do here (it may be on another floor)
					return false;
				}
				didSomething |= plantedThisRun.contains(point) || skippedPatches.contains(point);
				loaded++;
			}
			if (loaded < expected)
			{
				return false;
			}
		}
		// Everything here is planted, composted, skipped or still growing
		return true;
	}

	// ---- Events (client thread)

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		gameTicks++;
		updateFarmingVarbitsReady();
		int farming = client.getRealSkillLevel(Skill.FARMING);
		int crafting = client.getRealSkillLevel(Skill.CRAFTING);
		int hunter = client.getRealSkillLevel(Skill.HUNTER);
		if (farming != farmingLevel || crafting != craftingLevel || hunter != hunterLevel)
		{
			farmingLevel = farming;
			craftingLevel = crafting;
			hunterLevel = hunter;
			refreshPanel();
		}

		Route r = route;
		Player player = client.getLocalPlayer();
		if (player != null)
		{
			// Kept up to date even without a run, so bird house timers are right next time
			updateBirdHouses(player);
		}
		updatePouchRunes();
		updateLeprechaunItems();
		readBucketDialog();
		if (r == null || player == null)
		{
			return;
		}
		updatePatchStatuses();
		addBackWrongGuesses(player);
		updateNextPatch(player);
		checkInventorySpace(player);
		updateLegs(player);
		travelHint = computeTravelHint();
		logLegChange(player);
		String hint = travelHint + "|" + (remoteSpot != null);
		if (!hint.equals(lastTravelHint))
		{
			lastTravelHint = hint;
			refreshPanel();
		}
		updatePath(player);
		learnPlaces(getCurrentStep());
		if (!config.autoAdvance())
		{
			return;
		}
		if (skipGrowingStops(r))
		{
			return;
		}
		RouteStep step = getCurrentStep();
		if (step != null && step.getKind() == RouteStep.Kind.BANK && currentStep < r.getSteps().size() - 1
			&& !carriedItems.isEmpty() && getWithdrawItems().isEmpty())
		{
			// Everything for this trip is in your inventory or worn
			step.setDone(true);
			currentStep++;
			refreshPanel();
			return;
		}
		if (step != null && currentStep < r.getSteps().size() - 1 && isStopComplete(step, player))
		{
			log.debug("Farm Run Helper: {} done at {}; patches: {}", step.getTitle(), player.getWorldLocation(), describeSeen(step));
			step.setDone(true);
			currentStep++;
			nextPatch = null;
			nextPatchType = null;
			nextIsCompost = false;
			refreshPanel();
			return;
		}
		int region = player.getWorldLocation().getRegionID();
		if (region == lastRegion)
		{
			return;
		}
		lastRegion = region;
		if (step != null && step.getLocation() != null && step.getLocation().containsRegion(region))
		{
			// Already at the current stop. Its other part (like the Auburnvale tree after the belladonna) or
			// another stop sharing this area comes later, so don't jump ahead to it
			return;
		}

		List<RouteStep> steps = r.getSteps();
		for (int i = currentStep + 1; i < steps.size(); i++)
		{
			FarmLocation location = steps.get(i).getLocation();
			if (location != null && location.containsRegion(region))
			{
				for (int j = currentStep; j < i; j++)
				{
					steps.get(j).setDone(true);
				}
				currentStep = i;
				refreshPanel();
				return;
			}
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		int id = event.getContainerId();
		ItemContainer container = event.getItemContainer();
		if (container == null)
		{
			return;
		}
		if (id == InventoryID.BANK)
		{
			storeIds(container, bankItems, "ownedBankItems");
			bankHasAxe = bankItems.stream().anyMatch(this::isAxe);
		}
		else if (id == InventoryID.SEED_VAULT)
		{
			storeIds(container, vaultItems, "ownedVaultItems");
		}
		else if (id == InventoryID.INV || id == InventoryID.WORN)
		{
			Map<Integer, Integer> counts = count(container);
			if (id == InventoryID.WORN)
			{
				Item[] worn = container.getItems();
				wearingGloves = worn.length > GLOVES_SLOT && worn[GLOVES_SLOT].getId() > 0;
			}
			if (id == InventoryID.INV)
			{
				inventoryItems = counts;
				int used = 0;
				for (Item item : container.getItems())
				{
					if (item.getId() > 0 && item.getQuantity() > 0)
					{
						used++;
					}
				}
				freeSlots = 28 - used;
			}
			else
			{
				wornItems = counts;
			}
			updateCarried();
			if (route != null)
			{
				refreshPanel();
			}
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM)
		{
			return;
		}
		readBucketMessage(event.getMessage());
		if (route == null)
		{
			return;
		}
		if (!COMPOST_MESSAGE.matcher(event.getMessage()).find())
		{
			return;
		}
		// Compost goes on the patch you're standing at: the one being composted, or else the closest
		GameObject target = nextIsCompost ? nextPatch : null;
		Player player = client.getLocalPlayer();
		if (target == null && player != null)
		{
			int bestDistance = 6;
			for (GameObject object : patchObjects.keySet())
			{
				int distance = object.getWorldLocation().distanceTo(player.getWorldLocation());
				if (object.getPlane() == player.getWorldLocation().getPlane() && distance < bestDistance)
				{
					bestDistance = distance;
					target = object;
				}
			}
		}
		if (target != null)
		{
			compostedPatches.add(target.getWorldLocation());
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		GameObject object = event.getGameObject();
		PatchType type = patchType(object.getId());
		if (type != null)
		{
			// Time Tracking's list says exactly which kind of patch this is; names can mislead (a hardwood
			// patch growing a teak is called a "Teak tree", which reads like an ordinary tree patch)
			PatchType listed = TimeTrackingPatches.typeAt(object.getWorldLocation().getRegionID(), patchVarbit(object));
			if (listed != null)
			{
				type = listed;
			}
			else if (patchVarbit(object) != -1 && !TimeTrackingPatches.isFarmingVarbit(patchVarbit(object)))
			{
				// Not a farming patch at all, like the rose bushes in Varrock Palace's garden
				registerObject(object);
				return;
			}
			// Allotments (and some other patches) are several objects, one per tile, sharing one farming
			// state: keep one, so the patch is planted, composted and labelled once
			patchSpawnTicks.put(object, gameTicks);
			if (isSamePatchAsKnown(object))
			{
				spareTiles.put(object, type);
			}
			else
			{
				patchObjects.put(object, type);
			}
			return;
		}
		registerObject(object);
	}

	private boolean isSamePatchAsKnown(GameObject object)
	{
		int varbit = patchVarbit(object);
		if (varbit == -1)
		{
			return false;
		}
		int region = object.getWorldLocation().getRegionID();
		for (GameObject known : patchObjects.keySet())
		{
			if (known != object && known.getWorldLocation().getRegionID() == region && patchVarbit(known) == varbit)
			{
				return true;
			}
		}
		return false;
	}

	private int patchVarbit(GameObject object)
	{
		ObjectComposition composition = client.getObjectDefinition(object.getId());
		return composition != null ? composition.getVarbitId() : -1;
	}

	/**
	 * Remembers house objects (mounted items, portals, jewellery box) and spirit trees and fairy rings.
	 * Mounted items in the house are wall decorations, so this is called for those too.
	 */
	private void registerObject(TileObject object)
	{
		ObjectComposition composition = client.getObjectDefinition(object.getId());
		String name = composition != null && composition.getName() != null ? composition.getName().toLowerCase() : "";
		// Built house furniture like portals can show a different name from its base object
		String shown = composition != null && composition.getImpostorIds() != null ? currentName(object) : name;
		if (shown != null && !"null".equalsIgnoreCase(shown))
		{
			name = shown.toLowerCase();
		}
		if (shown != null && TravelLegs.isAnyLeg(shown, false))
		{
			travelObjects.put(object, shown.toLowerCase());
			return;
		}
		if (isHouseObjectName(name))
		{
			houseObjects.put(object, name);
			// Mounted items, a nexus or teleport portals only exist in a player-owned house
			if (!name.equals("portal"))
			{
				inHouse = true;
			}
		}
	}

	private void forgetObject(TileObject object)
	{
		houseObjects.remove(object);
		travelObjects.remove(object);
	}

	private boolean fairyLogScrolled;

	/**
	 * Scrolls the fairy ring travel log to the code for the next stop when it opens, and colours it,
	 * the same way the game does.
	 */
	@Subscribe
	public void onPostClientTick(PostClientTick event)
	{
		Widget contents = client.getWidget(InterfaceID.FairyringsLog.CONTENTS);
		if (contents == null || contents.isHidden())
		{
			fairyLogScrolled = false;
			return;
		}
		if (route == null || !config.highlightPatches())
		{
			return;
		}
		MenuTarget code = null;
		for (MenuTarget target : getMenuTargets())
		{
			if (target.isCode())
			{
				code = target;
			}
		}
		if (code == null)
		{
			return;
		}

		Widget entry = null;
		Widget faves = client.getWidget(InterfaceID.FairyringsLog.FAVES);
		if (faves != null && faves.getStaticChildren() != null)
		{
			for (Widget widget : faves.getStaticChildren())
			{
				if (widget != null && code.matches(widget.getText()))
				{
					entry = widget;
					break;
				}
			}
		}
		boolean inFaves = entry != null;
		if (entry == null && contents.getDynamicChildren() != null)
		{
			for (Widget widget : contents.getDynamicChildren())
			{
				if (widget != null && code.matches(widget.getText()))
				{
					entry = widget;
					break;
				}
			}
		}
		if (entry == null)
		{
			return;
		}
		entry.setTextColor(0x00FF00);
		// Scroll once each time the log opens, so you can still scroll it yourself afterwards
		if (!inFaves && !fairyLogScrolled)
		{
			int scrollY = Math.max(0, Math.min(entry.getRelativeY(), contents.getScrollHeight() - contents.getHeight()));
			contents.setScrollY(scrollY);
			contents.revalidateScroll();
			client.runScript(ScriptID.UPDATE_SCROLLBAR, InterfaceID.FairyringsLog.SCROLLBAR, InterfaceID.FairyringsLog.CONTENTS, scrollY);
		}
		fairyLogScrolled = true;
	}

	/**
	 * Colours right-click teleport options for the next stop, like "Farm Teleport" on an Ardougne cloak.
	 */
	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (route == null || !config.highlightPatches() || !isTravelling())
		{
			return;
		}
		MenuEntry entry = event.getMenuEntry();
		// Right-click options on people along the route, like "Follow" on Elkoy
		RouteStep current = getCurrentStep();
		List<String> legs = TravelLegs.legs(current.getLocation(), current.getTravel(), config);
		int leg = currentLeg(legs);
		String wantedOption = leg < legs.size() ? TravelLegs.dialogueOption(legs.get(leg)) : null;
		if (wantedOption != null && entry.getOption() != null && entry.getTarget() != null
			&& entry.getOption().equalsIgnoreCase(wantedOption)
			&& TravelLegs.matches(legs.get(leg), entry.getTarget().replaceAll("<[^>]*>", "").replaceAll("\\s*\\(level-\\d+\\)", "").trim()))
		{
			entry.setOption(ColorUtil.wrapWithColorTag(entry.getOption(), config.highlightColor()));
			return;
		}
		if (legsDone >= 0)
		{
			return;
		}
		String destination = teleportDestination(current.getTravel());
		String option = entry.getOption();
		if (destination == null || option == null || option.contains("<col="))
		{
			return;
		}
		String plain = option.toLowerCase();
		String wanted = destination.toLowerCase();
		// "Farm Teleport" for "Farm teleport", or "Monastery Teleport" for "Monastery"
		boolean named = plain.endsWith(" teleport") && plain.length() > " teleport".length()
			&& wanted.contains(plain.substring(0, plain.length() - " teleport".length()));
		if (plain.contains(wanted) || named)
		{
			entry.setOption(ColorUtil.wrapWithColorTag(option, config.highlightColor()));
		}
	}

	private static final int[] RUNE_POUCHES = {ItemID.BH_RUNE_POUCH, ItemID.BH_RUNE_POUCH_TROUVER,
		ItemID.DIVINE_RUNE_POUCH, ItemID.DIVINE_RUNE_POUCH_TROUVER};
	private static final int[][] POUCH_SLOTS = {
		{VarbitID.RUNE_POUCH_TYPE_1, VarbitID.RUNE_POUCH_QUANTITY_1},
		{VarbitID.RUNE_POUCH_TYPE_2, VarbitID.RUNE_POUCH_QUANTITY_2},
		{VarbitID.RUNE_POUCH_TYPE_3, VarbitID.RUNE_POUCH_QUANTITY_3},
		{VarbitID.RUNE_POUCH_TYPE_4, VarbitID.RUNE_POUCH_QUANTITY_4},
	};
	private Map<Integer, Integer> pouchRunes = Collections.emptyMap();
	private volatile Map<Integer, Integer> pouchContents = Collections.emptyMap();
	private boolean wearingGloves;
	private static final int GLOVES_SLOT = 9;

	/**
	 * What you're carrying: inventory, worn items, and the runes in a rune pouch in your inventory.
	 */
	private void updateCarried()
	{
		Map<Integer, Integer> merged = new HashMap<>(inventoryItems);
		wornItems.forEach((item, qty) -> merged.merge(item, qty, Integer::sum));
		pouchRunes.forEach((item, qty) -> merged.merge(item, qty, Integer::sum));
		// With the bottomless bucket, having the bucket is enough, filled or not
		if (merged.containsKey(ItemID.BOTTOMLESS_COMPOST_BUCKET))
		{
			merged.merge(ItemID.BOTTOMLESS_COMPOST_BUCKET_FILLED, 1, Integer::sum);
		}
		// Dust runes count as air and earth, an air staff as unlimited air runes, and so on
		RuneSources.addElements(merged, wornItems);
		if (wearingGloves)
		{
			// Any worn gloves will do for picking belladonna
			merged.merge(ItemID.LEATHER_GLOVES, 1, Integer::sum);
		}
		// Any axe will do for celastrus trees, carried or wielded
		for (int item : merged.keySet().toArray(new Integer[0]))
		{
			if (isAxe(item))
			{
				merged.merge(ItemID.BRONZE_AXE, 1, Integer::sum);
				break;
			}
		}
		carriedItems = merged;
	}

	private final Map<Integer, Boolean> axeIds = new ConcurrentHashMap<>();
	private static final Pattern WOODCUTTING_AXE = Pattern.compile(
		"(bronze|iron|steel|black|blessed|mithril|adamant|rune|gilded|dragon|3rd age|infernal|crystal|trailblazer|echo) (felling )?axe( \\(.*\\))?");
	private volatile boolean bankHasAxe;

	boolean isAxe(int itemId)
	{
		return axeIds.computeIfAbsent(itemId, id ->
		{
			ItemComposition item = client.getItemDefinition(id);
			String name = item != null && item.getName() != null ? item.getName().toLowerCase() : "";
			// Only woodcutting axes, not weapons like the zombie axe or throwing axes
			return WOODCUTTING_AXE.matcher(name).matches();
		});
	}

	/**
	 * Reads the rune pouch, so runes for teleport spells count as carried when they're in it.
	 */
	private void updatePouchRunes()
	{
		Map<Integer, Integer> runes = new HashMap<>();
		boolean hasPouch = false;
		for (int pouch : RUNE_POUCHES)
		{
			hasPouch |= inventoryItems.containsKey(pouch);
		}
		// What's in the pouch is known wherever it is, so the item list can say runes are in the pouch in your bank
		Map<Integer, Integer> contents = new HashMap<>();
		EnumComposition pouchTypes = client.getEnum(EnumID.RUNEPOUCH_RUNE);
		for (int[] slot : POUCH_SLOTS)
		{
			int type = client.getVarbitValue(slot[0]);
			int amount = client.getVarbitValue(slot[1]);
			int rune = type > 0 && pouchTypes != null ? pouchTypes.getIntValue(type) : -1;
			if (rune > 0 && amount > 0)
			{
				contents.merge(rune, amount, Integer::sum);
			}
		}
		RuneSources.addElements(contents, Collections.emptyMap());
		pouchContents = contents;
		if (hasPouch)
		{
			EnumComposition runeTypes = pouchTypes;
			for (int[] slot : POUCH_SLOTS)
			{
				int type = client.getVarbitValue(slot[0]);
				int amount = client.getVarbitValue(slot[1]);
				int rune = type > 0 && runeTypes != null ? runeTypes.getIntValue(type) : -1;
				if (rune > 0 && amount > 0)
				{
					runes.merge(rune, amount, Integer::sum);
				}
			}
		}
		if (!runes.equals(pouchRunes))
		{
			pouchRunes = runes;
			updateCarried();
			if (route != null)
			{
				refreshPanel();
			}
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		if (npc.getName() != null && TravelLegs.isAnyLeg(npc.getName(), true))
		{
			travelNpcs.put(npc, npc.getName().toLowerCase());
		}
		if ("Squirrel".equals(npc.getName()) && npc.getWorldLocation() != null
			&& FarmLocation.FOSSIL_ISLAND.containsRegion(npc.getWorldLocation().getRegionID()))
		{
			squirrels.add(npc);
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		travelNpcs.remove(event.getNpc());
		squirrels.remove(event.getNpc());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(DecorativeObjectSpawned event)
	{
		registerObject(event.getDecorativeObject());
	}

	@Subscribe
	public void onDecorativeObjectDespawned(DecorativeObjectDespawned event)
	{
		forgetObject(event.getDecorativeObject());
	}

	@Subscribe
	public void onWallObjectSpawned(WallObjectSpawned event)
	{
		registerObject(event.getWallObject());
	}

	@Subscribe
	public void onWallObjectDespawned(WallObjectDespawned event)
	{
		forgetObject(event.getWallObject());
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		spareTiles.remove(event.getGameObject());
		patchSpawnTicks.remove(event.getGameObject());
		PatchType removed = patchObjects.remove(event.getGameObject());
		if (removed != null)
		{
			// The patch is still there if another of its tiles is: use that one instead
			int varbit = patchVarbit(event.getGameObject());
			int region = event.getGameObject().getWorldLocation().getRegionID();
			for (Map.Entry<GameObject, PatchType> spare : spareTiles.entrySet())
			{
				if (varbit != -1 && patchVarbit(spare.getKey()) == varbit && spare.getKey().getWorldLocation().getRegionID() == region)
				{
					spareTiles.remove(spare.getKey());
					patchObjects.put(spare.getKey(), spare.getValue());
					break;
				}
			}
		}
		houseObjects.remove(event.getGameObject());
		travelObjects.remove(event.getGameObject());
		if (event.getGameObject() == nextPatch)
		{
			nextPatch = null;
			nextPatchType = null;
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOADING || event.getGameState() == GameState.LOGIN_SCREEN)
		{
			patchObjects.clear();
			spareTiles.clear();
			patchSpawnTicks.clear();
			houseObjects.clear();
			travelObjects.clear();
			travelNpcs.clear();
			squirrels.clear();
			inHouse = false;
			path = Collections.emptyList();
		}
		if (event.getGameState() == GameState.LOGIN_SCREEN)
		{
			birdHouseValues.clear();
			birdHouseChanged.clear();
			patchStatus.clear();
			patchWasEmpty.clear();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!FarmRunHelperConfig.GROUP.equals(event.getGroup()) || event.getKey().startsWith("owned")
			|| event.getKey().contains("birdhouse") || event.getKey().contains("pos."))
		{
			return;
		}
		Route r = route;
		if (r != null)
		{
			int step = currentStep;
			Route updated = new RoutePlanner(config, farmingLevel, hunterLevel, ownedItems(), planGrowing).plan(r.getRunType());
			int size = updated.getSteps().size();
			currentStep = Math.min(step, size - 1);
			for (int i = 0; i < currentStep; i++)
			{
				updated.getSteps().get(i).setDone(true);
			}
			route = updated;
			refreshPanel();
		}
	}

	// ---- Helpers

	/**
	 * Works out which kind of farming patch an object is from its name, or null if it isn't one.
	 */
	private PatchType patchType(int objectId)
	{
		ObjectComposition composition = client.getObjectDefinition(objectId);
		// Real farming patches and bird house spaces change with your crops, so they have other states.
		// Decorations that happen to be called a patch or bush (Varrock palace rose bushes) don't.
		if (composition == null || composition.getImpostorIds() == null)
		{
			return null;
		}
		PatchType type = patchTypeFromName(composition.getName());
		int[] variants = composition.getImpostorIds();
		if (type == null && variants != null)
		{
			ObjectComposition impostor = composition.getImpostor();
			if (impostor != null)
			{
				type = patchTypeFromName(impostor.getName());
			}
			// A patch that's growing something shows the crop's name, so check its other states too
			for (int i = 0; type == null && i < variants.length; i++)
			{
				ObjectComposition variant = variants[i] > 0 ? client.getObjectDefinition(variants[i]) : null;
				if (variant != null)
				{
					type = patchTypeFromName(variant.getName());
				}
			}
		}
		return type;
	}

	/**
	 * The name the patch shows right now, which changes as it's weeded, planted and grown.
	 */
	private String currentName(TileObject object)
	{
		ObjectComposition composition = client.getObjectDefinition(object.getId());
		if (composition == null)
		{
			return null;
		}
		if (composition.getImpostorIds() != null)
		{
			ObjectComposition impostor = composition.getImpostor();
			return impostor != null ? impostor.getName() : null;
		}
		return composition.getName();
	}

	/**
	 * Whether a patch name means nothing is growing in it ("Herb patch", "Allotment", an empty bird house space).
	 */
	private static boolean isEmptyName(String name)
	{
		String n = name.toLowerCase();
		return n.contains("patch") || n.equals("allotment") || n.equals("space") || n.contains("(empty)");
	}

	/**
	 * Follows the four bird house spaces, which the game reports wherever you are on Fossil Island.
	 */
	private void updateBirdHouses(Player player)
	{
		// The game only sends bird house states while you're on Fossil Island, so they read as empty elsewhere
		WorldPoint here = player.getWorldLocation();
		if (!FarmLocation.FOSSIL_ISLAND.containsRegion(here.getRegionID()))
		{
			onFossilIsland = false;
			return;
		}
		if (here.getPlane() != 0)
		{
			return;
		}
		long now = System.currentTimeMillis();
		if (!onFossilIsland)
		{
			onFossilIsland = true;
			fossilArrivedAt = now;
			fossilTicks = 0;
		}
		// The game sends the bird house states a moment after you arrive, and until then they read as
		// empty: give them a few ticks to arrive before reading anything into them
		if (++fossilTicks < 3)
		{
			return;
		}
		for (int space : BirdHouses.SPACES)
		{
			int value = client.getVarpValue(space);
			loadBirdHouse(space);
			Integer saved = birdHouseValues.get(space);
			if (saved != null && saved == value)
			{
				continue;
			}
			// A change you made standing at it happened now; one found on arrival happened at an unknown time
			boolean atIt = saved != null && isAtBirdHouse(space, here);
			if (value == 0 && saved != null && saved != 0 && !atIt)
			{
				// A bird house only empties when you collect or dismantle it, standing at it. Reading empty
				// from anywhere else is the game not having sent its state yet, so keep what we had; otherwise
				// it would look freshly filled when the real state comes through
				continue;
			}
			long changed = atIt ? now : 0;
			if (!atIt)
			{
				birdHouseSeenAt.put(space, now);
			}
			birdHouseValues.put(space, value);
			birdHouseChanged.put(space, changed);
			configManager.setRSProfileConfiguration(FarmRunHelperConfig.GROUP, "birdhouse." + space, value + ":" + changed);
			if (atIt && BirdHouses.isSeeded(value))
			{
				seededThisRun.add(space);
			}
		}
	}

	/**
	 * Loads a space's saved state and change time from your profile, once per session.
	 */
	private void loadBirdHouse(int space)
	{
		if (birdHouseValues.containsKey(space))
		{
			return;
		}
		String saved = configManager.getRSProfileConfiguration(FarmRunHelperConfig.GROUP, "birdhouse." + space);
		if (saved == null)
		{
			return;
		}
		String[] parts = saved.split(":");
		try
		{
			birdHouseValues.put(space, Integer.parseInt(parts[0]));
			birdHouseChanged.put(space, parts.length > 1 ? Long.parseLong(parts[1]) : 0L);
		}
		catch (NumberFormatException ignored)
		{
			// bad saved value: treat as unknown
		}
	}

	/**
	 * When a filled bird house is full, or 0 if it's unknown when it was filled.
	 */
	private long birdHouseFullAt(int space)
	{
		long changed = birdHouseChanged.getOrDefault(space, 0L);
		if (changed <= 0)
		{
			changed = timeTrackingFilledAt(space);
		}
		return changed > 0 ? changed + BirdHouses.FILL_MILLIS : 0;
	}

	/**
	 * When RuneLite's Time Tracking plugin saw this bird house reach its current state, if it did.
	 * It saves "state:epoch seconds" per space, and is often running when this plugin wasn't.
	 */
	private long timeTrackingFilledAt(int space)
	{
		String saved = configManager.getRSProfileConfiguration("timetracking", "birdhouse." + space);
		if (saved == null)
		{
			return 0;
		}
		String[] parts = saved.split(":");
		try
		{
			int value = Integer.parseInt(parts[0]);
			long seconds = Long.parseLong(parts[1]);
			long at = seconds * 1000L;
			// Time Tracking also notes the state when you arrive on the island, which isn't when the house was
			// filled: a time from around when we found it already like this tells us nothing
			Long seenAt = birdHouseSeenAt.get(space);
			if ((seenAt != null && at >= seenAt - 2 * 60_000L) || (fossilArrivedAt > 0 && at >= fossilArrivedAt - 2 * 60_000L))
			{
				// Noted when you arrived, not when it was filled (a fill while this plugin is running is
				// timed from when you did it, so this fallback isn't needed for those)
				return 0;
			}
			return value == birdHouseValues.getOrDefault(space, -1) && seconds > 0 ? at : 0;
		}
		catch (RuntimeException e)
		{
			return 0;
		}
	}

	private boolean isAtBirdHouse(int space, WorldPoint here)
	{
		for (Map.Entry<GameObject, PatchType> entry : patchObjects.entrySet())
		{
			if (entry.getValue() != PatchType.BIRDHOUSE || entry.getKey().getWorldLocation().distanceTo(here) > 3)
			{
				continue;
			}
			ObjectComposition composition = client.getObjectDefinition(entry.getKey().getId());
			if (composition != null && composition.getVarPlayerId() == space)
			{
				return true;
			}
		}
		return false;
	}

	private boolean isSpaceDone(int space)
	{
		// Done when it's filled and still filling up; a full one needs collecting first
		return skippedSpaces.contains(space)
			|| (BirdHouses.isSeeded(birdHouseValues.getOrDefault(space, 0)) && birdHouseFullAt(space) > System.currentTimeMillis());
	}

	/**
	 * The next bird house space to do, or -1 when all four are done.
	 */
	private int nextBirdHouseSpace()
	{
		// Ones in the part of the island you're in first, nearest first, so you don't take a mushtree
		// away and back again (coming from the hardwood patches, that's the Tar Swamp one, then the
		// Mushroom Meadow one, then Verdant Valley)
		Player player = client.getLocalPlayer();
		WorldPoint here = player != null ? player.getWorldLocation() : null;
		String zone = BirdHouses.zone(here);
		int best = -1;
		int bestDistance = Integer.MAX_VALUE;
		if (zone != null)
		{
			for (int space : BirdHouses.SPACES)
			{
				WorldPoint spot = spaceSpot(space);
				if (!isSpaceDone(space) && BirdHouses.area(space).equals(zone) && spot != null
					&& spot.distanceTo2D(here) < bestDistance)
				{
					best = space;
					bestDistance = spot.distanceTo2D(here);
				}
			}
		}
		if (best != -1)
		{
			return best;
		}
		for (int space : BirdHouses.SPACES)
		{
			if (!isSpaceDone(space))
			{
				return space;
			}
		}
		return -1;
	}

	/**
	 * Where a bird house space is: where it was seen, or roughly where it is if it hasn't been yet.
	 */
	private WorldPoint spaceSpot(int space)
	{
		WorldPoint known = spaceLocation(space);
		return known != null ? known : BirdHouses.defaultSpot(space);
	}

	/**
	 * A bird house filled this run is growing; one already full from before needs collecting.
	 */
	private PatchStatus birdHouseStatus(int space, long now)
	{
		int value = birdHouseValues.getOrDefault(space, client.getVarpValue(space));
		if (!BirdHouses.isSeeded(value))
		{
			return new PatchStatus(PatchStatus.Kind.EMPTY, -1);
		}
		long fullAt = birdHouseFullAt(space);
		if (fullAt == 0)
		{
			// Filled, but neither plugin saw when: it may or may not be full yet
			return new PatchStatus(PatchStatus.Kind.READY, -1);
		}
		return fullAt > now ? new PatchStatus(PatchStatus.Kind.GROWING, fullAt) : new PatchStatus(PatchStatus.Kind.READY, now);
	}

	/**
	 * Where to take the magic mushtree to reach the next bird house, or null if you don't need to.
	 */
	String getMushtreeDestination()
	{
		return mushtreeDestination;
	}

	/**
	 * Directions to the next bird house when it's a walk away and not in view yet, or null.
	 */
	String getBirdHouseHint()
	{
		return birdHouseHint;
	}

	/**
	 * Reads every loaded patch's state from the game, the same way RuneLite's Time Tracking plugin does,
	 * so planted patches stop being highlighted and growing ones get a timer.
	 */
	private void updatePatchStatuses()
	{
		long now = System.currentTimeMillis();
		int offset = farmTickOffset();
		boolean changed = false;
		for (Map.Entry<GameObject, PatchType> entry : patchObjects.entrySet())
		{
			GameObject object = entry.getKey();
			PatchType type = entry.getValue();
			WorldPoint point = object.getWorldLocation();
			PatchStatus status = null;
			Integer spawned = patchSpawnTicks.get(object);
			if (type != PatchType.BIRDHOUSE && (!farmingVarbitsReady || !inSameStop(point)))
			{
				// Read patches the way RuneLite's Time Tracking does: only for the stop you're standing in, and
				// not while a full-screen interface (like a boat journey) is up or just after, because the game
				// doesn't send farming states then and they still hold the last place's (at Weiss's dock, the
				// Troll Stronghold herb's). Keep what was last read
				continue;
			}
			if (spawned != null && gameTicks - spawned < 3)
			{
				// Patches in different places share the same farming varbits, and for a moment after you
				// arrive they still hold the last place's values (Troll Stronghold's herb when you reach Weiss).
				// Wait for this place's to come through before reading anything into them
				continue;
			}

			ObjectComposition composition = client.getObjectDefinition(object.getId());
			if (composition != null && PatchStatus.implementation(type) != null)
			{
				int value = -1;
				if (composition.getVarbitId() != -1)
				{
					value = client.getVarbitValue(composition.getVarbitId());
				}
				else if (composition.getVarPlayerId() != -1)
				{
					value = client.getVarpValue(composition.getVarPlayerId());
				}
				if (value >= 0)
				{
					status = PatchStatus.fromVarbit(type, value, now, offset);
				}
			}
			if (status == null && type == PatchType.BIRDHOUSE && composition != null
				&& BirdHouses.isSpace(composition.getVarPlayerId()))
			{
				rememberSpaceLocation(composition.getVarPlayerId(), point);
				status = birdHouseStatus(composition.getVarPlayerId(), now);
			}
			if (status == null)
			{
				status = statusFromName(object, type, point, now);
			}

			int seenVarbit = composition != null ? (composition.getVarbitId() != -1 ? composition.getVarbitId() : composition.getVarPlayerId()) : -1;
			String seenKey = point.getRegionID() + ":" + (seenVarbit != -1 ? seenVarbit : point.getX() * 10000 + point.getY());
			SeenPatch seen = seenPatches.computeIfAbsent(seenKey, k -> new SeenPatch(point, type));
			seen.point = point;
			seen.status = status;
			PatchStatus previous = status != null ? patchStatus.put(point, status) : patchStatus.remove(point);
			if (previous != null && status != null)
			{
				if (previous.getKind() == PatchStatus.Kind.EMPTY && status.isPlanted())
				{
					plantedThisRun.add(point);
				}
				else if (previous.getKind() != PatchStatus.Kind.EMPTY && status.getKind() == PatchStatus.Kind.EMPTY)
				{
					// Harvesting or clearing a patch uses up its compost
					compostedPatches.remove(point);
				}
			}
			if (previous == null ? status != null : status == null || previous.getKind() != status.getKind())
			{
				changed = true;
			}
		}
		if (changed)
		{
			refreshPanel();
		}
	}

	/**
	 * Fallback for patches without a known farming state (bird houses): spots an empty spot being
	 * filled from its name changing, and times it from then.
	 */
	private PatchStatus statusFromName(GameObject object, PatchType type, WorldPoint point, long now)
	{
		String name = currentName(object);
		if (name == null || "null".equals(name))
		{
			return null;
		}
		boolean empty = isEmptyName(name);
		Boolean wasEmpty = patchWasEmpty.put(point, empty);
		if (empty)
		{
			plantedPatches.remove(point);
			return new PatchStatus(PatchStatus.Kind.EMPTY, -1);
		}
		if (Boolean.TRUE.equals(wasEmpty) && !plantedPatches.containsKey(point))
		{
			plantedPatches.put(point, GrowthTimes.readyAt(type, config, now));
		}
		Long readyAt = plantedPatches.get(point);
		return readyAt != null ? new PatchStatus(PatchStatus.Kind.GROWING, readyAt) : new PatchStatus(PatchStatus.Kind.READY, now);
	}

	/**
	 * The account's farming tick offset in minutes, if the Time Tracking plugin has worked it out. 0 otherwise.
	 */
	private int farmTickOffset()
	{
		try
		{
			Integer precision = configManager.getRSProfileConfiguration("timetracking", "farmTickOffsetPrecision", Integer.class);
			Integer offset = configManager.getRSProfileConfiguration("timetracking", "farmTickOffset", Integer.class);
			// Only once Time Tracking is sure of it, as it uses it itself (it's precise enough for every crop at 40)
			return precision != null && offset != null && precision >= 40 ? offset : 0;
		}
		catch (RuntimeException e)
		{
			return 0;
		}
	}

	/**
	 * Picks the patch to plant next: the first patch type in the step's order that still has an
	 * unplanted patch at this location, and the closest patch of that type.
	 */
	private void updateNextPatch(Player player)
	{
		RouteStep step = getCurrentStep();
		GameObject best = null;
		PatchType bestType = null;
		boolean compost = false;
		if (step != null && step.getLocation() != null)
		{
			// Compost a patch you just planted before moving on to the next one
			best = closestPatch(step, player, true);
			compost = best != null;
			if (best == null)
			{
				best = closestPatch(step, player, false);
			}
			bestType = best != null ? patchObjects.get(best) : null;
		}
		// Bird houses on Fossil Island: if the next one isn't nearby, take the magic mushtree to its area
		String destination = null;
		String hint = null;
		if (best == null && step != null && step.getPatchTypes().contains(PatchType.BIRDHOUSE)
			&& step.getLocation() != null && step.getLocation().containsRegion(player.getWorldLocation().getRegionID()))
		{
			int space = nextBirdHouseSpace();
			if (space != -1)
			{
				bestType = PatchType.BIRDHOUSE;
				if (BirdHouses.area(space).equals(BirdHouses.zone(player.getWorldLocation())))
				{
					// Already in that part of the island: it's a walk, not another mushtree
					hint = BirdHouses.walkHint(space);
				}
				else
				{
					destination = BirdHouses.area(space);
				}
			}
		}
		// Fossil Island hardwood patches: from the House on the Hill (where the digsite pendant lands) or
		// anywhere else on the island, the mushtree to Mushroom Meadow is much quicker than walking
		if (destination == null && hint == null && step != null && step.getLocation() == FarmLocation.FOSSIL_ISLAND
			&& step.getPatchTypes().contains(PatchType.HARDWOOD) && !compost)
		{
			String zone = BirdHouses.zone(player.getWorldLocation());
			if (zone != null && !zone.equals("Mushroom Meadow") && !allDone(step, PatchType.HARDWOOD))
			{
				destination = "Mushroom Meadow";
			}
		}
		if (!Objects.equals(destination, mushtreeDestination) || !Objects.equals(hint, birdHouseHint))
		{
			mushtreeDestination = destination;
			birdHouseHint = hint;
			refreshPanel();
		}
		remoteSpot = best == null && destination == null && hint == null && step != null
			&& step.getLocation() != null && step.getLocation().containsRegion(player.getWorldLocation().getRegionID())
			? remotePatchSpot(step, player) : null;
		if (best != nextPatch || compost != nextIsCompost)
		{
			nextPatch = best;
			nextPatchType = bestType;
			nextIsCompost = compost;
			refreshPanel();
		}
	}

	/**
	 * Where to walk when nothing at this stop that needs doing is in view: a patch seen earlier this run
	 * that's still to do, or where a patch type not seen yet is (from an earlier visit). Null if neither.
	 */
	private WorldPoint remotePatchSpot(RouteStep step, Player player)
	{
		if (step == null || step.getKind() != RouteStep.Kind.LOCATION || step.getLocation() == null)
		{
			return null;
		}
		WorldPoint here = player.getWorldLocation();
		WorldPoint best = null;
		for (SeenPatch seen : seenPatches.values())
		{
			if (step.getPatchTypes().contains(seen.type) && seen.type != PatchType.BIRDHOUSE
				&& step.getLocation().containsRegion(seen.point.getRegionID()) && seen.status != null
				&& !seen.status.isPlanted() && !skippedPatches.contains(seen.point)
				&& (best == null || seen.point.distanceTo2D(here) < best.distanceTo2D(here)))
			{
				best = seen.point;
			}
		}
		if (best != null)
		{
			return best;
		}
		// A patch at this stop that hasn't been seen yet this run: head for where it is
		for (LocationPoints.Spot spot : LocationPoints.spots(step.getLocation()))
		{
			if (!step.getPatchTypes().contains(spot.type))
			{
				continue;
			}
			boolean seenIt = false;
			for (SeenPatch seen : seenPatches.values())
			{
				seenIt |= seen.type == spot.type && seen.point.distanceTo2D(spot.point) <= 6;
			}
			if (!seenIt && (best == null || spot.point.distanceTo2D(here) < best.distanceTo2D(here)))
			{
				best = spot.point;
			}
		}
		return best;
	}

	private volatile WorldPoint remoteSpot;
	private String lastTravelHint;

	/**
	 * Whether every patch of this type at the stop has been seen and is growing or skipped.
	 */
	private boolean allDone(RouteStep step, PatchType type)
	{
		int done = 0;
		for (SeenPatch seen : seenPatches.values())
		{
			if (seen.type == type && step.getLocation().containsRegion(seen.point.getRegionID())
				&& seen.status != null && (seen.status.isPlanted() || skippedPatches.contains(seen.point)))
			{
				done++;
			}
		}
		return done >= step.getLocation().getPatches().getOrDefault(type, 0);
	}

	/**
	 * The closest patch at this step of the first patch type (in planting order) that still needs
	 * planting, or with {@code compost} set, that was planted this run and still needs compost.
	 */
	private GameObject closestPatch(RouteStep step, Player player, boolean compost)
	{
		if (compost && config.compost() == CompostChoice.NONE)
		{
			return null;
		}
		// The nearest patch that needs doing, whatever it is, so you don't walk past one to get to another
		WorldPoint here = player.getWorldLocation();
		GameObject best = null;
		int bestDistance = Integer.MAX_VALUE;
		for (PatchType type : step.getPatchTypes())
		{
			if (compost && !type.takesCompost())
			{
				continue;
			}
			for (Map.Entry<GameObject, PatchType> entry : patchObjects.entrySet())
			{
				GameObject object = entry.getKey();
				WorldPoint point = object.getWorldLocation();
				if (entry.getValue() != type || object.getPlane() != here.getPlane()
					|| !step.getLocation().containsRegion(point.getRegionID()))
				{
					continue;
				}
				boolean wanted = compost
					? plantedThisRun.contains(point) && !compostedPatches.contains(point) && isHandled(point)
					: !isHandled(point);
				if (wanted && type == PatchType.BIRDHOUSE)
				{
					// Bird houses go in a set order, so only the next space counts
					ObjectComposition composition = client.getObjectDefinition(object.getId());
					int space = composition != null ? composition.getVarPlayerId() : -1;
					wanted = !BirdHouses.isSpace(space) || space == nextBirdHouseSpace();
				}
				if (!wanted)
				{
					continue;
				}
				int distance = point.distanceTo(here);
				if (distance < bestDistance)
				{
					bestDistance = distance;
					best = object;
				}
			}
		}
		return best;
	}

	/**
	 * Whether a patch is already growing something, or was skipped during this run.
	 */
	/**
	 * At the Fossil Island hardwood patches: the squirrel next to each grown tree, to pay to remove it once checked.
	 */
	List<NPC> getSquirrelTargets()
	{
		RouteStep step = getCurrentStep();
		if (step == null || step.getLocation() != FarmLocation.FOSSIL_ISLAND || squirrels.isEmpty())
		{
			return Collections.emptyList();
		}
		List<NPC> targets = new ArrayList<>();
		for (Map.Entry<GameObject, PatchType> entry : patchObjects.entrySet())
		{
			WorldPoint point = entry.getKey().getWorldLocation();
			PatchStatus status = patchStatus.get(point);
			if (entry.getValue() != PatchType.HARDWOOD || status == null || status.getKind() != PatchStatus.Kind.READY
				|| skippedPatches.contains(point))
			{
				continue;
			}
			NPC nearest = null;
			int best = 12;
			for (NPC squirrel : squirrels)
			{
				WorldPoint at = squirrel.getWorldLocation();
				if (at != null && at.distanceTo(point) <= best)
				{
					best = at.distanceTo(point);
					nearest = squirrel;
				}
			}
			if (nearest != null && !targets.contains(nearest))
			{
				targets.add(nearest);
			}
		}
		return targets;
	}

	/**
	 * Text to show over the next thing to click when the option isn't the usual left-click, like
	 * "Right-click: Dive" on the rowboat you dive from. Null otherwise.
	 */
	String getTravelLabel()
	{
		if (!isTravelling())
		{
			return null;
		}
		RouteStep step = getCurrentStep();
		List<String> legs = TravelLegs.legs(step.getLocation(), step.getTravel(), config);
		int leg = currentLeg(legs);
		if (leg >= legs.size() || TravelLegs.isNpc(legs.get(leg)))
		{
			return null;
		}
		String option = TravelLegs.dialogueOption(legs.get(leg));
		if (option == null)
		{
			return null;
		}
		if (option.equals("row out to sea"))
		{
			// The seaweed rowboat on the shore: click it, then "Row out to sea." is the third choice (quickly,
			// before the rock crabs close the menu)
			return "Press 3";
		}
		return "Right-click: " + Character.toUpperCase(option.charAt(0)) + option.substring(1);
	}

	/**
	 * What to do next while still on the way to the stop, for the panel, or null when you're there.
	 */
	String getTravelHint()
	{
		// Worked out each tick on the client thread; the panel only reads it
		return travelHint;
	}

	private volatile String travelHint;

	private String lastLegLog;

	// Writes to the log whenever the part of the route being highlighted changes, to help track down route problems
	private void logLegChange(Player player)
	{
		RouteStep step = getCurrentStep();
		String state = null;
		if (step != null && step.getTravel() != null && isTravelling())
		{
			List<String> legs = TravelLegs.legs(step.getLocation(), step.getTravel(), config);
			int leg = currentLeg(legs);
			state = step.getTitle() + ": leg " + leg + " (" + (leg < legs.size() ? legs.get(leg) : "stop") + "), done " + legsDone
				+ ", in view " + (leg < legs.size() ? loadedLeg(legs.get(leg)).size() : 0);
		}
		if (!Objects.equals(state, lastLegLog))
		{
			lastLegLog = state;
			log.debug("Farm Run Helper route: {} at {}", state, player.getWorldLocation());
		}
	}

	private String computeTravelHint()
	{
		if (!isTravelling())
		{
			return null;
		}
		RouteStep step = getCurrentStep();
		List<String> legs = TravelLegs.legs(step.getLocation(), step.getTravel(), config);
		int leg = currentLeg(legs);
		if (legsDone >= 0 && leg < legs.size())
		{
			String next = legs.get(leg);
			String name = next.replaceFirst("^npc:", "").replaceFirst("^~", "").replaceAll("[@|].*$", "");
			if (name.isEmpty())
			{
				return "follow the highlighted route";
			}
			return TravelLegs.isNpc(next) ? "talk to " + Character.toUpperCase(name.charAt(0)) + name.substring(1)
				: "use the " + name;
		}
		return "travel to " + step.getLocation().getDisplayName() + " (" + step.getTravel().getLabel().toLowerCase() + ")";
	}

	/**
	 * Whether there's a patch still to do at this stop that's out of view, which the path leads to.
	 */
	boolean hasPatchOutOfView()
	{
		return remoteSpot != null;
	}

	boolean isHandled(WorldPoint point)
	{
		PatchStatus status = patchStatus.get(point);
		return (status != null && status.isPlanted()) || skippedPatches.contains(point);
	}

	/**
	 * Whether the next thing to do is compost the patch you just planted.
	 */
	boolean isNextCompost()
	{
		return nextIsCompost;
	}

	GameObject getNextPatch()
	{
		return nextPatch;
	}

	PatchType getNextPatchType()
	{
		return nextPatchType;
	}

	/**
	 * Item ids to highlight in the inventory for the next patch.
	 */
	Set<Integer> getNextSeedItems()
	{
		RouteStep step = getCurrentStep();
		PatchType type = nextPatchType;
		if (nextIsCompost)
		{
			CompostChoice compost = config.compost();
			return compost.getItemId() > 0 ? Collections.singleton(compost.getItemId()) : Collections.emptySet();
		}
		return step != null && type != null ? step.getSeedItems(type) : Collections.emptySet();
	}

	/**
	 * Marks the next patch as done without planting, for patches that can't be planted right now.
	 */
	void skipNextPatch()
	{
		GameObject patch = nextPatch;
		if (patch != null)
		{
			(nextIsCompost ? compostedPatches : skippedPatches).add(patch.getWorldLocation());
		}
		if (nextPatchType == PatchType.BIRDHOUSE && !nextIsCompost)
		{
			int space = nextBirdHouseSpace();
			if (space != -1)
			{
				skippedSpaces.add(space);
			}
		}
	}

	private static boolean givesProduce(PatchType type)
	{
		switch (type)
		{
			case TREE:
			case HARDWOOD:
			case REDWOOD:
			case ANIMA:
			case HESPORI:
			case BIRDHOUSE:
				return false;
			default:
				return true;
		}
	}

	/**
	 * Warns when the inventory is nearly full while patches with produce are still left at this location.
	 */
	private void checkInventorySpace(Player player)
	{
		String warning = null;
		RouteStep step = getCurrentStep();
		int threshold = config.spaceWarning();
		int free = freeSlots;
		if (threshold > 0 && free <= threshold && step != null && step.getLocation() != null)
		{
			int plane = player.getWorldLocation().getPlane();
			int left = 0;
			for (Map.Entry<GameObject, PatchType> entry : patchObjects.entrySet())
			{
				GameObject object = entry.getKey();
				PatchType type = entry.getValue();
				PatchStatus status = patchStatus.get(object.getWorldLocation());
				if (givesProduce(type) && step.getPatchTypes().contains(type) && object.getPlane() == plane
					&& step.getLocation().containsRegion(object.getWorldLocation().getRegionID())
					&& !skippedPatches.contains(object.getWorldLocation())
					&& status != null && status.getKind() == PatchStatus.Kind.READY)
				{
					left++;
				}
			}
			if (left > 0)
			{
				warning = (free == 0 ? "Inventory full" : "Only " + free + " free slot" + (free == 1 ? "" : "s"))
					+ " and " + left + " patch" + (left == 1 ? "" : "es") + " left to harvest here. Use your produce on the tool leprechaun to note it.";
			}
		}
		String previous = spaceWarningText;
		spaceWarningText = warning;
		if (warning != null && previous == null)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag("Farm Run Helper: " + warning, java.awt.Color.RED), null);
		}
		if (!Objects.equals(previous, warning))
		{
			refreshPanel();
		}
	}


	private static PatchType patchTypeFromName(String name)
	{
		if (name == null)
		{
			return null;
		}
		String n = name.toLowerCase();
		if (n.contains("bird house") || n.contains("birdhouse"))
		{
			return PatchType.BIRDHOUSE;
		}
		if (n.equals("allotment") || n.contains("allotment patch"))
		{
			return PatchType.ALLOTMENT;
		}
		if (!n.contains("patch") || n.contains("spirit") || n.contains("compost"))
		{
			return null;
		}
		if (n.contains("herb"))
		{
			return PatchType.HERB;
		}
		if (n.contains("flower"))
		{
			return PatchType.FLOWER;
		}
		if (n.contains("bush"))
		{
			return PatchType.BUSH;
		}
		if (n.contains("cactus"))
		{
			return PatchType.CACTUS;
		}
		if (n.contains("belladonna"))
		{
			return PatchType.BELLADONNA;
		}
		if (n.contains("mushroom"))
		{
			return PatchType.MUSHROOM;
		}
		if (n.contains("seaweed"))
		{
			return PatchType.SEAWEED;
		}
		if (n.contains("anima"))
		{
			return PatchType.ANIMA;
		}
		if (n.contains("hespori"))
		{
			return PatchType.HESPORI;
		}
		if (n.contains("fruit"))
		{
			return PatchType.FRUIT_TREE;
		}
		if (n.contains("hardwood"))
		{
			return PatchType.HARDWOOD;
		}
		if (n.contains("calquat"))
		{
			return PatchType.CALQUAT;
		}
		if (n.contains("celastrus"))
		{
			return PatchType.CELASTRUS;
		}
		if (n.contains("redwood"))
		{
			return PatchType.REDWOOD;
		}
		if (n.contains("tree"))
		{
			return PatchType.TREE;
		}
		return null;
	}

	private Set<Integer> ownedItems()
	{
		Set<Integer> owned = new HashSet<>(bankItems);
		owned.addAll(vaultItems);
		owned.addAll(carriedItems.keySet());
		return owned;
	}

	private void storeIds(ItemContainer container, Set<Integer> target, String key)
	{
		Set<Integer> ids = new HashSet<>();
		for (Item item : container.getItems())
		{
			if (item.getId() > 0 && item.getQuantity() > 0)
			{
				ids.add(item.getId());
			}
		}
		if (ids.equals(target))
		{
			return;
		}
		target.clear();
		target.addAll(ids);
		StringBuilder csv = new StringBuilder();
		for (int itemId : ids)
		{
			if (csv.length() > 0)
			{
				csv.append(',');
			}
			csv.append(itemId);
		}
		configManager.setConfiguration(FarmRunHelperConfig.GROUP, key, csv.toString());
	}

	private static Map<Integer, Integer> count(ItemContainer container)
	{
		Map<Integer, Integer> counts = new HashMap<>();
		for (Item item : container.getItems())
		{
			if (item.getId() > 0 && item.getQuantity() > 0)
			{
				counts.merge(item.getId(), item.getQuantity(), Integer::sum);
			}
		}
		return counts;
	}

	private static void readIds(String csv, Set<Integer> target)
	{
		target.clear();
		if (csv == null || csv.isEmpty())
		{
			return;
		}
		for (String part : csv.split(","))
		{
			try
			{
				target.add(Integer.parseInt(part.trim()));
			}
			catch (NumberFormatException ignored)
			{
				// skip bad entries
			}
		}
	}

	private void refreshPanel()
	{
		FarmRunPanel p = panel;
		if (p != null)
		{
			SwingUtilities.invokeLater(p::refresh);
		}
	}
}

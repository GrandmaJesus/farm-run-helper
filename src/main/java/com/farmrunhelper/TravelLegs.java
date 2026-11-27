package com.farmrunhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The things you click, in order, on the way to a farming stop: spirit trees, fairy rings, mushtrees,
 * boats and the people who sail them. Names are lower case; "npc:" marks a person to talk to, with
 * "|option" for the right-click or dialogue option to pick (an object with an option and no "@x,y" spot
 * only counts when it has that option), and "~" matches any name containing the text.
 */
final class TravelLegs
{
	static final String SPIRIT_TREE = "spirit tree";
	static final String FAIRY_RING = "fairy ring";
	static final String MUSHTREE = "magic mushtree";

	private static final Map<String, String[]> EXTRA = new HashMap<>();

	static
	{
		EXTRA.put("WEISS.FAIRY_RING", new String[]{"npc:larry|weiss", "cave entrance@2859,3966"});
		EXTRA.put("TREE_GNOME_VILLAGE.SPIRIT_TREE", new String[]{"npc:elkoy|follow"});
		EXTRA.put("ETCETERIA.SEA_BOOTS", new String[]{"npc:sailor"});
		EXTRA.put("HARMONY.MOS_LE_HARMLESS", new String[]{"npc:brother tranquility"});
		EXTRA.put("BRIMHAVEN.KARAMJA_GLOVES", new String[]{"npc:vigroy"});
		EXTRA.put("BRIMHAVEN.ARDOUGNE_BOAT", new String[]{"npc:captain barnaby|brimhaven", "~gangplank"});
		EXTRA.put("SEAWEED.ROWBOAT", new String[]{MUSHTREE, "rowboat@3735,3894|row out to sea", "rowboat@3764,3899|dive"});
		EXTRA.put("ANGLERS_RETREAT.MYTHS_GUILD", new String[]{"rowboat@2543,2842"});
		EXTRA.put("ANGLERS_RETREAT.CORSAIR_COVE", new String[]{"rowboat@2543,2842"});
	}

	private static final Map<String, int[]> PEOPLE = new HashMap<>();

	static
	{
		PEOPLE.put("larry", new int[]{2706, 3733});
		PEOPLE.put("elkoy", new int[]{2514, 3159});
		PEOPLE.put("brother tranquility", new int[]{3681, 2963});
		PEOPLE.put("vigroy", new int[]{2836, 2956});
		PEOPLE.put("captain barnaby", new int[]{2682, 3276});
	}

	private TravelLegs()
	{
	}

	static List<String> legs(FarmLocation location, TeleportOption option, FarmRunHelperConfig config)
	{
		List<String> legs = new ArrayList<>();
		if (TravelKind.usesSpiritTree(option))
		{
			legs.add(SPIRIT_TREE);
		}
		if (TravelKind.usesFairyRing(option))
		{
			legs.add(FAIRY_RING);
		}
		String[] extra = EXTRA.get(location.name() + "." + option.name());
		if (extra != null)
		{
			Collections.addAll(legs, extra);
		}
		return legs;
	}

	/**
	 * Legs you teleport from, which leave you standing next to another one of the same kind.
	 */
	static boolean isTeleportLeg(String leg)
	{
		return leg.equals(SPIRIT_TREE) || leg.equals(FAIRY_RING) || leg.equals(MUSHTREE);
	}

	/**
	 * The dialogue option to pick after talking to this person, or null.
	 */
	static String dialogueOption(String leg)
	{
		List<String> options = dialogueOptions(leg);
		return options.isEmpty() ? null : options.get(0);
	}

	/**
	 * Every option to pick along this leg, in order, like "Row out to sea" and then "Dive" for the seaweed rowboat.
	 */
	static List<String> dialogueOptions(String leg)
	{
		int bar = leg.indexOf('|');
		if (bar < 0)
		{
			return Collections.emptyList();
		}
		List<String> options = new ArrayList<>();
		Collections.addAll(options, leg.substring(bar + 1).split("\\|"));
		return options;
	}

	/**
	 * The name part of a leg, without "npc:" or "|option".
	 */
	private static String name(String leg)
	{
		String name = isNpc(leg) ? leg.substring(4) : leg;
		int bar = name.indexOf('|');
		name = bar >= 0 ? name.substring(0, bar) : name;
		int at = name.indexOf('@');
		return at >= 0 ? name.substring(0, at) : name;
	}

	/**
	 * Where this one is, for legs like "rowboat@3735,3894" where several share a name, or null.
	 */
	static net.runelite.api.coords.WorldPoint position(String leg)
	{
		int at = leg.indexOf('@');
		if (at < 0)
		{
			return null;
		}
		int bar = leg.indexOf('|', at);
		String[] xy = leg.substring(at + 1, bar >= 0 ? bar : leg.length()).split(",");
		return new net.runelite.api.coords.WorldPoint(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]), 0);
	}

	// Where people on routes stand, so the path can lead there before they're in view
	static net.runelite.api.coords.WorldPoint knownPoint(String leg)
	{
		if (isNpc(leg))
		{
			int[] xy = PEOPLE.get(name(leg));
			if (xy != null)
			{
				return new net.runelite.api.coords.WorldPoint(xy[0], xy[1], 0);
			}
		}
		return position(leg);
	}

	static boolean isNpc(String leg)
	{
		return leg.startsWith("npc:");
	}

	static boolean matches(String leg, String name)
	{
		if (name == null)
		{
			return false;
		}
		String n = name.toLowerCase();
		if (isNpc(leg))
		{
			return n.equals(name(leg));
		}
		if (leg.startsWith("~"))
		{
			return n.contains(name(leg).substring(1));
		}
		if (leg.equals(SPIRIT_TREE))
		{
			return (n.contains("spirit tree") && !n.contains("patch")) || n.contains("spiritual fairy tree");
		}
		if (leg.equals(FAIRY_RING))
		{
			return n.contains("fairy ring") || n.contains("spiritual fairy tree");
		}
		return n.equals(name(leg));
	}

	/**
	 * Whether an object or NPC with this name might be part of some route.
	 */
	static boolean isAnyLeg(String name, boolean npc)
	{
		for (String leg : ALL)
		{
			if (isNpc(leg) == npc && matches(leg, name))
			{
				return true;
			}
		}
		return false;
	}

	private static final Set<String> ALL = new HashSet<>();

	static
	{
		ALL.add(SPIRIT_TREE);
		ALL.add(FAIRY_RING);
		for (String[] legs : EXTRA.values())
		{
			Collections.addAll(ALL, legs);
		}
	}
}

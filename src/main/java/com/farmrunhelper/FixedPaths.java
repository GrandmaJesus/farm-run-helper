package com.farmrunhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.coords.WorldPoint;

/**
 * The fixed walking paths that ship with the plugin (paths.txt, made by gen/pathgen/PathGen.java). Each place
 * a path can lead to (a patch, bird house, boat, cave, person or teleport) has paths from every landing spot
 * and nearby patch, worked out once from the game map, so the path shown is always the same.
 */
final class FixedPaths
{
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FixedPaths.class);

	// Step codes 0-7 in paths.txt
	private static final int[][] STEPS = {
		{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
	};
	// How far from a path you can be and still be shown it
	private static final int JOIN = 15;

	private static volatile FixedPaths instance;

	private final List<WorldPoint> goals = new ArrayList<>();
	private final List<List<String>> lines = new ArrayList<>();

	// The goal whose paths were last put together, tile -> next tile
	private int builtFor = -1;
	private Map<WorldPoint, WorldPoint> next = Collections.emptyMap();

	private FixedPaths()
	{
	}

	static FixedPaths get()
	{
		FixedPaths paths = instance;
		if (paths == null)
		{
			synchronized (FixedPaths.class)
			{
				if (instance == null)
				{
					instance = load();
				}
				paths = instance;
			}
		}
		return paths;
	}

	private static FixedPaths load()
	{
		FixedPaths paths = new FixedPaths();
		try (InputStream in = FixedPaths.class.getResourceAsStream("paths.txt"))
		{
			if (in == null)
			{
				return paths;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("#") || line.isEmpty())
				{
					continue;
				}
				if (line.startsWith("G "))
				{
					String[] p = line.split(" ");
					paths.goals.add(new WorldPoint(Integer.parseInt(p[1]), Integer.parseInt(p[2]), Integer.parseInt(p[3])));
					paths.lines.add(new ArrayList<>());
				}
				else if (!paths.lines.isEmpty())
				{
					paths.lines.get(paths.lines.size() - 1).add(line);
				}
			}
		}
		catch (IOException | RuntimeException e)
		{
			log.warn("Farm Run Helper: couldn't load the walking paths", e);
			paths.goals.clear();
			paths.lines.clear();
		}
		return paths;
	}

	/**
	 * The place with paths closest to this spot, if one is within the given distance, or -1.
	 */
	private int goalNear(WorldPoint spot, int within)
	{
		int best = -1;
		for (int i = 0; i < goals.size(); i++)
		{
			WorldPoint goal = goals.get(i);
			if (goal.getPlane() == spot.getPlane() && goal.distanceTo2D(spot) <= within
				&& (best == -1 || goal.distanceTo2D(spot) < goals.get(best).distanceTo2D(spot)))
			{
				best = i;
			}
		}
		return best;
	}

	/**
	 * The place with paths that a target (within 4 tiles of it) leads to, or null.
	 */
	synchronized WorldPoint goalFor(WorldPoint target)
	{
		int goal = goalNear(target, 4);
		return goal == -1 ? null : goals.get(goal);
	}

	/**
	 * The fixed path from near where you are to the place closest to the target (within 4 tiles of it),
	 * as the tiles to walk. Empty if there's no such place, or you're not near any of its paths.
	 */
	synchronized List<WorldPoint> path(WorldPoint target, WorldPoint from)
	{
		int goal = goalNear(target, 4);
		if (goal == -1)
		{
			return Collections.emptyList();
		}
		if (goal != builtFor)
		{
			next = build(goal);
			builtFor = goal;
		}
		WorldPoint start = next.containsKey(from) ? from : null;
		if (start == null)
		{
			// Not on a path: join the nearest one
			int bestDistance = JOIN + 1;
			for (WorldPoint tile : next.keySet())
			{
				int d = tile.distanceTo2D(from);
				if (tile.getPlane() == from.getPlane() && d < bestDistance)
				{
					bestDistance = d;
					start = tile;
				}
			}
			if (start == null)
			{
				return Collections.emptyList();
			}
		}
		List<WorldPoint> route = new ArrayList<>();
		if (!start.equals(from))
		{
			route.add(start);
		}
		WorldPoint at = start;
		while (next.containsKey(at) && route.size() < 1000)
		{
			at = next.get(at);
			route.add(at);
		}
		return route;
	}

	private Map<WorldPoint, WorldPoint> build(int goal)
	{
		Map<WorldPoint, WorldPoint> map = new HashMap<>();
		int plane = goals.get(goal).getPlane();
		for (String line : lines.get(goal))
		{
			String[] p = line.split(" ");
			int x = Integer.parseInt(p[0]);
			int y = Integer.parseInt(p[1]);
			String steps = p.length > 2 ? p[2] : "";
			WorldPoint at = new WorldPoint(x, y, plane);
			for (int i = 0; i < steps.length(); i++)
			{
				int[] d = STEPS[steps.charAt(i) - '0'];
				WorldPoint to = new WorldPoint(at.getX() + d[0], at.getY() + d[1], plane);
				map.putIfAbsent(at, to);
				at = to;
			}
		}
		return map;
	}
}

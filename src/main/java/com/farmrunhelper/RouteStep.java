package com.farmrunhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RouteStep
{
	public enum Kind
	{
		BANK,
		LOCATION,
		FINISH
	}

	private final Kind kind;
	private final int tripIndex;
	private final String title;
	private final List<String> lines = new ArrayList<>();
	private final FarmLocation location;
	private final Set<PatchType> patchTypes = EnumSet.noneOf(PatchType.class);
	private final Map<PatchType, Set<Integer>> seedItems = new EnumMap<>(PatchType.class);
	private final Map<PatchType, String> seedLabels = new EnumMap<>(PatchType.class);
	private TeleportOption travel;
	private volatile String note;
	private boolean done;

	RouteStep(Kind kind, int tripIndex, String title, FarmLocation location)
	{
		this.kind = kind;
		this.tripIndex = tripIndex;
		this.title = title;
		this.location = location;
	}

	void addLine(String line)
	{
		lines.add(line);
	}

	void addPatchType(PatchType type)
	{
		patchTypes.add(type);
	}

	public Kind getKind()
	{
		return kind;
	}

	public int getTripIndex()
	{
		return tripIndex;
	}

	public String getTitle()
	{
		return title;
	}

	public List<String> getLines()
	{
		return Collections.unmodifiableList(lines);
	}

	/**
	 * A status note shown under the step, like when it was skipped because everything is still growing.
	 */
	public String getNote()
	{
		return note;
	}

	void setNote(String note)
	{
		this.note = note;
	}

	void setTravel(TeleportOption travel)
	{
		this.travel = travel;
	}

	/**
	 * How you get to this step's location, or null for bank and finish steps.
	 */
	public TeleportOption getTravel()
	{
		return travel;
	}

	public FarmLocation getLocation()
	{
		return location;
	}

	void addSeedItem(PatchType type, int itemId, String label)
	{
		seedItems.computeIfAbsent(type, t -> new HashSet<>()).add(itemId);
		seedLabels.putIfAbsent(type, label);
	}

	/**
	 * Item ids planted in this patch type at this step (a bird house and its seeds for bird houses).
	 */
	public Set<Integer> getSeedItems(PatchType type)
	{
		Set<Integer> ids = seedItems.get(type);
		return ids != null ? ids : Collections.emptySet();
	}

	public String getSeedLabel(PatchType type)
	{
		return seedLabels.getOrDefault(type, type.getLabel());
	}

	/**
	 * Patch types at this step, in the order they should be planted.
	 */
	public Set<PatchType> getPatchTypes()
	{
		return patchTypes;
	}

	public boolean isDone()
	{
		return done;
	}

	public void setDone(boolean done)
	{
		this.done = done;
	}
}

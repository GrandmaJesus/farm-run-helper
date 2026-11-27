package com.farmrunhelper;

/**
 * What a patch needs right now, read from the game's farming state.
 */
final class PatchStatus
{
	enum Kind
	{
		/** Weeds or empty: ready to plant. */
		EMPTY,
		/** Something is growing; nothing to do until it's done. */
		GROWING,
		/** Fully grown and ready to harvest, check or clear. */
		READY,
		DISEASED,
		DEAD
	}

	private final Kind kind;
	private final long readyAt;

	PatchStatus(Kind kind, long readyAt)
	{
		this.kind = kind;
		this.readyAt = readyAt;
	}

	Kind getKind()
	{
		return kind;
	}

	/**
	 * Estimated epoch millis when a growing crop is done, or -1 if unknown.
	 */
	long getReadyAt()
	{
		return readyAt;
	}

	/**
	 * Whether the patch has been planted and needs nothing until it finishes growing.
	 */
	boolean isPlanted()
	{
		return kind == Kind.GROWING;
	}

	static PatchImplementation implementation(PatchType type)
	{
		switch (type)
		{
			case HERB:
				return PatchImplementation.HERB;
			case FLOWER:
				return PatchImplementation.FLOWER;
			case ALLOTMENT:
				return PatchImplementation.ALLOTMENT;
			case BUSH:
				return PatchImplementation.BUSH;
			case CACTUS:
				return PatchImplementation.CACTUS;
			case BELLADONNA:
				return PatchImplementation.BELLADONNA;
			case MUSHROOM:
				return PatchImplementation.MUSHROOM;
			case SEAWEED:
				return PatchImplementation.SEAWEED;
			case ANIMA:
				return PatchImplementation.ANIMA;
			case HESPORI:
				return PatchImplementation.HESPORI;
			case TREE:
				return PatchImplementation.TREE;
			case FRUIT_TREE:
				return PatchImplementation.FRUIT_TREE;
			case HARDWOOD:
				return PatchImplementation.HARDWOOD_TREE;
			case CALQUAT:
				return PatchImplementation.CALQUAT;
			case CELASTRUS:
				return PatchImplementation.CELASTRUS;
			case REDWOOD:
				return PatchImplementation.REDWOOD;
			default:
				return null;
		}
	}

	/**
	 * Works out the status from the patch's farming varbit value, or null if the value isn't recognised.
	 *
	 * @param offsetMinutes the account's farming tick offset (0 if unknown), as learned by Time Tracking
	 */
	static PatchStatus fromVarbit(PatchType type, int value, long nowMillis, int offsetMinutes)
	{
		PatchImplementation implementation = implementation(type);
		PatchState state = implementation != null ? implementation.forVarbitValue(value) : null;
		if (state == null)
		{
			return null;
		}
		if (state.getProduce() == Produce.WEEDS || state.getProduce() == Produce.SCARECROW
			|| state.getCropState() == CropState.EMPTY)
		{
			return new PatchStatus(Kind.EMPTY, -1);
		}
		switch (state.getCropState())
		{
			case HARVESTABLE:
				return new PatchStatus(Kind.READY, nowMillis);
			case DISEASED:
				return new PatchStatus(Kind.DISEASED, -1);
			case DEAD:
				return new PatchStatus(Kind.DEAD, -1);
			case GROWING:
				// The last growing stage is a plant waiting for you: a tree, bush or cactus to check the health
				// of, or a dead anima plant to clear. Time Tracking calls these growing, but they need doing
				if (state.getStages() > 1 && state.getStage() >= state.getStages() - 1)
				{
					return new PatchStatus(Kind.READY, nowMillis);
				}
				return new PatchStatus(Kind.GROWING, doneEstimate(state, nowMillis, offsetMinutes));
			default:
				return null;
		}
	}

	/**
	 * Same estimate as Time Tracking: growth happens on clock-aligned ticks, shifted by the account's offset.
	 */
	private static long doneEstimate(PatchState state, long nowMillis, int offsetMinutes)
	{
		int tickrate = state.getTickRate();
		int ticksLeft = state.getStages() - 1 - state.getStage();
		if (tickrate <= 0 || ticksLeft < 0)
		{
			return -1;
		}
		return tickTime(tickrate, ticksLeft, nowMillis / 1000L, offsetMinutes) * 1000L;
	}

	/**
	 * Epoch seconds of the growth tick {@code ticks} ticks after the one in progress at {@code seconds},
	 * as Time Tracking's FarmingTracker works it out.
	 */
	private static long tickTime(int tickrate, int ticks, long seconds, int offsetMinutes)
	{
		long offset = (offsetMinutes % tickrate) * 60L;
		long shifted = seconds + offset;
		long tickSeconds = tickrate * 60L;
		long currentTick = shifted - (shifted % tickSeconds);
		return currentTick + ticks * tickSeconds - offset;
	}

	/**
	 * Predicts a patch's state now from a value Time Tracking saved earlier, advancing its growth by
	 * the ticks since then (like Time Tracking's own prediction). Null if the value isn't recognised.
	 */
	static PatchStatus predict(PatchType type, int value, long savedSeconds, long nowMillis, int offsetMinutes)
	{
		PatchImplementation implementation = implementation(type);
		PatchState state = implementation != null ? implementation.forVarbitValue(value) : null;
		if (state == null)
		{
			return null;
		}
		if (state.getProduce() == Produce.WEEDS || state.getProduce() == Produce.SCARECROW
			|| state.getCropState() == CropState.EMPTY)
		{
			return new PatchStatus(Kind.EMPTY, -1);
		}
		switch (state.getCropState())
		{
			case DISEASED:
				return new PatchStatus(Kind.DISEASED, -1);
			case DEAD:
				return new PatchStatus(Kind.DEAD, -1);
			case HARVESTABLE:
				return new PatchStatus(Kind.READY, nowMillis);
			case GROWING:
				int tickrate = state.getTickRate();
				if (tickrate <= 0)
				{
					return new PatchStatus(Kind.GROWING, -1);
				}
				long nowSeconds = nowMillis / 1000L;
				long tickThen = tickTime(tickrate, 0, savedSeconds, offsetMinutes);
				long tickNow = tickTime(tickrate, 0, nowSeconds, offsetMinutes);
				int stage = state.getStage() + (int) ((tickNow - tickThen) / (tickrate * 60L));
				if (stage >= state.getStages() - 1)
				{
					return new PatchStatus(Kind.READY, nowMillis);
				}
				long done = tickTime(tickrate, state.getStages() - 1 - state.getStage(), tickThen, offsetMinutes);
				return new PatchStatus(Kind.GROWING, done * 1000L);
			default:
				return null;
		}
	}
}

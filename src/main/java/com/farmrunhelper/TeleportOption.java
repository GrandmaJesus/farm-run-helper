package com.farmrunhelper;

/**
 * One way of reaching a farming location. Each location has its own enum of options
 * (see {@link Teleports}) so the config dropdown only lists options that make sense there.
 */
public interface TeleportOption
{
	String name();

	String getLabel();

	String getInstruction();

	Req[] getRequirements();

	/**
	 * Items that can be used for this teleport. An empty array means a spell, tab, house portal
	 * or walking route that the planner doesn't need to check for.
	 */
	int[] getItemIds();

	default boolean isAuto()
	{
		return "AUTO".equals(name());
	}

	default boolean isSkip()
	{
		return "SKIP".equals(name());
	}
}

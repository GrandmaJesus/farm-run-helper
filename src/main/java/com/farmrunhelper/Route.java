package com.farmrunhelper;

import java.util.Collections;
import java.util.List;

public class Route
{
	private final RunType runType;
	private final List<RouteStep> steps;
	private final List<Trip> trips;
	private final List<String> warnings;

	Route(RunType runType, List<RouteStep> steps, List<Trip> trips, List<String> warnings)
	{
		this.runType = runType;
		this.steps = steps;
		this.trips = trips;
		this.warnings = warnings;
	}

	public RunType getRunType()
	{
		return runType;
	}

	public List<RouteStep> getSteps()
	{
		return Collections.unmodifiableList(steps);
	}

	public List<Trip> getTrips()
	{
		return Collections.unmodifiableList(trips);
	}

	public List<String> getWarnings()
	{
		return Collections.unmodifiableList(warnings);
	}

	public Trip getTrip(int index)
	{
		for (Trip trip : trips)
		{
			if (trip.getIndex() == index)
			{
				return trip;
			}
		}
		return null;
	}
}

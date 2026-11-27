package com.farmrunhelper;

import java.util.EnumSet;
import java.util.Set;

public enum RunType
{
	HERB("Herb run"),
	TREE("Tree run"),
	FARM("Full Farm run"),
	BIRDHOUSE("Bird house run"),
	CUSTOM("Custom run");

	private final String label;

	RunType(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}

	public Set<PatchType> patchTypes(FarmRunHelperConfig config)
	{
		switch (this)
		{
			case HERB:
				return EnumSet.of(PatchType.HERB);
			case TREE:
				return EnumSet.of(PatchType.TREE, PatchType.FRUIT_TREE, PatchType.HARDWOOD,
					PatchType.CALQUAT, PatchType.CELASTRUS, PatchType.REDWOOD);
			case BIRDHOUSE:
				return EnumSet.of(PatchType.BIRDHOUSE);
			case FARM:
				Set<PatchType> all = EnumSet.allOf(PatchType.class);
				all.remove(PatchType.HESPORI);
				all.remove(PatchType.MUSHROOM);
				return all;
			default:
				Set<PatchType> custom = EnumSet.noneOf(PatchType.class);
				for (PatchType type : PatchType.values())
				{
					if (type.isCustomEnabled(config))
					{
						custom.add(type);
					}
				}
				return custom;
		}
	}
}

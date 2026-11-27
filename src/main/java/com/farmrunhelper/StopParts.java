package com.farmrunhelper;

/**
 * Stops split in two because their patches are far apart, like the Catherby fruit tree down the beach
 * from the allotments. The second part follows straight on from the first, so it needs no teleport then,
 * and uses the same teleport choice as the first part when it's done on its own.
 */
final class StopParts
{
	private StopParts()
	{
	}

	/**
	 * The stop this one is the second part of, or null.
	 */
	static FarmLocation parentOf(FarmLocation location)
	{
		switch (location)
		{
			case CATHERBY_FRUIT:
				return FarmLocation.CATHERBY;
			case GNOME_STRONGHOLD_TREE:
				return FarmLocation.GNOME_STRONGHOLD;
			case AUBURNVALE_BELLADONNA:
				return FarmLocation.AUBURNVALE;
			default:
				return null;
		}
	}

	/**
	 * Whether two stops are the two parts of one split stop.
	 */
	static boolean sameStop(FarmLocation a, FarmLocation b)
	{
		return a != null && b != null && a != b && (parentOf(a) == b || parentOf(b) == a);
	}

	/**
	 * Whether the second part should be done first with this teleport, because it lands nearer to it:
	 * the quetzal lands at Auburnvale closer to the belladonna than the tree.
	 */
	static boolean secondPartFirst(FarmLocation parent, TeleportOption teleport)
	{
		return parent == FarmLocation.AUBURNVALE && teleport != null && "QUETZAL_WHISTLE".equals(teleport.name());
	}

	/**
	 * How to walk to the first part of a stop after doing the second part first.
	 */
	static String walkBack(FarmLocation parent)
	{
		return parent == FarmLocation.AUBURNVALE ? "Walk south-west from the belladonna to the tree patch" : "Walk to the next patch";
	}
}

package com.farmrunhelper;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

/**
 * Outlines the tools and compost in the tool leprechaun's store that the current trip still needs.
 */
class LeprechaunOverlay extends Overlay
{
	// The store's slot for each item
	private static final Map<Integer, Integer> SLOTS = new HashMap<>();

	static
	{
		SLOTS.put(ItemID.RAKE, InterfaceID.FarmingTools.RAKE);
		SLOTS.put(ItemID.DIBBER, InterfaceID.FarmingTools.DIBBER);
		SLOTS.put(ItemID.SPADE, InterfaceID.FarmingTools.SPADE);
		SLOTS.put(ItemID.SECATEURS, InterfaceID.FarmingTools.SECATEURS);
		SLOTS.put(ItemID.FAIRY_ENCHANTED_SECATEURS, InterfaceID.FarmingTools.SECATEURS);
		SLOTS.put(ItemID.BOTTOMLESS_COMPOST_BUCKET, InterfaceID.FarmingTools.BOTTOMLESS_BUCKET);
		SLOTS.put(ItemID.BOTTOMLESS_COMPOST_BUCKET_FILLED, InterfaceID.FarmingTools.BOTTOMLESS_BUCKET);
		SLOTS.put(ItemID.BUCKET_COMPOST, InterfaceID.FarmingTools.COMPOST);
		SLOTS.put(ItemID.BUCKET_SUPERCOMPOST, InterfaceID.FarmingTools.SUPERCOMPOST);
		SLOTS.put(ItemID.BUCKET_ULTRACOMPOST, InterfaceID.FarmingTools.ULTRACOMPOST);
	}

	private final Client client;
	private final FarmRunHelperPlugin plugin;
	private final FarmRunHelperConfig config;

	@Inject
	LeprechaunOverlay(Client client, FarmRunHelperPlugin plugin, FarmRunHelperConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Widget store = client.getWidget(InterfaceID.FarmingTools.ITEMS);
		if (!config.highlightPatches() || store == null || store.isHidden())
		{
			return null;
		}
		Set<Integer> needed = plugin.getWithdrawItems();
		for (Map.Entry<Integer, Integer> slot : SLOTS.entrySet())
		{
			if (!needed.contains(slot.getKey()))
			{
				continue;
			}
			Widget widget = client.getWidget(slot.getValue());
			Rectangle bounds = widget != null && !widget.isHidden() ? widget.getBounds() : null;
			if (bounds != null)
			{
				OverlayUtil.renderPolygon(graphics, bounds, config.highlightColor());
			}
		}
		return null;
	}
}

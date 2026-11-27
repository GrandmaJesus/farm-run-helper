package com.farmrunhelper;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

/**
 * Outlines the items you still need for this trip in your bank and seed vault.
 */
class StorageItemOverlay extends WidgetItemOverlay
{
	private final FarmRunHelperPlugin plugin;
	private final FarmRunHelperConfig config;
	private final ItemManager itemManager;

	@Inject
	StorageItemOverlay(FarmRunHelperPlugin plugin, FarmRunHelperConfig config, ItemManager itemManager)
	{
		this.plugin = plugin;
		this.config = config;
		this.itemManager = itemManager;
		showOnBank();
		showOnInterfaces(InterfaceID.SEED_VAULT);
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		java.util.Set<Integer> wanted = config.highlightPatches() ? plugin.getWithdrawItems() : java.util.Collections.emptySet();
		// "Any axe": outline whichever axes you have
		boolean anyAxe = wanted.contains(net.runelite.api.gameval.ItemID.BRONZE_AXE) && plugin.isAxe(itemId);
		if (!config.highlightPatches() || (!wanted.contains(itemId) && !anyAxe))
		{
			return;
		}
		Rectangle bounds = widgetItem.getCanvasBounds();
		BufferedImage outline = itemManager.getItemOutline(itemId, widgetItem.getQuantity(), config.highlightColor());
		if (bounds != null && outline != null)
		{
			graphics.drawImage(outline, (int) bounds.getX(), (int) bounds.getY(), null);
		}
	}
}

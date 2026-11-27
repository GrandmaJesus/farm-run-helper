package com.farmrunhelper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

/**
 * Outlines the teleport for the next stop while travelling, or the seed, sapling or compost for the next patch.
 */
class InventorySeedOverlay extends WidgetItemOverlay
{
	private final FarmRunHelperPlugin plugin;
	private final FarmRunHelperConfig config;
	private final ItemManager itemManager;

	@Inject
	InventorySeedOverlay(FarmRunHelperPlugin plugin, FarmRunHelperConfig config, ItemManager itemManager)
	{
		this.plugin = plugin;
		this.config = config;
		this.itemManager = itemManager;
		showOnInventory();
		showOnEquipment();
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		if (itemId == ItemID.BOTTOMLESS_COMPOST_BUCKET_FILLED || itemId == ItemID.BOTTOMLESS_COMPOST_BUCKET)
		{
			drawBucketLabel(graphics, widgetItem);
		}
		if (!config.highlightPatches() || !plugin.getHighlightItems().contains(itemId))
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

	// What's in the bottomless compost bucket, like "U 23", in the corner of the item
	private void drawBucketLabel(Graphics2D graphics, WidgetItem widgetItem)
	{
		String label = plugin.getBucketLabel();
		Rectangle bounds = widgetItem.getCanvasBounds();
		if (label == null || bounds == null)
		{
			return;
		}
		graphics.setFont(FontManager.getRunescapeSmallFont());
		int x = (int) bounds.getX();
		int y = (int) (bounds.getY() + bounds.getHeight());
		graphics.setColor(Color.BLACK);
		graphics.drawString(label, x + 1, y + 1);
		graphics.setColor(Color.WHITE);
		graphics.drawString(label, x, y);
	}
}

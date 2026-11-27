package com.farmrunhelper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.Perspective;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class PatchHighlightOverlay extends Overlay
{
	private final Client client;
	private final FarmRunHelperPlugin plugin;
	private final FarmRunHelperConfig config;

	@Inject
	PatchHighlightOverlay(Client client, FarmRunHelperPlugin plugin, FarmRunHelperConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		RouteStep step = plugin.getCurrentStep();
		Player player = client.getLocalPlayer();
		if (step == null || player == null)
		{
			return null;
		}

		int plane = player.getWorldLocation().getPlane();
		Color color = config.highlightColor();
		long now = System.currentTimeMillis();
		if (config.showPath())
		{
			WorldView view = client.getTopLevelWorldView();
			Color fill = new Color(config.pathColor().getRed(), config.pathColor().getGreen(), config.pathColor().getBlue(), 50);
			for (int[] tile : plugin.getPath())
			{
				LocalPoint local = LocalPoint.fromScene(tile[0], tile[1], view);
				Polygon poly = local != null ? Perspective.getCanvasTilePoly(client, local) : null;
				if (poly != null)
				{
					OverlayUtil.renderPolygon(graphics, poly, config.pathColor(), fill, new BasicStroke(1));
				}
			}
		}
		List<Object> targets = new ArrayList<>(plugin.getHouseTargets());
		targets.addAll(plugin.getTravelTargets());
		targets.addAll(plugin.getMushtreeTargets());
		String label = plugin.getTravelLabel();
		for (Object target : targets)
		{
			Shape hull = target instanceof NPC ? ((NPC) target).getConvexHull() : hull((TileObject) target);
			if (hull != null && config.highlightPatches())
			{
				OverlayUtil.renderPolygon(graphics, hull, color);
				if (label != null && target instanceof TileObject)
				{
					Point at = ((TileObject) target).getCanvasTextLocation(graphics, label, 100);
					if (at != null)
					{
						OverlayUtil.renderTextLocation(graphics, at, label, color);
					}
				}
			}
		}
		if (config.highlightPatches())
		{
			for (NPC squirrel : plugin.getSquirrelTargets())
			{
				Shape hull = squirrel.getConvexHull();
				if (hull != null)
				{
					OverlayUtil.renderPolygon(graphics, hull, color);
				}
				String text = "Pay to remove the tree";
				Point at = squirrel.getCanvasTextLocation(graphics, text, squirrel.getLogicalHeight() + 40);
				if (at != null)
				{
					OverlayUtil.renderTextLocation(graphics, at, text, color);
				}
			}
		}
		for (Map.Entry<GameObject, PatchType> entry : plugin.getPatchObjects().entrySet())
		{
			GameObject object = entry.getKey();
			if (object.getPlane() != plane)
			{
				continue;
			}
			WorldPoint point = object.getWorldLocation();
			PatchStatus status = plugin.getPatchStatus(point);
			if (status != null && config.growthTimers())
			{
				String text = null;
				Color textColor = Color.WHITE;
				switch (status.getKind())
				{
					case GROWING:
						if (status.getReadyAt() > 0)
						{
							text = GrowthTimes.format(status.getReadyAt() - now);
							textColor = status.getReadyAt() <= now ? Color.GREEN : Color.WHITE;
						}
						break;
					case READY:
						// No time means it's filled but when isn't known, so it may not be full yet
						text = status.getReadyAt() > 0 ? "Ready" : "Filled";
						textColor = status.getReadyAt() > 0 ? Color.GREEN : Color.YELLOW;
						break;
					case DISEASED:
						text = "Diseased";
						textColor = Color.ORANGE;
						break;
					case DEAD:
						text = "Dead";
						textColor = Color.RED;
						break;
					default:
						break;
				}
				if (text != null)
				{
					Point textLocation = object.getCanvasTextLocation(graphics, text, entry.getValue().isTree() ? 250 : 80);
					if (textLocation != null)
					{
						OverlayUtil.renderTextLocation(graphics, textLocation, text, textColor);
					}
				}
			}
			// Only the patch to plant next is highlighted
			if (!config.highlightPatches() || object != plugin.getNextPatch())
			{
				continue;
			}
			Shape hull = object.getConvexHull();
			if (hull != null)
			{
				OverlayUtil.renderPolygon(graphics, hull, color);
			}
		}
		return null;
	}

	private static Shape hull(TileObject object)
	{
		Shape shape = null;
		if (object instanceof GameObject)
		{
			shape = ((GameObject) object).getConvexHull();
		}
		else if (object instanceof DecorativeObject)
		{
			shape = ((DecorativeObject) object).getConvexHull();
		}
		else if (object instanceof WallObject)
		{
			shape = ((WallObject) object).getConvexHull();
		}
		return shape != null ? shape : object.getClickbox();
	}
}

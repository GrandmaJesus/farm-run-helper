package com.farmrunhelper;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

/**
 * Highlights the option to pick in game menus: the fairy ring code, the mushtree or quetzal
 * destination, or what to say in a dialogue.
 */
class MenuHighlightOverlay extends Overlay
{
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MenuHighlightOverlay.class);

	// Components to look through in each interface
	// The fairy ring travel log has a component for every code, so this covers a few hundred
	private static final int MAX_COMPONENTS = 400;

	private final Client client;
	private final FarmRunHelperPlugin plugin;
	private final FarmRunHelperConfig config;

	@Inject
	MenuHighlightOverlay(Client client, FarmRunHelperPlugin plugin, FarmRunHelperConfig config)
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
		if (!config.highlightPatches())
		{
			return null;
		}
		for (MenuTarget target : plugin.getMenuTargets())
		{
			for (int group : target.getInterfaces())
			{
				Widget option = findInInterface(group, target);
				if (option == null)
				{
					logOnce(group, target);
				}
				if (option != null)
				{
					Rectangle bounds = option.getBounds();
					if (bounds != null)
					{
						OverlayUtil.renderPolygon(graphics, bounds, config.highlightColor());
					}
				}
			}
		}
		return null;
	}

	private final java.util.Set<String> logged = new java.util.HashSet<>();

	/**
	 * When a menu is open but the option can't be found, writes what its widgets say to the client log
	 * (once per menu and option), so the matching can be fixed.
	 */
	private void logOnce(int group, MenuTarget target)
	{
		Widget root = client.getWidget(group, 0);
		if (root == null || root.isHidden() || !logged.add(group + ":" + target.getText()))
		{
			return;
		}
		StringBuilder dump = new StringBuilder();
		for (int i = 0; i < MAX_COMPONENTS; i++)
		{
			dump(client.getWidget(group, i), dump, 0);
		}
		log.debug("Farm Run Helper: couldn't find \"{}\" in interface {}. Widgets: {}", target.getText(), group, dump);
	}

	private static void dump(Widget widget, StringBuilder out, int depth)
	{
		if (widget == null || depth > 2 || out.length() > 6000)
		{
			return;
		}
		String text = widget.getText();
		String name = widget.getName();
		String[] actions = widget.getActions();
		if ((text != null && !text.isEmpty()) || (name != null && !name.isEmpty()) || actions != null)
		{
			out.append("[text=").append(text).append(", name=").append(name)
				.append(", actions=").append(actions == null ? "none" : String.join("/", java.util.Arrays.stream(actions).filter(java.util.Objects::nonNull).toArray(String[]::new)))
				.append("] ");
		}
		Widget[] children = widget.getDynamicChildren();
		if (children != null)
		{
			for (Widget child : children)
			{
				dump(child, out, depth + 1);
			}
		}
	}

	private Widget findInInterface(int group, MenuTarget target)
	{
		for (int i = 0; i < MAX_COMPONENTS; i++)
		{
			Widget widget = client.getWidget(group, i);
			if (widget == null)
			{
				continue;
			}
			Widget found = find(widget, target, 0);
			if (found != null)
			{
				return found;
			}
		}
		return null;
	}

	private static Widget find(Widget widget, MenuTarget target, int depth)
	{
		if (widget == null || widget.isHidden() || depth > 4)
		{
			return null;
		}
		if (target.matches(widget.getText()) || target.matches(widget.getName()))
		{
			return widget;
		}
		// Map icons, like on the quetzal whistle map, often only name the place in their right-click options
		String[] actions = widget.getActions();
		if (actions != null)
		{
			for (String action : actions)
			{
				if (target.matches(action))
				{
					return widget;
				}
			}
		}
		for (Widget[] children : new Widget[][]{widget.getDynamicChildren(), widget.getNestedChildren()})
		{
			if (children == null)
			{
				continue;
			}
			for (Widget child : children)
			{
				Widget found = find(child, target, depth + 1);
				if (found != null)
				{
					return found;
				}
			}
		}
		return null;
	}
}

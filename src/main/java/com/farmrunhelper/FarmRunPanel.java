package com.farmrunhelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

class FarmRunPanel extends PluginPanel
{

	private static final int TEXT_WIDTH = 170;

	private final FarmRunHelperPlugin plugin;
	private final JPanel content = new JPanel();
	private final JLabel status = new JLabel();
	private enum View
	{
		RUN,
		TELEPORTS,
		SEEDS
	}

	private View view = View.RUN;
	private final JButton teleportsButton = new JButton("Teleports");
	private final JButton seedsButton = new JButton("Seeds");

	FarmRunPanel(FarmRunHelperPlugin plugin)
	{
		// Wrapped in RuneLite's scroll pane so a long route scrolls instead of stretching the client window.
		super(true);
		this.plugin = plugin;
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBackground(ColorScheme.DARK_GRAY_COLOR);

		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		north.setBackground(ColorScheme.DARK_GRAY_COLOR);

		JLabel title = new JLabel("Farm Run Helper");
		title.setForeground(Color.WHITE);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
		title.setAlignmentX(Component.LEFT_ALIGNMENT);
		north.add(title);

		JPanel runs = new JPanel(new GridLayout(0, 2, 4, 4));
		runs.setBackground(ColorScheme.DARK_GRAY_COLOR);
		runs.setBorder(new EmptyBorder(6, 0, 6, 0));
		runs.setAlignmentX(Component.LEFT_ALIGNMENT);
		for (RunType type : RunType.values())
		{
			JButton button = new JButton(type.getLabel());
			button.setFocusable(false);
			button.addActionListener(e -> plugin.startRun(type));
			runs.add(button);
		}
		north.add(runs);

		JPanel views = new JPanel(new GridLayout(1, 2, 4, 4));
		views.setBackground(ColorScheme.DARK_GRAY_COLOR);
		views.setAlignmentX(Component.LEFT_ALIGNMENT);
		views.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		teleportsButton.setFocusable(false);
		teleportsButton.addActionListener(e -> toggleView(View.TELEPORTS));
		seedsButton.setFocusable(false);
		seedsButton.addActionListener(e -> toggleView(View.SEEDS));
		views.add(teleportsButton);
		views.add(seedsButton);
		north.add(views);
		north.add(Box.createVerticalStrut(6));

		status.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		status.setAlignmentX(Component.LEFT_ALIGNMENT);
		north.add(status);

		add(north, BorderLayout.NORTH);
		add(content, BorderLayout.CENTER);
		refresh();
	}

	/**
	 * Rebuilds the panel from the plugin's current state. Must be called on the Swing thread.
	 */
	void refresh()
	{
		try
		{
			refreshContent();
		}
		catch (Throwable e)
		{
			// Show what went wrong, and where, instead of a blank panel
			org.slf4j.LoggerFactory.getLogger(FarmRunPanel.class).warn("Farm Run Helper panel failed to draw", e);
			StringBuilder where = new StringBuilder();
			StackTraceElement[] trace = e.getStackTrace();
			for (int i = 0; i < Math.min(8, trace.length); i++)
			{
				where.append("<br>").append(escape(trace[i].getClassName().replace("com.farmrunhelper.", ""))).append('.')
					.append(escape(trace[i].getMethodName())).append(':').append(trace[i].getLineNumber());
			}
			content.removeAll();
			status.setText(wrap("<b>Something went wrong drawing this run.</b> Please send a screenshot of this:<br>"
				+ escape(String.valueOf(e)) + where));
			revalidate();
			repaint();
		}
	}

	private void refreshContent()
	{
		content.removeAll();
		if (view == View.TELEPORTS)
		{
			status.setText(wrap("How you get to each location. Auto picks the first route you have unlocked. Greyed out routes need something you haven't set as unlocked."));
			renderTeleports();
			revalidate();
			repaint();
			return;
		}
		if (view == View.SEEDS)
		{
			status.setText(wrap(plugin.getFarmingLevel() > 0
				? "What to plant in each patch type. Levels are shown next to each option. Anything you can't use yet, or don't have in your bank or seed vault, is greyed out with the reason underneath."
				: "What to plant in each patch type. Log in to grey out options your levels can't use yet."));
			renderSeeds();
			revalidate();
			repaint();
			return;
		}
		Route route = plugin.getRoute();
		if (route == null)
		{
			status.setText(wrap(plugin.getPanelMessage() != null ? plugin.getPanelMessage()
				: "First time? Fill in your quests, diaries and travel in the plugin settings, choose your seeds with the Seeds button, and open your bank and seed vault once. Then pick a run above."));
			revalidate();
			repaint();
			return;
		}

		List<RouteStep> steps = route.getSteps();
		int current = plugin.getCurrentStepIndex();
		RouteStep currentStep = current >= 0 && current < steps.size() ? steps.get(current) : null;
		int tripIndex = currentStep != null ? currentStep.getTripIndex() : 1;
		status.setText(wrap(route.getRunType().getLabel() + ": trip " + tripIndex + " of " + route.getTrips().size()
			+ ", step " + (current + 1) + " of " + steps.size()));

		String spaceWarning = plugin.getSpaceWarning();
		if (spaceWarning != null)
		{
			JLabel warningLabel = new JLabel(wrap("<b>" + spaceWarning + "</b>"));
			warningLabel.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
			warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			warningLabel.setBorder(new EmptyBorder(0, 0, 6, 0));
			content.add(warningLabel);
		}

		if (currentStep != null && currentStep.getKind() == RouteStep.Kind.LOCATION)
		{
			PatchType next = plugin.getNextPatchType();
			String mushtree = plugin.getMushtreeDestination();
			String hint = plugin.getBirdHouseHint();
			String nextText = mushtree != null
				? "<b>Next:</b> use the magic mushtree to " + mushtree + " for the next bird house"
				: hint != null && plugin.getNextPatch() == null ? "<b>Next:</b> " + hint
				: plugin.isNextCompost() && next != null
				? "<b>Next:</b> use your " + plugin.getConfig().compost().toString().toLowerCase() + " on the " + next.getLabel().toLowerCase() + " you just planted"
				: next != null ? "<b>Next:</b> plant " + next.getLabel().toLowerCase() + " (" + currentStep.getSeedLabel(next) + ")" : null;
			String travel = plugin.getTravelHint();
			if (travel != null)
			{
				nextText = "<b>Next:</b> " + travel;
			}
			else if (nextText == null && plugin.hasPatchOutOfView())
			{
				nextText = "<b>Next:</b> walk to the next patch (follow the path)";
			}
			JLabel nextLabel = new JLabel(wrap(nextText != null
				? nextText
				: "<b>Next:</b> nothing left to plant here. Press Next to move on."));
			nextLabel.setForeground(Color.WHITE);
			nextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			nextLabel.setBorder(new EmptyBorder(6, 0, 0, 0));
			content.add(nextLabel);
		}

		JPanel actions = new JPanel(new GridLayout(3, 2, 4, 4));
		actions.setBackground(ColorScheme.DARK_GRAY_COLOR);
		actions.setBorder(new EmptyBorder(6, 0, 6, 0));
		actions.setAlignmentX(Component.LEFT_ALIGNMENT);
		actions.add(button("Previous", plugin::previousStep));
		actions.add(button("Next", plugin::nextStep));
		actions.add(button("Copy setup", plugin::copyCurrentTripSetup));
		actions.add(button("Skip", plugin::skipNextPatch));
		actions.add(button("Reset", plugin::resetRun));
		content.add(actions);

		content.add(section("Steps"));
		boolean itemsShown = false;
		for (int i = 0; i < steps.size(); i++)
		{
			RouteStep step = steps.get(i);
			// This trip's items go just above its bank step, so they're next to where you are in the run
			if (!itemsShown && step.getTripIndex() == tripIndex && (step.getKind() == RouteStep.Kind.BANK || i == current))
			{
				addTripItems(route, tripIndex);
				itemsShown = true;
			}
			content.add(stepRow(step, i, i == current));
		}

		if (!route.getWarnings().isEmpty())
		{
			content.add(section("Notes"));
			for (String warning : route.getWarnings())
			{
				JLabel label = new JLabel(wrap(warning));
				label.setForeground(ColorScheme.PROGRESS_INPROGRESS_COLOR);
				label.setAlignmentX(Component.LEFT_ALIGNMENT);
				content.add(label);
			}
		}

		String message = plugin.getPanelMessage();
		if (message != null)
		{
			JLabel label = new JLabel(wrap(message));
			label.setForeground(ColorScheme.BRAND_ORANGE);
			label.setAlignmentX(Component.LEFT_ALIGNMENT);
			content.add(label);
		}

		revalidate();
		repaint();
	}

	private void toggleView(View target)
	{
		view = view == target ? View.RUN : target;
		teleportsButton.setText(view == View.TELEPORTS ? "Back to run" : "Teleports");
		seedsButton.setText(view == View.SEEDS ? "Back to run" : "Seeds");
		refresh();
	}

	private void renderTeleports()
	{
		FarmRunHelperConfig config = plugin.getConfig();
		for (FarmLocation location : FarmLocation.values())
		{
			if (StopParts.parentOf(location) != null)
			{
				// Goes the same way as the first part of its stop
				continue;
			}
			TeleportOption[] options = location.getTeleportOptions();
			addChoice(location.getDisplayName(), options, location.getSelectedTeleport(config),
				option ->
				{
					Req missing = firstUnmet(((TeleportOption) option).getRequirements(), config);
					return missing == null ? null : "needs " + missing.getDescription();
				},
				option -> plugin.setTeleport(location, (TeleportOption) option));
		}
	}

	private void renderSeeds()
	{
		FarmRunHelperConfig config = plugin.getConfig();
		int farming = plugin.getFarmingLevel();
		int crafting = plugin.getCraftingLevel();
		int hunter = plugin.getHunterLevel();
		boolean checkOwned = plugin.hasSeenStorage();
		for (SeedSetting setting : SeedSetting.ALL)
		{
			addChoice(setting.getLabel(), setting.getOptions(), setting.getSelected(config),
				option ->
				{
					SeedOption seed = (SeedOption) option;
					String reason = SeedSetting.blockedReason(seed, farming, crafting, hunter);
					if (reason == null && checkOwned && !(seed instanceof Seeds.BirdHouse) && !plugin.ownsItem(seed.getItemId()))
					{
						reason = "none in your bank or seed vault";
					}
					return reason;
				},
				option -> plugin.setSeed(setting, (SeedOption) option));
		}
	}

	/**
	 * A label with a dropdown under it. Options that {@code blocked} gives a reason for are greyed out and can't be picked.
	 */
	private void addChoice(String title, Object[] options, Object selected,
		Function<Object, String> blocked, Consumer<Object> onPick)
	{
		JLabel name = new JLabel(title);
		name.setForeground(Color.WHITE);
		name.setBorder(new EmptyBorder(6, 0, 2, 0));
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		content.add(name);

		JComboBox<Object> combo = new JComboBox<>(options);
		combo.setSelectedItem(selected);
		combo.setFocusable(false);
		combo.setAlignmentX(Component.LEFT_ALIGNMENT);
		combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
		combo.setRenderer(new DefaultListCellRenderer()
		{
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus)
			{
				String reason = value == null ? null : blocked.apply(value);
				String text = value == null ? "" : value.toString();
				if (value instanceof SeedOption)
				{
					text += SeedSetting.levelText((SeedOption) value);
				}
				Component c = super.getListCellRendererComponent(list, text, index, isSelected && reason == null, cellHasFocus);
				if (reason != null)
				{
					c.setForeground(ColorScheme.MEDIUM_GRAY_COLOR);
					if (c instanceof JLabel && index >= 0)
					{
						((JLabel) c).setToolTipText(reason.startsWith("none ") ? "None in your bank or seed vault" : "Needs " + reason.replaceFirst("^needs ", ""));
					}
				}
				return c;
			}
		});
		final Object[] last = {selected};
		combo.addActionListener(e ->
		{
			Object picked = combo.getSelectedItem();
			if (picked == null || picked == last[0])
			{
				return;
			}
			if (blocked.apply(picked) != null)
			{
				combo.setSelectedItem(last[0]);
				return;
			}
			last[0] = picked;
			onPick.accept(picked);
		});
		content.add(combo);

		StringBuilder greyed = new StringBuilder();
		for (Object option : options)
		{
			String reason = blocked.apply(option);
			if (reason != null)
			{
				if (greyed.length() > 0)
				{
					greyed.append("<br>");
				}
				greyed.append(escape(option.toString())).append(": ")
					.append(escape(reason.startsWith("needs ") || reason.startsWith("none ") ? reason : "needs " + reason));
			}
		}
		if (greyed.length() > 0)
		{
			JLabel note = new JLabel(wrap("<i>Greyed out</i><br>" + greyed));
			note.setForeground(ColorScheme.MEDIUM_GRAY_COLOR);
			note.setFont(note.getFont().deriveFont(11f));
			note.setBorder(new EmptyBorder(2, 0, 0, 0));
			note.setAlignmentX(Component.LEFT_ALIGNMENT);
			content.add(note);
		}
	}

	private static Req firstUnmet(Req[] reqs, FarmRunHelperConfig config)
	{
		for (Req req : reqs)
		{
			if (!req.isMet(config))
			{
				return req;
			}
		}
		return null;
	}

	/**
	 * The items for a trip, green once you're carrying (or wearing) enough of them.
	 */
	private void addTripItems(Route route, int tripIndex)
	{
		content.add(section("Trip " + tripIndex + " items"));
		Trip trip = route.getTrip(tripIndex);
		Map<Integer, Integer> carried = plugin.getCarriedItems();
		if (trip != null)
		{
			for (Map.Entry<Integer, Integer> entry : trip.getItems().entrySet())
			{
				int have = carried.getOrDefault(entry.getKey(), 0);
				int need = entry.getValue();
				String from = plugin.getItemSources(entry.getKey());
				JLabel label = new JLabel(wrap(ItemNames.get(entry.getKey()) + " x" + need
					+ (have > 0 && have < need ? " (have " + have + ")" : "")
					+ (from != null ? " <font color='#a0a0a0'>- " + from + "</font>" : "")));
				label.setForeground(have >= need ? ColorScheme.PROGRESS_COMPLETE_COLOR : ColorScheme.LIGHT_GRAY_COLOR);
				label.setAlignmentX(Component.LEFT_ALIGNMENT);
				content.add(label);
			}
			for (int itemId : trip.getWorn().values())
			{
				String from = plugin.getItemSources(itemId);
				JLabel label = new JLabel(wrap(FarmerOutfit.name(itemId) + " (wear)"
					+ (from != null ? " <font color='#a0a0a0'>- " + from + "</font>" : "")));
				label.setForeground(carried.containsKey(itemId) ? ColorScheme.PROGRESS_COMPLETE_COLOR : ColorScheme.LIGHT_GRAY_COLOR);
				label.setAlignmentX(Component.LEFT_ALIGNMENT);
				content.add(label);
			}
		}

	}

	private JPanel stepRow(RouteStep step, int index, boolean current)
	{
		JPanel row = new JPanel(new BorderLayout());
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		row.setBackground(current ? ColorScheme.DARKER_GRAY_HOVER_COLOR : ColorScheme.DARKER_GRAY_COLOR);
		row.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 3, 1, 0, current ? ColorScheme.BRAND_ORANGE : ColorScheme.DARK_GRAY_COLOR),
			new EmptyBorder(4, 4, 4, 4)));

		JCheckBox box = new JCheckBox();
		box.setSelected(step.isDone());
		box.setBackground(row.getBackground());
		box.setFocusable(false);
		box.addActionListener(e -> plugin.setStepDone(index, box.isSelected()));
		row.add(box, BorderLayout.WEST);

		StringBuilder html = new StringBuilder("<b>").append(escape(step.getTitle())).append("</b>");
		// Done steps shrink to their title, so long runs don't need so much scrolling
		boolean collapsed = step.isDone() && !current;
		for (String line : collapsed ? java.util.Collections.<String>emptyList() : step.getLines())
		{
			html.append("<br>").append(escape(line));
		}
		if (step.getNote() != null && !collapsed)
		{
			html.append("<br><i>").append(escape(step.getNote())).append("</i>");
		}
		JLabel text = new JLabel(wrap(html.toString(), TEXT_WIDTH - 30));
		text.setForeground(step.isDone() ? ColorScheme.MEDIUM_GRAY_COLOR : Color.WHITE);
		row.add(text, BorderLayout.CENTER);
		return row;
	}

	private JButton button(String label, Runnable action)
	{
		JButton button = new JButton(label);
		button.setFocusable(false);
		button.addActionListener(e -> action.run());
		return button;
	}

	private static JLabel section(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(ColorScheme.BRAND_ORANGE);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		label.setBorder(new EmptyBorder(8, 0, 2, 0));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	private static String wrap(String html)
	{
		return wrap(html, TEXT_WIDTH);
	}

	private static String wrap(String html, int width)
	{
		return "<html><body style='width:" + width + "px'>" + html + "</body></html>";
	}

	private static String escape(String text)
	{
		return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
}

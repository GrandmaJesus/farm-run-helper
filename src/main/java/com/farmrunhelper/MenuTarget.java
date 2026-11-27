package com.farmrunhelper;

/**
 * An option to highlight in a game menu: the fairy ring code, mushtree or quetzal destination, or a dialogue option.
 */
final class MenuTarget
{
	private final int[] interfaces;
	private final String text;
	private final boolean code;

	MenuTarget(String text, boolean code, int... interfaces)
	{
		this.interfaces = interfaces;
		this.text = text.toLowerCase();
		this.code = code;
	}

	String getText()
	{
		return text;
	}

	boolean isCode()
	{
		return code;
	}

	int[] getInterfaces()
	{
		return interfaces;
	}

	/**
	 * Whether a widget's text, name or option names this target.
	 */
	boolean matches(String raw)
	{
		if (raw == null)
		{
			return false;
		}
		String plain = raw.replaceAll("<[^>]*>", "").toLowerCase();
		if (code)
		{
			// Fairy ring codes can be shown as "DKS" or "D K S"
			// Ignore labels other plugins add, like "(Shortest Path) C J Q"
			String letters = plain.replaceAll("\\(.*?\\)", "").replaceAll("[^a-z]", "");
			return letters.equals(text) || plain.matches(".*\\b" + text + "\\b.*");
		}
		return plain.contains(text);
	}
}

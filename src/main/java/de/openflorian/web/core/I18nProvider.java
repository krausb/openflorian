package de.openflorian.web.core;

/**
 * Interface for creating I18n Providers.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public interface I18nProvider {

	/**
	 * Load localized text from i18n bundle
	 * 
	 * @param label
	 * @return {@link String} localized text
	 */
	public abstract String getText(String label);

	/**
	 * Load localized text from i18n bundle and replace {0} with the given {@code replacement}
	 * 
	 * @param label
	 * @param replacement
	 * @return {@link String} localized text
	 */
	public abstract String getText(String label, String replacement);
	
}
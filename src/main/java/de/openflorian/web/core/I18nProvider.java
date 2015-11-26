package de.openflorian.web.core;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

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
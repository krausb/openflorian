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
 * Core Constants
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class WebConstants {

	/*
	 * Stages
	 */
	public static final String SYSTEM_STAGE_TEST		= "TEST";
	public static final String SYSTEM_STAGE_PRELIVE	= "PRELIVE";
	public static final String SYSTEM_STAGE_LIVE		= "LIVE";

	/*
	 * Session
	 */
	public static final String SESSION_USER = "session.user.current";
	public static final String SESSION_VIEW_ACTION = "session.action.toview";
	
	/*
	 * I18n
	 */
	public static final String LOCALE_SESSION		= "session.current_locale";
	public static final String LOCALE_REQUEST		= "request_locale";
	public static final String LOCALE_DEFAULT		= "en_GB";

	public static final String DATE_FORMAT			= "dd.MM.yyyy";
	public static final String TIME_FORMAT			= "HH:mm";
	public static final String DATEPICKER_FORMAT	= "dd.mm.yy";
	
	/*
	 * Paths
	 */
	public static final String PATH_CONTENT_IMAGES = "/media";

	/*
	 * Media
	 */
	public static final String MEDIA_CHARSET = "UTF-8";
	
}

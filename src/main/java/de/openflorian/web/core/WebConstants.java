package de.openflorian.web.core;

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

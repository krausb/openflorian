package de.openflorian.web;

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
 * ZK Globals
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class WebGlobals {

	/**
	 * Login ZUL
	 */
	public static final String PAGE_LOGIN		= "/index.zul";
	
	/**
	 * Main ZUL
	 */
	public static final String PAGE_MAIN		= "/main.zul";
	
	/**
	 * About ZUL
	 */
	public static final String PAGE_ABOUT		= "/WEB-INF/zul/about.zul";
	
	/**
	 * Error ZUL
	 */
	public static final String PAGE_ERROR		= "/WEB-INF/zul/error.zul";
	
	/**
	 * Timeout ZUL
	 */
	public static final String PAGE_TIMEOUT		= "/timeout.zul";
	
	/**
	 * Default Media Charset
	 */
	public static final String MEDIA_CHARSET	= "UTF-8";
	
	/* *************************
	 * System Management
	 ****************************/
	
	/**
	 * Properties
	 */
	public static final String PAGE_SYSTEM_PROPERTIES	= "/WEB-INF/zul/system/properties.zul";

	/**
	 * Users
	 */
	public static final String PAGE_SYSTEM_USERS		= "/WEB-INF/zul/system/user_list.zul";

	/**
	 * Users edit
	 */
	public static final String PAGE_SYSTEM_USERS_EDIT	= "/WEB-INF/zul/system/user_edit.zul";
	
	/**
	 * System information
	 */
	public static final String PAGE_SYSTEM_INFORMATION	= "/WEB-INF/zul/system/information.zul";
	
	/* ****************************
	 * Operations Management
	 ******************************/
	
	/**
	 * Users
	 */
	public static final String PAGE_OPERATION_LIST 		= "/WEB-INF/zul/operation/operation_list.zul";

	/**
	 * Users edit
	 */
	public static final String PAGE_OPERATION_EDIT		= "/WEB-INF/zul/operation/operation_edit.zul";
	
	/* ****************************
	 * Resources Management
	 ******************************/
	
	/**
	 * Users
	 */
	public static final String PAGE_RESOURCES_LIST 		= "/WEB-INF/zul/operation/resources_list.zul";

	/**
	 * Users edit
	 */
	public static final String PAGE_RESOURCES_EDIT		= "/WEB-INF/zul/operation/resources_edit.zul";
	
	
	/* ****************************
	 * Operations View
	 ******************************/
	
	/**
	 * Operation Thin Client View
	 */
	public static final String OPERATION_VIEWPORT = "/operation/viewport.zul";
	
	/**
	 * Operation View Dispatcher
	 */
	public static final String OPERATION_DISPATCHER = "/operation/dispatcher.zul";
	
	/**
	 * Alarm View
	 */
	public static final String OPERATION_VIEW_ALARMVIEW = "/operation/views/alarm.zul";
	
	/**
	 * Weather View
	 */
	public static final String OPERATION_VIEW_WEATHERVIEW = "/operation/views/weather.zul";
	
	/**
	 * Default Date Time Format
	 */
	public static final String FORMAT_DATETIME 	= "dd.MM.yyyy HH:mm:ss";
	public static final String FORMAT_DATE		= "dd.MM.yyyy";
	
	/*
	 * REQUEST 
	 */
	public static final String REQUEST_USER		= "request.user";
	public static final String REQUEST_ENTITY	= "request.entity";
	
	public static final String REQUEST_OPERATION= "request.operation";
	
	/*
	 * ZK Event Queues
	 */
	public static final String EVENTS_QUEUE_ALARMEVENT = "AlarmEvent";
	
	/*
    * Attributes for notifications
    */
	public static final String NOTIFY_ERROR 		= "notifyError";
	public static final String NOTIFY_OK 			= "notifyOk";
	public static final String NOTIFY_WARNING 		= "notifyWarning";
	public static final String NOTIFY_INFO 			= "notifyInfo";
	public static final String WARNING_MESSAGE 		= "WarningMessage";
	public static final String ERROR_MESSAGE 		= "ErrorMessage";
	public static final String STATUS_MESSAGE 		= "StatusMessage";
	
	/**
	 * Avatar Placeholder
	 */
	public static final String AVATAR_PLACEHOLDER 	= "/includes/img/avatar-placeholder.png";
	
	/*
	 * Images / icons
	 */
	public static final String PATH_IMAGES				= "/includes/img/";

	public static final String ICON_IMAGE				= PATH_IMAGES + "icon/image_icon.png";
	public static final String ICON_VIDEO				= PATH_IMAGES + "icon/video_icon.png";
	public static final String ICON_YOUTUBE				= PATH_IMAGES + "icon/youtube_icon.png";
	public static final String ICON_VIMEO				= PATH_IMAGES + "icon/vimeo_icon.png";
	
	public static final String ICON_SMALL_OK 			= PATH_IMAGES + "icon/16/ok.png";
	public static final String ICON_SMALL_NOK			= PATH_IMAGES + "icon/16/nok.png"; 

	public static final String ICON_SMALL_UP 			= PATH_IMAGES + "icon/16/up.png";
	public static final String ICON_SMALL_DOWN			= PATH_IMAGES + "icon/16/down.png";
	public static final String ICON_SMALL_STEADY		= PATH_IMAGES + "icon/16/steady.png";
	
	public static final String STAGE_IMAGE_TEST			= PATH_IMAGES + "header_stage_test.png";
	public static final String STAGE_IMAGE_PRELIVE		= PATH_IMAGES + "header_stage_prelive.png";
	
}

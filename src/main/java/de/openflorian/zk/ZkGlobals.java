package de.openflorian.zk;

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
 * ZK Globals<br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class ZkGlobals {

	/**
	 * Login ZUL
	 */
	public static final String PAGE_LOGIN = "/login.zul";
	
	/**
	 * About ZUL
	 */
	public static final String PAGE_ABOUT = "/about.zul";
	
	/**
	 * Error ZUL
	 */
	public static final String PAGE_ERROR = "/error.zul";
	
	/**
	 * Timeout ZUL
	 */
	public static final String PAGE_TIMEOUT = "/timeout.zul";
	
	/**
	 * Default Media Charset
	 */
	public static final String MEDIA_CHARSET = "UTF-8";
	
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
	
}

package de.openflorian.web.user;

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
 * {@link ThreadLocal} to store the current {@link User} user object
 *  
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class AuthenticatedUserThreadLocal {

	private static ThreadLocal<User> currentUser = new ThreadLocal<User>();
	
	/**
	 * Store current authenticated user into ThreadLocal
	 * @param user
	 */
	public static void set(User user) {
		currentUser.set(user);
	}
	
	/**
	 * Retrieve current
	 * @return
	 */
	public static User get() {
		return currentUser.get();
	}
	
}

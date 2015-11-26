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

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import de.openflorian.web.core.RequestDataProvider;

/**
 * Authenticated Users Session Local<br/>
 * <br/>
 * User entity: {@link User}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AuthenticatedUserSessionLocal {

	/**
	 * Set authenticated {@link User} into session
	 * @param user
	 */
	public static void set(Principal user) {
		HttpServletRequest request = RequestDataProvider.getCurrentHttpRequest();
		if(request != null)
			request.getSession().setAttribute(RequestDataProvider.SESSION_USER, user);
	}
	
	/**
	 * Get authenticated {@link User} from HTTP Session
	 * @return
	 */
	public static User get() {
		HttpServletRequest request = RequestDataProvider.getCurrentHttpRequest();
		if(request != null)
			return (User)request.getSession().getAttribute(RequestDataProvider.SESSION_USER);
		else
			return null;
	}
	
}

package de.openflorian.web.client;

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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Determines wether the session cookie is present or not
 *  
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class CookieStateChecker implements Filter {

	private static ThreadLocal<Boolean> sessionCookiePresent = new ThreadLocal<Boolean>();
	public static boolean sessionCookiePresent() {
		return sessionCookiePresent.get();
	}
	
	public static String SESSION_COOKIE = "JSESSIONID";
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		sessionCookiePresent.set(new Boolean(false));
		
		Cookie [] cookies = ((HttpServletRequest)request).getCookies();
		if(cookies != null && cookies.length > 0) {
			for(Cookie c: cookies)
				if(c.getName().equals(SESSION_COOKIE))
					sessionCookiePresent.set(new Boolean(true));
		}
		
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// nothing to do
	}

}

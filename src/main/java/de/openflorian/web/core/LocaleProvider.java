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

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * I18n handling {@link Filter}<br/>
 * <br/>
 * Provides user environment locales.
 * <br/>
 * Reacts on {@link ServletRequest#getParameter(String)} with param {@link WebConstants#LOCALE_REQUEST}.
 * If an error occurs somewhere, the default locale set in {@link WebConstants#LOCALE_DEFAULT} is set.
 *  
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class LocaleProvider implements Filter {

	private static Logger log = LoggerFactory.getLogger(LocaleProvider.class);
	
	public static void setCurrentLocale(Locale locale) {
		RequestDataProvider.getCurrentHttpRequest().getSession().setAttribute(WebConstants.LOCALE_SESSION, locale);
	}
	
	public static Locale getCurrentLocale() {
		Locale current = null;
		try {
			current = (Locale)RequestDataProvider.getCurrentHttpRequest().getSession().getAttribute(WebConstants.LOCALE_SESSION);
		} catch (Exception e) {
			current = getDefaultLocale();
		}
		return current;
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		try {
			// if no session locale is set, do it now
			if(RequestDataProvider.getCurrentHttpRequest().getSession().getAttribute(WebConstants.LOCALE_SESSION) == null)
				RequestDataProvider.getCurrentHttpRequest().getSession().setAttribute(WebConstants.LOCALE_SESSION, getDefaultLocale());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		if(request.getParameter(WebConstants.LOCALE_REQUEST) != null) {
			try {
				Locale newLocale = new Locale(request.getParameter(WebConstants.LOCALE_REQUEST));
				log.debug("User wants to change the current locale '" + RequestDataProvider.getCurrentHttpRequest().getSession().getAttribute(WebConstants.LOCALE_SESSION) + "' to '" + newLocale + "'");
				setCurrentLocale(newLocale);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				// if an error occured, set default locale
				setCurrentLocale(getDefaultLocale());
			}
		}		
		
		// proceed filter chain
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// nothing to do
	}

	public static Locale getDefaultLocale() {
		return new Locale(WebConstants.LOCALE_DEFAULT);
	}
	
}

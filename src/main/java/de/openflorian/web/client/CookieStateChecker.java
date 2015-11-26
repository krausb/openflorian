package de.openflorian.web.client;

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

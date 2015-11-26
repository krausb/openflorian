package de.openflorian.web.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Provides http request data for application request scope
 *  
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class RequestDataProvider implements Filter {

	private static ThreadLocal<HttpServletRequest> request;

	public static String SESSION_USER = "session.user";
	public static String REQUEST_USER = "request.user";
	
	public static HttpServletRequest getCurrentHttpRequest() {
		return request.get();
	} 
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		RequestDataProvider.request.set((HttpServletRequest) request);
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		if(request == null) request = new ThreadLocal<HttpServletRequest>();
	}

}

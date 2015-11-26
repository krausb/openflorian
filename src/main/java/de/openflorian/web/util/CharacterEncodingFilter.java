package de.openflorian.web.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Character Encoding Filter<br/>
 * <Br/>
 * Filter enforces UTF-8 Encoding on all IN
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class CharacterEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	// nothing to do
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    	// nothing to do
    }

}

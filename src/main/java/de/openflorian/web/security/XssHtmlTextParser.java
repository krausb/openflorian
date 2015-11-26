package de.openflorian.web.security;

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

import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.esapi.ESAPI;

/**
 * XSS HTML Text Parser<br/>
 * <br/>
 * Removes:
 * - IFrame tags and anything in between
 * - Script tags
 * - anything in a src="" attribute,
 * - javascript:
 * - vbscript:
 * - onclick*=
 * - onload*=
 * - expression(...)
 * - eval(...)
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class XssHtmlTextParser {

	private static Pattern iframeTagPattern = Pattern.compile("<iframe>(.*?)</iframe>", Pattern.CASE_INSENSITIVE);
	private static Pattern iframeSingleTagPattern = Pattern.compile("<iframe(.*?)/>",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static Pattern iframeStartTagPattern = Pattern.compile("<iframe(.*?)>",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static Pattern iframeEndTagPattern = Pattern.compile("</iframe>", Pattern.CASE_INSENSITIVE);
	private static Pattern scriptTagPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
	private static Pattern scriptStartTagPattern = Pattern.compile("<script(.*?)>",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static Pattern scriptEndTagPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
	private static Pattern srcAttributePattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static Pattern evalPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static Pattern expressionPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static Pattern javascriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
	private static Pattern vbscriptPattern = Pattern.compile("vbscript:",  Pattern.CASE_INSENSITIVE);
	private static Pattern onloadPattern = Pattern.compile("onload(.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static Pattern onclickPattern = Pattern.compile("onclick(.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	
	/**
	 * Filter <code>unsecureText</code> with various patterns to prevent xss and scripting attacks
	 * 
	 * @param 
	 * 		unsecureText
	 * @return 
	 * 		{@link String} secureText
	 */
	public static String secureText(String unsecureText) {
		String secureText = "";
		if (unsecureText != null) {
			// avoid encoded attacks (ESAPI library)
			
			secureText = ESAPI.encoder().canonicalize(unsecureText);

			// Avoid null characters
			secureText = secureText.replaceAll("", "");

			// Avoid anything between script tags 
			secureText = scriptTagPattern.matcher(secureText).replaceAll("");

			// Avoid anything in a src='...' type of expression
			secureText = srcAttributePattern.matcher(secureText).replaceAll("");
			secureText = srcAttributePattern.matcher(secureText).replaceAll("");

			// Remove any lonesome </script> tag
			secureText = scriptEndTagPattern.matcher(secureText).replaceAll("");

			// Remove any lonesome <script ...> tag
			secureText = scriptStartTagPattern.matcher(secureText).replaceAll("");

			// Remove anything between iframe tags
			secureText = iframeTagPattern.matcher(secureText).replaceAll("");
			
			// Remove any single <iframe .../> tag
			secureText = iframeSingleTagPattern.matcher(secureText).replaceAll("");
			
			// Remove any lonesome <iframe ...> tag
			secureText = iframeStartTagPattern.matcher(secureText).replaceAll("");
			
			// Remove any lonesome </iframe> tag
			secureText = iframeEndTagPattern.matcher(secureText).replaceAll("");			
			
			// Avoid eval(...) expressions
			secureText = evalPattern.matcher(secureText).replaceAll("");

			// Avoid expression(...) expressions
			secureText = expressionPattern.matcher(secureText).replaceAll("");

			// Avoid javascript:... expressions
			secureText = javascriptPattern.matcher(secureText).replaceAll("");

			// Avoid vbscript:... expressions
			secureText = vbscriptPattern.matcher(secureText).replaceAll("");

			// Avoid onload= expressions
			secureText = onloadPattern.matcher(secureText).replaceAll("");
			
			// Avoid onclick= expressions
			secureText = onclickPattern.matcher(secureText).replaceAll("");
		}
		return secureText;
	}
	
	/**
	 * Strip HTML tags: removes all html tags from text
	 * 
	 * @param htmlText
	 * @return
	 */
	public static String stripTags(String htmlText) {
		return StringEscapeUtils.escapeHtml(htmlText);
	}

}

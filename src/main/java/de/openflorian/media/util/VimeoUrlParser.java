package de.openflorian.media.util;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.ParseException;

/**
 * Youtube Link Parser<br/>
 * <Br/>
 * Parses a given Link and matches out the video id
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 *
 */
public class VimeoUrlParser {

	private static final String embedPatternString = "(\\?clip_id=|/video/)([0-9]*)";
	private static final String normalPatternString = "(vimeo.com/)([0-9]*)";
	
	private static Pattern embedPattern = Pattern.compile(embedPatternString);
	private static Pattern normalPattern = Pattern.compile(normalPatternString);
	
	/**
	 * Regexes given link and extracts vimeo video id
	 * 
	 * @param link
	 * @return
	 */
	public static String parse(String link) {

		String foundId = null;
		
	    Matcher embedMatcher = embedPattern.matcher(link);
	    if(embedMatcher.find()){
	    	foundId = embedMatcher.group(2);
	    }
	    
	    if(StringUtils.isEmpty(foundId)) {
	    	Matcher normalMather = normalPattern.matcher(link);
	    	if(normalMather.find())
	    		foundId = normalMather.group(2);
	    }
	    
	    if(StringUtils.isEmpty(foundId))
	    	throw new ParseException("Unable to parse given URL: " + link);
	    	
	    return foundId;
	}
	
}

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

/**
 * Youtube Link Parser<br/>
 * <Br/>
 * Parses a given Link and matches out the video id
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class YoutubeUrlParse {

	private static final String embedPatternString = ".*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*";
	
	private static Pattern embedPattern = Pattern.compile(embedPatternString);
	
	/**
	 * Parse given link and extract youtube video id
	 * 
	 * @param link
	 * @return
	 */
	public static String parse(String link) {

		String foundId = null;
		
	    Matcher embedMatcher = embedPattern.matcher(link);
	    if(embedMatcher.find()){
	    	foundId = embedMatcher.group(1);
	    }

	    return foundId;
	}
	
}


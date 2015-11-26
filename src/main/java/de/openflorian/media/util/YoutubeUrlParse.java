package de.openflorian.media.util;

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
	    
	    if(StringUtils.isEmpty(foundId))
	    	throw new ParseException("Unable to parse given URL: " + link);
	    	
	    return foundId;
	}
	
}


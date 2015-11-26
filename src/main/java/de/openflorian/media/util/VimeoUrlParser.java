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

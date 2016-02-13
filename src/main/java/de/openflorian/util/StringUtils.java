package de.openflorian.util;

/**
 * Utility Class for handling Strings
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class StringUtils {

	/**
	 * Determines wether <code>target</code> is null
	 * or empty.
	 * 
	 * @return {@link Boolean}
	 * 		true if string is null or empty, otherwise false
	 */
	public static boolean isEmpty(String target) {
		return (target ==  null || target.isEmpty());
	}

}

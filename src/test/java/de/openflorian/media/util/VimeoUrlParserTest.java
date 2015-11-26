package de.openflorian.media.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testcase for {@link VimeoUrlParser}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class VimeoUrlParserTest {

	private Map<String, Boolean> testCases;
	
	public static String VIDEO_ID = "12983746";
	
	@Before
	public void setUp() throws Exception {
		testCases = new HashMap<String, Boolean>();
		
		testCases.put("http://player.vimeo.com/video/" + VIDEO_ID + "?portrait=0&color=333", true);
		testCases.put("player.vimeo.com/video/" + VIDEO_ID + "?portrait=0&color=333", true);
		testCases.put("https://vimeo.com/moogaloop.swf?clip_id=" + VIDEO_ID + "&server=vimeo.com&color=00adef&fullscreen=1", true);
		testCases.put("vimeo.com/video/?portrait=0&color=333", false);
		testCases.put("vimeo.com/video/asdfas?portrait=0&color=333", false);
		testCases.put("vimeo.com/" + VIDEO_ID + "?portrait=0&color=333", true);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test the setUp() 
	 */
	@Test
	public void testMatcher() {
		for(Entry<String, Boolean> testCase: testCases.entrySet()) {
			try {
				String videoId = VimeoUrlParser.parse(testCase.getKey());
				assertTrue("Test should pass with url: " + testCase.getKey(), videoId.equals(VIDEO_ID));
			} catch (Exception e) {
				assertFalse("Test should pass with url: " + testCase.getKey(), testCase.getValue());
			}	
		}
	}

}

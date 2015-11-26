package de.openflorian.media.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testcase for {@link YoutubeUrlParse}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class YoutubeUrlParserTest {

	private Map<String, Boolean> testCases;
	
	public static String VIDEO_ID = "QDK8U-VIH_o";
	
	@Before
	public void setUp() throws Exception {
		testCases = new HashMap<String, Boolean>();
		
		testCases.put("http://www.youtube.com/watch?v=" + VIDEO_ID + "&feature=feedrec_grec_index", true);
		testCases.put("http://www.youtube.com/user/SomeUser#p/a/u/1/" + VIDEO_ID + "?portrait=0&color=333", true);
		testCases.put("http://www.youtube.com/v/" + VIDEO_ID + "?fs=1&amp;hl=en_US&amp;rel=0", true);
		testCases.put("http://www.youtube.com/watch?v=" + VIDEO_ID + "#t=0m10s", true);
		testCases.put("http://www.youtube.com/embed/" + VIDEO_ID + "?rel=0", true);
		testCases.put("http://www.youtube.com/watch?v=" + VIDEO_ID + "?portrait=0&color=333", true);
		testCases.put("http://youtu.be/" + VIDEO_ID, true);
		
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
				String videoId = YoutubeUrlParse.parse(testCase.getKey());
				assertTrue("Test should pass with url: " + testCase.getKey(), videoId.equals(VIDEO_ID));
			} catch (Exception e) {
				assertFalse("Test should pass with url: " + testCase.getKey(), testCase.getValue());
			}	
		}
	}

}

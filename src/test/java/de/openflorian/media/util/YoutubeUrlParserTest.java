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
 * @author Bastian Kraus <bofh@k-hive.de>
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

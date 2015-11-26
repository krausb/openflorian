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

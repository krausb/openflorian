package de.openflorian.web.system;

import org.junit.Test;

/**
 * TestCase for Testing {@link SystemInformation}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class SystemInformationTest {

	@Test
	public void systemInformationTest() throws Exception {
		SystemInformation info = new SystemInformation();
		
		info.sysoutAllInformation();
	}

}

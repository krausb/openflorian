package de.openflorian.web.system;

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

import org.junit.Test;

/**
 * TestCase for Testing {@link SystemInformation}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class SystemInformationTest {

	@Test
	public void systemInformationTest() throws Exception {
		SystemInformation info = new SystemInformation();
		
		info.sysoutAllInformation();
	}

}

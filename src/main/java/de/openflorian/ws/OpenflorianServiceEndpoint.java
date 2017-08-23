package de.openflorian.ws;

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

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.openflorian.ws.api.impl.AlarmResourceImpl;
import de.openflorian.ws.api.impl.OperationResourceImpl;

/**
 * Openflorian REST Service Endpoint
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
@ApplicationPath("/rest/api")
public class OpenflorianServiceEndpoint extends Application {

	private final Set<Object> singletons = new HashSet<Object>();

	public OpenflorianServiceEndpoint() {
		singletons.add(new AlarmResourceImpl());
		singletons.add(new OperationResourceImpl());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}

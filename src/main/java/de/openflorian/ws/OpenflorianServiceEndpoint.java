package de.openflorian.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.openflorian.ws.api.impl.AlarmResourceImpl;

/**
 * Openflorian REST Service Endpoint
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
@ApplicationPath("/rest/api")
public class OpenflorianServiceEndpoint extends Application {

	private Set<Object> singletons = new HashSet<Object>();

	public OpenflorianServiceEndpoint() {
		singletons.add(new AlarmResourceImpl());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}

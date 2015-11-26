package de.openflorian.web.user;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import de.openflorian.web.service.AbstractRemoteDataService;

/**
 * Permission Service<br/>
 * <br/>
 * Templates basic permission management functionality for {@link User}<br/>
 * related {@link Permission}s.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class PermissionService extends AbstractRemoteDataService {

	public static final String CONFIG_DATA_REST_PATH = "web.permission.webservice";
	private String DATA_REST_PATH = null;
	
	protected PermissionResource resource;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		
		DATA_REST_PATH = configuration.getProperty(CONFIG_DATA_REST_PATH);
		
		// Initializing REST Interfaces
		ResteasyClient client = new ResteasyClientBuilder()
		.connectionPoolSize(RESTEASY_CONNECTIONPOOL_SIZE)
		.connectionTTL(RESTEASY_CONNECTION_TTL, RESTEASY_CONNECTION_TTL_TIMEUNIT)
		.build();
		ResteasyWebTarget target = null;
		try {
			target = client.target(DATA_REST_PATH);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e.getMessage(),e);
		}
		
		resource = (PermissionResource) target.proxy(PermissionResource.class);
	}
	
	/**
	 * Checks if an {@link User} has a given <code>permission</code>
	 * 
	 * @param permission
	 * @param user
	 * @return
	 */
	public boolean hasPermission(User user, String permission) {
		try {
			Permission p = resource.getByUserAndPermission(user.getId(), permission);
			return (p != null);
		} catch (EntityNotFoundException e) {
			// nothing to do here
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return false;
	}
	
	/**
	 * Get all {@link Permission} from given <code>user</code>.
	 * 
	 * @param user
	 * 		{@link User}
	 * @return
	 * 		{@link List}<{@link Permission}>
	 */
	public List<Permission> getPermissions(User user) {
		return Arrays.asList(resource.list(user.getId()));
	}
	
	/**
	 * Creates a <code>permission</code> for a given <code>user</code>.
	 * 
	 * @param user
	 * 		{@link User}
	 * @param permission
	 * 		{@link String}
	 */
	public Permission createPermission(User user, String permission) {
		Permission p = resource.getByUserAndPermission(user.getId(), permission);
		if(p == null)
			return resource.persist(Permission.fromUserAndPermission(user, permission));
		return p;
	}
	
	/**
	 * Remove a <code>permission</code> from an <code>user</code>.
	 * 
	 * @param user
	 * 		{@link User}
	 * @param permission
	 * 		{@link String}
	 */
	public void removePermission(User user, String permission) {
		resource.remove(user.getId(), permission);
	}
	
}

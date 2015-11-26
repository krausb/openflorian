package de.openflorian.web.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import de.openflorian.web.service.AbstractRemoteDataService;

/**
 * User Service Bean
 * 
 * Spring Bean to Access user 
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class UserService extends AbstractRemoteDataService  {

	public static final String CONFIG_DATA_REST_PATH = "web.user.webservice";
	private String DATA_REST_PATH = null;
	
	protected UserResource resource;
	
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
		
		resource = (UserResource) target.proxy(UserResource.class);
	}
	
	/**
	 * Load a list of {@link User}
	 */
	public User[] list() {
		return resource.list();
	}

	/**
	 * Load an {@link User} by username
	 * @param name
	 * @return
	 */
	public User getByName(String name) {
		return resource.getByName(name);
	}
	
	/**
	 * Load an {@link User} by pk id 
	 * 
	 * @param id {@link User#getPersonId()}
	 * @return {@link User}
	 */
	public User getById(Long id) {
		return resource.getById(id);
	}

	/**
	 * Persist an {@link User} entity.
	 * 
	 * @param o {@link User}
	 * @return {@link User} 
	 */
	public User persist(User o) {
		return resource.persist(o);
	}

	/**
	 * Remove an {@link User} entity
	 * 
	 * @param id {@link User#getPersonId()}
	 */
	public void remove(long id) {
		resource.remove(id);
	}

	/**
	 * Search a list of {@link User} entities by {@code column} and {@code filter}
	 * 
	 * @param column
	 * @param filter
	 * @return {@link User}[]
	 */
	public User[] search(String column, String filter) {
		return resource.search(column, filter);
	}

	/**
	 * Count all available {@link User} entities
	 * @return
	 */
	public Long count() {
		return resource.count();
	}

	/**
	 * Get a list of {@link User} entities paged by {@code activePage}
	 * 
	 * @param activePage
	 * @return {@link User}[]
	 */
	public List<User> listByPage(String sort, int activePage, int pageSize) {
		return Arrays.asList(resource.listByPage(sort, activePage, pageSize));
	}

	/**
	 * Get a list of {@link User} entities filtered by <code>permission</code>
	 * 
	 * @param permission
	 * @return {@link List}<{@link User}>
	 */
	public List<User> listByPermission(String permission) {
		// TODO: implement permission manager
		return new ArrayList<User>();
	}
	
}

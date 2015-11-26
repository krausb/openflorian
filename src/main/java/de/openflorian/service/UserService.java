package de.openflorian.service;

import java.util.List;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.openflorian.data.dao.principal.UserDao;
import de.openflorian.web.user.User;

/**
 * User Data Service Bean
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Service
public class UserService{

	@Autowired
	private UserDao dao;
	
	/**
	 * Get the amount of all available {@link User} in persistence context.
	 * @return
	 * 		{@link long}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public long count() {
		return dao.sizeOf();
	}

	/**
	 * Gets a specific {@link User} by <code>id</code>
	 * 
	 * @param id
	 * @return
	 * 		{@link User}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public User getById(Long id) {
		return dao.findById(id);
	}

	/**
	 * Gets a specific {@link User} by <code>name</code>
	 * 
	 * @param name
	 * @return
	 * 		{link User}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public User getByName(String name) {
		return dao.findByAttribute("name", name);
	}

	/**
	 * Lists all available {@link User}s
	 * 
	 * @return
	 * 		{@link List}<{@link User}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<User> list() {
		return dao.findAll("name");
	}

	/**
	 * Lists {@link User}s paged by
	 * 
	 * @param order
	 * @param activePage
	 * @param pageSize
	 * @return
	 * 		{@link List}<{@link User}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<User> listByPage(String order, int activePage, int pageSize) {
		return dao.findAllByPage(order, pageSize, activePage);
	}

	/**
	 * Persist given <code>o</code> {@link User} to persistence context
	 * 
	 * @param object
	 * @return
	 * 		persisted <code>object</code>
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public User persist(User o) throws ValidationException { 
		return dao.merge(o);
	}

	/**
	 * Remove given entity by <code>id</code> from persistence context
	 * 
	 * @param id
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void remove(long id) {
		dao.remove(id);
	}

	/**
	 * Search {@link User} entities whose <code>attribute</code> matches to <code>value</code>
	 * 
	 * @param attribute
	 * @param value
	 * @return
	 * 		{@link List}<{@link User}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<User> search(String attribute, String value) {
		return dao.findAllByAttribute(attribute, value);
	}

}

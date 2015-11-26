package de.openflorian.service;

import java.util.List;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.openflorian.data.dao.OperationDao;
import de.openflorian.data.model.Operation;

/**
 * {@link Operation} Data Service Bean
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Service
public class OperationService{

	@Autowired
	private OperationDao dao;
	
	/**
	 * Get the amount of all available {@link Operation} in persistence context.
	 * @return
	 * 		{@link long}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public long count() {
		return dao.sizeOf();
	}

	/**
	 * Gets a specific {@link Operation} by <code>id</code>
	 * 
	 * @param id
	 * @return
	 * 		{@link Operation}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public Operation getById(Long id) {
		return dao.findById(id);
	}

	/**
	 * Lists all available {@link Operation}s
	 * 
	 * @return
	 * 		{@link List}<{@link Operation}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<Operation> list() {
		return dao.findAll("name");
	}

	/**
	 * Lists {@link Operation}s paged by
	 * 
	 * @param order
	 * @param activePage
	 * @param pageSize
	 * @return
	 * 		{@link List}<{@link Operation}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<Operation> listByPage(String order, int activePage, int pageSize) {
		return dao.findAllByPage(order, pageSize, activePage);
	}

	/**
	 * Persist given <code>o</code> {@link Operation} to persistence context
	 * 
	 * @param object
	 * @return
	 * 		persisted <code>object</code>
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Operation persist(Operation o) throws ValidationException { 
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
	 * Search {@link Operation} entities whose <code>attribute</code> matches to <code>value</code>
	 * 
	 * @param attribute
	 * @param value
	 * @return
	 * 		{@link List}<{@link Operation}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<Operation> search(String attribute, String value) {
		return dao.findAllByAttribute(attribute, value);
	}

}

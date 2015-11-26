package de.openflorian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.openflorian.data.dao.principal.CountryDao;
import de.openflorian.data.model.principal.Country;

/**
 * Spring Service Bean for {@link Countr} DTOs
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Service
public class CountryService {
	
	@Autowired
	CountryDao dao;

	/**
	 * Get the amount of all available {@link Country} in persistence context.
	 * @return
	 * 		{@link long}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public long count() {
		return dao.sizeOf();
	}

	/**
	 * Gets a specific {@link Country} by <code>id</code>
	 * 
	 * @param id
	 * @return
	 * 		{@link Country}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public Country getById(Long id) {
		return dao.findById(id);
	}

	/**
	 * Lists all available {@link Country}s
	 * 
	 * @return
	 * 		{@link List}<{@link Country}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<Country> list() {
		return dao.findAll("shortName");
	}

	/**
	 * Lists {@link Country}s paged by
	 * 
	 * @param order
	 * @param activePage
	 * @param pageSize
	 * @return
	 * 		{@link List}<{@link Country}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<Country> listByPage(String order, int activePage, int pageSize) {
		return dao.findAllByPage(order, pageSize, activePage);
	}

	/**
	 * Search {@link Country} entities whose <code>attribute</code> matches to <code>value</code>
	 * 
	 * @param attribute
	 * @param value
	 * @return
	 * 		{@link List}<{@link Country}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<Country> search(String attribute, String value) {
		return dao.findAllByAttribute(attribute, value);
	}
}

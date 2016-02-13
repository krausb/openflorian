package de.openflorian.service;

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

import java.util.List;

import javax.persistence.NoResultException;
import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.openflorian.data.dao.OperationResourceDao;
import de.openflorian.data.model.Operation;
import de.openflorian.data.model.OperationResource;

/**
 * {@link OperationResource} Data Service Bean
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Service
public class OperationResourceService{

	@Autowired
	private OperationResourceDao dao;
	
	/**
	 * Get the amount of all available {@link OperationResource} in persistence context.
	 * @return
	 * 		{@link long}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public long count() {
		return dao.sizeOf();
	}

	/**
	 * Gets a specific {@link OperationResource} by <code>id</code>
	 * 
	 * @param id
	 * @return
	 * 		{@link OperationResource}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public OperationResource getById(Long id) {
		return dao.findById(id);
	}

	/**
	 * Lists all available {@link OperationResource}s
	 * 
	 * @return
	 * 		{@link List}<{@link OperationResource}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<OperationResource> list() {
		return dao.findAll("name");
	}

	/**
	 * Lists {@link OperationResource}s paged by
	 * 
	 * @param order
	 * @param activePage
	 * @param pageSize
	 * @return
	 * 		{@link List}<{@link OperationResource}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<OperationResource> listByPage(String order, int activePage, int pageSize) {
		return dao.findAllByPage(order, pageSize, activePage);
	}

	/**
	 * Persist given <code>o</code> {@link OperationResource} to persistence context
	 * 
	 * @param object
	 * @return
	 * 		persisted <code>object</code>
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public OperationResource persist(OperationResource o) throws ValidationException { 
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
	 * Search {@link OperationResource} entities whose <code>attribute</code> matches to <code>value</code>
	 * 
	 * @param attribute
	 * @param value
	 * @return
	 * 		{@link List}<{@link Operation}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<OperationResource> search(String attribute, String value) {
		return dao.findAllByAttribute(attribute, value);
	}
	
	/**
	 * Get an {@link OperationResource} by <code>callname</code>
	 * 
	 * @param callname
	 * @return
	 * 		{@link OperationResource} or {@link NoResultException}
	 */
	public OperationResource getResourceByCallname(String callname) {
		OperationResource resource = null;
		try {
			resource = dao.getResourceByCallname(callname);
		} catch (NoResultException e) {}
		return resource;
	}

}

package de.openflorian.data.dao;

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

import javax.persistence.Query;
import javax.transaction.Transactional;

import de.openflorian.data.jpa.GenericDao;
import de.openflorian.data.model.OperationResource;

/**
 * {@link OperationResourceDao}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class OperationResourceDao extends GenericDao<OperationResource, Long> {

	/**
	 * Get an {@link OperationResource} by <code>callname</code>
	 * 
	 * @param callname
	 * @return {@link OperationResource} or null
	 */
	@Transactional
	public OperationResource getResourceByCallname(String callname) {
		Query query = getEntityManager().createQuery(
				"from " + OperationResource.class.getCanonicalName() + " o " + "WHERE o.callName = :callName");
		query.setParameter("callName", callname);
		return (OperationResource) query.getSingleResult();
	}

}

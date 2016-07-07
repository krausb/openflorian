package de.openflorian.data.dao;

import de.openflorian.data.jdbc.DatabaseConnector;

/**
 * {@link OperationResourceDao}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class OperationResourceDao extends DatabaseConnector {

	// /**
	// * Get an {@link OperationResource} by <code>callname</code>
	// *
	// * @param callname
	// * @return {@link OperationResource} or null
	// */
	// @Transactional
	// public OperationResource getResourceByCallname(String callname) {
	// Query query = getEntityManager().createQuery(
	// "from " + OperationResource.class.getCanonicalName() + " o " + "WHERE o.callName = :callName");
	// query.setParameter("callName", callname);
	// return (OperationResource) query.getSingleResult();
	// }

}

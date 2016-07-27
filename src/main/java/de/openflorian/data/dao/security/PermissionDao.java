package de.openflorian.data.dao.security;

import de.openflorian.data.jdbc.DatabaseConnector;
import de.openflorian.web.user.Permission;

/**
 * {@link Permission} DAO<br/>
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class PermissionDao extends DatabaseConnector {

	// /**
	// * Get a specific {@link Permission} for a given <code>u</code> and a given <code>permission</code>.
	// *
	// * @param u
	// * @param permission
	// * @return {@link Permission}
	// */
	// public Permission getByUserAndPermission(User u, String permission) {
	// final Query query = getEntityManager().createQuery("from " + Permission.class.getCanonicalName() + " p "
	// + "WHERE p.user.id = :userid " + "AND p.permission = :permission");
	// query.setParameter("userid", u.getId());
	// query.setParameter("permission", permission);
	// return (Permission) query.getSingleResult();
	// }

}

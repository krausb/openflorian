package de.openflorian.data.dao.security;

import javax.persistence.Query;

import de.openflorian.data.jpa.GenericDao;
import de.openflorian.web.user.Permission;
import de.openflorian.web.user.User;

/**
 * {@link Permission} DAO<br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class PermissionDao extends GenericDao<Permission, Long>{

	/**
	 * Get a specific {@link Permission} for a given <code>u</code> and
	 * a given <code>permission</code>.
	 * 
	 * @param u
	 * @param permission
	 * @return
	 * 		{@link Permission}
	 */
	public Permission getByUserAndPermission(User u, String permission) {
    	Query query = getEntityManager().createQuery(
    			"from " + Permission.class.getCanonicalName() + " p " +
    			"WHERE p.user.id = :userid " + 
    			"AND p.permission = :permission");
    	query.setParameter("userid", u.getId());
    	query.setParameter("permission", permission);
    	return (Permission)query.getSingleResult();
    }
	
	
}

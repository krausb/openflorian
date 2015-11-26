package de.openflorian.web.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * {@link Permission} data transfer object
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Entity
@Table(name="user_permission")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Permission implements Serializable {
	private static final long serialVersionUID = 8108610154018524080L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	
	@OneToOne
	protected User user;
	
	protected String permission;
	
	/**
	 * Factory Method for creating a {@link Permission} from a given <code>user</code> and<br/>
	 * a given <code>permission</code>
	 * 
	 * @param user
	 * @param permission
	 * @return
	 * 		{@link Permission}
	 */
	public static Permission fromUserAndPermission(User user, String permission) {
		Permission p = new Permission();
		p.setUser(user);
		p.setPermission(permission);
		return p;
	}
	
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public User getUser() {
		return this.user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getPermission() {
		return this.permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission other = (Permission) obj;
		if (id != other.id)
			return false;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
}

package de.openflorian.ui.renderer;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import de.openflorian.web.user.User;

/**
 * {@link User} Listbox Listitem Renderer
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class UserListitemRenderer implements ListitemRenderer<User> {
	
	public void render(Listitem item, User user, int index) throws Exception {
		item.setValue(user);
		
		Listcell username = null;
		if(user.getName() != null) 
			username = new Listcell(user.getName());
		else
			username = new Listcell("");
		username.setParent(item);
		
		Listcell firstname = null;
		if(user.getFirstname() != null) 
			firstname = new Listcell(user.getFirstname());
		else
			firstname = new Listcell("");
		firstname.setParent(item);
		
		Listcell lastname = null;
		if(user.getLastname() != null) 
			lastname = new Listcell(user.getLastname());
		else
			lastname = new Listcell("");
		lastname.setParent(item);
		
		Listcell email = null;
		if(user.getEmail() != null) 
			email = new Listcell(user.getEmail());
		else
			email = new Listcell("");
		email.setParent(item);
		
	}

}

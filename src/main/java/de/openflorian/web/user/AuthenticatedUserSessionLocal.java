package de.openflorian.web.user;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import de.openflorian.web.core.RequestDataProvider;

/**
 * Authenticated Users Session Local<br/>
 * <br/>
 * User entity: {@link User}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AuthenticatedUserSessionLocal {

	/**
	 * Set authenticated {@link User} into session
	 * @param user
	 */
	public static void set(Principal user) {
		HttpServletRequest request = RequestDataProvider.getCurrentHttpRequest();
		if(request != null)
			request.getSession().setAttribute(RequestDataProvider.SESSION_USER, user);
	}
	
	/**
	 * Get authenticated {@link User} from HTTP Session
	 * @return
	 */
	public static User get() {
		HttpServletRequest request = RequestDataProvider.getCurrentHttpRequest();
		if(request != null)
			return (User)request.getSession().getAttribute(RequestDataProvider.SESSION_USER);
		else
			return null;
	}
	
}

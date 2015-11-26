package de.openflorian.web.user;


/**
 * {@link ThreadLocal} to store the current {@link User} user object
 *  
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AuthenticatedUserThreadLocal {

	private static ThreadLocal<User> currentUser = new ThreadLocal<User>();
	
	/**
	 * Store current authenticated user into ThreadLocal
	 * @param user
	 */
	public static void set(User user) {
		currentUser.set(user);
	}
	
	/**
	 * Retrieve current
	 * @return
	 */
	public static User get() {
		return currentUser.get();
	}
	
}

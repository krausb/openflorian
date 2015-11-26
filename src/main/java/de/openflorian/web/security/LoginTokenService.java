package de.openflorian.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.crypt.CryptCipherService;
import de.openflorian.crypt.CryptCipherService.CipherTarget;
import de.openflorian.web.core.AvailableComponent;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.user.User;

/**
 * Security Manager<br/>
 * <br/>
 * Brings basic security functionality like Login Token etc.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class LoginTokenService {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * Login token TTL: 30 days
	 */
	public static final int TOKEN_TTL = 86400 * 30;
	public static final String PRINCIPAL_ID_PREFIX = "aid";
	public static final String TTL_PREFIX = "ttl";
	
	public static final String REQUEST_ACTOR_TOKEN = "at";
	public static final String REQUEST_LOGIN_TOKEN = "t";
	
	private CryptCipherService cryptService = (CryptCipherService)ContainerManager.getComponent(AvailableComponent.CryptService);
	
	/**
	 * Create a login token for the login token interceptor
	 * @param a
	 * @return
	 */
	public String createFrontendLoginToken(User u) {
		String plainToken = cryptService.encrypt(PRINCIPAL_ID_PREFIX + "=" + String.valueOf(u.getId()), CipherTarget.Blowfish) + ";" + 
				cryptService.encrypt(TTL_PREFIX + "=" + String.valueOf((System.currentTimeMillis() / 1000) + TOKEN_TTL), CipherTarget.Blowfish);
		return cryptService.encrypt(plainToken, CipherTarget.Xor);
	}
	
	/**
	 * Create a encoded {@link Actor#getPersonId()} token
	 *  
	 * @param a
	 * @return
	 */
	public String createActorToken(User u) {
		String plainToken = cryptService.encrypt(PRINCIPAL_ID_PREFIX + "=" + String.valueOf(u.getId()), CipherTarget.Blowfish);
		return cryptService.encrypt(plainToken, CipherTarget.Xor);
	}
	
	/**
	 * Extract actorId from given <code>actorToken</code>
	 * 
	 * @param actorToken
	 * @return
	 */
	public long extractActorIdFromToken(String actorToken) throws IllegalArgumentException {
		try {
			String plainToken = cryptService.decrypt(actorToken,CipherTarget.Xor);
			return Long.parseLong(cryptService.decrypt(plainToken, CipherTarget.Blowfish).substring(PRINCIPAL_ID_PREFIX.length() + 1));
		} catch (Exception e) {
			throw new IllegalArgumentException("Actor token could not be extracted from given argument value.");
		}
	}
	
	/**
	 * Validate a given <code>token</code> for a correct set <code>a</code> 
	 * 
	 * @param u
	 * @param token
	 * @return
	 */
	public boolean validateFrontendLoginToken(User u, String token) {
		try {
			String plainToken = cryptService.decrypt(token, CipherTarget.Xor);
			String[] stoken = plainToken.split(";");
			
			if(stoken.length != 2)
				return false;
			
			String principalstr = cryptService.decrypt(stoken[0], CipherTarget.Blowfish);
			String ttlstr = cryptService.decrypt(stoken[1], CipherTarget.Blowfish);
			
			if(principalstr.startsWith(PRINCIPAL_ID_PREFIX) && ttlstr.startsWith(TTL_PREFIX)) {
				long principalId = Long.parseLong(principalstr.substring(PRINCIPAL_ID_PREFIX.length() + 1));
				
				if(u.getId() == principalId) {
					long ttl = Long.parseLong(ttlstr.substring(TTL_PREFIX.length() + 1));
					long currentTime = System.currentTimeMillis() / 1000;
					if(currentTime <= ttl)
						return true;
				}
			}
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
		}
		
		return false;
	}

}

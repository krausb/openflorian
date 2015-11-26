package de.openflorian.crypt.provider;

import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Cipher<br/>
 * <br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public abstract class AbstractCipher {

	protected Logger log = LoggerFactory.getLogger(getClass());
	
	protected final String key;
	
	public AbstractCipher(String key) {
		this.key = key;
	}
	
	protected String getKey() {
		return this.key;
	}
	
	/**
	 * Decrypt given <code>str</code>
	 * 
	 * @param crypted string
	 * @return 
	 * 		{@link String} Decrypted String (UTF-8)
	 * @throws GeneralSecurityException 
	 */
	public abstract String decrypt(String crypted) throws GeneralSecurityException;
	
	/**
	 * Blowfish encrypt String<br/>
	 * 
	 * @param str plain text
	 * @return 
	 * 		{@link String} Encrypted string (UTF-8)
	 * @throws GeneralSecurityException 
	 */
	public abstract String encrypt(String plain) throws GeneralSecurityException;
	
}

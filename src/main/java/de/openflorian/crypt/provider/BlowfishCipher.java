package de.openflorian.crypt.provider;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Blowfish Cipher Utility
 * 
 * Used cipher: Blowfish/ECB/PKCS5Padding
 * 
 * Requires Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy in the JVM/JRE
 * for unlimited key length support.
 * 
 * See http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
 * See PROJECT_DIR/config/jce_policy-8.zip
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class BlowfishCipher extends AbstractCipher {

	private static Logger log = LoggerFactory.getLogger(BlowfishCipher.class);
	
	public BlowfishCipher(String key) {
		super(key);
	}
	
	@Override
	public String decrypt(String str) throws GeneralSecurityException {
		if(key == null || key.isEmpty())
			throw new IllegalStateException("The key is not set or is length=0.");
		
		if(str == null)
			return null;
		
		try {
			SecretKeySpec keySpec;
			keySpec = new SecretKeySpec(key.getBytes("UTF8"), "Blowfish");
			
			Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			
			return new String(cipher.doFinal(Base64.decodeBase64(str.getBytes("UTF8"))), "UTF8");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralSecurityException(e.getMessage(), e);
		}
	}
	
	@Override
	public String encrypt(String str) throws GeneralSecurityException {
		if(key == null || key.isEmpty())
			throw new IllegalStateException("The Blowfish Secret is not set or is length=0.");
		
		if(str == null)
			return null;
		
		try {
			SecretKeySpec keySpec;
			keySpec = new SecretKeySpec(key.getBytes("UTF8"), "Blowfish");
			
			Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			
			return new Base64(true).encodeToString(cipher.doFinal(str.getBytes("UTF8"))).trim();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralSecurityException(e.getMessage(), e);
		}
	}
	
}

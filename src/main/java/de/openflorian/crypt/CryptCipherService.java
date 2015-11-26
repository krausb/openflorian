package de.openflorian.crypt;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.openflorian.config.ConfigurationProvider;
import de.openflorian.crypt.provider.BlowfishCipher;
import de.openflorian.crypt.provider.XorCipher;

/**
 * Crypt Cipher Web Service Resource<br/>
 * <br/>
 * Integrates following crypt cipher resources:<br/>
 * - {@link PasswordCryptResource}<br/>
 * - {@link AttributeCryptResource}<br/>
 * - {@link BlockCryptResource}<br/>
 * <br/>
 * Important:<br/>
 * ==========<br/>
 * For correct usage of blowfish cipher, the JVM / JRE has to be extended with<br/> 
 * <br/>
 * Keys are stored web service side.<br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Service
public class CryptCipherService implements InitializingBean {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	public static final String CONFIG_CIPHER_KEY_BLOWFISH = "cipher.key.blowfish";
	public static final String CONFIG_CIPHER_KEY_XOR = "cipher.key.xor";
	
	private static String CIPHER_KEY_BLOWFISH;
	private static String CIPHER_KEY_XOR;
	
    static {
    	Logger log = LoggerFactory.getLogger(CryptCipherService.class);
    	InitialContext ctx = null;
    	try {
    		ctx = new InitialContext();
    	} catch (NamingException e) {
    		log.error(e.getMessage(), e);
    	}
    	
    	synchronized (ctx) {
    		try {
    			CIPHER_KEY_BLOWFISH = (String) ctx.lookup( "java:/comp/env/" + CONFIG_CIPHER_KEY_BLOWFISH );
    			CIPHER_KEY_XOR = (String) ctx.lookup( "java:/comp/env/" + CONFIG_CIPHER_KEY_XOR );
    		} catch (NamingException e) {
    			log.warn("Warning while loading cipher keys occured: " + e.getMessage(), e);
    		}
		}
    }
    
	public static final String SECRET_BLIND_STRING = "*******";

	private BlowfishCipher xorCipher;
	private XorCipher blowfishCipher;

	private ConfigurationProvider config;
	
	public enum CipherTarget {
		Blowfish,
		Xor
	}
	
	@Override
	public void afterPropertiesSet() {
		log.info("Initializing CryptCipherService...");
		
		if(config != null) {
			if(StringUtils.isEmpty(CIPHER_KEY_BLOWFISH) ||StringUtils.isEmpty(CIPHER_KEY_XOR)) {
				try {
					CIPHER_KEY_BLOWFISH = config.getProperty(CONFIG_CIPHER_KEY_BLOWFISH);
					CIPHER_KEY_XOR = config.getProperty(CONFIG_CIPHER_KEY_XOR);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			log.trace("blowfish key recieved: " + CIPHER_KEY_BLOWFISH);
			log.trace("xor key recieved: " + CIPHER_KEY_XOR);
			
			xorCipher = new BlowfishCipher(CIPHER_KEY_BLOWFISH);
			blowfishCipher = new XorCipher(CIPHER_KEY_XOR);
		} else {
			log.error("Configuration Provider not available!");
		}
		
		log.info(".. finished!");
	}

	/**
	 * Encrypt {@code plaintext} by {@link CipherTarget} {@code target}
	 * 
	 * @param plaintext  plain {@link String} to encrypt
	 * @param target {@link CipherTarget}
	 * @return {@link String}
	 */
	public String encrypt(String plaintext, CipherTarget target) {
		try {
			switch(target) {
				case Blowfish:
					return this.blowfishCipher.encrypt(plaintext);
				case Xor:
					return this.xorCipher.encrypt(plaintext);
				default:
					throw new RuntimeException("Method not implemented.");
			}
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Decrypt {@code cryptedtext} by {@link CipherTarget} {@code target}
	 * 
	 * @param cryptedtext  crypted {@link String} to encrypt
	 * @param target {@link CipherTarget}
	 * @return {@link String}
	 */
	public String decrypt(String cryptedtext, CipherTarget target) {
		try {
			switch(target) {
				case Blowfish:
					return this.blowfishCipher.decrypt(cryptedtext);
				case Xor:
					return this.xorCipher.decrypt(cryptedtext);
				default:
					throw new RuntimeException("Method not implemented.");
			}
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Dependency injection: {@link ConfigurationProvider}
	 * 
	 * @param config
	 */
	public void setConfigurationProvider(ConfigurationProvider config) {
		this.config = config;
	}
	
	
}

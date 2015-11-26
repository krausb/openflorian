package de.openflorian.crypt.provider;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * Classical XOR Cipher
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class XorCipher extends AbstractCipher {

	public XorCipher(String key) {
		super(key);
	}
	
	@Override
	public String encrypt(String plain) throws GeneralSecurityException {
		if(StringUtils.isEmpty(plain))
			throw new IllegalArgumentException("String AND Key may not be empty.");
		if(StringUtils.isEmpty(key))
			throw new IllegalStateException("Xor cipher key is not set.");
			
		
		char[] keychars = key.toCharArray();
		char[] targetchars = plain.toCharArray();
		
		int keycharsLength=keychars.length, targetcharsLength=targetchars.length;
		
		char[] newtarget = new char[targetcharsLength];
		
		for(int i=0; i < targetcharsLength; i++) 
			newtarget[i] = (char)(targetchars[i] ^ keychars[i%keycharsLength]); 
		
		keychars = null; targetchars = null;
		
		try {
			return Base64.encodeBase64String(new String(newtarget).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new GeneralSecurityException(e);
		}
	}
	
	@Override
	public String decrypt(String target) throws GeneralSecurityException {
		if(StringUtils.isEmpty(target))
			throw new IllegalArgumentException("String AND Key may not be empty.");
		if(StringUtils.isEmpty(key))
			throw new IllegalStateException("Xor cipher key is not set.");
		
		char[] keychars = key.toCharArray();
		char[] targetchars = new String(Base64.decodeBase64(target)).toCharArray();
		
		int keycharsLength=keychars.length, targetcharsLength=targetchars.length;
		
		char[] newtarget = new char[targetcharsLength];
		
		for(int i=0; i < targetcharsLength; i++) 
			newtarget[i] = (char)(targetchars[i] ^ keychars[i%keycharsLength]); 
		
		keychars = null; targetchars = null;
		
		return new String(newtarget);
	}
	
}

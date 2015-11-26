package de.openflorian.crypt.provider;

import java.security.MessageDigest;

import org.apache.commons.lang.NotImplementedException;

/**
 * Classical MD5 Cipher<br/>
 * <br/>
 * Creates a MD5 Hex Hash from given data.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class Md5Cipher extends AbstractCipher{

	public Md5Cipher() {
		super("");
	}
	
	@Override
	public String encrypt(String plaintext) {
		
		MessageDigest md = null;
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
		if(md != null) {
			md.update(plaintext.getBytes());
			byte[] data = md.digest();
			
			StringBuffer out = new StringBuffer();
			for(int c=0; c < data.length; c++) 
				out.append(Integer.toString((data[c] & 0xff) + 0x100, 16).substring(1));
			
			return out.toString();
		}
		
		throw new IllegalStateException("MD5 MessageDigest not present");
	}
	
	@Override
	public String decrypt(String crypted) {
		throw new NotImplementedException();
	}
	
}

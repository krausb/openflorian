package de.openflorian.crypt.provider;

import org.junit.Test;

/**
 * TestCase for Testing {@link XorCipher}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class CipherTest {

	private final String BLOWFISH_SECRET = "12e9812he98fa9h21e98f(*Wehr9283898h9a8f98232398f";
	private final String XOR_SECRET = "viufdgb8ieu87*G&*&GIUGIUygjygjHjhBj788y7giYGkkjs";
	
	private final String PLAIN_TEXT = "test";
	
	@Test
	public void cryptTextTest() throws Exception {
		String crypted = new BlowfishCipher(BLOWFISH_SECRET).encrypt(new XorCipher(XOR_SECRET).encrypt(PLAIN_TEXT));
		System.out.println("\"" + crypted + "\"");
		System.out.println("decrypted string: " + new XorCipher(XOR_SECRET).decrypt(new BlowfishCipher(BLOWFISH_SECRET).decrypt(crypted)));
		
	}

}

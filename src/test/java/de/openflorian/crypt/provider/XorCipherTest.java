package de.openflorian.crypt.provider;

import org.junit.Test;

/**
 * TestCase for Testing {@link XorCipher}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class XorCipherTest {

	private final String XOR_SECRET_1 = "asidfu2i3/23as9ef80yiuh{}@#0fas";
	private final String XOR_SECRET_2 = "87g8*&G*87[][90_]_09u09usd98fsd";
	
	private final String PLAIN_TEXT = "Husum: 'Neptun-Seehaus' (aufgeloest 2008)";
	
	private final String CRYPTED_TEST_1 = "KQYaEQtPEk59SkJHFB0UNgNdWBgcBk9bVSFWVgEEHw4WGhBGRwJZCwY=";
	private final String CRYPTED_TEST_2 = "cEIUTUccZw12UispLlcdDDg6WFgAQx5VWwVMXgEWCFdSFEwKFHcaAB4=";
		
	@Test
	public void cryptTextTest() throws Exception {
		String pwd = new XorCipher(XOR_SECRET_2).encrypt(PLAIN_TEXT);
		System.out.println("\"" + pwd + "\"");
		System.out.println("decrypted string: " + new XorCipher(XOR_SECRET_2).decrypt(pwd));
		
	}

}

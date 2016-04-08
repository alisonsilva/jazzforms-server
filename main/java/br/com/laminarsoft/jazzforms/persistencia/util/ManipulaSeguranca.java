package br.com.laminarsoft.jazzforms.persistencia.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ManipulaSeguranca {
	
	public static String getHashMD5(String valorOriginal) {
		String valorAlterado = null;
		try {			
			byte[] arrVlrOriginal = valorOriginal.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(arrVlrOriginal);
			byte[] thedigest = md.digest();
			BigInteger bigInt = new BigInteger(1, thedigest);
			valorAlterado = bigInt.toString(16);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return valorAlterado;
	}

}

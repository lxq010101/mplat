package com.ustcinfo.mobile.platform.core.utils;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Decryptor {

	private static final String Algorithm = "DESede"; // 定义 加密算法,可用


	/**
	 * 解密
	 * 
	 *            加密密钥，长度为24字节
	 * @param src
	 *            加密后的缓冲区
	 * @return
	 * @author SHANHY
	 * @date 2015-8-18
	 */
	private static byte[] decryptMode(byte[] src, String key) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(key.getBytes(), Algorithm);

			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
	}
	
	@SuppressWarnings("restriction")
	public static String execute(String oldString , String key) {
		if(oldString == null)
			return null;
		byte[] encode = null;
		try {
			encode = Base64Utils.decode(oldString);
			byte[] srcBytes = decryptMode(encode,key);
			return new String(srcBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
}

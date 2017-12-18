package com.ustcinfo.mobile.platform.core.utils;



import java.security.MessageDigest;
public class MD5Encrypt {
	
//	private static Log log = LogFactory.getLog(MD5Encrypt.class);
	
	/**
	 * 转换字节数组为16进制字串
	 * @return 16进制字串
	 */
	public static String byteArrayToString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			// 使用本函数则返回加密结果的10进制数字字串，即全数字形式
			resultSb.append(byteToNumString(b[i]));
		}
		return resultSb.toString();
	}

	/**byte转换成数字字符串*/
	private static String byteToNumString(byte b) {
		int tempB = b;
		if (tempB < 0) {
			tempB = 256 + tempB;
		}

		return String.valueOf(tempB);
	}

	/**MD5加密*/
	public static String MD5Encode(String str) {
		String resultString = null;
		
		try {
			resultString = new String(str);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = MD5Encrypt.byteArrayToString(md.digest(resultString.getBytes()));
		} catch (Exception e) {
//			log.error("MD5Encrypt.MD5Encode,MD5加密失败!", e);
		}
		
		return resultString;
	}
	
	public static void main(String[] args) {
		System.out.println(MD5Encode("dic2012"));
	}
}

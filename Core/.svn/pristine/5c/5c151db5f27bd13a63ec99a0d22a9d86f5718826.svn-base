package com.ustcinfo.mobile.platform.core.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Descry {

    public static final String des_password = "12345678";

    /*
     * 加密
     */
    public static String encrypt(String datasource) {            
        try{
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(des_password.getBytes());
        //创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        //用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        //现在，获取数据并加密
        //正式执行加密操作
        return  byte2hex(cipher.doFinal(datasource.getBytes()));
        }catch(Exception e){
                e.printStackTrace();
        }
        return null;
    }
	
    //解密
    public static String decrypt(String src)   {
    	if(src == null) return null;
    	byte[] b = hex2byte(src);
    	
    	try{
	        // DES算法要求有一个可信任的随机数源
	        SecureRandom random = new SecureRandom();
	        // 创建一个DESKeySpec对象
	        DESKeySpec desKey = new DESKeySpec(des_password.getBytes());
	        // 创建一个密匙工厂
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        // 将DESKeySpec对象转换成SecretKey对象
	        SecretKey securekey = keyFactory.generateSecret(desKey);
	        // Cipher对象实际完成解密操作
	        Cipher cipher = Cipher.getInstance("DES");
	        // 用密匙初始化Cipher对象
	        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
	        // 真正开始解密操作
	        return new String(cipher.doFinal(b));
    	}catch(Exception e){
            e.printStackTrace();
    	}
    	return null;
    }
    
    //转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";

        for (int n=0;n<b.length;n++) {
            stmp=(Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
            else hs=hs+stmp;
            if (n<b.length-1)  hs=hs+":";
        }
        return hs.toUpperCase();
    }
    
    private static byte uniteBytes(String src0, String src1) {  
        byte b0 = Byte.decode("0x" + src0).byteValue();  
        b0 = (byte) (b0 << 4);  
        byte b1 = Byte.decode("0x" + src1).byteValue();  
        byte ret = (byte) (b0 | b1);  
        return ret;  
    }  
    
    // 字符串转换16进制
    public static byte[] hex2byte(String str) {
        if(str == null) return null;
        String[] arr = str.split(":");
        byte[] bb = new  byte[arr.length];
        for (int n=0;n<arr.length;n++) {
        	bb[n] = uniteBytes(arr[n].substring(0,1),  arr[n].substring(1));
        }
        return bb;
    }
    
    public static void main(String args[]){
        //待加密内容
        String str = "{'data':'{'userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595'}','header':{'userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595','userId':'L7442595'}}";
        String result = encrypt(str);
        System.out.println("加密后内容为："+new String(result));

        //直接将如上内容解密
        String decryResult = decrypt(result);
        System.out.println("解密后内容为："+new String(decryResult));
      }
}

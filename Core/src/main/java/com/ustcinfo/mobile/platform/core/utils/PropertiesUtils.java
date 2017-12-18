package com.ustcinfo.mobile.platform.core.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesUtils {

	// 读取properties的全部信息
	public static void readProperties(String src) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(src));
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String value = props.getProperty(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//根据key读取value
	public static String readValue(String src, String key) {
		Properties props = new Properties();
		String value = null ;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(src));
			props.load(in);
			value = props.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value ;
	}
}

/******************************************************************* 
 * Copyright (c) 2015 by USTC SINOVATE SOFTWARE ,Inc. 
 * All rights reserved. 
 * 
 * This file is proprietary and confidential to USTC SINOVATE SOFTWARE. 
 * No part of this file may be reproduced, stored, transmitted, 
 * disclosed or used in any form or by any means other than as 
 * expressly provided by the written permission from the project
 * team of mobile application platform
 * 
 * 
 * Create by SunChao on 2015/04/29
 * Ver1.0
 * 
 * ****************************************************************/
package com.ustcinfo.mobile.platform.core.utils;

import android.os.Environment;

import com.ustcinfo.mobile.platform.core.constants.Constants;


public class SDCardUtils {
	
	
	public static boolean isHavaExternalStorage(){
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	
	public static String getExternalStorageDirectory() throws Exception{
		if(!isHavaExternalStorage())
			throw new Exception("SDcard is not prepared!") ;
		String director = Environment.getExternalStorageDirectory().getAbsolutePath();
		return director;
	}
	
	public static String getImagePath() throws Exception{
		if(!isHavaExternalStorage())
			throw new Exception("SDcard is not prepared!") ;
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ Constants.PATH_DOWNLOAD_IMG;
		return path;
	}
}

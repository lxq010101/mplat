package com.ustcinfo.mobile.platform.core.utils;

import android.graphics.drawable.Drawable;

import com.ustcinfo.mobile.platform.core.core.MApplication;


public class ResourceUtils {

	public static String getString(int id){
		return MApplication.getApplication().getResources().getString(id);
	}
	
	public static int getColor(int id){
		return MApplication.getApplication().getResources().getColor(id);
	}

	public static Drawable getDrawable(int id){
		return MApplication.getApplication().getResources().getDrawable(id);
	}
}

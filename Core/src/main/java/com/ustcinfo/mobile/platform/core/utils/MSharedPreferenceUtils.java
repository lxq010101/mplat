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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ustcinfo.mobile.platform.core.constants.Constants;


public class MSharedPreferenceUtils {


    public static void saveStringSettings(Context context, String key, String value, boolean isOverWrite) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_SETTINGS_NAME, Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (isOverWrite) {
            editor.putString(key, value);
        } else {
            if ("".equals(sp.getString(key, ""))) {
                editor.putString(key, value);
            }
        }
        editor.commit();
    }


    public static void saveIntSettings(Context context, String key, int value, boolean isOverWrite) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_SETTINGS_NAME, Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (isOverWrite) {
            editor.putInt(key, value);
        } else {
            if (sp.getInt(key, 0) == 0) {
                editor.putInt(key, value);
            }
        }
        editor.commit();
    }

    public static void saveBooleanSettings(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_SETTINGS_NAME, Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static void saveIntByPreference(Context context, String namePre, String key, int value, boolean isOverWrite) {
        SharedPreferences sp = context.getSharedPreferences(namePre, Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (isOverWrite) {
            editor.putInt(key, value);
        } else {
            if ("".equals(sp.getString(key, ""))) {
                editor.putInt(key, value);
            }
        }
        editor.commit();
    }

    public static void saveStringByPreference(Context context, String namePre, String key, String value, boolean isOverWrite) {
        SharedPreferences sp = context.getSharedPreferences(namePre, Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (isOverWrite) {
            editor.putString(key, value);
        } else {
            if ("".equals(sp.getString(key, ""))) {
                editor.putString(key, value);
            }
        }
        editor.commit();
    }

    public static void removeStringByPreference(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_SETTINGS_NAME, Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (null != sp.getString(key, "")) {
            editor.remove(key);
        }
        editor.commit();
    }

    public static String queryStringBySettings(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_SETTINGS_NAME, Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static boolean queryBooleanBySettings(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_SETTINGS_NAME, Activity.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }


    public static int queryIntBySettings(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_SETTINGS_NAME, Activity.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    public static boolean queryBooleanByPreference(Context context, String namePre, String key) {
        SharedPreferences sp = context.getSharedPreferences(namePre, Activity.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static String queryStringByPreference(Context context, String namePre, String key) {
        SharedPreferences sp = context.getSharedPreferences(namePre, Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static int queryIntByPreference(Context context, String namePre, String key) {
        SharedPreferences sp = context.getSharedPreferences(namePre, Activity.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }


}

	

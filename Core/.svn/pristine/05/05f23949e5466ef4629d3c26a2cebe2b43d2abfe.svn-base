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
 * version v1.0
 *
 * ****************************************************************/
package com.ustcinfo.mobile.platform.core.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.database.database.DataBaseManager;
import com.database.database.SqliteDatabase;
import com.database.interfaces.SqliteInitICallBack;
import com.ustcinfo.mobile.platform.core.appstore.AppStoreUtils;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.constants.Constants;
import com.ustcinfo.mobile.platform.core.interfaces.MCallbackCoreInit;
import com.ustcinfo.mobile.platform.core.log.Logger;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.AsyncImageLoader;
import com.ustcinfo.mobile.platform.core.utils.FileUtil;
import com.ustcinfo.mobile.platform.core.utils.MSharedPreferenceUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


public class SystemCore {

    public static final String TAG = "SystemCore";

    private static SystemCore mInstance;

    private SqliteDatabase database;

    private List<Activity> activityList = new LinkedList<Activity>();

    private boolean isActivityRunning = false;

    private Context mContext;

    private SystemCore() {

    }

    public static SystemCore get() {
        if (mInstance == null) {
            mInstance = new SystemCore();
        }
        return mInstance;
    }


    public void init(Context context, MCallbackCoreInit callback) {
        mContext = context;
        try {
            if (initDatabase()) {
                if (callback != null)
                    callback.onSuccess();
            } else {
                if (callback != null)
                    callback.onFailed("database init failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
        clearApplistCache();
        clearUserCache();
    }

    //清空缓存中的文件：包含apk和image
    public void clearFileCache() {
        File apkCacheFile = new File(AppStoreUtils.getDownloadPath());
        File imgCacheFile = new File(AsyncImageLoader.get().getImgPath());
        FileUtil.deleteDir(apkCacheFile);
        FileUtil.deleteDir(imgCacheFile);
        Glide.get(mContext).clearMemory();  //清除内存缓存、
        //磁盘缓存
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                Glide.get(mContext).clearDiskCache();
                return null;
            }
        };
    }

    public void clearApplistCache() {
        getDatabase().execSQL("delete from app_list");
    }

    public void clearAppSequence() {
        getDatabase().execSQL("delete from app_sequence");
    }


    public void clearUserCache() {
        getDatabase().execSQL("delete from user");
    }

    private boolean initDatabase() {
        database = DataBaseManager.getInstance().setDBName(Constants.DB_NAME)
                .setDBVersion(Constants.DB_VERSION)
                .setOnInitListener(new SqliteInitICallBack() {

                    @Override
                    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
                    }

                    @Override
                    public void onInitSuccess(SqliteDatabase db) {
                    }

                    @Override
                    public void onInitFailed(String msg) {
                    }

                    @Override
                    public void onCreate(SQLiteDatabase db) {
                        // 创建 应用市场列表
                        StringBuilder appListTableBuilder = new StringBuilder();
                        appListTableBuilder
                                .append("create table if not exists app_list (")
                                .append("app_id TEXT primary key,") //应用id
                                .append("name TEXT ,") //应用名称
                                .append("version TEXT ,") //版本
                                .append("package TEXT ,") //包名
                                .append("type TEXT, ") //应用类型  0.android / 1.ios / 2.web app / 3.url /
                                .append("start_url TEXT ,")  //启动url
                                .append("release_area TEXT ,") //发布区域
                                .append("display TEXT ,") //是否显示出来 0：不显示  1：显示
                                .append("use_type TEXT ,") //应用使用类型：0：工具; 1：应用
                                .append("summary TEXT ,")  //概要说明
                                .append("update_content TEXT, ") //升级内容
                                .append("detail TEXT ,") //详细说明
                                .append("corner_mark TEXT, ") //是否显示角标 0：不显示  1：显示
                                .append("corner_url TEXT, ") //角标请求地址
                                .append("size TEXT, ") //大小
                                .append("automatic_download TEXT, ") // 是否预先自动下载 0：不自动下载 1：自动下载
                                .append("sequence TEXT, ") // 排列顺序
                                .append("parentId TEXT, ") // 关联的父应用ID
                                .append("forceUpdate TEXT, ") // 强制升级标志位
                                .append("release_time TEXT )") //发布时间
                        ;
                        db.execSQL(appListTableBuilder.toString());

                        //创建用户表
                        StringBuilder userTableBuilder = new StringBuilder();
                        userTableBuilder
                                .append("create table if not exists user (")
                                .append("user_id TEXT primary key ,")
                                .append("area_code TEXT ,")
                                .append("password TEXT ,")
                                .append("telephone TEXT ,")
                                .append("ticket TEXT ,")
                                .append("date TEXT)")
                        ;
                        db.execSQL(userTableBuilder.toString());

                        //支持模块拖动排序
                        StringBuilder appOrderByTableBuilder = new StringBuilder();
                        appOrderByTableBuilder
                                .append("create table if not exists app_sequence (")
                                .append("app_id TEXT primary key,")
                                .append("package TEXT ,")
                                .append("number TEXT)")
                        ;
                        db.execSQL(appOrderByTableBuilder.toString());

                    }
                }).initDataBase(mContext).getDatabase(mContext);

        if (database == null) {

            Logger.e(TAG, " datebase init error!!!");
            return false;
        }
        return true;
    }

    public SqliteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            Logger.w(TAG, " system core get database failed ,and init database now");
            initDatabase();
        }
        return database;
    }

    /**
     * activity manager
     *
     * @suggest the function must be called when activity on created
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }


    /**
     * make application exit
     */
    public void exit(Context context) {
        Logger.i(TAG, " application on destory");
        MSharedPreferenceUtils.saveBooleanSettings(context, Constants.KEY_PREFERENCE_AUTO_LOGIN, false);
        SystemCore.get().clearCache();
        UserInfo.get().destory();
        for (Activity activity : activityList) {
            activity.finish();
        }
        try {
            Intent i = null;
            if (MConfig.releaseProvince == Constants.Province.ANHUI) {
                i = new Intent(context, Class.forName(MConfig.getActivity("login")));
            } else if (MConfig.releaseProvince == Constants.Province.GANSU) {
                i = new Intent(context, Class.forName(MConfig.getActivity("login")));
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean isApplicationActivityRunning() {
        return isActivityRunning;
    }


    public void setApplicationActivityRunning(boolean running) {
        this.isActivityRunning = running;
    }
}

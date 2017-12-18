package com.ustcinfo.mobile.platform.application;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.bugly.crashreport.CrashReport;
import com.ustcinfo.mobile.platform.core.core.MApplication;
import com.zxy.recovery.core.Recovery;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 学祺 on 2017/11/3.
 */

public class MplatApplication extends MApplication {

    @Override
    public void onCreate() {
        super.onCreate();


        //极光推送
//        JPushInterface.init(getApplicationContext());
//        JPushInterface.setDebugMode(true);


        /*输出详细的Bugly SDK的Log；
        每一条Crash都会被立即上报；
        自定义日志将会在Logcat中输出。
        建议在测试阶段建议设置成true，发布时设置为false*/
        CrashReport.initCrashReport(getApplicationContext(), "25115ae693", false);

        //错误立马显示
        Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .recoverEnabled(true)
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

package com.ustcinfo.mobile.platform.core.core;

import android.app.Activity;
import android.app.Application;
import android.graphics.Point;
import android.view.Display;

import com.ustcinfo.mobile.platform.ability.application.AbilityApplication;
import com.ustcinfo.mobile.platform.ability.utils.ActivityLifecycleHelper;

/**
 * Created by SunChao on 2017/5/15.
 */

public class MApplication extends AbilityApplication {

    private static MApplication application;

    private int screentWidth;
    private int screenHeight;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SystemCore.get().init(this, null);
        // CrashHandler.getInstance().init(this);//初始化全局异常管理
    }

    public static MApplication getApplication() {
        return application;
    }

    public void init(Activity activity) {
        Display mDisplay = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mDisplay.getSize(size);
        screentWidth = size.x;
        screenHeight = size.y;
    }

    /**
     * @returns screen resolution width * height
     */
    public String getDisplayMetrics() {
        return new StringBuilder().append(screentWidth).append("*").
                append(screenHeight).toString();
    }



}

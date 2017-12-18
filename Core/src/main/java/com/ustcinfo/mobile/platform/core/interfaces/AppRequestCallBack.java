package com.ustcinfo.mobile.platform.core.interfaces;


import com.ustcinfo.mobile.platform.core.model.AppInfo;

import java.util.List;

/**
 * Created by SunChao on 2017/8/10.
 */

public interface AppRequestCallBack {
    void onAppsSuccess(List<AppInfo> list) ;
    void onAppSuccess(AppInfo app) ;
    void onFailed(String msg) ;
}

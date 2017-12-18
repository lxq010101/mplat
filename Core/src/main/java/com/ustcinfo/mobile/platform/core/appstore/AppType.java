package com.ustcinfo.mobile.platform.core.appstore;

/**
 * Created by SunChao on 2017/8/14.
 */
public class AppType {

    public static final int ANDROID = 0 ;

    public static final int IOS = 1 ;

    //离线webApp，将前端开发的h5应用的压缩包解压至sd卡中，进行离线加载
    public static final int WEB_APP = 2 ;

    //在线web界面
    public static final int WEB_INLINE_APP = 3 ;

    //APP安装状态
    public enum StatusType {
        INSTALL,
        UNINSTALL,
        UPGRADE,
    }

    //app发布类型
    public enum releaseType{
        MODULE ,//模块
        TOOLS ,//工具
        OTHERS ,//其它：非模块和工具类型，例如点击一个字符打开其它应用
        ALL
    }
}

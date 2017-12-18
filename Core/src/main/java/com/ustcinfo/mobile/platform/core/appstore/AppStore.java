package com.ustcinfo.mobile.platform.core.appstore;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;


import com.ustcinfo.mobile.platform.core.R;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.core.SystemCore;
import com.ustcinfo.mobile.platform.core.interfaces.AppRequestCallBack;
import com.ustcinfo.mobile.platform.core.interfaces.FileCallBack;
import com.ustcinfo.mobile.platform.core.interfaces.HttpRequestCallbak;
import com.ustcinfo.mobile.platform.core.model.AppInfo;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.MHttpClient;
import com.ustcinfo.mobile.platform.core.utils.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Created by SunChao on 2017/8/14.
 */

public class AppStore {

    private static AppStore mInstance;

    private List<AppInfo> appsCache;

    private AppStore() {
        appsCache = new ArrayList<>();
    }

    public static AppStore get() {
        if (mInstance == null)
            mInstance = new AppStore();
        return mInstance;
    }

    public void init() {
        insertToAppsCache();
    }

    public void init(Activity activity) {
        autoDownloadApps(activity);
    }

    /**
     * 获取所有应用市场中发布的APP应用（注意：不包含父应用中的所有子入口）
     */
    public void getAppsByReleaseType(Activity activity, AppRequestCallBack callBack, AppType.releaseType type) {
        List<AppInfo> apps = new ArrayList<>();
        //先从内存中取数据
        if (appsCache != null && appsCache.size() != 0) {
            for (AppInfo info : appsCache) {
                switch (type) {
                    case ALL:
                        if (TextUtils.equals("-1", info.parentId))
                            apps.add(info);
                        break;
                    case MODULE:
                        //模块类型
                        if (TextUtils.equals("-1", info.parentId) && info.useType == 1)
                            apps.add(info);
                        break;
                    case TOOLS:
                        //工具类型
                        if (TextUtils.equals("-1", info.parentId) && info.useType == 0)
                            apps.add(info);
                        break;
                    case OTHERS:
                        //其它类型
                        if (TextUtils.equals("-1", info.parentId) && info.useType == 2)
                            apps.add(info);
                        break;
                }
            }
            callBack.onAppsSuccess(apps);
        } else {
            //内存中没有数据，从缓存中取
            String sql = null;
            switch (type) {
                case ALL:
                    sql = "select * from app_list where parentId =='-1'";
                    break;
                case MODULE:
                    //模块类型
                    sql = "select * from app_list where use_type = '1' and parentId =='-1'";
                    break;
                case TOOLS:
                    //后台配置在桌面显示，并且是工具类型
                    sql = "select * from app_list where use_type = '0' and parentId =='-1'";
                    break;
                case OTHERS:
                    //后台配置在桌面显示，并且是其它类型
                    sql = "select * from app_list where use_type = '2' and parentId =='-1'";
                    break;
            }
            if (TextUtils.isEmpty(sql)) {
                callBack.onFailed(activity.getString(R.string.make_sure_your_query_type));
                return;
            }
            getAppsFromCache(activity, sql, callBack);
        }
    }

    /**
     * 获取所有入口应用
     * （注意：入口应用也就是可展示的应用，若父应用中包含子入口，则不显示父应用 ，只显示该应用的所有子入口，若父应用中不包含子应用，则显示该父应用）
     */
    public void getAppsEntranceByReleaseType(Activity activity, final AppRequestCallBack callBack, AppType.releaseType type) {
        List<AppInfo> sonApps = new ArrayList<>();
        List<AppInfo> parentApps = new ArrayList<>();

        //先从内存中取数据
        if (appsCache != null && appsCache.size() != 0) {
            for (AppInfo info : appsCache) {
                if (!info.isDesktopDisplay)
                    continue;
                switch (type) {
                    case ALL:
                        //父应用
                        if (TextUtils.equals("-1", info.parentId)) {
                            parentApps.add(info);
                        } else {
                            //所有子入口
                            sonApps.add(info);
                        }
                        break;
                    case MODULE:
                        //模块类型
                        if (info.useType == 1) {
                            if (TextUtils.equals("-1", info.parentId)) {
                                parentApps.add(info);
                            } else if (info.useType == 1) {
                                //所有子入口
                                sonApps.add(info);
                            }
                        }
                        break;
                    case TOOLS:
                        //工具类型
                        if (info.useType == 0) {
                            if (TextUtils.equals("-1", info.parentId)) {
                                parentApps.add(info);
                            } else if (info.useType == 0) {
                                //所有子入口
                                sonApps.add(info);
                            }
                        }
                        break;
                    case OTHERS:
                        //后台配置在桌面显示，并且是其它类型
                        if (info.useType == 2) {
                            if (TextUtils.equals("-1", info.parentId)) {
                                parentApps.add(info);
                            } else if (info.useType == 2) {
                                //所有子入口
                                sonApps.add(info);
                            }
                        }
                        break;
                }
            }
            //把含有子入口的父应用剔除
            List<AppInfo> tempApps = new ArrayList<>();
            for (AppInfo parent : parentApps) {
                boolean isParent = false;
                for (AppInfo son : sonApps) {
                    if (TextUtils.equals(parent.id, son.parentId)) {
                        isParent = true;
                    }
                }
                if (!isParent)
                    tempApps.add(parent);
            }
            tempApps.addAll(sonApps);
            Collections.sort(tempApps);
            callBack.onAppsSuccess(tempApps);
        } else {
            //内存中没有数据，从缓存中取
            String sql = null;
            switch (type) {
                case ALL:
                    sql = "select e.* from (select a.* , c.count from app_list a LEFT OUTER JOIN (select b.parentId , count(*) as count from app_list b where b.parentId <> '-1'  group by b.parentId ) c ON c.parentId = a.app_id) e where e.count is null";
                    break;
                case MODULE:
                    //模块类型
                    sql = "select e.* from (select a.* , c.count from app_list a LEFT OUTER JOIN (select b.parentId , count(*) as count from app_list b where b.parentId <> '-1'  group by b.parentId ) c ON c.parentId = a.app_id) e where e.count is null and display = '1' and use_type = '1' ";
                    break;
                case TOOLS:
                    //工具类型
                    sql = "select e.* from (select a.* , c.count from app_list a LEFT OUTER JOIN (select b.parentId , count(*) as count from app_list b where b.parentId <> '-1'  group by b.parentId ) c ON c.parentId = a.app_id) e where e.count is null and display = '1' and use_type = '0' ";
                    break;
                case OTHERS:
                    //其它类型
                    sql = "select e.* from (select a.* , c.count from app_list a LEFT OUTER JOIN (select b.parentId , count(*) as count from app_list b where b.parentId <> '-1'  group by b.parentId ) c ON c.parentId = a.app_id) e where e.count is null and display = '1' and use_type = '2' ";
                    break;
            }

            getAppsFromCache(activity, sql, new AppRequestCallBack() {
                @Override
                public void onAppsSuccess(List<AppInfo> list) {
                    callBack.onAppsSuccess(list);
                }

                @Override
                public void onAppSuccess(AppInfo app) {
                }

                @Override
                public void onFailed(String msg) {
                }
            });
        }
    }


    private void getAppsFromCache(final Activity activity, final String sql, final AppRequestCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, String>> list = SystemCore.get().getDatabase().find(sql);
                final List<AppInfo> appInfoList = new ArrayList<>();
                for (Map<String, String> map : list) {
                    String id = map.get("app_id");
                    String name = map.get("name");
                    String version = map.get("version");
                    String packageName = map.get("package");
                    int type = Integer.valueOf(map.get("type"));
                    String startUrl = map.get("start_url");
                    String releaseArea = map.get("release_area");
                    boolean isDesktopDisplay = Integer.valueOf(map.get("isDesktopShow") == null ? "2" : map.get("isDesktopShow")) == 1 ? true : false;
                    boolean autoDownload = Integer.valueOf(map.get("isBeforeAutoDown") == null ? "2" : map.get("isBeforeAutoDown")) == 1 ? true : false;
                    String summay = map.get("summary");
                    String updateContent = map.get("update_content");
                    String detail = map.get("detail");
                    int isShowCornerMark = Integer.valueOf(map.get("corner_mark"));
                    String cornerUrl = map.get("corner_url");
                    String size = map.get("size");
                    String releaseTime = map.get("release_time");
                    String parentId = map.get("parentId");
                    int order = Integer.valueOf(map.get("sequence"));
                    int useType = Integer.valueOf(map.get("use_type"));
                    int forceUpdate = Integer.valueOf(map.get("forceUpdate"));

                    AppInfo info = new AppInfo(id, name, version, packageName, type, startUrl, releaseArea, isDesktopDisplay, summay, updateContent, detail, isShowCornerMark,
                            size, autoDownload, releaseTime, order, cornerUrl, useType, parentId, forceUpdate);
                    appInfoList.add(info);
                }
                Collections.sort(appInfoList);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onAppsSuccess(appInfoList);
                    }
                });
            }
        }).start();
    }

    /**
     * 根据应用名称查应用的相关信息
     */
    public void getAppByName(String name, AppRequestCallBack callBack) {
        String sql = "select * from app_list where name= ?";
        Map<String, String> map = SystemCore.get().getDatabase().findOne(sql, new String[]{name});
        if (map.size() == 0) {
            callBack.onFailed(null);
            return;
        }

        String id = map.get("app_id");
        String version = map.get("version");
        String packageName = map.get("package");
        int type = Integer.valueOf(map.get("type"));
        String startUrl = map.get("start_url");
        String releaseArea = map.get("release_area");
        boolean isDesktopDisplay = Integer.valueOf(map.get("display")) == 1 ? true : false;
        boolean autoDownload = Integer.valueOf(map.get("automatic_download")) == 1 ? true : false;
        String summay = map.get("summary");
        String updateContent = map.get("update_content");
        String detail = map.get("detail");
        int isShowCornerMark = Integer.valueOf(map.get("corner_mark"));
        String cornerUrl = map.get("corner_url");
        String size = map.get("size");
        String releaseTime = map.get("release_time");
        String parentId = map.get("parentId");
        int order = Integer.valueOf(map.get("sequence"));
        int useType = Integer.valueOf(map.get("use_type"));
        int forceUpdate = Integer.valueOf(map.get("forceUpdate"));

        AppInfo info = new AppInfo(id, name, version, packageName, type, startUrl, releaseArea, isDesktopDisplay, summay, updateContent, detail, isShowCornerMark,
                size, autoDownload, releaseTime, order, cornerUrl, useType, parentId, forceUpdate);
        callBack.onAppSuccess(info);
    }

    /**
     * 从服务器中获取应用列表
     *
     * @param useCache 使用缓存中的数据，不会请求
     */
    public void getApps(final AppRequestCallBack callBack, boolean useCache) {
        //从内存中查找应用列表
        if (useCache && appsCache.size() > 0) {
            callBack.onAppsSuccess(appsCache);
            return;
        }
        //从网络中获取数据
        RequestParams p = new RequestParams();
        p.put("clientType", "1");
        p.put("city", UserInfo.get().getCityCode());
        MHttpClient.get().post(MConfig.get("getAppList"), p, new HttpRequestCallbak() {
            @Override
            public void onSuccess(JSONObject responseObj) {
                SystemCore.get().clearApplistCache();
                callBack.onAppsSuccess(appsJson2List(responseObj));
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFailed(msg);
            }
        });
    }

    /**
     * 处理返回的应用列表,并存储在数据库表中
     */
    public List<AppInfo> appsJson2List(JSONObject responseObj) {
        try {
            JSONObject dataObj = responseObj.getJSONObject("data");
            JSONArray apps = dataObj.getJSONArray("appList");
            appsCache.clear();
            for (int i = 0; i < apps.length(); i++) {
                JSONObject appInfoObj = apps.getJSONObject(i);
                String id = appInfoObj.getString("appId");
                String name = appInfoObj.getString("appName");
                String version;
                try {
                    version = appInfoObj.getString("appVersion") == null ? "" : appInfoObj.getString("appVersion");
                } catch (Exception e) {
                    version = "";
                }
                String pkg;
                try {
                    pkg = appInfoObj.getString("packageName") == null ? "" : appInfoObj.getString("packageName");
                } catch (Exception e) {
                    pkg = "";
                }

                String parentId = appInfoObj.getString("parentId");
                String releaseTime = appInfoObj.getString("publishTime");

                boolean isDesktopDisplay = Integer.valueOf(appInfoObj.getString("isDesktopShow")) == 1 ? true : false;
                boolean autoDownload = Integer.valueOf(appInfoObj.getString("isBeforeAutoDown")) == 1 ? true : false;
                int order = Integer.valueOf(appInfoObj.getString("appOrder"));
                int useType = Integer.valueOf(appInfoObj.getString("appUseType"));
                int isCornerMark = Integer.valueOf(appInfoObj.getString("isShowCornerMark"));
                int updateFlag = Integer.valueOf(appInfoObj.getString("updateFlag"));
                int type = -1;
                String startUrl = null;
                switch (appInfoObj.getString("appType")) {

                    case "apk":
                        type = 0;
                        try {
                            startUrl = appInfoObj.getString("startUrl");
                        } catch (Exception e) {
                        }
                        break;
                    case "zip":
                        type = 2;
                        try {
                            startUrl = appInfoObj.getString("startUrl");
                        } catch (Exception e) {
                        }
                        break;
                    case "url":
                        type = 3;
                        try {
                            startUrl = appInfoObj.getString("appLink");
                        } catch (Exception e) {
                        }
                        break;
                }
                //以下变量若后台没有配置可能会存在获取不到的现象
                String areaStr = null;
                try {
                    areaStr = appInfoObj.getString("publishArea");
                } catch (Exception e) {
                }



                String summary = null;
                try {
                    summary = appInfoObj.getString("appResume");
                } catch (Exception e) {
                }

                String detail = null;
                try {
                    detail = appInfoObj.getString("appIntroduction");
                } catch (Exception e) {
                }

                String updateContent = null;
                try {
                    updateContent = appInfoObj.getString("upgradeContent");
                } catch (Exception e) {
                }

                String size = null;
                try {
                    size = appInfoObj.getString("appSize");
                } catch (Exception e) {
                }

                String cornerMarkUrl = null;
                try {
                    cornerMarkUrl = appInfoObj.getString("cornerMarkUrl");
                } catch (Exception e) {
                }

                AppInfo info = new AppInfo(id, name, version, pkg, type, startUrl, areaStr, isDesktopDisplay
                        , summary, updateContent, detail, isCornerMark, size, autoDownload, releaseTime, order, cornerMarkUrl, useType, parentId, updateFlag);
                appsCache.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        insertToAppsCache();
        return appsCache;
    }

    //将应用信息插入缓存
    public void insertToAppsCache() {
        for (AppInfo info : appsCache) {
            info.save();
        }
    }

    //检测需要自动下载的程序
    public void autoDownloadApps(final Context context) {
        for (final AppInfo info : appsCache) {
            if (info.autoDownload) {
                MHttpClient.get().getFileByAppInfo(info, new FileCallBack() {
                    @Override
                    public void onResposne(File file) {
                        AppStoreUtils.installedAppByType(context, null, file, info.type);
                    }

                    @Override
                    public void inProgress(int progress) {
                    }

                    @Override
                    public void onError(String msg) {
                    }
                });
            }
        }
    }
}

package com.ustcinfo.mobile.platform.core.appstore;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.sdsmdg.tastytoast.TastyToast;
import com.ustcinfo.mobile.platform.ability.activity.common.BaseActivity;
import com.ustcinfo.mobile.platform.core.R;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.constants.Constants;
import com.ustcinfo.mobile.platform.core.core.MApplication;
import com.ustcinfo.mobile.platform.core.core.MLogin;
import com.ustcinfo.mobile.platform.core.core.SystemCore;
import com.ustcinfo.mobile.platform.core.interfaces.HttpRequestCallbak;
import com.ustcinfo.mobile.platform.core.interfaces.InstallFailedCallBack;
import com.ustcinfo.mobile.platform.core.log.Logger;
import com.ustcinfo.mobile.platform.core.model.AppInfo;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.Key64;
import com.ustcinfo.mobile.platform.core.utils.MHttpClient;
import com.ustcinfo.mobile.platform.core.utils.MSharedPreferenceUtils;
import com.ustcinfo.mobile.platform.core.utils.NativeAppUtils;
import com.ustcinfo.mobile.platform.core.utils.RSAUtils;
import com.ustcinfo.mobile.platform.core.utils.RequestParams;
import com.ustcinfo.mobile.platform.core.utils.SDCardUtils;
import com.ustcinfo.mobile.platform.core.utils.WebAppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class AppStoreUtils {

    /**
     * 检测应用是否已经发布到应用市场
     */
    public static boolean isAppPublicInStore(String pkg) {
        String sql = "SELECT * FROM app_list WHERE package = " + "'" + pkg + "'";
        if (SystemCore.get().getDatabase().find(sql).size() > 0)
            return true;
        return false;
    }

    /**
     * 获取应用的下载地址
     */
    public static String getDownloadPath() {
        String path = null;
        if (SDCardUtils.isHavaExternalStorage()) {
            path = Environment.getExternalStorageDirectory().toString() + Constants.PATH_DOWNLOAD_APK;
            File f = new File(path);
            if (!f.exists())
                f.mkdirs();
        } else {
            path = MApplication.getApplication().getCacheDir().getAbsolutePath();
        }
        return path;
    }

    /**
     * 获取web app解压路径
     */
    public static final String getWebAppExtractPath() {
        String path = null;
        if (SDCardUtils.isHavaExternalStorage()) {
            path = Environment.getExternalStorageDirectory().toString() + Constants.PATH_WEB_CACHE;
            File f = new File(path);
            if (!f.exists())
                f.mkdirs();
        } else {
            path = MApplication.getApplication().getCacheDir().getAbsolutePath();
        }
        return path;
    }

    /**
     * 获取应用下载到本地的存放名称
     */
    public static String getFileNameByAppInfo(AppInfo info) {
        String extension = null;
        switch (info.type) {
            case 0:
                extension = ".apk";
                break;
            case 2:
                extension = ".zip";
                break;
        }
        return new StringBuilder().append(info.packageName)
                .append("-").append(info.version).append(extension).toString();
    }


    /**
     * 根据应用类型安装应用
     * 原生应用打开安装界面
     * Web应用解压至下载目录
     */
    public static void installedAppByType(Context activity, InstallFailedCallBack callback, File file, int type) {
        Logger.d("AppStoreUtils", "install app ,name:" + file.getName() + " ,type" + type);
        if (type == 0) {
            NativeAppUtils.installApp(activity, file, 2);
        } else if (type == 2) {
            try {
                WebAppUtils.installApp(file);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("AppStoreUtils", e.getMessage());
                if (callback != null)
                    callback.onFailed(e.getMessage());
            }
        }
    }

    public static void launchApp(final Activity activity, final AppInfo info) {
            switch (info.type) {
                case 0:
                    try {

                        NativeAppUtils.launch(activity, info);
                    }catch (ActivityNotFoundException ex){
                        TastyToast.makeText(activity,"没有找到该页面",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                    break;
                case 2:
                case 3:
                    WebAppUtils.launch(activity, info);
                    break;
        }
    }

    public static void launchAppByInfo(final Activity activity, final AppInfo info) {
        if (info.packageName.equals("com.ict.liaction.check") || info.packageName.equals("cn.com.starit.ess.mobile.view.labelprint") || info.packageName.equals("com.kdgc.bluetoothprinter")) {
            Log.e("TAG", info.packageName);
            switch (info.type) {
                case 0:
                    NativeAppUtils.launch(activity, info);
                    break;
                case 2:
                case 3:
                    WebAppUtils.launch(activity, info);
                    break;
            }
        } else {
            RequestParams requestParams = new RequestParams();
            ((BaseActivity) activity).showProgressBar();
            try {
                requestParams.put("packageName", info.packageName);
                requestParams.put("orgUuid", com.alibaba.fastjson.JSONObject.parseObject(MSharedPreferenceUtils.queryStringBySettings(activity, "access_token")).getJSONObject("user").getString("orgUuid"));
                requestParams.put("access_token", RSAUtils.encryptByPrivateKeyStr(MSharedPreferenceUtils.queryStringBySettings(activity, "access_token"), Constants.EOS_PRIVITE_KEY));
                Log.d("TAG",RSAUtils.encryptByPrivateKeyStr(MSharedPreferenceUtils.queryStringBySettings(activity, "access_token"), Constants.EOS_PRIVITE_KEY));
            } catch (Exception e) {
                e.printStackTrace();
            }

            MHttpClient.get().post2(MConfig.get("getAuthCode"), requestParams, new HttpRequestCallbak() {
                @Override
                public void onSuccess(JSONObject responseObj) {
                    ((BaseActivity) activity).closeProgressBar();
                    try {
                        if (responseObj.get("code").equals("0")) {
                            switch (info.type) {
                                case 0:
                                    NativeAppUtils.launch(activity, info, responseObj.getJSONObject("data").getString("code"), responseObj.getJSONObject("data").getString("redirectUri"));
                                    break;
                                case 2:
                                case 3:
                                    WebAppUtils.launch(activity, info, responseObj.getJSONObject("data").getString("code"), responseObj.getJSONObject("data").getString("redirectUri"));
                                    break;
                            }
                        } else {
                            TastyToast.makeText(activity, "登陆过期,请重新登陆", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            Intent intent = new Intent(activity, Class.forName(MConfig.getActivity("login")));
                            activity.startActivity(intent);
                            SystemCore.get().exit(activity);
                            activity.finish();
                        }
                    } catch (com.alibaba.fastjson.JSONException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(String msg) {
                    ((BaseActivity) activity).closeProgressBar();
                    TastyToast.makeText(activity, "打开失败", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            });
        }
    }


    public static void uninstallAppByInfo(Context context, AppInfo info) {
        switch (info.type) {
            case AppType.ANDROID:
                NativeAppUtils.uninstall(context, info);
                break;
            case AppType.WEB_APP:
                WebAppUtils.uninstall(info);
                break;
            case AppType.WEB_INLINE_APP:
                Toast.makeText(context, context.getString(R.string.can_not_uninstall_url_app), Toast.LENGTH_LONG).show();
            default:
                Toast.makeText(context, context.getString(R.string.can_not_uninstall_url_app), Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * 检测应用是否安装
     *
     * @warn 原生应用根据包名检测是否安装
     * WebApp 检测SD卡中是否具有文件目录
     * 在线配置的应用（url）返回值永远为true
     * 本地工程代码配置的应用（local）返回值永远为true
     */
    public static boolean isAppInstalled(Context context, AppInfo info) {
        if (info.type == 0) {
            //原生应用
            return NativeAppUtils.isInstalled(context, info.packageName);
        } else if (info.type == 2) {
            //web App 查看存储路径是否有安装包
            return WebAppUtils.isAppInstalled(info);
        } else if (info.type == 3 || info.type == 4) {
            //url && local  默认都是已安装
            return true;
        }
        return false;
    }


    public static boolean checkUpgrade(Context context, AppInfo info) {
        String localVer = null;
        if (info.type == 0) {
            //原生应用
            localVer = NativeAppUtils.getVersion(context, info.packageName);
        } else if (info.type == 2) {
            //web App 查看存储路径是否有安装包
            localVer = WebAppUtils.getVersion(info);
        } else if (info.type == 3 || info.type == 4) {
            //url && local  默认都是已安装
            return false;
        }
        if (TextUtils.isEmpty(localVer) || !TextUtils.equals(localVer, info.version)) {
            return true;
        }
        return false;
    }

}

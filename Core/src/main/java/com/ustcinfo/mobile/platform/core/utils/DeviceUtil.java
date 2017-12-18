package com.ustcinfo.mobile.platform.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

public class DeviceUtil {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final int seconds_of_1minute = 60;
    private static final int seconds_of_30minutes = 30 * 60;
    private static final int seconds_of_1hour = 60 * 60;
    private static final int seconds_of_1day = 24 * 60 * 60;
    private static final int seconds_of_15days = seconds_of_1day * 15;
    private static final int seconds_of_30days = seconds_of_1day * 30;
    private static final int seconds_of_6months = seconds_of_30days * 6;
    private static final int seconds_of_1year = seconds_of_30days * 12;

    /**
     * 获取设备号
     *
     * @param context
     * @return
     */
    public static String getSnNumber(Context context) {
        String imei;
        try {
            TelephonyManager mTelephonyMgr;
            mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = mTelephonyMgr.getDeviceId();
        } catch (Exception e) {
            return "";
        }
        return imei;
    }

    /**
     * 获取Android_ID
     *
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * 返回屏幕宽高 int[宽，高]
     *
     * @param activity
     */
    public static int[] getScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        return new int[]{screenWidth, screenHeigh};
    }

    /**
     * 隐藏软键盘
     *
     * @param v
     */
    public static void hideSoftInput(View v, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 打开软键盘
     *
     * @param context
     */
    public static void showSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 自动获取焦点并弹出软键盘
     *
     * @param v
     * @param context
     */
    public static void autoFocusShowSoftInput(View v, final Context context) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                showSoftInput(context);
            }
        }, 830);
    }

    /**
     * 判断是否有网络连接
     *
     * @param
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFriendlyDate(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.setTimeZone(TimeZone.getDefault());
        long interval = new Date().getTime() - date.getTime();
        long sec = interval / 1000;
        String str = "";

        if (sec < 60) {
            str = "刚刚";
        } else if (sec < 60 * 60) {
            long min = sec / 60;
            str = min + "分钟前";
        } else if (sec < 24 * 60 * 60) {
            long hour = sec / (60 * 60);
            str = hour + "小时前";
        } else {
            long day = sec / (24 * 60 * 60);
            str = day + "天前";
        }

        return str;
    }

    /**
     * @return timtPoint距离现在经过的时间，分为
     * 刚刚，1-29分钟前，半小时前，1-23小时前，1-14天前，半个月前，1-5个月前，半年前，1-xxx年前
     */
    public static String getTimeElapse(long createTime) {
        long nowTime = new Date().getTime() / 1000;
        //createTime是发表文章的时间
        long oldTime = createTime / 1000;
        //elapsedTime是发表和现在的间隔时间
        long elapsedTime = nowTime - oldTime;
        if (elapsedTime < seconds_of_1minute) {
            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {
            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {
            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {
            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime < seconds_of_15days) {
            return elapsedTime / seconds_of_1day + "天前";
        }
        if (elapsedTime < seconds_of_30days) {
            return "半个月前";
        }
        if (elapsedTime < seconds_of_6months) {
            return elapsedTime / seconds_of_30days + "月前";
        }
        if (elapsedTime < seconds_of_1year) {
            return "半年前";
        }
        if (elapsedTime >= seconds_of_1year) {
            return elapsedTime / seconds_of_1year + "年前";
        }
        return "";
    }


    public static Date parseDate(String dateStr, String formatStr) {
        if (dateStr == null) {
            return null;
        }

        SimpleDateFormat formate = new SimpleDateFormat(formatStr, Locale.CHINA);
        Date date = null;

        try {
            date = formate.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String formatDate(Date date, String formatStr) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat formate = new SimpleDateFormat(formatStr, Locale.CHINA);
        return formate.format(date);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
       // final float scale = context.getResources().getDisplayMetrics().density;
       // return (int) (dpValue * scale + 0.5f);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }



    public static String getIp(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return getLocalIpAddress();
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return getIpForWife(context);
            }
        } else {
            Toast.makeText(context, "当前没有网络连接！请确认！", Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    public static String getIpForWife(Context context) {

        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }


    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

}

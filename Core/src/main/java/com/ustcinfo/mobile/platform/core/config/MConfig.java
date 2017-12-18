package com.ustcinfo.mobile.platform.core.config;

import android.content.Context;
import android.os.Environment;


import com.ustcinfo.mobile.platform.core.BuildConfig;
import com.ustcinfo.mobile.platform.core.constants.Constants;
import com.ustcinfo.mobile.platform.core.log.Logger;
import com.ustcinfo.mobile.platform.core.utils.XmlUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MConfig {

    private static Map<String, String> urlMap;

    private static Map<String, String> activityMap;
    /**
     * 设置发布省份
     */
    public static Constants.Province releaseProvince = Constants.Province.ANHUI;

    /**
     * 日志开关
     */
    public static final Boolean DEBUGABLE = true;

    /**
     * 日志名称
     */
    public static final String LOG_FILE_NAME = "log.txt";

    /**
     * 日志文件夹
     */
    public static final String LOG_FILE_FOLDER = "mplat";

    /**
     * 日志路径
     */
    public static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + LOG_FILE_FOLDER + File.separator + LOG_FILE_NAME;

    /**
     * 日志是否写在SD卡
     */
    public static final Boolean LOG_2_SDCARD = true;

    /**
     * 日志文件大小
     */
    public static final long LOG_FIZE_SIZE = 5 * 1024 * 1024;

    public static void init(Context context) throws Exception {
        initUrl(context);
    }

    private static void initUrl(Context context) throws Exception {
        urlMap = new HashMap<>();
        activityMap = new HashMap<>();
        InputStream is = context.getAssets().open("url.xml");
        Document doc = XmlUtil.parseXml(is);
        NodeList nl = doc.getElementsByTagName("config");
        String baseUrl = doc.getElementById("base_url").getTextContent();
        Element tn = null;
        String key = null;
        for (int i = 0; i < nl.getLength(); i++) {
            tn = (Element) nl.item(i);
            key = tn.getAttribute("url");
            if (tn.getNodeType() == Node.ELEMENT_NODE) {
                String value = null;
                if (tn.hasChildNodes()) {
                    value = tn.getFirstChild().getNodeValue();
                    Logger.e("MConfig", "platform url config ,name:" + key + "->" + value);
                    value = baseUrl.trim() + value;
                    urlMap.put(key, value);
                }
            }
        }

        is = context.getAssets().open("activity.xml");
        doc = XmlUtil.parseXml(is);
        nl = doc.getElementsByTagName("config");
        tn = null;
        key = null;
        for (int i = 0; i < nl.getLength(); i++) {
            tn = (Element) nl.item(i);
            key = tn.getAttribute("activity");
            if (tn.getNodeType() == Node.ELEMENT_NODE) {
                String value = null;
                if (tn.hasChildNodes()) {
                    value = tn.getFirstChild().getNodeValue();
                    Logger.e("MConfig", "platform activity config ,name:" + key + "->" + value);
                    activityMap.put(key, value);
                }
            }
        }
    }

    public static String get(String key) {
        if (urlMap == null) {
            Logger.e("MConfig", "platform has not initialized ,please init right now...");
            return null;
        }
        return urlMap.get(key);
    }


    public static String getActivity(String key) {
        if (activityMap == null) {
            Logger.e("MConfig", "platform has not initialized ,please init right now...");
            return null;
        }
        return activityMap.get(key);
    }

}

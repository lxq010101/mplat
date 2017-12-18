package com.ustcinfo.mobile.platform.core.log;


import android.os.Environment;
import android.util.Log;

import com.ustcinfo.mobile.platform.core.config.MConfig;

import org.apache.log4j.Level;

import de.mindpipe.android.logging.log4j.LogConfigurator;


public class Logger {

    private static final String TAG = "Logger";

    private static final String PREFIX = "Mplat--->";

    public static void init() {
        LogConfigurator logConfigurator = new LogConfigurator();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            logConfigurator.setFileName(MConfig.LOG_FILE_PATH);
            Log.i(TAG, "log path :" + MConfig.LOG_FILE_PATH);
        } else {
            Log.e(TAG, "no sdcard !!! log file create failed.");
        }

        logConfigurator.setRootLevel(Level.ALL);
        logConfigurator.setLevel("org.apache", Level.ALL);
        logConfigurator.setMaxFileSize(MConfig.LOG_FIZE_SIZE);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
    }

    public static void i(String tag, String msg) {
        msg = PREFIX + msg;
        log2File(tag, msg);
        if (!MConfig.DEBUGABLE)
            return;
        Log.i(TAG, msg);
    }

    public static void d(String tag, String msg) {
        msg = PREFIX + msg;
        log2File(tag, msg);
        if (!MConfig.DEBUGABLE)
            return;
        Log.d(TAG, msg);
    }

    public static void w(String tag, String msg) {
        msg = PREFIX + msg;
        log2File(tag, msg);
        if (!MConfig.DEBUGABLE)
            return;
        Log.w(TAG, msg);
    }

    public static void e(String tag, String msg) {
        msg = PREFIX + msg;
        log2File(tag, msg);
        if (!MConfig.DEBUGABLE)
            return;
        Log.e(tag, msg);
    }

    private static void log2File(String tag, String msg) {
        if (MConfig.LOG_2_SDCARD) {
            org.apache.log4j.Logger.getLogger(tag).trace(msg);
        }
    }
}

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
package com.ustcinfo.mobile.platform.core.constants;

public class Constants {

    //枚举：发布省份
    public enum Province {
        ANHUI,
        GANSU,
        ALL
    }

    // 数据库名称
    public static final String DB_NAME = "mplat.db";

    // 数据库版本
    public static final int DB_VERSION = 1;

    // 设置文件 preference 名称
    public static final String SHARED_PREFERENCE_SETTINGS_NAME = "settings";

    // 设置文件 preference 自动登录
    public static final String KEY_PREFERENCE_AUTO_LOGIN = "autoLogin";

    // 设置文件 preference 切换账号登陆
    public static final String KEY_PREFERENCE_CHANGE_ACCOUNT = "account";

    // 设置文件 preference 自动登录
    public static final String KEY_PREFERENCE_LOGIN_TIME = "time";

    // 设置UserId
    public static final String KEY_PREFERENCE_LOGIN_USERID = "userId";

    // 设置AreaId
    public static final String KEY_PREFERENCE_LOGIN_AREAID = "areaId";

    /**
     * 安装包存储路径
     */
    public static final String PATH_DOWNLOAD_APK = "/mplat/apk/";

    public static final String PATH_DOWNLOAD_IMG = "/mplat/image/";

    /**
     * web app 解压路径
     */
    public static final String PATH_WEB_CACHE = "/mplat/webCache/";

//    public static final String

    public static final String EOS_PRIVITE_KEY ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMxGAMHPrQ2LyWrkbpYRzEtpEKmZ20+fGjiGKdpkAxqTEH2cNl3EBE3WO/PxN7bvnHlKqq7CCvwK8fHThOnKC4V/wRQHDELfpa+zJgp4rMAbTesjG8NKy3lumU88CPrQRbyzgMtAi5jienzXXRK5+oR1b1tDoa5AZ8Ed2nPq6+OXAgMBAAECgYBUuI/l0ilW3ewavkUzToxplljHzr0Lx9QLL3G6VxKGegoz3o8Z0otqcPkKkrfl0nkWQSaLNoVxxWRVRMHyaM7fv/SckbD+tyDo/0J0eMZWEcCm6VH+o2RrHqj9yhY+J6Z65wI8HevAxXv/R6fGw3DIO4DD9e4LCxwWNlJWABwIoQJBAPtURZxiglsWrhHm5PzL0k2nTfBe/9juJHfXRWFo1sHh7yweFjV5qjg3oArdD9LDUW95oPqsqtOrfLu4Z4wh8zECQQDQEdub4MeSK/FrV1ql9iszXx9g+Q9hmTuBYEgBQmEDQsR7jgoxadFX8nEEwVoTzAMg2WlpnEYkkiLGW5w2YkFHAkBXuFAbhx5sYHwc73PY7+LVC6HeVaoCswuzcEVc/FSIky0BPvcNbwuEV5XadNHDBDz2JYOUOpPdESuV8YTimi7hAkEAglsdzjv2bJKRTuHMjft7N1UaEKQZSdk5maWblwZiRyu2c34azrhwCnx+6C7G425Ga3cqfpsEqvbPSJyucrf+EwJAMLK6wzwdwpGVVWmejNCVo0RYQXkL+U1Xp9hDJoYu1QTUmiDCZ+pbhoYYNuRCzqI9oHv8ehFM7yApmA0HDPVHaA==";
}

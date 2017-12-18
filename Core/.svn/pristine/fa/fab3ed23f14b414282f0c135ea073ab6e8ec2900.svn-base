package com.ustcinfo.mobile.platform.core.plugin;

import android.text.TextUtils;


import com.ustcinfo.mobile.platform.core.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SunChao on 2017/5/25.
 */

public class RemoteInterAdapter {

    public static final String AUTH_ACCESS_TOKEN = "access_token";

    public static String invoke(String optr, String jsonStr) {
        JSONObject obj = new JSONObject();
        try {
            if (TextUtils.equals(optr, AUTH_ACCESS_TOKEN)) {
                obj.put("userId", UserInfo.get().getUserId());
                obj.put("areaId", UserInfo.get().getAreaCode());
                obj.put("telephoneNumber", UserInfo.get().getTelephoneNumber());
                obj.put("password", UserInfo.get().getPasswordCache());
                return ResultFactory.createSuccess(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ResultFactory.createFailed(ResultFactory.CODE_FAILED_UNKONW_OPERATE);
    }
}

package com.ustcinfo.mobile.platform.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;


import com.ustcinfo.mobile.platform.core.appstore.AppStoreUtils;
import com.ustcinfo.mobile.platform.core.log.Logger;
import com.ustcinfo.mobile.platform.core.plugin.RemoteInterAdapter;
import com.ustcinfo.mobile.platform.core.plugin.ResultFactory;
import com.ustcinfo.mobile.platform.service.aidl.PluginService;

import org.json.JSONException;
import org.json.JSONObject;

public class PluginServiceImpl extends Service {

    //第三方APP在进行平台跨应用请求时，请求的参数：SECURITY_KEY需要跟本地保持有一致，
    // 防止别人窃取aidl文件后，能直接请求平台数据
    private static final String SECURITY_KEY ="798798" ;

    private PluginServiceBinder sBinder = new PluginServiceBinder() ;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return sBinder;
    }
    class PluginServiceBinder extends PluginService.Stub{

        @Override
        public String invoke(String operatorType, String jsonString) throws RemoteException {
            Logger.i("PluginServiceImp","optType: "+ operatorType+" ,Json:"+jsonString);
            String result = null ;
            try {
                JSONObject obj = new JSONObject(jsonString) ;
                String key = obj.getString("SECURITY_KEY");
                String pkg = obj.getString("package") ;
                //安全密匙校验
                if(!TextUtils.equals(key ,SECURITY_KEY)){
                    return ResultFactory.createFailed(ResultFactory.CODE_FAILED_SECURITY_KEY) ;
                }
                //是否在应用市场中发布
                if(!AppStoreUtils.isAppPublicInStore(pkg)){
                    return ResultFactory.createFailed(ResultFactory.CODE_FAILED_NOT_PUBLISH_ON_STORE) ;
                }
                return RemoteInterAdapter.invoke(operatorType,jsonString) ;
            } catch (JSONException e) {
                e.printStackTrace();
                result = ResultFactory.createFailed(ResultFactory.CODE_FAILED_JSON_PARSE) ;
            }
            return result;
        }
    }
}

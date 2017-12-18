package com.ustcinfo.mobile.platform.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.ustcinfo.mobile.platform.core.log.Logger;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by 学祺 on 2017/9/26.
 */

public class RequestParams extends com.loopj.android.http.RequestParams {


    public RequestParams getBodyContent(){

        JSONObject json = new JSONObject();
        Set<String> s = this.urlParams.keySet();
        Iterator<String> it = s.iterator();
        while(it.hasNext()){
            String key = it.next();
            json.put(key,urlParams.get(key));
        }
        urlParams.clear();
        urlParams.put("data", Key64.encrypt(json.toJSONString()));
        Logger.d("RequestParams",urlParams.toString());
        return this;
    }

}

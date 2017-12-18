package com.ustcinfo.mobile.platform.core.utils;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.ustcinfo.mobile.platform.core.R;
import com.ustcinfo.mobile.platform.core.appstore.AppStoreUtils;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.constants.Constants;
import com.ustcinfo.mobile.platform.core.core.MApplication;
import com.ustcinfo.mobile.platform.core.core.SystemCore;
import com.ustcinfo.mobile.platform.core.interfaces.FileCallBack;
import com.ustcinfo.mobile.platform.core.interfaces.HttpRequestCallbak;
import com.ustcinfo.mobile.platform.core.log.Logger;
import com.ustcinfo.mobile.platform.core.model.AppInfo;
import com.ustcinfo.mobile.platform.core.model.UserInfo;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class MHttpClient {
	
	public static final String TAG = "MHttpClient";

	private static MHttpClient mInstance ;
	
	private AsyncHttpClient client ;

	private  MHttpClient (){
		client = new AsyncHttpClient();
		client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
		client.setTimeout(30*1000);
	}

	public static  MHttpClient get(){
		if(mInstance == null){
			mInstance = new MHttpClient();
		}
		return mInstance;
	}
	public void get(String url , HttpRequestCallbak callback){
		final HttpRequestCallbak httpCallBack = callback ;
		Logger.d(TAG, "get:"+url);
		if(TextUtils.isEmpty(url))
			return ;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] headers, byte[] arg2, Throwable arg3) {
				Logger.e(TAG, "connect error,server connected failed:"+arg3.getMessage());
				if(httpCallBack != null)
					httpCallBack.onFailed(ResourceUtils.getString(R.string.error_contact_server));
			}

			@Override
			public void onSuccess(int statusCode, Header[] arg1, byte[] resByte) {
				try {
					String rsp = new String (resByte,"UTF-8");
					handleResponse(rsp, httpCallBack);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void postWithoutEncript(String url , RequestParams params, HttpRequestCallbak callback){
		final HttpRequestCallbak httpCallBack = callback ;
		params.put("ticket", UserInfo.get().getTicketCache());
		if(TextUtils.isEmpty(url))
			return ;
		boolean isAccountChanged = MSharedPreferenceUtils.queryBooleanBySettings(MApplication.getApplication() , Constants.KEY_PREFERENCE_CHANGE_ACCOUNT) ;
		String userId = UserInfo.get().getUserId() ;
		if(isAccountChanged)
			userId = UserInfo.get().getUserIdCache() ;
		if(!params.has("userId"))
			params.put("userId", userId);
		Logger.d(TAG, "post:"+url+"?"+params);
		client.post(url, params ,new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] headers, byte[] arg2,Throwable arg3) {
				Logger.e(TAG, "connect error,server connected failed:"+arg3.getMessage());
				if(httpCallBack != null)
					httpCallBack.onFailed(ResourceUtils.getString(R.string.error_contact_server));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] resByte) {
				try {
					String rsp = new String (resByte,"UTF-8");
					handleResponse(rsp, httpCallBack);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

		});
	}
	
	public void post(String url , RequestParams params, HttpRequestCallbak callback){
		final HttpRequestCallbak httpCallBack = callback ;
		params.put("ticket", UserInfo.get().getTicketCache());
		if(TextUtils.isEmpty(url))
			return ;
		boolean isAccountChanged = MSharedPreferenceUtils.queryBooleanBySettings(MApplication.getApplication() , Constants.KEY_PREFERENCE_CHANGE_ACCOUNT) ;
		String userId = UserInfo.get().getUserId() ;
		if(isAccountChanged)
			userId = UserInfo.get().getUserIdCache() ;
		if(!params.has("userId"))
			params.put("userId", userId);
		Logger.d(TAG, "post:"+url+"?{data={"+params+"}}");
		client.post(url, params.getBodyContent() ,new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] headers, byte[] arg2,Throwable arg3) {
				Logger.e(TAG, "connect error,server connected failed:"+arg3.getMessage());
				if(httpCallBack != null)
					httpCallBack.onFailed(ResourceUtils.getString(R.string.error_contact_server));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] resByte) {
				try {
					String rsp = new String (resByte,"UTF-8");
					handleResponse(rsp, httpCallBack);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
				
		});
	}

	public void post2(String url , RequestParams params, HttpRequestCallbak callback){
		final HttpRequestCallbak httpCallBack = callback ;
		params.put("ticket", UserInfo.get().getTicketCache());
		if(TextUtils.isEmpty(url))
			return ;
		boolean isAccountChanged = MSharedPreferenceUtils.queryBooleanBySettings(MApplication.getApplication() , Constants.KEY_PREFERENCE_CHANGE_ACCOUNT) ;
		String userId = UserInfo.get().getUserId() ;
		if(isAccountChanged)
			userId = UserInfo.get().getUserIdCache() ;
		if(!params.has("userId"))
			params.put("userId", userId);
		Logger.d(TAG, "post:"+url+"?{data={"+params+"}}");
		client.post(url, params.getBodyContent() ,new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] headers, byte[] arg2,Throwable arg3) {
				Logger.e(TAG, "connect error,server connected failed:"+arg3.getMessage());
				if(httpCallBack != null)
					httpCallBack.onFailed(ResourceUtils.getString(R.string.error_contact_server));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] resByte) {
				try {
					String rsp = new String (resByte,"UTF-8");
					handleResponse2(rsp, httpCallBack);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

		});
	}
	
	private void handleResponse(String response ,HttpRequestCallbak  callback){
		Logger.d(TAG, response);
		if(callback == null) 
				return ;
		try {
			JSONObject responseObj = new JSONObject(response);
			//结果码： 0->成功  1->失败
			try {
				int	code = responseObj.getInt("code");
				if(code == 0){
					callback.onSuccess(responseObj);
				}else if(code == -100){
					callback.onFailed(responseObj.getString("desc"));
					SystemCore.get().exit(MApplication.getApplication());
				}else{
					callback.onFailed(responseObj.getString("desc"));
					//callback.onSuccess(responseObj);
				}
			} catch (Exception e) {
				//为了兼容其它接口，code如果找不到，也会返回成功
				callback.onSuccess(responseObj);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			callback.onFailed(ResourceUtils.getString(R.string.json_parse_error));
		}
	}

	private void handleResponse2(String response ,HttpRequestCallbak  callback){
		Logger.d(TAG, response);
		if(callback == null)
			return ;
		try {
			JSONObject responseObj = new JSONObject(response);
			//结果码： 0->成功  1->失败
			try {
				int	code = responseObj.getInt("code");
				if(code == 0){
					callback.onSuccess(responseObj);
				}else if(code == -100){
					callback.onFailed(responseObj.getString("desc"));
					SystemCore.get().exit(MApplication.getApplication());
				}else{
					//callback.onFailed(responseObj.getString("desc"));
					callback.onSuccess(responseObj);
				}
			} catch (Exception e) {
				//为了兼容其它接口，code如果找不到，也会返回成功
				callback.onSuccess(responseObj);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			callback.onFailed(ResourceUtils.getString(R.string.json_parse_error));
		}
	}
	
	
	public void getFileByAppInfo(AppInfo info , FileCallBack callback){
		
		File cacheFile = new File(AppStoreUtils.getDownloadPath(), AppStoreUtils.getFileNameByAppInfo(info)) ;
		String url = new StringBuilder().append(MConfig.get("downloadApp")).append("?").append("appId=").append(info.id)
				.append("&ticket=").append(UserInfo.get().getTicketCache()).append("&userId=").append(UserInfo.get().getUserIdCache())
				.append("&city=").append(UserInfo.get().getCityCode()).append("&clientType=").append("1").toString() ;
		getFileByUrl(url ,callback ,cacheFile) ;
	}
	
	public void getFileByUrl(String url ,FileCallBack callback ,File file){
		final FileCallBack httpCallBack = callback ;
		Logger.d(TAG, "get file url:"+url);
		if(TextUtils.isEmpty(url))
			return ;
		client.get(url, new FileAsyncHttpResponseHandler(file){

			@Override
			public void onFailure(int statusCode, Header[] hander, Throwable throwable,File file) {
				Logger.d(TAG, "get file onFailure --->"+throwable.getMessage());
				
				if(httpCallBack != null)
					httpCallBack.onError(throwable.getMessage());
			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				super.onProgress(bytesWritten, totalSize);
				long progress = bytesWritten*100/totalSize ;
				if(httpCallBack != null)
					httpCallBack.inProgress((int)progress);
			}

			@Override
			public void onSuccess(int statusCode, Header[] hander, File file) {
				if(httpCallBack == null) return ;
				if(statusCode == 200){
					Logger.d(TAG, "get file onSuccess :file name:"+file.getAbsolutePath()+" ,size:"+file.length()) ;
					httpCallBack.onResposne(file);
				}else{
					httpCallBack.onError(String.valueOf(statusCode));
				}
			}}) ;
	}

}

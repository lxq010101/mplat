package com.ustcinfo.mobile.platform.core.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ustcinfo.mobile.platform.ability.jsbridge.BridgeHandler;
import com.ustcinfo.mobile.platform.ability.jsbridge.BridgeWebView;
import com.ustcinfo.mobile.platform.ability.jsbridge.CallBackFunction;
import com.ustcinfo.mobile.platform.ability.jsbridge.DefaultHandler;
import com.ustcinfo.mobile.platform.core.R;
import com.ustcinfo.mobile.platform.core.appstore.AppStoreUtils;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.plugin.RemoteInterAdapter;
import com.ustcinfo.mobile.platform.core.ui.widget.MAlertDialog;
import com.ustcinfo.mobile.platform.core.ui.widget.MProgressDialog;

/**
 * Created by SunChao on 2017/5/18.
 */

public class WebViewActivity extends Activity {

    private static final String TAG = "WebViewActivity" ;

    private BridgeWebView webView;

    private MProgressDialog pDialog;

    private MAlertDialog alertDialog;

    private boolean isShowTitle = true;

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, android.webkit.JsResult result) {
            result.confirm();
            return true;
        };

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result) {

            View.OnClickListener confirmL =  new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    dissmissAlertDialog();
                    result.confirm();
                }
            } ;
            View.OnClickListener cancelL =  new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    dissmissAlertDialog();
                    result.cancel();
                }
            } ;
            showAlertDialog(null ,message ,cancelL ,confirmL);
            return true;
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int type = getIntent().getIntExtra("type", 0) ;
        //web app需要隐藏actionbar
        if(type == 2) 
        	requestBaseWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.web_view);
        initViews();
        String url = getIntent().getStringExtra("url");

    	//assert文件夹
//		prefix = "file:///android_asset/" ;
        if(type == 2){
            webView.loadUrl("file://"+ AppStoreUtils.getWebAppExtractPath()+url);
        }else{
            url = new StringBuilder().append(url).append("?userId=").append(UserInfo.get().getUserId()).toString() ;
            webView.loadUrl(url);
        }
        Log.d(TAG, "start web browser: type:"+type+" ,url："+url) ;
    }

    private void initViews() {
      //  webView = (BridgeWebView) findViewById(R.id.js_web_view);
        webView.setDefaultHandler(new DefaultHandler());
        //设置setWebChromeClient对象
        webView.setWebChromeClient(webChromeClient);
        //清楚缓存数据
        webView.clearCache(true);
        webView.clearHistory();
        functionrRegister();
    }

    private void functionrRegister(){
        authTokenRegister() ;
    }

    //单点登录
    private void authTokenRegister(){
        webView.registerHandler("authToken", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.d("atfg","sunchao: authToken data:"+data);
                function.onCallBack(RemoteInterAdapter.invoke(RemoteInterAdapter.AUTH_ACCESS_TOKEN,data));
            }
        });
    }

    public void showProgressDialog() {
        showProgressDialog(null);
    }

    public void showProgressDialog(String str) {
        String text = str;
        if (TextUtils.isEmpty(text))
            text = getResources().getString(R.string.loading);
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
        pDialog = new MProgressDialog(this, text);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void showAlertDialog(String title, String content, View.OnClickListener cancelL, View.OnClickListener confirmL) {
        alertDialog = new MAlertDialog(this, false, true);
        alertDialog.setCancelable(true).setDialogCanceledOnTouchOutside(false).setTitle(title).setContent(content)
                .setCancelClickListener(cancelL).setConfirmClickListener(confirmL).show();
    }

    public void showAlertDialog(String title, String content, View.OnClickListener cancelL, View.OnClickListener confirmL, boolean cancelAble, boolean confirmAble) {
        alertDialog = new MAlertDialog(this, false, true);
        alertDialog.setCancelable(cancelAble).setDialogCanceledOnTouchOutside(false).setConfirmable(confirmAble)
                .setTitle(title).setContent(content)
                .setCancelClickListener(cancelL).setConfirmClickListener(confirmL).show();
    }

    public void dissmissAlertDialog() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public void requestBaseWindowFeature(int featureId) {
        requestWindowFeature(featureId);
        isShowTitle = Window.FEATURE_NO_TITLE == featureId ? false : true;
    }



}

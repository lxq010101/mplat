package com.ustcinfo.mobile.platform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.core.MLogin;
import com.ustcinfo.mobile.platform.core.interfaces.LoginCallBack;
import com.ustcinfo.mobile.platform.core.utils.MSharedPreferenceUtils;

import static com.ustcinfo.mobile.platform.ui.activity.PatternLockActivity.FLAG_SP_LOCK_PASSWORD;

public class SplashActivity extends BaseActivity implements LoginCallBack {

    public static final int REQUEST_CODE_TO_PATTERN = 0x23; // 到达解锁的请求码

    private boolean autoLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestBaseWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginBySSO();
            }
        }, 1000);
    }

    private void checkLoginBySSO() {
        if (!TextUtils.isEmpty(MSharedPreferenceUtils.queryStringBySettings(this, FLAG_SP_LOCK_PASSWORD))) {
            // 如果设置了手势密码,则进行解锁后登陆
            startActivityForResult(new Intent(this, PatternLockActivity.class), REQUEST_CODE_TO_PATTERN);
        } else {
            new MLogin(this, autoLogin, this).loginBySSO();
        }
    }

    private void goHomePage() {
        Intent i = new Intent();
        i.setClass(mActivity, HomePageActivity.class);
        mActivity.startActivity(i);
        mActivity.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TO_PATTERN && resultCode == RESULT_OK) {
            new MLogin(this, autoLogin, this).loginBySSO();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_splash;
    }

    @Override
    public void onLoginStart() {
    }

    @Override
    public void onLoginSuccess() {
        goHomePage();
    }

    @Override
    public void onLoginFailed(String message) {
        toast(message);
        if ("没有此用户".equals(message)) {
            Intent i = new Intent();
            try {
                i.setClass(mActivity, Class.forName(MConfig.getActivity("login")));
                mActivity.startActivity(i);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}

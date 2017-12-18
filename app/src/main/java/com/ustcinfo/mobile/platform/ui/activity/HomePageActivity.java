package com.ustcinfo.mobile.platform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.core.appstore.AppStore;
import com.ustcinfo.mobile.platform.core.appstore.AppStoreUtils;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.interfaces.FileCallBack;
import com.ustcinfo.mobile.platform.core.interfaces.HttpRequestCallbak;
import com.ustcinfo.mobile.platform.core.model.AppInfo;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.MHttpClient;
import com.ustcinfo.mobile.platform.core.utils.NativeAppUtils;
import com.ustcinfo.mobile.platform.core.utils.RequestParams;
import com.ustcinfo.mobile.platform.ui.fragment.BaseFragment;
import com.ustcinfo.mobile.platform.ui.fragment.HomePageFragment;
import com.ustcinfo.mobile.platform.ui.fragment.ToolsFragment;
import com.ustcinfo.mobile.platform.ui.fragment.UserFragment;
import com.ustcinfo.mobile.platform.widget.BottomBar;
import com.ustcinfo.mobile.platform.widget.BottomBarTab;
import com.ustcinfo.mobile.platform.widget.ChangeColorIconWithTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by SunChao on 2017/5/12.
 */

public class HomePageActivity extends BaseActivity {
    private Handler mHandler = new Handler();

//    @BindView(R.id.content_viewstub)
//     ViewStub viewStub;


    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    private List<BaseFragment> mTabs = new ArrayList<BaseFragment>();

    private List<ChangeColorIconWithTextView> mTabIndicator =
            new ArrayList<ChangeColorIconWithTextView>();

    private HomePageFragment homePageFragment; // 首页 fragment

    private ToolsFragment toolsFragment; // 工具 fragment

    private UserFragment userFragment; // 我的 fragment


    public static final String APPID = "appId";
    public static final String APPNAME = "appName";
    private int backCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initRegisterId();
        if (savedInstanceState == null) {
            initViews(new HomePageFragment(), new ToolsFragment(), new UserFragment());
            if (getIntent().getStringExtra(APPID) != null && getIntent().getStringExtra(APPNAME) != null)
                needUpdate();
        } else {
            // Restore the fragment's instance
            homePageFragment = (HomePageFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, "homePageFragment");
            toolsFragment = (ToolsFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, "toolsFragment");
            userFragment = (UserFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, "userFragment");
            initViews(homePageFragment,toolsFragment,userFragment);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(homePageFragment!=null&&toolsFragment!=null&&userFragment!=null){
            getSupportFragmentManager().putFragment(outState, "homePageFragment", homePageFragment);
            getSupportFragmentManager().putFragment(outState, "toolsFragment", toolsFragment);
            getSupportFragmentManager().putFragment(outState, "userFragment", userFragment);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra(APPID) != null && intent.getStringExtra(APPNAME) != null)
            needUpdate();

    }

    /**
     * 推送过来需要更新
     */
    private void needUpdate() {
        if (UserInfo.get() != null && UserInfo.get().getUserId() != null) {
            showAlertDialog(getString(R.string.upgrade_notice),
                    String.format(getString(R.string.upgrade_software), getIntent().getStringExtra(APPNAME)),
                    v -> {
                        dissmissAlertDialog();
                    }, v -> {
                        dissmissAlertDialog();
                        showProgressDialog(getString(R.string.downloading));

                        File cacheFile = new File(AppStoreUtils.getDownloadPath(), AppStoreUtils.getFileNameByAppInfo(new AppInfo()));
                        String url = new StringBuilder().append(MConfig.get("downloadApp")).append("?").append("appId=").append(getIntent().getStringExtra(APPID))
                                .append("&ticket=").append(UserInfo.get().getTicketCache()).append("&userId=").append(UserInfo.get().getUserIdCache())
                                .append("&city=").append(UserInfo.get().getCityCode()).append("&clientType=").append("1").toString();
                        MHttpClient.get().getFileByUrl(url, new FileCallBack() {

                            @Override
                            public void onResposne(File file) {
                                dismissProgressDialog();
                                AppStoreUtils.installedAppByType(HomePageActivity.this, msg -> toast(msg), file, 0);
                            }

                            @Override
                            public void inProgress(int progress) {
                                String str = "下载中" + progress + "%";
                                showProgressDialog(str);
                            }

                            @Override
                            public void onError(String msg) {
                                dismissProgressDialog();
                                toast(getString(R.string.download_failed));
                            }
                        }, cacheFile);
                    }, true, true);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.APPID, getIntent().getStringExtra(APPID));
            intent.putExtra(LoginActivity.APPNAME, getIntent().getStringExtra(APPNAME));
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    /*   SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        //一天后重新进行登陆
        try {
            Date loginTime = sdf.parse(MSharedPreferenceUtils.queryStringBySettings(this, Constants.KEY_PREFERENCE_LOGIN_TIME)) ;
            int passTime = (int)((new Date().getTime() - loginTime.getTime()) / 1000 * 60 ) ;
            Logger.d(TAG ,"login pass time "+passTime);
            if(passTime > 2){
                Intent i =  new Intent(this,SplashActivity.class) ;
                startActivity(i);
                finish() ;
            }
        }catch (ParseException e){
        }*/
    }

    /**
     * 初始化registerId
     */
    private void initRegisterId() {
//         String userId = UserInfo.get().getUserId();
        try {
            MConfig.init(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String userId = "H8472580";
        String registerId = JPushInterface.getRegistrationID(getApplicationContext());
        RequestParams p = new RequestParams();
        p.put("userId", userId);
        p.put("registrationId", registerId);
        p.put("flag", "false");//true 为注销   false为绑定

        String url = MConfig.get("bindRegisterId");
        MHttpClient.get().post(url, p, new HttpRequestCallbak() {

            @Override
            public void onSuccess(JSONObject responseObj) {
                Log.e(this.getClass().getSimpleName(), responseObj.toString());
            }

            @Override
            public void onFailed(String msg) {
                Log.e(this.getClass().getSimpleName(), msg);
            }
        });
    }


    //检测升级：支持根据地理信息发布升级

    private void checkUpgrade() {
        final String pkg = getPackageName();
        RequestParams p = new RequestParams();
        p.put("package", pkg);
        p.put("clientType", 1);
        MHttpClient.get().post(MConfig.get("checkUpgrade"), p, new HttpRequestCallbak() {

            @Override
            public void onSuccess(JSONObject responseObj) {
                try {
                    JSONObject data = responseObj.getJSONObject("data");

                    String id = data.getString("appId");
                    String appName = data.getString("appName");
                    String version = data.getString("appVersion");
                    String upgradeContent = "你有新版本需要升级";
                    try {
                        upgradeContent = data.getString("upgradeContent");
                    } catch (Exception e) {
                    }

                    String upgradeFlag = data.getString("updateFlag");
                    final AppInfo info = new AppInfo();
                    info.id = id;
                    info.name = appName;
                    info.version = version;
                    info.packageName = pkg;
                    //本地的版本号version name与服务器的版本进行比对，不一致则提示升级
                    String locVersion = NativeAppUtils.getVersion(mActivity, pkg);
                    if (!TextUtils.equals(locVersion, version)) {
                        //强制升级
                        //非强制升级
                        showAlertDialog(getString(R.string.upgrade_notice), upgradeContent,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        //强制升级
                                        dissmissAlertDialog();
                                        if (TextUtils.equals(upgradeFlag, "1")) {
                                            toast("请务必下载最新版本");
                                            finish();
                                        }
                                    }
                                }, new OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        //确认
                                        dissmissAlertDialog();
                                        downloadAppByInfo(info);
                                    }
                                });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String msg) {
                Log.e(this.getClass().getSimpleName(), msg);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_home_page;
    }

    //下载APP
    private void downloadAppByInfo(AppInfo info) {
        showProgressDialog(getString(R.string.updating));
        MHttpClient.get()
                .getFileByAppInfo(info, new FileCallBack() {

                    @Override
                    public void onResposne(File file) {
                        dismissProgressDialog();
                        NativeAppUtils.installApp(mActivity, file,1);
                    }

                    @Override
                    public void inProgress(int progress) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(String msg) {
                        dismissProgressDialog();
                    }
                });
    }

    private void initViews(HomePageFragment homePageFragment, ToolsFragment toolsFragment, UserFragment userFragment) {

        this.homePageFragment = homePageFragment;
        this.toolsFragment = toolsFragment;
        this.userFragment = userFragment;
        mTabs.add(homePageFragment);
        mTabs.add(toolsFragment);
        mTabs.add(userFragment);

        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.add(R.id.contentContainer, mTabs.get(0));
        fTransaction.add(R.id.contentContainer, mTabs.get(1));
        fTransaction.add(R.id.contentContainer, mTabs.get(2));
        hideAllFragment(fTransaction);
        fTransaction.show(mTabs.get(0));
        fTransaction.commit();
        bottomBar
                .addItem(new BottomBarTab(getApplicationContext(), R.mipmap.ic_home, R.mipmap.ic_home, getResources().getString(R.string.home_page)))
                .addItem(new BottomBarTab(getApplicationContext(), R.mipmap.ic_tools, R.mipmap.ic_tools, getResources().getString(R.string.tools)))
                .addItem(new BottomBarTab(getApplicationContext(), R.mipmap.ic_me, R.mipmap.ic_me, getResources().getString(R.string.me)));
        bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
                hideAllFragment(fTransaction);
                fTransaction.show(mTabs.get(position));
                fTransaction.commitAllowingStateLoss();
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
            }
        });
        checkUpgrade();
        AppStore.get().init(mActivity);

    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (homePageFragment != null) fragmentTransaction.hide(homePageFragment);
        if (toolsFragment != null) fragmentTransaction.hide(toolsFragment);
        if (userFragment != null) fragmentTransaction.hide(userFragment);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                toast("请务必下载最新版本");
                finish();
            } else {
                toast("正在安装");
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            homePageFragment.resetDeleteIcon();
            toolsFragment.resetDeleteIcon();
            //3秒之内连续两次返回按钮，则退出应用
            if (backCount == 0) {
                backCount = 1;
                toast(getString(R.string.make_sure_exit));
                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        backCount = 0;
                    }
                }.start();
            } else {
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    static class DelayRunnable implements Runnable {
        private WeakReference<HomePageActivity> homePageActivityWeakReference;
        private WeakReference<HomePageFragment> homePageFragmentWeakReference;
        private WeakReference<ToolsFragment> toolsFragmentWeakReference;
        private WeakReference<UserFragment> userFragmentWeakReference;

        public DelayRunnable(HomePageActivity homePageActivity, HomePageFragment homePageFragment, ToolsFragment toolsFragment, UserFragment userFragment) {
            homePageActivityWeakReference = new WeakReference(homePageActivity);
            homePageFragmentWeakReference = new WeakReference(homePageFragment);
            toolsFragmentWeakReference = new WeakReference(toolsFragment);
            userFragmentWeakReference = new WeakReference(userFragment);
        }

        @Override
        public void run() {
            if (homePageActivityWeakReference.get() != null) {
                homePageActivityWeakReference.get().initViews(homePageFragmentWeakReference.get(), toolsFragmentWeakReference.get(),
                        userFragmentWeakReference.get());
            }

        }
    }
}

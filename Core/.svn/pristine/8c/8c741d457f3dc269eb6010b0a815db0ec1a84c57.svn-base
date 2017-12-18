package com.ustcinfo.mobile.platform.core.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.log.Logger;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.ui.widget.MAlertDialog;

import rx.functions.Action1;

public class IndexActivity extends Activity {

    private AMapLocationClient mLocationClient;

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();

        checkPermissions();

    }


    @SuppressLint("NewApi")
    private void checkPermissions() {

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request
                (Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(!aBoolean){

                            final   MAlertDialog alertDialog = new MAlertDialog(IndexActivity.this, false, true);
                            alertDialog.setCancelable(true).setDialogCanceledOnTouchOutside(false).setTitle("应用权限").setContent("请务必给予应用相应的权限")
                                    .setCancelClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    }).setConfirmClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent localIntent = new Intent();
                                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    if (Build.VERSION.SDK_INT >= 9) {
                                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                        localIntent.setData(Uri.fromParts("package", getApplication().getPackageName(), null));
                                    } else if (Build.VERSION.SDK_INT <= 8) {
                                        localIntent.setAction(Intent.ACTION_VIEW);
                                        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                        localIntent.putExtra("com.android.settings.ApplicationPkgName", getApplicationContext().getPackageName());
                                    }
                                    startActivity(localIntent);
                                    alertDialog.dismiss();
                                }
                            }).show();


                        }else{
                            Logger.init();
                            initLocation();
                            try {
                                Intent intent = new Intent();
                                MConfig.init(IndexActivity.this);
                                intent.setClass(IndexActivity.this, Class.forName(MConfig.getActivity("splash")));
//            intent.setClass(this, Class.forName (MConfig.getActivity("web")));
                                startActivity(intent);
                            } catch (Exception e) {
                                finish();
                            }
                        }
                    }
                });
    }


    private void initLocation() {
        // 初始化定位信息
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                Logger.d(TAG, "init location:" + amapLocation);
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    String cityCode = amapLocation.getCityCode();
                    String province = amapLocation.getProvince();
                    UserInfo.get().setCityCode(cityCode);
                    UserInfo.get().setProvince(province);
                    mLocationClient.stopLocation();
                    mLocationClient.onDestroy();
                }
                finish();
            }
        });

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

}

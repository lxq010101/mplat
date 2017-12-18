package com.ustcinfo.mobile.platform.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.ui.activity.BaseActivity;

public class AboutActivity extends BaseActivity {

    private TextView versionView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anhui_activity_about);
        versionView = (TextView)findViewById(R.id.about_version) ;
        setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;
            }
        });
        setVersion();
    }

    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_about;
    }

    private void setVersion(){
        try{
            String versionName = getPackageManager().getPackageInfo(getPackageName() ,0).versionName;
            versionView.setText("V"+versionName);
        }catch (Exception e){
        }
    }
}

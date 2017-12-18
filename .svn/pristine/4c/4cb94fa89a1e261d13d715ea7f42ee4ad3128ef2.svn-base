package com.ustcinfo.mobile.platform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ustcinfo.mobile.platform.R;

import butterknife.OnClick;

public class AccountMangerActivity extends BaseActivity {

    private TextView versionView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_account_manager;
    }

    @OnClick({R.id.modify_password})
    public void onClick(View v){
        Intent intent = new Intent() ;
        switch (v.getId()){
            case R.id.modify_password :
                intent = new Intent(mActivity , ModifyPasswordActivity.class) ;
                intent.putExtra("type","modify") ;
                startActivity(intent);
                break;
        }
    }

}

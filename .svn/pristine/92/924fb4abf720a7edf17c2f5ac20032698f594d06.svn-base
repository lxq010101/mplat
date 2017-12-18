package com.ustcinfo.mobile.platform.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.ui.activity.BaseActivity;

import butterknife.BindView;

public class PersonalDetailsActivity extends BaseActivity {

    @BindView(R.id.user_name)
     TextView nameView ;

    @BindView(R.id.user_organization)
     TextView orgView ;

    @BindView(R.id.user_telephone)
     TextView teleView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;
            }
        });
        setViews();
    }

    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_personal_details;
    }

    private void setViews(){
        nameView.setText(UserInfo.get().getName());
        orgView.setText(UserInfo.get().getDepartment());
        teleView.setText(UserInfo.get().getTelephoneNumber());
    }
}

package com.ustcinfo.mobile.platform.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.OnClick;
import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.ui.activity.BaseActivity;

import butterknife.BindView;

public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.feed_back_content)
     TextView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.feed_back));
        setBackClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_feed_back;
    }

    @OnClick({R.id.feed_back_submit})
    public void onSubmit(View v) {
        if (contentView.getText().toString().length() > 0 && contentView.getText().toString().length() <= 255) {
            showProgressDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    toast("提交成功");
                    finish();
                }
            }, 3000);
        } else if (contentView.getText().toString().length() > 255) {
            toast("反馈信息过长");
        } else {
            toast("请输入反馈信息");
        }
    }

}

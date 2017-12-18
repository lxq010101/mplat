package com.ustcinfo.mobile.platform.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.interfaces.HttpRequestCallbak;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.MHttpClient;
import com.ustcinfo.mobile.platform.core.utils.RequestParams;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyPasswordActivity extends BaseActivity {

    @BindView(R.id.modify_account)
     TextView accountView;

    @BindView(R.id.modify_old_password)
     TextView oldPwdView;

    @BindView(R.id.modify_new_password)
    TextView newPwdView;

    @BindView(R.id.confirm_new_pwd)
    TextView pwdConfirmView;

    @BindView(R.id.verify_code)
    TextView verifyCodeView;

    @BindView(R.id.verify_code_btn)
    TextView verifyCodeBtn;

    private CountDownTimer verifyCodePassedTimer;

    //reset为密码重置，modify为密码修改,密码修改需要输入原始密码，但是修改是不会去做校验，走的还是密码重置接口
    private String launchType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.modify_password));
        setBackClickListener(v -> finish());

        launchType = getIntent().getStringExtra("type") ;
        if(TextUtils.equals("reset" ,launchType)){
            oldPwdView.setVisibility(View.GONE);
        }

        if(TextUtils.equals("modify" ,launchType) && !TextUtils.isEmpty(UserInfo.get().getUserId())){
            accountView.setText(UserInfo.get().getUserId());
        }
        initValues() ;
    }

    private void initValues() {
        //获取验证码自动倒计时
        verifyCodePassedTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                if(verifyCodeView != null)
                    verifyCodeBtn.setText(String.format(getString(R.string.get_verify_code_after_seconds), (int) (l / 1000)));
            }

            @Override
            public void onFinish() {
                if(verifyCodeBtn == null)
                    return;
                verifyCodeBtn.setEnabled(true);
                verifyCodeBtn.setText(getString(R.string.retry_verify_code));
            }
        };
    }


    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_modify_password;
    }


    //获取验证码
    @OnClick(R.id.verify_code_btn)
    public void getVerifyCode(View v){
        if (!TextUtils.isEmpty(accountView.getText().toString())) {
            RequestParams p = new RequestParams();
            p.put("userId", accountView.getText().toString());
            showProgressBar("正在获取验证码");
            MHttpClient.get().post(MConfig.get("getVerifyCode"), p, new HttpRequestCallbak() {
                @Override
                public void onSuccess(JSONObject responseObj) {
                    closeProgressBar();
                    verifyCodePassedTimer.start();
                    verifyCodeBtn.setEnabled(false);
                }

                @Override
                public void onFailed(String msg) {
                    closeProgressBar();
                    toast(msg);
                    verifyCodeBtn.setEnabled(true);
                }
            });
        } else {
            toast(getString(R.string.input_user_name_please));
        }
    }

    //提交
    @OnClick({R.id.password_confirm_btn})
    public void modifyPassword(View v) {
        if(checkSubmitValidity()){
            RequestParams p = new RequestParams();
            p.put("telephoneCode", verifyCodeView.getText().toString());
            p.put("userId", accountView.getText().toString());
            p.put("newPassword", newPwdView.getText().toString());

            showProgressBar("正在提交");
            MHttpClient.get().post(MConfig.get("modify_password"), p, new HttpRequestCallbak() {
                @Override
                public void onSuccess(JSONObject responseObj) {
                    closeProgressBar();
                    toast("修改成功");
                    finish();
                }

                @Override
                public void onFailed(String msg) {
                    closeProgressBar();
                    toast(msg);
                }
            });
        }
    }


    private boolean checkSubmitValidity(){
        String userId = accountView.getText().toString().trim() ;
        String oldPwd = newPwdView.getText().toString().trim() ;
        String newPwd = newPwdView.getText().toString().trim() ;
        String confirmPwd = pwdConfirmView.getText().toString().trim() ;
        String verifyCode = verifyCodeView.getText().toString().trim() ;

        if(TextUtils.isEmpty(userId)){
            toast("请输入工号！");
            return false ;
        }

        if(TextUtils.isEmpty(oldPwd) && !TextUtils.equals("reset" ,launchType)){
            toast("请输入原始密码！");
            return false ;
        }

        if(TextUtils.isEmpty(newPwd)){
            toast("请输入新密码！");
            return false ;
        }

        if(!TextUtils.equals(newPwd ,confirmPwd)){
            toast("两次密码不一致，请重新输入");
            return false ;
        }

        if(TextUtils.isEmpty(verifyCode)){
            toast("请输入验证码!");
            return false ;
        }

        if(newPwd.length() < 8 || newPwd.length() > 20){
            toast("密码长度不能低于8位或者不大于20位");
            return false ;
        }

        boolean includeLowerCaseChar = false ;
        boolean includeUpCaseChar = false ;
        boolean includeNumber = false ;
        boolean specialChar = false ;


        Pattern pattern = Pattern.compile("[0-9]") ;
        for(int i= 0 ;i<newPwd.length() ;i++){
            char index = newPwd.charAt(i);
            Matcher isNumbic = pattern.matcher(String.valueOf(index)) ;
            if(isNumbic.matches()){
                includeNumber = true ;
            }else{
                int ascii = (int)index ;
                if(ascii >= 65 && ascii <= 90){
                    includeUpCaseChar = true ;
                }else if(ascii >= 97 && ascii <= 122){
                    includeLowerCaseChar = true ;
                }else{
                    specialChar = true;
                }
            }
        }

        int validCount = 0 ;
        if(includeLowerCaseChar)
            validCount +=1 ;
        if(includeUpCaseChar)
            validCount +=1 ;
        if(includeNumber)
            validCount +=1 ;
        if(specialChar)
            validCount +=1 ;

        if(validCount < 3){
            toast("密码规则不符合规范，请重新输入!");
            return false ;
        }
        return true ;
    }
}

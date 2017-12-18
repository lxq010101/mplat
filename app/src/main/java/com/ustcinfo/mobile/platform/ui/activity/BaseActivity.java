package com.ustcinfo.mobile.platform.ui.activity;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.umeng.analytices.MobclickAgent;
import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.ability.keyboard.KeyboardTouchListener;
import com.ustcinfo.mobile.platform.ability.keyboard.KeyboardUtil;
import com.ustcinfo.mobile.platform.ability.presenter.common.BasePresenter;
import com.ustcinfo.mobile.platform.core.core.SystemCore;
import com.ustcinfo.mobile.platform.core.ui.widget.MAlertDialog;
import com.ustcinfo.mobile.platform.core.ui.widget.MProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

public  class BaseActivity extends com.ustcinfo.mobile.platform.ability.activity.common.BaseActivity {

    public String TAG;

    public BaseActivity mActivity;

    public InputMethodManager inputMethodManager;

    // ActionBar显示容器
    private FrameLayout actionBarContainer;

    // 默认显示的主ActionBar
    private View actionBarMain;

    private ImageButton backBtn;

    private Button confirmBtn;

    private TextView titleView;

    private boolean isShowTitle = true;

    private MProgressDialog pDialog;

    private MAlertDialog alertDialog;

    protected KeyboardUtil keyboardUtil;
    Unbinder binder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断用户是否做了Window.FEATURE_NO_TITLE设置，此设置会导致actionbar
        if (isShowTitle)
            initActionBar();
        SystemCore.get().addActivity(this);
        TAG = this.getClass().getSimpleName();
        mActivity = this;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        binder = ButterKnife.bind(this);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }


    protected void initMoveKeyBoard(LinearLayout container, ScrollView scroller, EditText ... edittexts) {
        keyboardUtil = new KeyboardUtil(this, container, scroller);
        for (EditText edittext : edittexts) {
        keyboardUtil.setOtherEdittext(edittext);
            edittext.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_ABC, -1));
            // monitor the KeyBarod state
            keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
            // monitor the finish or next Key
            keyboardUtil.setInputOverListener(new inputOverListener());
        }
    }

    public void showProgressDialog() {
        showProgressBar();
    }

    public void showProgressDialog(String str) {
      showProgressBar(str);
    }

    public void dismissProgressDialog() {
      closeProgressBar();
    }

    public void showAlertDialog(String title, String content, OnClickListener cancelL, OnClickListener confirmL) {
        alertDialog = new MAlertDialog(mActivity, false, false);
        alertDialog.setCancelable(true).setDialogCanceledOnTouchOutside(false).setTitle(title).setContent(content)
                .setCancelClickListener(cancelL).setConfirmClickListener(confirmL).show();
    }

    public void showAlertDialog(String title, String content, OnClickListener cancelL, OnClickListener confirmL, boolean cancelAble, boolean confirmAble) {
        alertDialog = new MAlertDialog(mActivity, false, true);
        alertDialog.setCancelable(cancelAble).setDialogCanceledOnTouchOutside(false).setConfirmable(confirmAble)
                .setTitle(title).setContent(content)
                .setCancelClickListener(cancelL).setConfirmClickListener(confirmL).show();
    }

    public void dissmissAlertDialog() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public void toast(String str) {
        showToast(str);
    }


    // 设置ActionBar
    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null)
            return;
        // 设置背景
        actionBarContainer = (FrameLayout) getLayoutInflater().inflate(R.layout.anhui_acitonbar_title_base, null);
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        // 设置要显示的内容
        actionBarMain = getLayoutInflater().inflate(R.layout.anhui_actionbar_main, null);
        actionBarContainer.addView(actionBarMain);

        backBtn = (ImageButton) actionBarMain.findViewById(R.id.back);
        confirmBtn = (Button) actionBarMain.findViewById(R.id.confirm);
        titleView = (TextView) actionBarMain.findViewById(R.id.title);

        setBackClickListener(v -> finish());
        backBtn.setVisibility(View.INVISIBLE);
        confirmBtn.setVisibility(View.INVISIBLE);

        actionBar.setCustomView(actionBarContainer, p);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    public void setTitleView(View v) {
        actionBarContainer.removeAllViews();
        actionBarContainer.addView(v);
    }


    // 设置标题确认按钮隐藏与显示
    public void setConfirmButtonIsVisible(Boolean isVisble) {
        if (isVisble) {
            confirmBtn.setVisibility(View.VISIBLE);
        } else {
            confirmBtn.setVisibility(View.INVISIBLE);
        }
    }

    // 设置标题确认按钮监听
    public void setConfirmClickListener(OnClickListener l) {
        if (null == l)
            return;
        confirmBtn.setOnClickListener(l);
        confirmBtn.setVisibility(View.VISIBLE);
    }

    public void setBackClickListener(OnClickListener l) {
        if (null == l)
            return;
        backBtn.setOnClickListener(l);
        backBtn.setVisibility(View.VISIBLE);
    }

    // 设置标题显示的內容
    public void setTitle(String str) {
        if (TextUtils.isEmpty(str))
            return;
        titleView.setText(str);
        titleView.setVisibility(View.VISIBLE);
    }

    public void requestBaseWindowFeature(int featureId) {
        requestWindowFeature(featureId);
        isShowTitle = Window.FEATURE_NO_TITLE == featureId ? false : true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binder.unbind();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 用户在输入框以外的位置点击屏幕时，关闭输入法
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void setTitle() {

    }


    class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {

        @Override
        public void KeyBoardStateChange(int state, EditText editText) {
//            System.out.println("state" + state);
//            System.out.println("editText" + editText.getText().toString());
        }
    }

    class inputOverListener implements KeyboardUtil.InputFinishListener {

        @Override
        public void inputHasOver(int onclickType, EditText editText) {
//            System.out.println("onclickType" + onclickType);
//            System.out.println("editText" + editText.getText().toString());
        }
    }

    @Override
    public boolean supportSlideBack() {
        return false;
    }
}

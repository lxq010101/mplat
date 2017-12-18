package com.ustcinfo.mobile.platform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.core.SystemCore;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.Descry;
import com.ustcinfo.mobile.platform.core.utils.MSharedPreferenceUtils;
import com.ustcinfo.mobile.platform.core.utils.ResourceUtils;

import java.util.List;

public class PatternLockActivity extends BaseActivity implements PatternLockViewListener {

  private PatternLockView mPatternLockView;

  private String mRightPattern = "03478";
  private String mLastPatter = "";

  private static final int DEFAULT_MAX_COUNT = 5; // 默认次数
  private static final int DEFAULT_LOCK_DOT_COUNT = 4; // 设置密码点的个数
  private int mUnLockCount = 0;

  private TextView mUnLockCountTv; // 解锁提示
  private View mResetPatterLayout; // 重置修改手势密码
  private View mCancelResetPatterLayout; // 取消修改
  private View mUseNameAndPwdLoginLayout; // 使用用户名密码登录

  private boolean isHaveLockPassword = false; // 是否有锁屏密码
  private boolean isResetLockPassword = false; // 是否进行重置修改密码

  // 当重置密码时,输入原密码错误次数过多,是否要进入登录界面,还是可以继续允许使用原密码
  private static final boolean isForceToLoginWhenErrorCountTooMuchWithReset = true;

  /**
   * 获取解锁提示内容
   */
  private Spanned getUnLockTipText(int count) {
    return Html.fromHtml("还有 <font color=\"red\" size=\"23\">" + count + "</font> 次机会");
  }

  public static final String FLAG_SP_LOCK_PASSWORD = Descry.encrypt("FLAG_SP_LOCK_PASSWORD");

  @Override protected void onCreate(Bundle savedInstanceState) {
    requestBaseWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_gesture_lock);

    ((TextView) findViewById(R.id.profile_name)).setText(UserInfo.get().getName());
    mUnLockCountTv = (TextView) findViewById(R.id.un_lock_count_tv);
    mResetPatterLayout = findViewById(R.id.reset_lock_password);
    mCancelResetPatterLayout = findViewById(R.id.cancel_reset_lock_password);
    mUseNameAndPwdLoginLayout = findViewById(R.id.forget_lock_password);

    // 测试使用
    //MSharedPreferenceUtils.saveStringSettings(this, FLAG_SP_LOCK_PASSWORD, "", true);

    isHaveLockPassword = !TextUtils.isEmpty(
        MSharedPreferenceUtils.queryStringBySettings(this, FLAG_SP_LOCK_PASSWORD));
    // 如果设置了手势密码,则无需再设置
    if (isHaveLockPassword) {
      mResetPatterLayout.setVisibility(View.VISIBLE);
      mCancelResetPatterLayout.setVisibility(View.GONE);
      mRightPattern =
          Descry.decrypt(MSharedPreferenceUtils.queryStringBySettings(this, FLAG_SP_LOCK_PASSWORD));
      mUnLockCountTv.setText(getUnLockTipText(DEFAULT_MAX_COUNT));
    } else {
      mResetPatterLayout.setVisibility(View.GONE);
      mCancelResetPatterLayout.setVisibility(View.GONE);
      mUseNameAndPwdLoginLayout.setVisibility(View.GONE);

      firstTipLock();
    }



    mPatternLockView = (PatternLockView) findViewById(R.id.patter_lock_view);
    mPatternLockView.setAspectRatioEnabled(true);
    mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
    mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
    mPatternLockView.setDotAnimationDuration(150);
    mPatternLockView.setPathEndAnimationDuration(100);
    mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(R.color.colorPrimary));
    mPatternLockView.setWrongStateColor(ResourceUtils.getColor(R.color.colorAccent));
    mPatternLockView.setInStealthMode(false);
    mPatternLockView.setTactileFeedbackEnabled(false);
    mPatternLockView.setInputEnabled(true);
    mPatternLockView.addPatternLockListener(this);

    changeSafeMode();
    userLogin();
    resetOrCancelGesturePassword();
  }


  @Override
  public int getLayoutId() {
    return R.layout.layout_gesture_lock;
  }

  private void firstTipLock() {
    mUnLockCountTv.setText("请设置解锁密码,至少" + DEFAULT_LOCK_DOT_COUNT + "个点");
  }

  private void changeSafeMode() {
    // 密码安全模式切换
    findViewById(R.id.open_close_safe_mode).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mPatternLockView.setInStealthMode(!mPatternLockView.isInStealthMode());
        toast(mPatternLockView.isInStealthMode() ? getString(R.string.lock_safe_mode)
            : getString(R.string.lock_un_safe_mode));
      }
    });
  }

  private void userLogin() {
    // 用户名登录
    mUseNameAndPwdLoginLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        goToLogin();
      }
    });
  }

  /**
   * 进入用户名密码登录界面
   */
  private void goToLogin() {
    // 清除手势密码
    MSharedPreferenceUtils.saveStringSettings(PatternLockActivity.this, FLAG_SP_LOCK_PASSWORD, "",
        true);

    SystemCore.get().exit(mActivity);

    // 进入登陆界面
    Intent i = null;
    try {
      i = new Intent(PatternLockActivity.this, Class.forName(MConfig.getActivity("login")));
      startActivity(i);
      finish();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void resetOrCancelGesturePassword() {
    // 重置修改手势密码
    mResetPatterLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mUnLockCountTv.setText(R.string.please_yes_lock_password);
        mResetPatterLayout.setVisibility(View.GONE);
        mCancelResetPatterLayout.setVisibility(View.VISIBLE);
        mUnLockCount = 0;
        mLastPatter = "";
        isResetLockPassword = true;
        isHaveLockPassword = false;
        mPatternLockView.clearPattern();
      }
    });

    // 取消
    mCancelResetPatterLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mResetPatterLayout.setVisibility(View.VISIBLE);
        mCancelResetPatterLayout.setVisibility(View.GONE);
        isHaveLockPassword = true;
        isResetLockPassword = false;
        mPatternLockView.clearPattern();
        mUnLockCount = 0;
        mLastPatter = "";
        mUnLockCountTv.setText(getUnLockTipText(DEFAULT_MAX_COUNT));
      }
    });
  }

  @Override public void onProgress(List<PatternLockView.Dot> progressPattern) {
    if (isHaveLockPassword || isResetLockPassword) {
      if (TextUtils.equals(mRightPattern,
          PatternLockUtils.patternToString(mPatternLockView, progressPattern))) {
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
      } else {
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
      }
      return;
    }
    if (TextUtils.isEmpty(mLastPatter)) { // 还没设置呢
      return;
    }
    if (TextUtils.equals(mLastPatter,
        PatternLockUtils.patternToString(mPatternLockView, progressPattern))) {
      mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
    } else {
      mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
    }
  }

  @Override public void onComplete(List<PatternLockView.Dot> pattern) {
    String pwd = PatternLockUtils.patternToString(mPatternLockView, pattern);

    if (isResetLockPassword) { // 修改密码
      if (++mUnLockCount >= DEFAULT_MAX_COUNT) {
        if (isForceToLoginWhenErrorCountTooMuchWithReset) {
          toast(getString(R.string.un_lock_count_too_much));
          goToLogin();
          return;
        }
        mUnLockCountTv.setText(R.string.error_un_lock_too_much_use_old_password);
        mPatternLockView.clearPattern();
        mResetPatterLayout.setVisibility(View.VISIBLE);
        mCancelResetPatterLayout.setVisibility(View.GONE);
        isHaveLockPassword = true;
        isResetLockPassword = false;
        mUnLockCount = 0;
        return;
      }
      if (!TextUtils.equals(pwd, mRightPattern)) {
        mUnLockCountTv.setText(getUnLockTipText(DEFAULT_MAX_COUNT - mUnLockCount));
        return;
      }

      // 原密码确认成功,进行修改
      isResetLockPassword = false;
      firstTipLock();
      mPatternLockView.clearPattern();
      return;
    }

    if (isHaveLockPassword) {
      if (++mUnLockCount >= DEFAULT_MAX_COUNT) {
        toast(getString(R.string.un_lock_count_too_much));
        goToLogin();
        return;
      }
      // 不相等时进行提示
      if (!TextUtils.equals(pwd, mRightPattern)) {
        mUnLockCountTv.setText(getUnLockTipText(DEFAULT_MAX_COUNT - mUnLockCount));
        return;
      }
      mUnLockCountTv.setText(R.string.un_lock_success);
      setResult(RESULT_OK);
      finish();
      return;
    }

    if (mUnLockCount == DEFAULT_LOCK_DOT_COUNT) {
      mUnLockCount = 0;
      mUnLockCountTv.setText(R.string.first_set_lock_error_too_much);
      mPatternLockView.clearPattern();
      mLastPatter = "";
      return;
    }

    // 进行密码判断

    if (pwd.length() < DEFAULT_LOCK_DOT_COUNT) {
      mUnLockCountTv.setText(R.string.lock_password_too_simple);
      mPatternLockView.clearPattern();
      return;
    }
    // 设置第一次密码
    if (TextUtils.isEmpty(mLastPatter)) {
      mLastPatter = pwd;
      mUnLockCountTv.setText(R.string.lock_yes_once);
      mPatternLockView.clearPattern();
      return;
    }

    // 判断两次密码是否一致
    if (!TextUtils.equals(mLastPatter, pwd)) {
      mUnLockCount++;
      mUnLockCountTv.setText(R.string.lock_yes_twice);
      return;
    }

    mUnLockCount = 0; // 重置,为了解锁使用
    mLastPatter = "";
    // 存储密码
    MSharedPreferenceUtils.saveStringSettings(this, FLAG_SP_LOCK_PASSWORD, Descry.encrypt(pwd),
        true);
    isHaveLockPassword = true;
    mResetPatterLayout.setVisibility(View.VISIBLE);
    mUseNameAndPwdLoginLayout.setVisibility(View.VISIBLE);
    mCancelResetPatterLayout.setVisibility(View.GONE);

    mRightPattern =
        Descry.decrypt(MSharedPreferenceUtils.queryStringBySettings(this, FLAG_SP_LOCK_PASSWORD));

    // 进行解锁登录
    mUnLockCountTv.setText(R.string.lock_set_ok_can_un_lock_login);
    mPatternLockView.clearPattern();
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {

    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (!isHaveLockPassword && !isResetLockPassword) {
        finish();
      } else {
        moveTaskToBack(true);
      }
      return true;
    }

    return super.onKeyDown(keyCode, event);
  }

  @Override public void onStarted() {
  }

  @Override public void onCleared() {
  }
}

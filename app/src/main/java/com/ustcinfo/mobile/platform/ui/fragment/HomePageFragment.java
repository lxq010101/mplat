package com.ustcinfo.mobile.platform.ui.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.ability.utils.ThreadUtils;
import com.ustcinfo.mobile.platform.core.appstore.AppStore;
import com.ustcinfo.mobile.platform.core.appstore.AppStoreUtils;
import com.ustcinfo.mobile.platform.core.appstore.AppType;
import com.ustcinfo.mobile.platform.core.interfaces.AppRequestCallBack;
import com.ustcinfo.mobile.platform.core.interfaces.HttpRequestCallbak;
import com.ustcinfo.mobile.platform.core.model.AppInfo;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.MHttpClient;
import com.ustcinfo.mobile.platform.core.utils.RequestParams;
import com.ustcinfo.mobile.platform.ui.activity.BaseActivity;
import com.ustcinfo.mobile.platform.ui.adapter.DragAdapter;
import com.ustcinfo.mobile.platform.widget.ArcProgress;
import com.ustcinfo.mobile.platform.widget.FullyLinearLayoutManager;
import com.ustcinfo.mobile.platform.widget.itemhelp.OnStartDragListener;
import com.ustcinfo.mobile.platform.widget.itemhelp.SimpleItemTouchHelperCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import rx.Observable;

/**
 * Created by SunChao on 2017/5/12.
 */

public class HomePageFragment extends BaseFragment implements OnStartDragListener {


    private int progressHeight;


    private DragAdapter adapter;

    private String dayCount = "0";

    List<DragAdapter.AppDisplayDragBean> dragBeanList = new ArrayList<>();

    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.handle_progress)
    ArcProgress progressView;

    @BindView(R.id.welcome_title)
    TextView welcomeTitle;


    @BindView(R.id.wait_handle_number)
    TextView waitHandleNumberView;

    @BindView(R.id.done_number)
    TextView doneNumberView;

    @BindView(R.id.indicator_number)
    TextView indicatorNumberView;


    private ViewTreeObserver vto;

    //隐藏和打开进度条区域按钮
    @BindView(R.id.drag_switch)
    ImageView dragSwitcherBtn;

    private ItemTouchHelper mItenHelper;

    //今日完成量
    private int completed = 10;

    private final int MSG_COMPLETE = 0x110;

    private Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.anhui_fragment_home_page2;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }


    @Override
    public void onResume() {
        super.onResume();

        refreshHomePageViews();
    }

    private void refreshHomePageViews() {
        loadApps();
    }

    /**
     * 用于消除长按操作非drag状态下的删除按钮显示
     *
     * @return true : 删除按钮已经消除,可以继续返回按钮逻辑
     * false: 直接清除返回按钮
     */
    public boolean resetDeleteIcon() {

        // 由于暂时没有其他特殊情况,只要有一个状态改变,其它也是一样的,所以没有必要进行全集合遍历了,判断第一个就好
        if (dragBeanList.size() > 0 && !dragBeanList.get(0).isCanShowDeleteIcon) {
            return true;
        }
        showDeleteIcon(dragBeanList, false);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    private void showDeleteIcon(List<DragAdapter.AppDisplayDragBean> list, boolean yesOrNo) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isCanShowDeleteIcon = yesOrNo;
        }
    }


    private void setWaitHandleByNumber(final int tipCount) {
        //设置当前待办量
        SpannableString s1 = new SpannableString(String.format(mActivity.getString(R.string.wait_handle_number_now), tipCount));
        s1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        waitHandleNumberView.setText(s1);
        //设置当日完成量
        SpannableString s2 = new SpannableString(String.format(mActivity.getString(R.string.done_number_about_today), 10));
        s2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        doneNumberView.setText(s2);

        //设置当月指标
        SpannableString s3 = new SpannableString(String.format(mActivity.getString(R.string.indicator_about_this_month), 100));
        s3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        indicatorNumberView.setText(s3);
        int ps = 0;
        if (tipCount > 0)
            ps = (int) ((float) tipCount / (tipCount + completed) * 100);

        progressView.setProgress(ps);

        progressView.setOnCenterDraw((canvas, rectF, x, y, storkeWidth, progress) -> {

            //画
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            float radis = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
            p.setColor(Color.parseColor("#DAE4EE"));
            canvas.drawCircle(x, y, radis, p);

            //待装量数字
            float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 35,
                    getResources().getDisplayMetrics());
            p.setTextSize(textSize);
            p.setAntiAlias(true);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            p.setColor(Color.parseColor("#0190e7"));
            Rect rect = new Rect();
            // String progressStr = String.valueOf(progress + "%");
            String progressStr = String.valueOf(tipCount);
            p.getTextBounds(progressStr, 0, progressStr.length(), rect);
            float textX = x - (p.measureText(progressStr) / 2);
            float textY = y - ((p.descent() - p.ascent()) / 2) + 20;
            canvas.drawText(progressStr, textX - rect.left, textY - rect.top, p);

            //显示"当前待装量"
            Paint paint = new Paint();
            paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                    getResources().getDisplayMetrics()));
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#0190e7"));
            String text = getString(R.string.wait_install);
            Rect rect2 = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect2);
            float textX2 = x - (paint.measureText(text) / 2);
            float textY2 = textY - ((paint.descent() - (paint.ascent())));
            canvas.drawText(text, textX2 - rect2.left, textY2 - rect2.top, paint);
            drawPercentage(canvas, "0%", x - (dip2px(getContext(), 90) - storkeWidth) * (float) Math.cos(Math.toRadians(35)) + dip2px(getContext(), 5), y + (dip2px(getContext(), 90) - storkeWidth) * (float) Math.sin(Math.toRadians(35)) + dip2px(getContext(), 5));
            drawPercentage(canvas, "50%", x, y - dip2px(getContext(), 90) + storkeWidth + dip2px(getContext(), 10));
            drawPercentage(canvas, "100%", x + (dip2px(getContext(), 90) - storkeWidth) * (float) Math.cos(Math.toRadians(35)) - dip2px(getContext(), 5), y + (dip2px(getContext(), 90) - storkeWidth) * (float) Math.sin(Math.toRadians(35)) + dip2px(getContext(), 5));

        });
        progressView.postInvalidate();
    }

    private void drawPercentage(Canvas canvas, String s, float x, float y) {
        Paint paint = new Paint();
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8,
                getResources().getDisplayMetrics()));
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#0190e7"));
        Rect rect = new Rect();
        paint.getTextBounds(s, 0, s.length(), rect);
        float textX = x - (paint.measureText(s) / 2);
        float textY = y + (paint.descent() - (paint.ascent())) / 2;
        canvas.drawText(s, textX - rect.left, textY - rect.top, paint);
    }


    private void loadApps() {
        List<DragAdapter.AppDisplayDragBean> templist = new ArrayList<>();
        new Handler().post(() -> {
            AppStore.get().getAppsEntranceByReleaseType(mActivity, new AppRequestCallBack() {
                @Override
                public void onAppsSuccess(List<AppInfo> list) {
                    dragBeanList.clear();
                    templist.clear();
                    for (int i = 0; i < list.size(); i++) {
                        AppInfo info = list.get(i);
                        //判断应用是否已安装
                        if (AppStoreUtils.isAppInstalled(mActivity, info)) {
                            DragAdapter.AppDisplayDragBean displayDragBean = new DragAdapter.AppDisplayDragBean();
                            displayDragBean.appInfo = list.get(i);
                            if (displayDragBean.appInfo.isShowCornerMark == 1) {
                                templist.add(displayDragBean);
                            }
                            dragBeanList.add(displayDragBean);

                        }
                    }
                    adapter.notifyDataSetChanged();
                    setWaitHandleByNumber(0);


                    for (DragAdapter.AppDisplayDragBean s : dragBeanList) {
                        if (s.appInfo.isShowCornerMark == 1) {
                            getCornerMark(s, templist.size());
                        }
                    }

//                    for (int i = 0; i < list.size(); i++) {
//                        AppInfo info = list.get(i);
//                        //判断应用是否已安装
//                        if (AppStoreUtils.isAppInstalled(mActivity, info)) {
//                            DragAdapter.AppDisplayDragBean displayDragBean = new DragAdapter.AppDisplayDragBean();
//                            displayDragBean.appInfo = list.get(i);
//                            templist.add(displayDragBean);
//
//                        }
//                    }
//                    for (DragAdapter.AppDisplayDragBean s : templist) {
//                        if (s.appInfo.isShowCornerMark == 1) {
//                            getCornerMark(s, templist.size());
//                        } else {
//                            dragBeanList.add(s);
//                        }
//                    }
//
//                    //没有一个应用展示角标 直接刷新界面
//                    if (dragBeanList.size() == templist.size()) {
//                        ThreadUtils.runOnUiThread(() -> {
//                            adapter.notifyDataSetChanged();
//                            setWaitHandleByNumber(0);
//                        });
//
                }


                @Override
                public void onFailed(String msg) {
                    mActivity.toast(msg);
                }

                @Override
                public void onAppSuccess(AppInfo app) {
                }

            }, AppType.releaseType.MODULE);

        });

    }

    private void initViews() {
        welcomeTitle.setText(String.format(mActivity.getString(R.string.welcome_titile), UserInfo.get().getName()));
        vto = progressView.getViewTreeObserver();
        vto.addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                progressHeight = progressView.getHeight();
                progressView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        adapter = new DragAdapter((BaseActivity) getActivity(), dragBeanList, this);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(getActivity(), 4);
        rv.setLayoutManager(manager);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);

        mItenHelper = new ItemTouchHelper(callback);
        mItenHelper.attachToRecyclerView(rv);
        //  setWaitHandleByNumber(0);
    }

    @OnClick({R.id.drag_switch, R.id.handle_progress, R.id.toolbar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.handle_progress:
            case R.id.toolbar:
                resetDeleteIcon();
                break;

            default:
                resetDeleteIcon();
                break;

        }

    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItenHelper.startDrag(viewHolder);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        // final float scale = context.getResources().getDisplayMetrics().density;
        // return (int) (dpValue * scale + 0.5f);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    int index = 0;

    /**
     * 获取角标
     */
    public void getCornerMark(DragAdapter.AppDisplayDragBean displayDragBean, int length) {
        //显示待办数  :应用后台配置并且不是删除状态时，显示应用角标

        RequestParams p = new RequestParams();
        String j = UserInfo.get().getUserId();
        p.put("userid", j);

        MHttpClient.get().postWithoutEncript(displayDragBean.appInfo.cornerMarkUrl, p, new HttpRequestCallbak() {
            @Override
            public void onSuccess(JSONObject responseObj) {
                try {
                    index++;
                    String tipCount = responseObj.getString("CORNERMARKCOUNT");
                    if (TextUtils.equals("null", tipCount)) {
                        displayDragBean.count = Integer.valueOf(0);
                    } else {
                        displayDragBean.count = Integer.valueOf(tipCount);
                    }

                    if (index == length) {
                        index = 0;
                        setWaitHandleByNumber(getTipCount());
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailed(String msg) {
                displayDragBean.count = Integer.valueOf(0);
                if (index == length) {
                    index = 0;
                    setWaitHandleByNumber(getTipCount());
                    adapter.notifyDataSetChanged();
                }

            }
        });

    }

    /**
     * 获取待装量
     */
    int tipCount;

    public int getTipCount() {
        tipCount = 0;
        Observable.from(dragBeanList).subscribe(s -> tipCount += s.count);
        return tipCount;

    }


}

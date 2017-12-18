package com.ustcinfo.mobile.platform.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.ability.utils.ThreadUtils;
import com.ustcinfo.mobile.platform.core.appstore.AppStore;
import com.ustcinfo.mobile.platform.core.appstore.AppStoreUtils;
import com.ustcinfo.mobile.platform.core.appstore.AppType;
import com.ustcinfo.mobile.platform.core.interfaces.AppRequestCallBack;
import com.ustcinfo.mobile.platform.core.model.AppInfo;
import com.ustcinfo.mobile.platform.ui.activity.BaseActivity;
import com.ustcinfo.mobile.platform.ui.adapter.DragAdapter;
import com.ustcinfo.mobile.platform.ui.adapter.DragAdapter.AppDisplayDragBean;
import com.ustcinfo.mobile.platform.widget.itemhelp.divider.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by SunChao on 2017/5/16.
 */

public class ToolsFragment extends BaseFragment {


    @BindView(R.id.rv)
    RecyclerView rv;

    DragAdapter adapter;

    private List<AppDisplayDragBean> dragBeanList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.anhui_fragment_tools;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        adapter = new DragAdapter((BaseActivity) getActivity(), dragBeanList, null);
        adapter.setUninstallCallBack(() -> loadApps());
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        DividerGridItemDecoration dividerGridItemDecoration = new DividerGridItemDecoration(getContext());
        rv.addItemDecoration(dividerGridItemDecoration);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        rv.setLayoutManager(manager);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadApps();
    }

    private void loadApps() {
       new Handler().post(()->{
           showProgressBar();
           AppStore.get().getAppsEntranceByReleaseType(mActivity, new AppRequestCallBack() {
               @Override
               public void onAppsSuccess(List<AppInfo> list) {
                   dragBeanList.clear();
                   //创建一个“+”号
                   AppInfo more = new AppInfo();
                   more.id = "more";
                   list.add(more);
                   for (int i = 0; i < list.size(); i++) {
                       AppInfo info = list.get(i);
                       //判断应用是否已安装
                       if (AppStoreUtils.isAppInstalled(mActivity, info) || TextUtils.equals(info.id, "more")) {
                           AppDisplayDragBean displayDragBean = new AppDisplayDragBean();
                           displayDragBean.appInfo = list.get(i);
                           dragBeanList.add(displayDragBean);
                       }
                   }
                   adapter.notifyDataSetChanged();
                   closeProgressBar();


               }

               @Override
               public void onFailed(String msg) {
                   closeProgressBar();
                   mActivity.toast(msg);
               }

               @Override
               public void onAppSuccess(AppInfo app) {
                   closeProgressBar();
               }
           }, AppType.releaseType.TOOLS);
       });
    }

    public boolean resetDeleteIcon() {

        // 由于暂时没有其他特殊情况,只要有一个状态改变,其它也是一样的,所以没有必要进行全集合遍历了,判断第一个就好
        if (dragBeanList.size() > 0 && !dragBeanList.get(0).isCanShowDeleteIcon) {
            return true;
        }
        showDeleteIcon(dragBeanList, false);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        return false;
    }

    private void showDeleteIcon(List<AppDisplayDragBean> list, boolean yesOrNo) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isCanShowDeleteIcon = yesOrNo;
        }
    }
}

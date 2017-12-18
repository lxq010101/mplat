package com.ustcinfo.mobile.platform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.ability.utils.GlideApp;
import com.ustcinfo.mobile.platform.core.appstore.AppStore;
import com.ustcinfo.mobile.platform.core.appstore.AppStoreUtils;
import com.ustcinfo.mobile.platform.core.appstore.AppType;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.interfaces.AppRequestCallBack;
import com.ustcinfo.mobile.platform.core.interfaces.FileCallBack;
import com.ustcinfo.mobile.platform.core.interfaces.InstallFailedCallBack;
import com.ustcinfo.mobile.platform.core.model.AppInfo;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.MHttpClient;
import com.ustcinfo.mobile.platform.core.utils.RequestParams;
import com.ustcinfo.mobile.platform.widget.ArcProgress;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SunChao on 2017/5/16.
 */

public class AppStoreActivity extends BaseActivity {

    private AppStoreListAdapter mAdapter;

    private ListView appListView;

    List<AppInfo> appList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anhui_activity_app_store);
        appListView = (ListView) findViewById(R.id.app_store_list);

        RequestParams p = new RequestParams();
        p.put("clientType", "1");
        p.put("city", UserInfo.get().getCityCode());
        showProgressDialog();
        AppStore.get().getApps(new AppRequestCallBack() {
            @Override
            public void onAppsSuccess(List<AppInfo> list) {
                appList = list;
                loadApps();
                AppStore.get().init(mActivity);
                dismissProgressDialog();
            }
            @Override
            public void onFailed(String msg) {
                toast(msg);
                dismissProgressDialog();
            }
            @Override
            public void onAppSuccess(AppInfo app) {
            }
        }, false);
        setBackClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadApps();
    }

    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_app_store;
    }

    private void loadApps() {
        List<AppInfo> list = new ArrayList<>();
        for (AppInfo info : appList) {
            //后台配置应用为其它类型、或者是子应用入口的时候，不显示在应用列表中
            if (info.useType == 2 || !TextUtils.equals(info.parentId, "-1")) continue;
            //未安装的应用显示在应用列表中
            if (!AppStoreUtils.isAppInstalled(this, info)) {
                info.setStatus(AppType.StatusType.UNINSTALL);
                list.add(info);
            } else if (AppStoreUtils.checkUpgrade(this, info)) {
                //需要升级的应用，显示在应用列表中
                info.setStatus(AppType.StatusType.UPGRADE);
                list.add(info);
            }
        }
        mAdapter = new AppStoreListAdapter(mActivity, list);
        appListView.setAdapter(mAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadApps();
    }

    class AppStoreListAdapter extends BaseAdapter {

        private List<AppInfo> appList;

        public AppStoreListAdapter(Context context, List<AppInfo> list) {
            this.appList = list;
        }

        @Override
        public int getCount() {
            return appList.size();
        }

        @Override
        public Object getItem(int position) {
            return appList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            @ViewInject(R.id.app_store_ll)
            LinearLayout appLL;

            @ViewInject(R.id.app_store_icon)
            ImageView iconView;

            @ViewInject(R.id.app_store_name)
            TextView nameView;

            @ViewInject(R.id.app_store_size)
            TextView sizeView;

            @ViewInject(R.id.app_store_version)
            TextView versionView;

            @ViewInject(R.id.app_store_summary)
            TextView summaryView;

            @ViewInject(R.id.app_store_download)
            Button downloadBtn;

            @ViewInject(R.id.app_store_progress)
            ArcProgress progressbarView;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = new ViewHolder();
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_app_store,
                        null);
                ViewUtils.inject(holder, view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final AppInfo info = appList.get(i);

            holder.nameView.setText(info.name);

            float size = Long.valueOf(info.size) / (float) (1024 * 1024);
            DecimalFormat df = new DecimalFormat("0.00");
            holder.sizeView.setText(String.format("%s M", df.format(size)));
            holder.versionView.setText(info.version);

            if (!TextUtils.equals("null", info.summay) && !TextUtils.isEmpty(info.summay))
                holder.summaryView.setText(info.summay);


         /*  AsyncImageLoader.get().loadIconByAppId(mActivity, holder.iconView,
                    info.id, getResources().getDrawable(R.mipmap.ic_delete));*/
            String url = MConfig.get("downloadAppIcon")+"?"+"appId="+info.id+"&ticket="+ UserInfo.get().getTicketCache()+"&userId="+UserInfo.get().getUserIdCache() ;
            GlideApp.with(mActivity).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.iconView);

            holder.downloadBtn.setTag(1);

            if (info.getStatus() == AppType.StatusType.INSTALL) {
                holder.downloadBtn.setText(getString(R.string.download));
            } else if (info.getStatus() == AppType.StatusType.UPGRADE) {
                holder.downloadBtn.setText(getString(R.string.upgrade));
            }

            holder.progressbarView.setVisibility(View.GONE);
            final ArcProgress progressBar = holder.progressbarView;
            final Button downloadBtn = holder.downloadBtn;
            progressBar.setTag(info.id);
            downloadBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    downloadBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    MHttpClient.get().getFileByAppInfo(info,
                            new FileCallBack() {

                                @Override
                                public void onResposne(File file) {
                                    downloadBtn.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    AppStoreUtils.installedAppByType(mActivity, new InstallFailedCallBack() {
                                                @Override
                                                public void onFailed(String msg) {
                                                    toast(msg);
                                                }
                                            } ,file, info.type);
                                    loadApps();
                                }

                                @Override
                                public void onError(String msg) {
                                    downloadBtn.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void inProgress(int progress) {
                                    String tagId = (String) progressBar
                                            .getTag();
                                    if (TextUtils.equals(tagId, info.id)) {
                                        progressBar.setProgress(progress);
                                    }
                                }
                            });
                }
            });
            return view;
        }

    }
}

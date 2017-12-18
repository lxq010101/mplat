package com.ustcinfo.mobile.platform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fingdo.statelayout.StateLayout;
import com.liaoinstan.springview.widget.SpringView;
import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.core.model.AnnouncementInfo;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.present.AnnouncementPresenter;
import com.ustcinfo.mobile.platform.core.view.AnnouncementView;
import com.ustcinfo.mobile.platform.ui.adapter.AnnouncementAdapter;
import com.ustcinfo.mobile.platform.widget.MplatAnimRefreshFooter;
import com.ustcinfo.mobile.platform.widget.MplatAnimRefreshHeader;
import com.ustcinfo.mobile.platform.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by xueqili on 2017/11/15. 公告列表页面
 */
public class AnnouncementActivity extends BaseActivity implements AnnouncementView {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rv_annoucement)
    RecyclerView rvAnnoucement;
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    private int pageNo = 1;
    private int pageSize = 10;
    private List<AnnouncementInfo> mDatas = new ArrayList<>();
    private AnnouncementAdapter announcementAdapter;
    private String userId = UserInfo.get().getUserIdCache();
    private int pageTotal;
    public static final String ANNOUNCEMENTINFO = "announcement";

    @Override
    public int getLayoutId() {
        return R.layout.activity_announcement;
    }

    @Override
    protected AnnouncementPresenter createPresenter() {
        return new AnnouncementPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("公告");
        back.setVisibility(View.VISIBLE);
        announcementAdapter = new AnnouncementAdapter(getApplicationContext(), R.layout.item_news, mDatas);
        rvAnnoucement.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvAnnoucement.addItemDecoration(new RecycleViewDivider(
                getApplicationContext(), LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(getApplicationContext(), R.color.bg_gray)));

        announcementAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(AnnouncementActivity.this, AnnouncemntDetailsActivity.class);
            intent.putExtra(ANNOUNCEMENTINFO, mDatas.get(position));
            startActivity(intent);
        });

        springview.setType(SpringView.Type.FOLLOW);
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (this != null && !AnnouncementActivity.this.isFinishing()) {
                            pageNo = 1;
                            mDatas.clear();
                            ((AnnouncementPresenter) mvpPresenter).getAnnouncementInfos(pageNo, pageSize, userId);
                        }
                    }
                }, 500);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(() -> {
                    if (this != null && !AnnouncementActivity.this.isFinishing()) {
                        if (pageNo < pageTotal) {
                            pageNo++;
                            ((AnnouncementPresenter) mvpPresenter).getAnnouncementInfos(pageNo, pageSize, userId);
                        } else {
                            springview.onFinishFreshAndLoad();
                            showToast("没有更多数据");
                        }
                    }
                }, 500);
            }
        });
        rvAnnoucement.setAdapter(announcementAdapter);
        springview.setHeader(new MplatAnimRefreshHeader(getApplicationContext()));
        springview.setFooter(new MplatAnimRefreshFooter(getApplicationContext()));
        ((AnnouncementPresenter) mvpPresenter).getAnnouncementInfos(pageNo, pageSize, userId);
        stateLayout.setRefreshListener(new StateLayout.OnViewRefreshListener() {
            @Override
            public void refreshClick() {
                ((AnnouncementPresenter) mvpPresenter).getAnnouncementInfos(pageNo, pageSize, userId);
            }

            @Override
            public void loginClick() {

            }
        });
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override
    public void getAnnouncementInfos(int pageTotal, List<AnnouncementInfo> list) {
        stateLayout.showContentView();
        mDatas.addAll(list);
        announcementAdapter.notifyDataSetChanged();
        springview.onFinishFreshAndLoad();
        this.pageTotal = pageTotal;
    }

    @Override
    public void getDetailContent(String content,String title) {

    }

    @Override
    public void getAnnoucementImg(String img) {

    }


    @Override
    public void error(String message) {
        super.error(message);
        stateLayout.showErrorView();
        springview.onFinishFreshAndLoad();
    }

    @Override
    public void empty() {
        super.empty();
        stateLayout.showEmptyView();
    }
}

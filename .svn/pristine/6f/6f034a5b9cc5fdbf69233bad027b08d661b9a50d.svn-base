package com.ustcinfo.mobile.platform.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.ability.presenter.common.BasePresenter;
import com.ustcinfo.mobile.platform.core.model.AnnouncementInfo;
import com.ustcinfo.mobile.platform.core.present.AnnouncementPresenter;
import com.ustcinfo.mobile.platform.core.view.AnnouncementView;

import java.util.List;

import butterknife.BindView;

public class AnnouncemntDetailsActivity extends BaseActivity implements AnnouncementView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.lin)
    LinearLayout lin;
    public static final String ID = "ID";
    private AnnouncementInfo announcementInfo;
    private String id;
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        announcementInfo = (AnnouncementInfo) getIntent().getSerializableExtra(AnnouncementActivity.ANNOUNCEMENTINFO);
        back.setVisibility(View.VISIBLE);
        if(announcementInfo!=null) {
            ((AnnouncementPresenter) mvpPresenter).getDtailDrug(announcementInfo.getContent());
            title.setText(announcementInfo.getTitle());
        }else{
            ((AnnouncementPresenter) mvpPresenter).getDtailDrugById(getIntent().getStringExtra(ID));
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_announcemnt_details;
    }

    @Override
    protected BasePresenter createPresenter() {
        return new AnnouncementPresenter(this);
    }

    @Override
    public void getAnnouncementInfos(int pageTotal, List<AnnouncementInfo> list) {

    }

    @Override
    public void getDetailContent(String content,String title) {

        webview = new WebView(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        webview.setLayoutParams(layoutParams);
        initWebview(content);
        lin.addView(webview, 0);
        if(title!=null)
        this.title.setText(title);
    }

    @Override
    public void getAnnoucementImg(String img) {

    }

    @SuppressLint("SetJavaScriptEnabled")
    @android.webkit.JavascriptInterface
    private void initWebview(String content) {
        WebSettings webSettings = webview.getSettings();
        //设置webview缓存属性
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webview.setDownloadListener((url, s1, s2, s3, l) -> {
            if (url != null && url.startsWith("http://"))
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });
        //加载需要显示的网页
        //设置Web视图
        webview.setHorizontalScrollBarEnabled(false);//水平不显示
        webview.setVerticalScrollBarEnabled(true); //垂直不显示
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
        webview.getSettings().setJavaScriptEnabled(true);

    }
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100)
                closeProgressBar();

            super.onProgressChanged(view, newProgress);
        }
    }
}

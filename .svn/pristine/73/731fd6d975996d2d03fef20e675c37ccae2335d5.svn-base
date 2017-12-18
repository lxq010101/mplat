package com.ustcinfo.mobile.platform.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liaoinstan.springview.container.BaseHeader;
import com.liaoinstan.springview.utils.DensityUtil;
import com.ustcinfo.mobile.platform.R;

/**
 * Created by 学祺 on 2017/3/15.
 */

public class MplatAnimRefreshHeader extends BaseHeader {

    private AnimationDrawable animationPull;
    private AnimationDrawable animationPullFan;
    private AnimationDrawable animationRefresh;

    private Context context;
    private ImageView header_img;
    private int[] pullAnimSrcs = new int[]{R.mipmap.icon_pull0,R.mipmap.icon_pull1,R.mipmap.icon_pull2,R.mipmap.icon_pull3};
    private int[] refreshAnimSrcs = new int[]{R.mipmap.icon_pull0,R.mipmap.icon_pull1,R.mipmap.icon_pull2,R.mipmap.icon_pull3};

    public MplatAnimRefreshHeader(Context context){
        this(context,null,null);
    }
    public MplatAnimRefreshHeader(Context context, int[] pullAnimSrcs, int[] refreshAnimSrcs){
        this.context = context;
        if (pullAnimSrcs!=null) this.pullAnimSrcs = pullAnimSrcs;
        if (refreshAnimSrcs!=null) this.refreshAnimSrcs = refreshAnimSrcs;
        animationPull = new AnimationDrawable();
        animationPullFan = new AnimationDrawable();
        animationRefresh = new AnimationDrawable();
        for (int i=1;i< this.pullAnimSrcs.length;i++) {
            animationPull.addFrame(ContextCompat.getDrawable(context, this.pullAnimSrcs[i]),100);
            animationRefresh.setOneShot(true);
        }
        for (int i= this.pullAnimSrcs.length-1;i>=0;i--){
            animationPullFan.addFrame(ContextCompat.getDrawable(context, this.pullAnimSrcs[i]), 100);
            animationRefresh.setOneShot(true);
        }
        for (int src: this.refreshAnimSrcs) {
            animationRefresh.addFrame(ContextCompat.getDrawable(context, src),150);
            animationRefresh.setOneShot(false);
        }
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(com.liaoinstan.springview.R.layout.meituan_header, viewGroup, true);
        header_img = (ImageView) view.findViewById(com.liaoinstan.springview.R.id.meituan_header_img);
        if (pullAnimSrcs !=null&& pullAnimSrcs.length>0)
            header_img.setImageResource(pullAnimSrcs[0]);
        return view;
    }

    @Override
    public void onPreDrag(View rootView) {
    }

    @Override
    public void onDropAnim(View rootView, int dy) {
        int maxw = DensityUtil.dip2px(context, 45);
        float w = maxw*Math.abs(dy)/rootView.getMeasuredHeight();
        if (w>maxw) return;
        ViewGroup.LayoutParams layoutParams = header_img.getLayoutParams();
        layoutParams.width = (int) w;
        header_img.setLayoutParams(layoutParams);
    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {
        if (!upORdown){
            header_img.setImageDrawable(animationPull);
            animationPull.start();
        }else {
            header_img.setImageDrawable(animationPullFan);
            animationPullFan.start();
        }
    }

    @Override
    public void onStartAnim() {
        header_img.setImageDrawable(animationRefresh);
        animationRefresh.start();
    }

    @Override
    public void onFinishAnim() {
        if (pullAnimSrcs !=null&& pullAnimSrcs.length>0)
            header_img.setImageResource(pullAnimSrcs[0]);
    }

}
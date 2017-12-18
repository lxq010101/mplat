package com.ustcinfo.mobile.platform.widget;



import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liaoinstan.springview.R.id;
import com.liaoinstan.springview.R.layout;
import com.liaoinstan.springview.container.BaseFooter;
import com.ustcinfo.mobile.platform.R;

/**
 * Created by 学祺 on 2017/3/22.
 */
public class MplatAnimRefreshFooter extends BaseFooter {
    private AnimationDrawable animationLoading;
    private Context context;
    private ImageView footer_img;
    private int[] loadingAnimSrcs;

    public MplatAnimRefreshFooter(Context context) {
        this(context, (int[])null);
    }

    public MplatAnimRefreshFooter(Context context, int[] loadingAnimSrcs) {
        this.loadingAnimSrcs = new int[]{R.mipmap.load01, R.mipmap.load02,R.mipmap.load03,R.mipmap.load04,R.mipmap.load05};
        this.context = context;
        if(loadingAnimSrcs != null) {
            this.loadingAnimSrcs = loadingAnimSrcs;
        }

        this.animationLoading = new AnimationDrawable();
        int[] var3 = this.loadingAnimSrcs;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int src = var3[var5];
            this.animationLoading.addFrame(ContextCompat.getDrawable(context, src), 150);
            this.animationLoading.setOneShot(false);
        }

    }

    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(layout.meituan_footer, viewGroup, true);
        this.footer_img = (ImageView)view.findViewById(id.meituan_footer_img);
        if(this.animationLoading != null) {
            this.footer_img.setImageDrawable(this.animationLoading);
        }

        return view;
    }

    public void onPreDrag(View rootView) {
        this.animationLoading.stop();
        if(this.animationLoading != null && this.animationLoading.getNumberOfFrames() > 0) {
            this.footer_img.setImageDrawable(this.animationLoading.getFrame(0));
        }

    }

    public void onDropAnim(View rootView, int dy) {
    }

    public void onLimitDes(View rootView, boolean upORdown) {
    }

    public void onStartAnim() {
        if(this.animationLoading != null) {
            this.footer_img.setImageDrawable(this.animationLoading);
        }

        this.animationLoading.start();
    }

    public void onFinishAnim() {
        this.animationLoading.stop();
        if(this.animationLoading != null && this.animationLoading.getNumberOfFrames() > 0) {
            this.footer_img.setImageDrawable(this.animationLoading.getFrame(0));
        }

    }
}

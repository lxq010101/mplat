package com.ustcinfo.mobile.platform.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

public class CustomLinearLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = false;

    public CustomLinearLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }


    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
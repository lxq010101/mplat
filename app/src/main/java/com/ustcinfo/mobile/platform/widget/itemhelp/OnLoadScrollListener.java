package com.ustcinfo.mobile.platform.widget.itemhelp;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract  class OnLoadScrollListener extends RecyclerView.OnScrollListener {
    boolean isSlidingToLast = false;


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
            int totalItemCount = manager.getItemCount();
            if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                //加载更多功能的代码
                load();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) {
            isSlidingToLast = true;
        } else {
            isSlidingToLast = false;
        }
    }

    public abstract void load();
}

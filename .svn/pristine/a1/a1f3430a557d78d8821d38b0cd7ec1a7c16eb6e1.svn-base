package com.ustcinfo.mobile.platform.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by SunChao on 2017/8/9.
 * 该自定义view创建目的是为了解决GridView与ScrollView混合使用时显示不全的问题
 */

public class DivideLineGridView extends GridView {
    public DivideLineGridView(Context context) {
        super(context);
    }

    public DivideLineGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DivideLineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

package com.ustcinfo.mobile.platform.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ustcinfo.mobile.platform.R;

/**
 * Created by li.qiya@ustcinfo.com on 17/08/10.
 * Description : custom layout to draw conner mark
 */
public class CornerMarkView extends FrameLayout {

    private Paint paint;
    private Path path;

    public TextView tvCount;

    public CornerMarkView(Context context) {
        super(context);
        init();
    }

    public CornerMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CornerMarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tvCount = new TextView(getContext());
        tvCount.setLayoutParams(params);
        tvCount.setTextSize(12);
        tvCount.setGravity(Gravity.CENTER);
        tvCount.setBackgroundResource(R.drawable.shape_wait_handler_tip);
        tvCount.setTextColor(Color.WHITE);

        addView(tvCount);
    }

    public void setConnerMarkText(String count) {
        tvCount.setText(count);
    }
}

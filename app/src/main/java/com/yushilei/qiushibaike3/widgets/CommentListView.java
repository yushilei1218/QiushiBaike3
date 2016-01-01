package com.yushilei.qiushibaike3.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by yushilei on 2015/12/30.
 */
public class CommentListView extends ListView {
    public CommentListView(Context context) {
        this(context, null);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE /2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }
}

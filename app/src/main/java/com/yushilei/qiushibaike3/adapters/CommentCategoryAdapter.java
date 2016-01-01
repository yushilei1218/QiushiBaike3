package com.yushilei.qiushibaike3.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yushilei.qiushibaike3.widgets.CommentListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushilei on 2015/12/30.
 */
public class CommentCategoryAdapter extends PagerAdapter {
    private List<CommentListView> list;
    private Context context;

    public CommentCategoryAdapter(Context context, List<CommentListView> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
        list.get(position).setLayoutParams(layoutParams);

        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof CommentListView) {
            CommentListView view = (CommentListView) object;
            container.removeView(view);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return (CharSequence) list.get(position).getTag();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}

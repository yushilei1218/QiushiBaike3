package com.yushilei.qiushibaike3.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by yushilei on 2016/1/3.
 */
public class BannerAdapter extends PagerAdapter implements View.OnClickListener {
    private Context context;
    private List<Integer> list;

    public BannerAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null && list.size() > 0) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(list.get(position % list.size()));

        imageView.setOnClickListener(this);
        imageView.setTag(position % list.size());

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView v = (ImageView) object;
        container.removeView(v);
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        Toast.makeText(context, "点击了第" + tag + "个", Toast.LENGTH_SHORT).show();
    }
}

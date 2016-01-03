package com.yushilei.qiushibaike3.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yushilei.qiushibaike3.R;
import com.yushilei.qiushibaike3.adapters.BannerAdapter;
import com.yushilei.qiushibaike3.adapters.DiscoverItemAdapter;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager pager;
    private LinearLayout indicatorContainer;
    private GridView itemGrid;
    private PtrClassicFrameLayout ptrView;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager = (ViewPager) view.findViewById(R.id.discover_view_pager);
        indicatorContainer = (LinearLayout) view.findViewById(R.id.discover_indicator_container);
        itemGrid = (GridView) view.findViewById(R.id.discover_item_grid_view);

        ptrView = (PtrClassicFrameLayout) view.findViewById(R.id.discover_ptr);

        //-------------设置头部banner
        List<Integer> list = new ArrayList<Integer>();
        list.add(R.mipmap.banner_1);
        list.add(R.mipmap.banner_2);
        list.add(R.mipmap.banner_3);
        list.add(R.mipmap.banner_4);
        pager.setAdapter(new BannerAdapter(getContext(), list));
        //初始化indicator
        for (int i = 0; i < list.size(); i++) {
            ImageView v = new ImageView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 10;
            v.setLayoutParams(lp);
            v.setImageResource(R.mipmap.page_indicator_unfocused);
            v.setTag(i);
            v.setOnClickListener(this);
            indicatorContainer.addView(v);
        }
        pager.addOnPageChangeListener(this);

        pager.setCurrentItem(Integer.MAX_VALUE / 2);
        //---------------设置业务分类部分
        List<Integer> mList = new ArrayList<Integer>();
        List<String> sList = new ArrayList<String>();
        initMList(mList, sList);
        itemGrid.setAdapter(new DiscoverItemAdapter(getContext(), mList, sList));
    }

    private void initMList(List<Integer> mList, List<String> sList) {
        mList.add(R.mipmap.grid_1);
        mList.add(R.mipmap.grid_2);
        mList.add(R.mipmap.grid_3);
        mList.add(R.mipmap.grid_4);
        mList.add(R.mipmap.grid_5);
        mList.add(R.mipmap.grid_6);
        mList.add(R.mipmap.grid_7);
        mList.add(R.mipmap.grid_8);
        sList.add("机票");
        sList.add("酒店客栈");
        sList.add("旅游度假");
        sList.add("签证");
        sList.add("门票");
        sList.add("火车票");
        sList.add("汽车票");
        sList.add("领里程");
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int count = indicatorContainer.getChildCount();
        int select = position % count;
        for (int i = 0; i < count; i++) {
            ImageView view = (ImageView) indicatorContainer.getChildAt(i);
            if (i == select) {
                view.setImageResource(R.mipmap.page_indicator_focused);
            } else {
                view.setImageResource(R.mipmap.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        Log.d("DiscoverFragment", "onClick");

        int tag = (int) v.getTag();
        Toast.makeText(getContext(), "点击了第" + tag + "banner", Toast.LENGTH_SHORT).show();
    }
}

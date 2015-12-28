package com.yushilei.qiushibaike3.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yushilei.qiushibaike3.R;
import com.yushilei.qiushibaike3.adapters.CommonPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QiushiFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager pager;
    private List<String> list;
    private CommonPagerAdapter adapter;


    public QiushiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qiushi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = (TabLayout) view.findViewById(R.id.qiushi_tab_layout);
        pager = (ViewPager) view.findViewById(R.id.qiushi_pager);
        list = new ArrayList<String>();
        initList();
        adapter = new CommonPagerAdapter(getChildFragmentManager(), list);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }

    private void initList() {
        list.add("专享");
        list.add("视频");
        list.add("纯文");
        list.add("纯图");
        list.add("精华");
        list.add("百科");
        list.add("糗事");
        list.add("隔壁");
        list.add("话题");
    }
}

package com.yushilei.qiushibaike3.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yushilei.qiushibaike3.fragments.ToQiushiFragment;

import java.util.List;

/**
 * Created by yushilei on 2015/12/28.
 */
public class CommonPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> list;

    public CommonPagerAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return ToQiushiFragment.newInstance(list.get(position));
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
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}

package com.yushilei.qiushibaike3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yushilei.qiushibaike3.adapters.ImagePageAdapter;

import java.util.ArrayList;
import java.util.List;

public class BootActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String COUNT = "count";
    private static final String APP_CONFIG = "app-config";
    private ImageView bootImage;
    private ImageView bootTitle;

    private ViewPager pager;
    private TextView bootTime;

    private boolean isFirstBoot;
    private boolean isFirstScrollToEnd;

    private static final int CODE_FINISHED = 1;
    private static final int CODE_COUNT_DOWN = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case CODE_FINISHED:
                    startActivity(new Intent(BootActivity.this, MainActivity.class));
                    finish();
                    break;
                case CODE_COUNT_DOWN:
                    bootTime.setVisibility(View.VISIBLE);
                    int arg1 = msg.arg1;
                    if (arg1 < 4) {
                        bootTime.setText(Integer.toString(5 - arg1) + "秒后跳转");
                    } else {
                        startActivity(new Intent(BootActivity.this, MainActivity.class));
                        finish();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);

        bootImage = (ImageView) findViewById(R.id.boot_image);
        bootTitle = (ImageView) findViewById(R.id.boot_title);
        pager = (ViewPager) findViewById(R.id.boot_view_pager);
        bootTime = (TextView) findViewById(R.id.boot_time);


        SharedPreferences preferences = getSharedPreferences(APP_CONFIG, MODE_PRIVATE);
        int count = preferences.getInt(COUNT, 0);
        if (count <= 0) {
            // TODO: 2016/1/2 启动viewPager 第一次使用
            isFirstBoot = true;
            bootImage.setVisibility(View.GONE);
            bootTitle.setVisibility(View.GONE);
            bootTime.setVisibility(View.GONE);
        } else {
            // TODO: 2016/1/2 其他情况都启动扉页 加载动画效果 2秒后自动跳转
            isFirstBoot = false;
            pager.setVisibility(View.GONE);
            bootTime.setVisibility(View.GONE);

            bootImage.setImageResource(R.mipmap.q01_new);

        }
        if (count <= 5) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt(COUNT, ++count);
            edit.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstBoot) {
            // TODO: 2016/1/2 如果是第一次启动在 onResume
            List<Integer> list = new ArrayList<Integer>();
            initList(list);
            pager.setAdapter(new ImagePageAdapter(this, list));
            pager.addOnPageChangeListener(this);

        } else {
            //非第一次启动
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = handler.obtainMessage();
                    message.what = CODE_FINISHED;
                    handler.sendMessage(message);
                }
            }).start();
            // TODO: 2016/1/2 如果不是第一次启动 在onResume 加载动画 2秒后跳转
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.boot_image_anim);
            bootTitle.startAnimation(animation);
        }
    }

    private void initList(List<Integer> list) {
        list.add(R.mipmap.app_1);
        list.add(R.mipmap.app_2);
        list.add(R.mipmap.app_3);
        list.add(R.mipmap.app_4);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if (position == pager.getChildCount() && !isFirstScrollToEnd) {
            isFirstScrollToEnd = true;
            // TODO: 2016/1/2 倒计时开始
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = handler.obtainMessage();
                        message.what = CODE_COUNT_DOWN;
                        message.arg1 = i;
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

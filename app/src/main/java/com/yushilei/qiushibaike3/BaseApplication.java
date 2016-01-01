package com.yushilei.qiushibaike3;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by yushilei on 2015/12/30.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}

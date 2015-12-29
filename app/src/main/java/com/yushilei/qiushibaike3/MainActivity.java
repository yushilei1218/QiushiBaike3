package com.yushilei.qiushibaike3;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.yushilei.qiushibaike3.Utils.CircleTranform;
import com.yushilei.qiushibaike3.fragments.DiscoverFragment;
import com.yushilei.qiushibaike3.fragments.QiushiFragment;
import com.yushilei.qiushibaike3.fragments.QiuyouquanFragment;
import com.yushilei.qiushibaike3.fragments.SmallPagerFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private FrameLayout mainContainer;
    private QiushiFragment qiushiFragment;
    private QiuyouquanFragment qiuyouquanFragment;
    private DiscoverFragment discoverFragment;
    private SmallPagerFragment smallPagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        mainContainer = (FrameLayout) findViewById(R.id.main_container);
        //初始化头像
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, 0, 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();
        drawer.setDrawerListener(toggle);
        //向FrameLayout内部添加Fragment
        initShowFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initShowFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        qiushiFragment = new QiushiFragment();
        qiuyouquanFragment = new QiuyouquanFragment();
        discoverFragment = new DiscoverFragment();
        smallPagerFragment = new SmallPagerFragment();

        transaction.add(R.id.main_container, qiushiFragment);
        transaction.add(R.id.main_container, qiuyouquanFragment);
        transaction.add(R.id.main_container, discoverFragment);
        transaction.add(R.id.main_container, smallPagerFragment);
        transaction.show(qiushiFragment);
        transaction.hide(qiuyouquanFragment);
        transaction.hide(discoverFragment);
        transaction.hide(smallPagerFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Log.d("MainActivity", "onNavigationItemSelected");
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_qiushi:
                Toast.makeText(this, "qiushi", Toast.LENGTH_SHORT).show();
                transaction.show(qiushiFragment);
                transaction.hide(qiuyouquanFragment);
                transaction.hide(discoverFragment);
                transaction.hide(smallPagerFragment);
                break;
            case R.id.action_qiuyouquan:
                transaction.hide(qiushiFragment);
                transaction.show(qiuyouquanFragment);
                transaction.hide(discoverFragment);
                transaction.hide(smallPagerFragment);
                break;
            case R.id.action_discover:
                transaction.hide(qiushiFragment);
                transaction.hide(qiuyouquanFragment);
                transaction.show(discoverFragment);
                transaction.hide(smallPagerFragment);
                break;
            case R.id.action_small_pager:
                transaction.hide(qiushiFragment);
                transaction.hide(qiuyouquanFragment);
                transaction.hide(discoverFragment);
                transaction.show(smallPagerFragment);
                break;
            case R.id.action_my:
                break;
            case R.id.action_quit:
                ActivityCompat.finishAffinity(this);
                break;
        }
        transaction.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

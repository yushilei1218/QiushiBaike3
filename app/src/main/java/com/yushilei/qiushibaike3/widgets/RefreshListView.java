package com.yushilei.qiushibaike3.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yushilei.qiushibaike3.R;
import com.yushilei.qiushibaike3.interfaces.NeedRefreshCallBack;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by yushilei on 2015/12/27.
 */
public class RefreshListView extends LinearLayout implements View.OnTouchListener {
    private String refreshData;
    private ListView listView;
    private RelativeLayout header;

    private boolean loadOnce;
    private MarginLayoutParams mP;
    /**
     * 以下为该自定义View中的子View
     */
    private ImageView arrowUp;
    private ImageView arrowDown;
    private ImageView loadingIcon;
    private TextView headerTitle;
    private TextView headerDate;

    private boolean isAbleToPull;//判定当前listview的状态 看是否满足header的出现条件
    private boolean isNeedRefresh;//判定当前是否要刷新
    private boolean isRefreshIng;//是否正在刷新

    private boolean headerDown;
    private boolean headerUp;
    private boolean headerRealese;
    /**
     * 下拉刷新 阈值 当header下拉的距离>= 该值时 满足刷新条件
     */
    private static final float ARROW_CHANGE_SIZE = 240;

    private RotateAnimation rotateAnimation;
    private String data;
    private SimpleDateFormat dateFormat;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化操作 1: 把XML定义的子View加载到 该自定义View中
     */
    private void init(Context context, AttributeSet attrs) {
        //将指定的XML布局添加到该自定义View中
        header = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.refresh_header, null, true);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
        header.setLayoutParams(lp);
        //找到子View
        arrowUp = (ImageView) header.findViewById(R.id.header_arrow_up);
        arrowDown = (ImageView) header.findViewById(R.id.header_arrow);
        loadingIcon = (ImageView) header.findViewById(R.id.header_loading);
        headerTitle = (TextView) header.findViewById(R.id.header_title);
        headerDate = (TextView) header.findViewById(R.id.header_date);
        dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        //添加header到 该自定义View中
        addView(header, 0);
    }

    /**
     * 这个函数会被调用 调用的条件就是当子View的宽高 padding margin发生变化 影响到其他子View的时候
     * 在这里做了些初始化的操作:1 设置 header 的margin= - header的高度 (也就是隐藏起来)
     * 找到XML里面 添加的ListView 并给这个listView设置 onTouch监听器!!!这个很重要 是我们要处理的主要方法!!
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            loadOnce = true;
            mP = (MarginLayoutParams) header.getLayoutParams();
            mP.topMargin = -header.getHeight();
            listView = (ListView) getChildAt(1);
            listView.setOnTouchListener(this);
        }
    }

    private float yDown;//记录满足下拉动作 时 记录点击的Y
    private float yLastMove;//满足下拉动作 记录每次Move的Y

    /**
     * 我们需要监听ListView的touch事件,
     * DOWN: 手指按下的时候第一时间判定按下的时候是否满足下拉操作的前提条件,如果满足记录yDown
     * MOVE: 只有当DOWN满足下拉操作 MOVE才会计算 滑动的距离并根据 阈值 来设置 header样式,并设置isNeedRefresh
     * UP: 1:当手指抬起的一瞬间 判定是否 isNeedRefresh 如果要刷新 则触发刷新操作
     * 2: 如果不需要刷新 则 setHeaderToNormal
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //每次按下的一瞬间 先判定是否满足下拉刷新的前提条件
                isAbleToPull();

                Log.d("RefreshListView", "isAbleToPull=" + isAbleToPull);
                if (isAbleToPull) {
                    yDown = event.getY(); //如果满足 下拉操作条件记录点击的Y坐标
                    Log.d("RefreshListView", "yDown=" + yDown);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isAbleToPull) {//如果满足
                    float eY = event.getY();
                    yLastMove = eY;
                    float distance = eY - yDown;
                    if (distance > 0 && distance < ARROW_CHANGE_SIZE) {
                        // TODO: 2015/12/27
                        isNeedRefresh = false;
                        scrollDownHeader(distance);
                    } else if (distance >= ARROW_CHANGE_SIZE) {
                        scrollUpHeader(distance);
                        isNeedRefresh = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedRefresh && !isRefreshIng) {
                    //松开手指  需要刷新 并且  当前状态不是"正在刷新状态"
                    isRefreshIng = true;
                    if (callBack != null) {
                        startLoading();
                        callBack.needRefreshCallBack();
                    } else {
                        setHeaderToNormal(false);
                    }
                } else if (isRefreshIng) {
                    //如果现在正在刷新. header的样式将不发生变化  donothing
                } else {
                    setHeaderToNormal(false);
                }
                break;
        }
        return false;
    }

    /**
     * 判定ListView当前位置是否满足下拉操作的前提条件
     * 条件1: 自定义listView中第一个可显示的
     * 条件2: 只有当ListView的top=0 ,也就是说只有listView的头部与父容器相同时
     * 其他任何时候都不满足该条件的!!!
     */
    private void isAbleToPull() {
        int position = listView.getFirstVisiblePosition(); //第一个显示的必须是0
        View childAt = listView.getChildAt(0);
        int top1 = childAt.getTop();
        int top = listView.getTop();
        float y = listView.getY();
        float translationY = listView.getTranslationY();
        Log.d("RefreshListView", " :top=" + top + " getY=" + y + " tY=" + translationY + " 子View top=" + top1);


        if (position == 0 && top1 == 0) {
            if (!isAbleToPull) {
                isAbleToPull = true;
            }
        } else {
            isAbleToPull = false;
        }
    }

    /**
     * 当松开手指 并且 需要刷新的时候调用该方法 启动动画loading效果 并隐藏其他子View
     */
    private void startLoading() {
        //加载动画效果
        rotateAnimation = new RotateAnimation(0, 3600, loadingIcon.getWidth() / 2f, loadingIcon.getHeight() / 2f);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        loadingIcon.startAnimation(rotateAnimation);
        //其他的textView隐藏起来
        arrowUp.setVisibility(INVISIBLE);
        arrowDown.setVisibility(INVISIBLE);
        loadingIcon.setVisibility(VISIBLE);
        //设置文本
        headerTitle.setText("正在刷新...");
        headerDate.setText("");
        //设置需要刷新的时候 header的位置
        mP.topMargin = 0;
        header.setLayoutParams(mP);
    }

    /**
     * 当检测到符合下拉操作header样式 并且 未到节点 ARROW_CHANGE_SIZE的时候
     *
     * @param distance
     */
    private void scrollDownHeader(float distance) {
        arrowUp.setVisibility(INVISIBLE);
        arrowDown.setVisibility(VISIBLE);
        loadingIcon.setVisibility(INVISIBLE);
        Log.d("RefreshListView", "loadingIcon.setVisibility 隐藏");

        headerTitle.setText("下拉刷新");
        // headerDate.setText("2015年12月27号 下午15:23:23");
        mP.topMargin = (int) (-header.getHeight() + distance / 2);
        header.setLayoutParams(mP);
    }

    /**
     * 当检测到符合下拉操作header样式 并且 已经超过节点 ARROW_CHANGE_SIZE的时候
     *
     * @param distance
     */
    private void scrollUpHeader(float distance) {
        arrowUp.setVisibility(VISIBLE);
        arrowDown.setVisibility(INVISIBLE);
        loadingIcon.setVisibility(INVISIBLE);
        headerTitle.setText("松开刷新");
        //headerDate.setText("2015年12月27号 下午15:23:23");
        mP.topMargin = (int) (-header.getHeight() + distance / 2);
        header.setLayoutParams(mP);
    }

    /**
     * 把header的状态设置到 初始状态  供内部 和外部调用
     */
    public void setHeaderToNormal(boolean isRefreshed) {
        //每次清除loadingView上的动画 以便可以正常隐藏
        loadingIcon.clearAnimation();
        //设置状态参数 初始化
        isNeedRefresh = false;
        isRefreshIng = false;
        //设置子View状态初始化
        mP.topMargin = -header.getHeight();
        header.setLayoutParams(mP);
        arrowUp.setVisibility(INVISIBLE);
        arrowDown.setVisibility(VISIBLE);
        loadingIcon.setVisibility(INVISIBLE);

        headerTitle.setText("下拉刷新");
        if (isRefreshed) {
            long timeMillis = System.currentTimeMillis();
            data = dateFormat.format(new Date(timeMillis));
            headerDate.setText(data);
        } else {
            headerDate.setText("啦啦啦.....");
        }
    }

    private NeedRefreshCallBack callBack;

    /**
     * 设置监听接口 当自定义View发现应该刷新的时候 告诉this 需要刷新
     *
     * @param callBack
     */
    public void setNeedRefreshListener(NeedRefreshCallBack callBack) {
        this.callBack = callBack;
    }


}

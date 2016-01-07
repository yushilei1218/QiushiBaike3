package com.yushilei.qiushibaike3;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yushilei.qiushibaike3.Utils.CircleTranform;
import com.yushilei.qiushibaike3.Utils.HttpUtils;
import com.yushilei.qiushibaike3.Utils.UrlFormat;
import com.yushilei.qiushibaike3.adapters.CommentsItemAdapter;
import com.yushilei.qiushibaike3.entitys.CommentsResponse;
import com.yushilei.qiushibaike3.entitys.SuggestResponse;
import com.yushilei.qiushibaike3.widgets.CommentListView;

import java.io.Serializable;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CommentsActivity extends AppCompatActivity implements Callback<CommentsResponse>, AbsListView.OnScrollListener, View.OnTouchListener {
    private CommentListView commentsList;
    private CommentsItemAdapter adapter;

    private ScrollView scrollView;
    private TextView userName;
    private ImageView userIcon1;
    private TextView hotLevel;
    private ImageView hotIcon;
    private TextView content;
    private ImageView contentImage;
    private TextView commentPart;
    private ImageView supportIcon;
    private ImageView unSupportIcon;
    private int pageNo = 1;

    private boolean isAbleToLoad;//能否继续加载更多comments
    private boolean isBottomRefreshing;
    private long userId;
    private Call<CommentsResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        //设置ActionBar
        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        Serializable item = intent.getSerializableExtra("item");

        if (item != null) {
            if (item instanceof SuggestResponse.ItemsEntity) {
                SuggestResponse.ItemsEntity entity = (SuggestResponse.ItemsEntity) item;
                userId = entity.getId();
                //设置actionBar
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setTitle("糗事 " + Long.toString(userId));
                }
                scrollView = (ScrollView) findViewById(R.id.comments_scroll_view);
                userIcon1 = (ImageView) findViewById(R.id.comments_icon);
                userName = (TextView) findViewById(R.id.comments_name);
                hotLevel = (TextView) findViewById(R.id.comments_hot_level);
                hotIcon = (ImageView) findViewById(R.id.comments_hot_icon);
                content = (TextView) findViewById(R.id.comments_content);
                contentImage = (ImageView) findViewById(R.id.comments_image);
                commentPart = (TextView) findViewById(R.id.comments_comment_part);
                supportIcon = (ImageView) findViewById(R.id.comments_support_icon);
                unSupportIcon = (ImageView) findViewById(R.id.comments_unsupport_icon);

                commentsList = (CommentListView) findViewById(R.id.comments_comment_list);
                adapter = new CommentsItemAdapter(this);
                commentsList.setAdapter(adapter);
                //处理下拉加载更多comments
                View view = LayoutInflater.from(this).inflate(R.layout.list_view_footer, null, true);
                commentsList.addFooterView(view, null, false);

                //commentsList.setOnScrollListener(this);
                scrollView.setOnTouchListener(this);


                initLayout(entity);
                //初始化CommentList数据 默认进来请求第一页数据
                call = HttpUtils.getService().getComments(entity.getId(), pageNo);
                call.enqueue(this);

            }
        }
    }

    private void initLayout(SuggestResponse.ItemsEntity entity) {
        if (entity.getUser() != null) {
            userName.setText(entity.getUser().getLogin());
            Picasso.with(this)
                    .load(UrlFormat.getIconUrl(entity.getUser().getId(), entity.getUser().getIcon()))
                    .transform(new CircleTranform())
                    .into(userIcon1);
        } else {
            userName.setText("匿名用户");
            userIcon1.setImageResource(R.mipmap.default_image);
        }
        content.setText(entity.getContent());

        if (entity.getImage() != null) {
            Picasso.with(this).load(UrlFormat.getImageUrl(entity.getImage()))
                    .resize(getWindowManager().getDefaultDisplay().getWidth(), 0)
                    .placeholder(R.mipmap.placeholder_image)
                    .error(R.mipmap.error_image)
                    .into(contentImage);
        } else {
            contentImage.setVisibility(View.GONE);
        }
        //设置 hotLevel
        if (entity.getType() != null) {
            String type = entity.getType();
            hotIcon.setVisibility(View.VISIBLE);
            hotLevel.setVisibility(View.VISIBLE);
            if (type.equals("hot")) {
                hotIcon.setImageResource(R.mipmap.ic_rss_hot);
                hotLevel.setText("热贴");
            } else if (type.equals("fresh")) {
                hotIcon.setImageResource(R.mipmap.hot_level_fresh);
                hotLevel.setText("新鲜");
            } else {
                hotIcon.setVisibility(View.GONE);
                hotLevel.setVisibility(View.GONE);
            }
        } else {
            hotIcon.setVisibility(View.GONE);
            hotLevel.setVisibility(View.GONE);
        }

        StringBuffer sb = new StringBuffer();
        if (entity.getVotes().getUp() > 0) {
            sb.append("好评 " + entity.getVotes().getUp()).append(".");
        }
        if (entity.getComments_count() > 0) {
            sb.append("评论 " + entity.getComments_count()).append(".");
        }
        if (entity.getShare_count() > 0) {
            sb.append("分享 " + entity.getShare_count());
        }
        commentPart.setText(sb.toString());
        boolean isSupport = entity.isSupport();
        boolean isUnSupport = entity.isUnSupport();
        if (isSupport) {
            supportIcon.setSelected(true);
        } else {
            supportIcon.setSelected(false);
        }
        if (isUnSupport) {
            unSupportIcon.setSelected(true);
        } else {
            unSupportIcon.setSelected(false);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Response<CommentsResponse> response, Retrofit retrofit) {
        if (response != null) {
            if (response.body().getCount() < 20) {
                isAbleToLoad = false;
            } else {
                isAbleToLoad = true;
            }
            adapter.addAll(response.body().getItems());
        }
        isBottomRefreshing = false;
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        isBottomRefreshing = false;
        Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int i = totalItemCount - (firstVisibleItem + visibleItemCount);
        Log.d("CommentsActivity", "firstVisibleItem" + firstVisibleItem + " visibleItemCount" + visibleItemCount +
                " totalItemCount= " + totalItemCount);

        if (i <= 0 && isAbleToLoad && !isBottomRefreshing) {
            Log.d("CommentsActivity", "下拉刷新");

            isBottomRefreshing = true;
            call = HttpUtils.getService().getComments(userId, ++pageNo);
            call.enqueue(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        call.cancel();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        View view = scrollView.getChildAt(0);

        float scaleY = scrollView.getScaleY();
        float eventY = event.getY();
        float rawY = event.getRawY();
        int height = view.getHeight();
        float scaleY1 = view.getScaleY();
        int top = view.getTop();

        Log.d("CommentsActivity", " height=" + height +
                " event.getY()=" + eventY + " rawY=" + rawY + " scaleY1" + scaleY1 + " viewtop=" + top);


        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}

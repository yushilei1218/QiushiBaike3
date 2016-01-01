package com.yushilei.qiushibaike3.fragments;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
import com.yushilei.qiushibaike3.CommentsActivity;
import com.yushilei.qiushibaike3.R;
import com.yushilei.qiushibaike3.Utils.DBUtils;
import com.yushilei.qiushibaike3.Utils.HttpUtils;
import com.yushilei.qiushibaike3.adapters.ToQiushiItemAdapter;
import com.yushilei.qiushibaike3.entitys.SuggestResponse;
import com.yushilei.qiushibaike3.entitys.ZhuangxiangResponse;
import com.yushilei.qiushibaike3.interfaces.DBLoadFinishCallBack;
import com.yushilei.qiushibaike3.interfaces.NeedRefreshCallBack;
import com.yushilei.qiushibaike3.interfaces.ZhuanxiangService;
import com.yushilei.qiushibaike3.loader.DBAsyncLoadTask;
import com.yushilei.qiushibaike3.loader.ItemAsyncLoader;
import com.yushilei.qiushibaike3.widgets.RefreshListView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToQiushiFragment extends Fragment implements Callback<SuggestResponse>, NeedRefreshCallBack, View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, LoaderManager.LoaderCallbacks<List<SuggestResponse.ItemsEntity>>, DBLoadFinishCallBack {
    private static final String TEXT = "text";
    private String type;
    private int pageNo;

    private ToQiushiItemAdapter adapter;

    private RefreshListView refreshListView;
    private ListView refreshList;

    private boolean isBottomRefreshing;
    private boolean isTopRefreshing;
    private boolean loadOnce;

    public ToQiushiFragment() {
        // Required empty public constructor
    }

    public static ToQiushiFragment newInstance(String text) {
        ToQiushiFragment fragment = new ToQiushiFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TEXT, text);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_qiushi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshList = (ListView) view.findViewById(R.id.toqiushi_list_refresh);
        refreshListView = (RefreshListView) view.findViewById(R.id.toqiushi_refresh_view);
        refreshListView.setNeedRefreshListener(this);

        Bundle arguments = getArguments();
        if (arguments.containsKey(TEXT)) {
            String s = arguments.getString(TEXT);
            type = s;
            //获取传入的Type类型
            if ("专享".equals(s)) {
                type = "suggest";
            } else if ("视频".equals(s)) {
                type = "video";
            } else if ("纯文".equals(s)) {
                type = "text";
            } else if ("纯图".equals(s)) {
                type = "image";
            } else if ("精华".equals(s)) {
                type = "latest";
            }
            if (type != null) {
                adapter = new ToQiushiItemAdapter(getContext(), new ArrayList<SuggestResponse.ItemsEntity>());
                refreshList.setAdapter(adapter);

                adapter.setOnClickListener(this);
                //item点击事件处理
                refreshList.setOnItemClickListener(this);
                //listView 滑动到最后加载更多处理
                refreshList.setOnScrollListener(this);

                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.list_view_footer, null, false);
                refreshList.addFooterView(inflate, null, false);
// TODO: 2016/1/1
//                LoaderManager manager = getLoaderManager();
//                Bundle bundle = new Bundle();
//                bundle.putString("type", type);
//                bundle.putInt("pageNo", 1);
//                bundle.putInt("pageSize", 20);
//                manager.initLoader(998, bundle, this);
                boolean canLoadFromDB = DBUtils.isCanLoadFromDB(getContext());
                if (canLoadFromDB) {
                    new DBAsyncLoadTask(getContext(), this).execute("StartLoadDataFromDB");
                    Log.d("ToQiushiFragment", "从数据库加载");

                } else {
                    //初始化调用
                    Call<SuggestResponse> article = HttpUtils.getService().getArticle(type, ++pageNo, 20);
                    article.enqueue(this);
                    Log.d("ToQiushiFragment", "从网络加载");
                }
            }
        }
    }

    @Override
    public void onResponse(Response<SuggestResponse> response, Retrofit retrofit) {
        Log.d("ToQiushiFragment", "onResponse");

        if (response.body().getItems() != null) {
            if (pageNo == 1) {
                adapter.clearAll();
            }
            adapter.addAll(response.body().getItems());
            if (!DBUtils.isCanLoadFromDB(getContext())) {
                Log.d("ToQiushiFragment", "存数据库");

                DBUtils.itemsListInsert(getContext(), response.body().getItems());
            }
        }
        refreshListView.setHeaderToNormal(true);
        isBottomRefreshing = false;
        isTopRefreshing = false;
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        refreshListView.setHeaderToNormal(true);
        isBottomRefreshing = false;
        isTopRefreshing = false;
        Toast.makeText(getContext(), "网络问题", Toast.LENGTH_SHORT).show();
    }

    /**
     * refreshListView 回调 :下拉触发需要刷新 一定是刷新第一页
     */
    @Override
    public void needRefreshCallBack() {
        if (!isTopRefreshing && !isBottomRefreshing) {
            isTopRefreshing = true;
            pageNo = 1;
            Call<SuggestResponse> article = HttpUtils.getService().getArticle(type, pageNo, 20);
            article.enqueue(this);
        }
    }

    /**
     * ListView Item内部控件点击回调处理
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Object tag = v.getTag();
        if (tag != null) {
            if (tag instanceof Integer) {
                int position = (int) tag;
                SuggestResponse.ItemsEntity item =
                        (SuggestResponse.ItemsEntity) adapter.getItem(position);
                switch (id) {
                    case R.id.toqiushi_comments_icon:
                        int count = item.getComments_count();
                        if (count > 0) {
                            startCommentsActivity(item);
                        }
                        break;
                    case R.id.toqiushi_support_icon:
                        item.setIsSupport(true);
                        item.setIsUnSupport(false);
                        adapter.notifyDataSetChanged();
                        startSelectedAnimation(v);
                        break;
                    case R.id.toqiushi_unsupport_icon:
                        item.setIsSupport(false);
                        item.setIsUnSupport(true);
                        adapter.notifyDataSetChanged();
                        startSelectedAnimation(v);
                        break;
                }
            }
        }

    }

    /**
     * View的点击动画处理
     */

    private void startSelectedAnimation(View v) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.comments_image_pressed);
        v.startAnimation(animation);
    }

    /**
     * ListView 点击跳转  回调
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SuggestResponse.ItemsEntity itemAtPosition = (SuggestResponse.ItemsEntity) parent.getItemAtPosition(position);
        startCommentsActivity(itemAtPosition);
    }

    /**
     * 评论Activity的跳转
     *
     * @param entity
     */
    private void startCommentsActivity(SuggestResponse.ItemsEntity entity) {
        int count = entity.getComments_count();
        if (count > 0) {
            Intent intent = new Intent(getContext(), CommentsActivity.class);
            intent.putExtra("item", entity);
            startActivity(intent);
        }
    }

    //----------ListView滑动监听处理 下拉加载更多
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int i = totalItemCount - (firstVisibleItem + visibleItemCount);
        if (i <= 0 && !isBottomRefreshing && !isTopRefreshing) {
            isBottomRefreshing = true;
            Call<SuggestResponse> article = HttpUtils.getService().getArticle(type, ++pageNo, 20);
            article.enqueue(this);
        }
    }

    //---------------------------尝试用loader的形式
    @Override
    public Loader<List<SuggestResponse.ItemsEntity>> onCreateLoader(int id, Bundle args) {
        Loader<List<SuggestResponse.ItemsEntity>> ret = null;
        if (args != null) {
            Log.d("ToQiushiFragment", "onCreateLoader");

            ret = new ItemAsyncLoader(getContext(), args.getString("type"),
                    args.getInt("pageNo"), args.getInt("pageSize"));
        }
        return ret;
    }

    @Override
    public void onLoadFinished(Loader<List<SuggestResponse.ItemsEntity>> loader, List<SuggestResponse.ItemsEntity> data) {
        Log.d("ToQiushiFragment", "onLoadFinished");
        if (data != null && data.size() > 0) {


            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<SuggestResponse.ItemsEntity>> loader) {

    }

    //------------------------s数据库有数据 查询完毕接口回调
    @Override
    public void dbLoadFinishCallBack(List<SuggestResponse.ItemsEntity> entityList) {
        adapter.addAll(entityList);
    }
}

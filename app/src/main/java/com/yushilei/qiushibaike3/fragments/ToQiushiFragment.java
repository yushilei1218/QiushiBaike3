package com.yushilei.qiushibaike3.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.yushilei.qiushibaike3.R;
import com.yushilei.qiushibaike3.adapters.ToQiushiItemAdapter;
import com.yushilei.qiushibaike3.entitys.ZhuangxiangResponse;
import com.yushilei.qiushibaike3.interfaces.NeedRefreshCallBack;
import com.yushilei.qiushibaike3.interfaces.ZhuanxiangService;
import com.yushilei.qiushibaike3.widgets.RefreshListView;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToQiushiFragment extends Fragment implements Callback<ZhuangxiangResponse>, NeedRefreshCallBack {
    private static final String TEXT = "text";
    private String type;
    private int pageNo;

    private TextView textView;

    private ListView listView;
    private Call<ZhuangxiangResponse> call;
    private ToQiushiItemAdapter adapter;

    private RefreshListView refreshListView;
    private ListView refreshList;
    private ZhuanxiangService service;

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
            //获取传入的Tpye类型
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
                //普通list
                listView = (ListView) view.findViewById(R.id.toqiushi_list);
                adapter = new ToQiushiItemAdapter(getContext(), new ArrayList<ZhuangxiangResponse.ItemsEntity>());
                // TODO: 2015/12/29
                refreshList.setAdapter(adapter);
                //listView.setAdapter(adapter);

                //使用Retrofit 来进行网络数据的加载,  使用Gson 来解析数据
                Retrofit build = new Retrofit.Builder().baseUrl("http://m2.qiushibaike.com")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                service = build.create(ZhuanxiangService.class);
                //默认网络请求
                call = service.getResponse(type, ++pageNo);
                call.enqueue(this);
            }
        }
    }

    @Override
    public void onResponse(Response<ZhuangxiangResponse> response, Retrofit retrofit) {
        adapter.addAll(response.body().getItems());
        refreshListView.setHeaderToNormal();
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
    }

    /**
     * 下拉触发需要刷新
     */
    @Override
    public void needRefreshCallBack() {
        Call<ZhuangxiangResponse> call = service.getResponse(type, ++pageNo);
        call.enqueue(this);
    }
}

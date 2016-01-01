package com.yushilei.qiushibaike3.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.yushilei.qiushibaike3.Utils.DBUtils;
import com.yushilei.qiushibaike3.Utils.HttpUtils;
import com.yushilei.qiushibaike3.entitys.SuggestResponse;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by yushilei on 2016/1/1.
 */
public class ItemAsyncLoader extends AsyncTaskLoader<List<SuggestResponse.ItemsEntity>> implements Callback<SuggestResponse> {
    private String type;
    private int pageSize;
    private int pageNo;

    public ItemAsyncLoader(Context context, String type, int pageNo, int pageSize) {
        super(context);
        this.type = type;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<SuggestResponse.ItemsEntity> loadInBackground() {
        List<SuggestResponse.ItemsEntity> ret = null;
        //优先取数据库
        ret = DBUtils.queryAll(getContext());
        if (ret != null && ret.size() > 0) {
            Log.d("ItemAsyncLoader", "数据库获取的数据");
            return ret;
        } else {
            Log.d("ItemAsyncLoader", "网络请求的数据");

            Call<SuggestResponse> call = HttpUtils.getService().getArticle(type, pageNo, pageSize);
            call.enqueue(this);
        }
        return ret;
    }

    @Override
    public void onResponse(Response<SuggestResponse> response, Retrofit retrofit) {
        if (response != null) {
            List<SuggestResponse.ItemsEntity> items = response.body().getItems();
            //插入数据库
            DBUtils.itemsListInsert(getContext(), items);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
    }
}

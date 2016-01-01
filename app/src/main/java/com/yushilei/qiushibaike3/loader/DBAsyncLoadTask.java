package com.yushilei.qiushibaike3.loader;

import android.content.Context;
import android.os.AsyncTask;

import com.yushilei.qiushibaike3.Utils.DBUtils;
import com.yushilei.qiushibaike3.entitys.SuggestResponse;
import com.yushilei.qiushibaike3.interfaces.DBLoadFinishCallBack;

import java.util.List;

/**
 * Created by yushilei on 2016/1/1.
 */
public class DBAsyncLoadTask extends AsyncTask<String, Void, List<SuggestResponse.ItemsEntity>> {
    private Context context;
    private DBLoadFinishCallBack callBack;


    public DBAsyncLoadTask(Context context, DBLoadFinishCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    protected List<SuggestResponse.ItemsEntity> doInBackground(String... params) {
        //数据库中读取数据
        List<SuggestResponse.ItemsEntity> ret = null;
        List<SuggestResponse.ItemsEntity> itemsEntities = DBUtils.queryAll(context);
        if (itemsEntities != null && itemsEntities.size() > 0) {
            ret = itemsEntities;
        }
        return ret;
    }

    @Override
    protected void onPostExecute(List<SuggestResponse.ItemsEntity> entityList) {
        if (entityList != null && entityList.size() > 0) {
            callBack.dbLoadFinishCallBack(entityList);
        }
    }
}

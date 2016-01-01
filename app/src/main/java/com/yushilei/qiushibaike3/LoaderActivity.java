package com.yushilei.qiushibaike3;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.yushilei.qiushibaike3.entitys.SuggestResponse;
import com.yushilei.qiushibaike3.loader.ItemAsyncLoader;

import java.util.List;

public class LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<SuggestResponse.ItemsEntity>> {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        listView = (ListView) findViewById(R.id.loader_list_view);




        LoaderManager manager = getSupportLoaderManager();
        // TODO: 2015/12/31  测试
        Bundle bundle = new Bundle();
        bundle.putString("type", "video");
        bundle.putInt("pageNo", 1);
        bundle.putInt("pageSize", 20);
        manager.initLoader(998, bundle, this);

    }

    @Override
    public Loader<List<SuggestResponse.ItemsEntity>> onCreateLoader(int id, Bundle args) {
        Loader<List<SuggestResponse.ItemsEntity>> ret = null;
        if (args != null && args.containsKey("type") && args.containsKey("pageSize")) {
            ret = new ItemAsyncLoader(this, args.getString("type"),
                    args.getInt("pageNo"), args.getInt("pageSize"));
        }
        return ret;
    }

    @Override
    public void onLoadFinished(Loader<List<SuggestResponse.ItemsEntity>> loader, List<SuggestResponse.ItemsEntity> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<SuggestResponse.ItemsEntity>> loader) {

    }
}

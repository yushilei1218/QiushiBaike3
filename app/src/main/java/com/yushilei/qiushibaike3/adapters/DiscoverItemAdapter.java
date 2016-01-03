package com.yushilei.qiushibaike3.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yushilei.qiushibaike3.R;

import java.util.List;

/**
 * Created by yushilei on 2016/1/3.
 */
public class DiscoverItemAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> list;
    private List<String> stringList;

    public DiscoverItemAdapter(Context context, List<Integer> list, List<String> stringList) {
        this.context = context;
        this.list = list;
        this.stringList = stringList;
    }

    @Override
    public int getCount() {


        int i = 0;
        if (list != null && stringList != null) {
            i = list.size() > stringList.size() ? list.size() : stringList.size();
        }
        Log.d("DiscoverItemAdapter", "getCount=" + i);
        return i;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.discover_item, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.itemIcon.setImageResource(list.get(position));
        holder.itemTitle.setText(stringList.get(position));

        return convertView;
    }

    private class ViewHolder {
        public ImageView itemIcon;
        public TextView itemTitle;

        public ViewHolder(View view) {
            itemIcon = (ImageView) view.findViewById(R.id.discover_item_icon);
            itemTitle = (TextView) view.findViewById(R.id.discover_item_title);
        }
    }
}

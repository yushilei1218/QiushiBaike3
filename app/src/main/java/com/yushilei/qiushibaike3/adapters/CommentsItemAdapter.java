package com.yushilei.qiushibaike3.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yushilei.qiushibaike3.R;
import com.yushilei.qiushibaike3.Utils.CircleTranform;
import com.yushilei.qiushibaike3.Utils.UrlFormat;
import com.yushilei.qiushibaike3.entitys.CommentsResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yushilei on 2015/12/30.
 */
public class CommentsItemAdapter extends BaseAdapter {
    private Context context;
    private List<CommentsResponse.ItemsEntity> entityList;

    public CommentsItemAdapter(Context context) {
        this.context = context;
        entityList = new ArrayList<CommentsResponse.ItemsEntity>();
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (entityList != null) {
            ret = entityList.size();
        }
        Log.d("CommentsItemAdapter", "getCount" + ret);

        return ret;
    }

    @Override
    public Object getItem(int position) {
        return entityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comments_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        CommentsResponse.ItemsEntity itemsEntity = entityList.get(position);
        if (itemsEntity.getUser() != null) {
            holder.commentsUserName.setText(itemsEntity.getUser().getLogin());
        } else {
            holder.commentsUserName.setText("匿名用户");
        }
        holder.commentsContent.setText(itemsEntity.getContent());

        long created = itemsEntity.getCreated_at();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > created) {
            long minute = (currentTimeMillis - created) / (1000 * 1000 * 60);
            if (minute >= 60) {
                long hour = minute / 60;
                if (hour < 24) {
                    holder.commentsDate.setText(Long.toString(hour) + "小时前");
                } else {
                    long day = hour / 24;
                    holder.commentsDate.setText(Long.toString(day) + "天前");
                }
            } else if (minute < 60 && minute > 0) {
                holder.commentsDate.setText(Long.toString(minute) + "分钟前");
            } else {
                holder.commentsDate.setText("");
            }
        }
        //设置评论部分用户头像
        Picasso.with(context).load(
                UrlFormat.getIconUrl(itemsEntity.getUser().getId(), itemsEntity.getUser().getIcon()))
                .transform(new CircleTranform())
                .placeholder(R.mipmap.default_image)
                .error(R.mipmap.default_image)
                .into(holder.commentsUserIcon);
        return convertView;
    }

    public void addAll(Collection<? extends CommentsResponse.ItemsEntity> collection) {
        if (collection != null) {
            entityList.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void clearAll() {
        entityList.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public ImageView commentsUserIcon;
        public TextView commentsContent;
        public TextView commentsUserName;
        public TextView commentsDate;

        public ViewHolder(View view) {
            commentsUserIcon = (ImageView) view.findViewById(R.id.comments_item_icon);
            commentsContent = (TextView) view.findViewById(R.id.comments_item_content);
            commentsUserName = (TextView) view.findViewById(R.id.comments_item_name);
            commentsDate = (TextView) view.findViewById(R.id.comments_item_time);
        }
    }
}

package com.yushilei.qiushibaike3.adapters;

import android.content.Context;
import android.media.Image;
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
import com.yushilei.qiushibaike3.entitys.ZhuangxiangResponse;

import java.util.Collection;
import java.util.List;

/**
 * Created by yushilei on 2015/12/29.
 */
public class ToQiushiItemAdapter extends BaseAdapter {
    private Context context;
    private List<ZhuangxiangResponse.ItemsEntity> list;

    public ToQiushiItemAdapter(Context context, List<ZhuangxiangResponse.ItemsEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.to_qiushi_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        ZhuangxiangResponse.ItemsEntity entity = list.get(position);

        holder.content.setText(entity.getContent());

        if (entity.getUser() != null) {
            Log.d("ToQiushiItemAdapter", "getView");

            holder.userName.setText(entity.getUser().getLogin());
            Picasso.with(context)
                    .load(UrlFormat.getIconUrl(entity.getUser().getId(), entity.getUser().getIcon()))
                    .transform(new CircleTranform())
                    .into(holder.userIcon);
        } else {
            holder.userName.setText("匿名用户");
            holder.userIcon.setImageResource(R.mipmap.unlogin_icon);
        }
        if (entity.getImage() != null) {
            Picasso.with(context).load(UrlFormat.getImageUrl(entity.getImage()))
                    .resize(parent.getWidth(), 0)
                    .placeholder(R.mipmap.placeholder_image)
                    .error(R.mipmap.error_image)
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setVisibility(View.GONE);
        }
        //设置 hotLevel
        if (entity.getType() != null) {
            String type = entity.getType();
            holder.hotLevelIcon.setVisibility(View.VISIBLE);
            holder.hotLevelDesc.setVisibility(View.VISIBLE);
            if (type.equals("hot")) {
                holder.hotLevelIcon.setImageResource(R.mipmap.ic_rss_hot);
                holder.hotLevelDesc.setText("火热");
            } else if (type.equals("fresh")) {
                holder.hotLevelIcon.setImageResource(R.mipmap.hot_level_fresh);
                holder.hotLevelDesc.setText("新鲜");
            } else {
                holder.hotLevelIcon.setVisibility(View.GONE);
                holder.hotLevelDesc.setVisibility(View.GONE);
            }
        } else {
            holder.hotLevelIcon.setVisibility(View.GONE);
            holder.hotLevelDesc.setVisibility(View.GONE);
        }
        //设置 点评部分
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
        holder.userComment.setText(sb.toString());
        return convertView;
    }

    public void addAll(Collection<? extends ZhuangxiangResponse.ItemsEntity> collection) {
        list.addAll(0, collection);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView content;
        public TextView userName;
        public ImageView userIcon;
        public ImageView itemImage;

        public ImageView hotLevelIcon;
        public TextView hotLevelDesc;
        public TextView userComment;

        public ViewHolder(View view) {
            content = (TextView) view.findViewById(R.id.user_content);
            userName = (TextView) view.findViewById(R.id.user_name);
            userIcon = (ImageView) view.findViewById(R.id.user_icon);
            itemImage = (ImageView) view.findViewById(R.id.user_image);

            hotLevelIcon = (ImageView) view.findViewById(R.id.user_hot_icon);
            hotLevelDesc = (TextView) view.findViewById(R.id.user_hot_level);
            userComment = (TextView) view.findViewById(R.id.user_comment_part);
        }
    }
}

package com.yushilei.qiushibaike3.adapters;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;
import com.yushilei.qiushibaike3.R;
import com.yushilei.qiushibaike3.Utils.CircleTranform;
import com.yushilei.qiushibaike3.Utils.UrlFormat;
import com.yushilei.qiushibaike3.entitys.SuggestResponse;
import com.yushilei.qiushibaike3.entitys.ZhuangxiangResponse;

import java.util.Collection;
import java.util.List;

/**
 * Created by yushilei on 2015/12/29.
 */
public class ToQiushiItemAdapter extends BaseAdapter {
    private View.OnClickListener onClicklistener;
    private Context context;
    private List<SuggestResponse.ItemsEntity> list;

    public ToQiushiItemAdapter(Context context, List<SuggestResponse.ItemsEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        Log.d("ToQiushiItemAdapter", "getCount" + ret);

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

        SuggestResponse.ItemsEntity entity = list.get(position);

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
            holder.userIcon.setImageResource(R.mipmap.ic_launcher);
        }
        //视频图片 or content图片处理
        String format = entity.getFormat();
        if (format.equals("video")) {
            holder.videoConatainer.setVisibility(View.VISIBLE);
            Picasso.with(context).load(entity.getPic_url())
                    .resize(parent.getWidth(),0)
                    .placeholder(R.mipmap.placeholder_image)
                    .error(R.mipmap.error_image)
                    .into(holder.videoImage);
        } else if (entity.getImage() != null) {
            holder.videoConatainer.setVisibility(View.GONE);
            Picasso.with(context).load(UrlFormat.getImageUrl(entity.getImage()))
                    .resize(parent.getWidth(), 0)
                    .placeholder(R.mipmap.placeholder_image)
                    .error(R.mipmap.error_image)
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setVisibility(View.GONE);
            holder.videoConatainer.setVisibility(View.GONE);
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
        //处理评论 部分
        holder.imageComments.setTag(position);
        //处理 赞 un赞 部分
        boolean isSupport = entity.isSupport();
        boolean isUnSupport = entity.isUnSupport();
        if (isSupport) {
            holder.imageSupport.setSelected(true);
        } else {
            holder.imageSupport.setSelected(false);
        }
        if (isUnSupport) {
            holder.imageUnSupport.setSelected(true);
        } else {
            holder.imageUnSupport.setSelected(false);
        }
        holder.imageUnSupport.setTag(position);
        holder.imageSupport.setTag(position);

        return convertView;
    }

    public void addAll(Collection<? extends SuggestResponse.ItemsEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();
    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClicklistener = onClickListener;
    }

    private class ViewHolder {
        public TextView content;
        public TextView userName;
        public ImageView userIcon;
        public ImageView itemImage;
        //-------------
        public ImageView hotLevelIcon;
        public TextView hotLevelDesc;
        public TextView userComment;
        //------------评论点击部分
        public ImageView imageComments;
        public ImageView imageSupport;
        public ImageView imageUnSupport;
        //---
        public ImageView videoImage;
        public FrameLayout videoConatainer;

        public ViewHolder(View view) {
            content = (TextView) view.findViewById(R.id.user_content);
            userName = (TextView) view.findViewById(R.id.user_name);
            userIcon = (ImageView) view.findViewById(R.id.user_icon);
            itemImage = (ImageView) view.findViewById(R.id.user_image);
            videoImage = (ImageView) view.findViewById(R.id.user_video_image);
            videoConatainer = (FrameLayout) view.findViewById(R.id.user_video_container);

            hotLevelIcon = (ImageView) view.findViewById(R.id.user_hot_icon);
            hotLevelDesc = (TextView) view.findViewById(R.id.user_hot_level);
            userComment = (TextView) view.findViewById(R.id.user_comment_part);

            imageComments = (ImageView) view.findViewById(R.id.toqiushi_comments_icon);
            imageSupport = (ImageView) view.findViewById(R.id.toqiushi_support_icon);
            imageUnSupport = (ImageView) view.findViewById(R.id.toqiushi_unsupport_icon);

            imageSupport.setOnClickListener(onClicklistener);
            imageUnSupport.setOnClickListener(onClicklistener);
            imageComments.setOnClickListener(onClicklistener);
        }
    }
}

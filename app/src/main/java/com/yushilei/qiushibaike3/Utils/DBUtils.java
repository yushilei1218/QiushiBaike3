package com.yushilei.qiushibaike3.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.yushilei.qiushibaike3.db.DBHelper;
import com.yushilei.qiushibaike3.entitys.SuggestResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushilei on 2016/1/1.
 */
public class DBUtils {
    public static void itemsListInsert(Context context, List<SuggestResponse.ItemsEntity> entityList) {
        if (entityList != null && entityList.size() > 0) {
            for (int i = 0; i < entityList.size(); i++) {
                SuggestResponse.ItemsEntity entity = entityList.get(i);
                itemsInsert(context, entity);
            }
        }
    }

    //数据库插入操作
    public static Uri itemsInsert(Context context, SuggestResponse.ItemsEntity entity) {

        Uri ret = null;

        ContentValues values = new ContentValues();
        values.put("item_id", entity.getId());
        values.put("format", entity.getFormat());
        values.put("image", entity.getImage());
        values.put("content", entity.getContent());
        values.put("comments_count", entity.getComments_count());
        values.put("share_count", entity.getShare_count());
        values.put("type", entity.getType());
        values.put("pic_url", entity.getPic_url());

        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://item/items");
        ret = resolver.insert(uri, values);
        if (entity.getUser() != null) {
            userInsert(context, entity);
        }
        if (entity.getVotes() != null) {
            votesInsert(context, entity);
        }
        Log.d("DBUtils", "itemsInsert" + ret);

        return ret;
    }

    public static Uri userInsert(Context context, SuggestResponse.ItemsEntity entity) {
        Uri ret = null;
        SuggestResponse.ItemsEntity.UserEntity user = entity.getUser();
        ContentValues values = new ContentValues();
        values.put("_id", entity.getId());
        values.put("login", user.getLogin());
        values.put("user_id", user.getId());
        values.put("icon", user.getIcon());

        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://item/user");
        ret = resolver.insert(uri, values);
        return ret;
    }

    public static Uri votesInsert(Context context, SuggestResponse.ItemsEntity entity) {
        Uri ret = null;
        SuggestResponse.ItemsEntity.VotesEntity votes = entity.getVotes();

        ContentValues values = new ContentValues();
        values.put("_id", entity.getId());
        values.put("down", votes.getDown());
        values.put("up", votes.getUp());

        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://item/votes");
        ret = resolver.insert(uri, values);
        return ret;
    }

    //数据库查询
    public static List<SuggestResponse.ItemsEntity> queryAll(Context context) {
        List<SuggestResponse.ItemsEntity> entityList = new ArrayList<SuggestResponse.ItemsEntity>();
        Cursor cursor = null;
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://item/items");
        cursor = resolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SuggestResponse.ItemsEntity entity = new SuggestResponse.ItemsEntity();
                int index = cursor.getColumnIndex(DBHelper.item_id);
                int itemId = cursor.getInt(index);

                index = cursor.getColumnIndex(DBHelper.comments_count);
                int comments_count = cursor.getInt(index);
                index = cursor.getColumnIndex(DBHelper.format);
                String format = cursor.getString(index);
                index = cursor.getColumnIndex(DBHelper.image);
                String image = cursor.getString(index);
                index = cursor.getColumnIndex(DBHelper.content);
                String content = cursor.getString(index);
                index = cursor.getColumnIndex(DBHelper.type);
                String type = cursor.getString(index);
                index = cursor.getColumnIndex(DBHelper.share_count);
                int share_count = cursor.getInt(index);
                index = cursor.getColumnIndex(DBHelper.pic_url);
                String pic_url = cursor.getString(index);
                entity.setId(itemId);
                entity.setType(type);
                entity.setComments_count(comments_count);
                entity.setFormat(format);
                entity.setImage(image);
                entity.setContent(content);
                entity.setShare_count(share_count);
                entity.setPic_url(pic_url);


                uri = Uri.parse("content://item/user");
                // TODO: 2016/1/1
                Cursor userCursor = resolver.query(uri, null, "_id=?", new String[]{Integer.toString(itemId)}, null);
                if (userCursor != null && userCursor.getCount() > 0) {
                    SuggestResponse.ItemsEntity.UserEntity userEntity = new SuggestResponse.ItemsEntity.UserEntity();
                    userCursor.moveToNext();
                    index = userCursor.getColumnIndex(DBHelper.login);
                    String login = userCursor.getString(index);
                    index = userCursor.getColumnIndex(DBHelper.icon);
                    String icon = userCursor.getString(index);
                    index = userCursor.getColumnIndex(DBHelper.user_id);
                    int user_id = userCursor.getInt(index);

                    userEntity.setLogin(login);
                    userEntity.setId(user_id);
                    userEntity.setIcon(icon);

                    entity.setUser(userEntity);
                }
                if (userCursor != null) {
                    userCursor.close();
                }
                uri = Uri.parse("content://item/votes");
                // TODO: 2016/1/1
                Cursor votesCursor = resolver.query(uri, null, "_id=?", new String[]{Integer.toString(itemId)}, null);
                if (votesCursor != null && votesCursor.getCount() > 0) {
                    votesCursor.moveToNext();
                    SuggestResponse.ItemsEntity.VotesEntity votesEntity = new SuggestResponse.ItemsEntity.VotesEntity();
                    index = votesCursor.getColumnIndex(DBHelper.down);
                    int down = votesCursor.getInt(index);
                    index = votesCursor.getColumnIndex(DBHelper.up);
                    int up = votesCursor.getInt(index);
                    votesEntity.setDown(down);
                    votesEntity.setUp(up);
                    entity.setVotes(votesEntity);
                }
                if (votesCursor != null) {
                    votesCursor.close();
                }
                entityList.add(entity);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return entityList;
    }

    public static boolean isCanLoadFromDB(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://item/items");
        Cursor query = resolver.query(uri, null, null, null, null);
        if (query != null && query.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}

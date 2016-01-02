package com.yushilei.qiushibaike3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yushilei on 2016/1/1.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "qiushi.db";
    private static final int DB_VERSION = 1;
    //3张表 表名
    public static final String ITEMS = "items";
    public static final String USER = "user";
    public static final String VOTES = "votes";
    //字段名
    public static final String item_id = "item_id";
    public static final String format = "format";
    public static final String image = "image";
    public static final String content = "content";
    public static final String comments_count = "comments_count";
    public static final String pic_url = "pic_url";
    public static final String share_count = "share_count";
    public static final String type = "type";
    //新增字段用来区分 不同类型加载的类别
    public static final String item_type = "item_type";

    public static final String u_id = "_id";
    public static final String login = "login";
    public static final String user_id = "user_id";
    public static final String icon = "icon";

    public static final String votes_id = "_id";
    public static final String down = "down";
    public static final String up = "up";


    //创建3张表
    private static final String CREATE_TABLE_ITEMS = "create table " + ITEMS + " (" +
            "item_id integer primary key," +
            "item_type text," +
            "format text," +
            "image text," +
            "content text," +
            "comments_count integer," +
            "pic_url text," +
            "share_count integer," +
            "type text)";
    private static final String CREATE_TABLE_USER = "create table " + USER + " (" +
            "_id integer primary key," +
            "login text," +
            "user_id integer," +
            "icon text," +
            "foreign  key (_id) references items(item_id) )";
    private static final String CREATE_TABLE_VOTES = "create table " + VOTES + " (" +
            "_id integer primary key," +
            "down integer," +
            "up integer," +
            "foreign  key (_id) references items(item_id) )";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEMS);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_VOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

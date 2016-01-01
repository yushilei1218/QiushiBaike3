package com.yushilei.qiushibaike3.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.yushilei.qiushibaike3.db.DBHelper;

/**
 * Created by yushilei on 2016/1/1.
 */
public class ItemDataProvider extends ContentProvider {
    private DBHelper dbHelper;
    private static UriMatcher uriMatcher;

    private static final String ITEMS = "items";
    private static final String USER = "user";
    private static final String VOTES = "votes";

    private static final int CODE_ITEMS = 1;
    private static final int CODE_USER = 2;
    private static final int CODE_VOTES = 3;


    static {
        uriMatcher = new UriMatcher(0);
        uriMatcher.addURI("*", ITEMS, CODE_ITEMS);
        uriMatcher.addURI("*", USER, CODE_USER);
        uriMatcher.addURI("*", VOTES, CODE_VOTES);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor ret = null;
        int code = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (code) {
            case CODE_ITEMS:
                ret = db.query(DBHelper.ITEMS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_USER:
                ret = db.query(DBHelper.USER, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_VOTES:
                ret = db.query(DBHelper.VOTES, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        return ret;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret = null;
        int code = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rid = -1;
        switch (code) {
            case CODE_ITEMS:
                rid = db.insert(DBHelper.ITEMS, null, values);
                break;
            case CODE_USER:
                rid = db.insert(DBHelper.USER, null, values);
                break;
            case CODE_VOTES:
                db.insert(DBHelper.VOTES, null, values);
                break;
        }
        if (rid != -1) {
            ret = ContentUris.withAppendedId(uri, rid);
        }
        return ret;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

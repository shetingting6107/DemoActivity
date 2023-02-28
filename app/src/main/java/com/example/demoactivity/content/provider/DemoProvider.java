package com.example.demoactivity.content.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demoactivity.content.DemoTable;

public class DemoProvider extends ContentProvider {

    private static final String DB_NAME = "demo.db";
    private static final String DB_TABLE = "demoinfo";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    public static final int MULTIPLE_DEMO = 1;
    public static final int SINGLE_DEMO = 2;
    public static final int TABLE1_DIR = 3;
    public static final int TABLE1_ITEM = 4;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DemoTable.AUTHORITY, DemoTable.PATH_MULTIPLE, MULTIPLE_DEMO);
        uriMatcher.addURI(DemoTable.AUTHORITY, DemoTable.PATH_SINGLE, SINGLE_DEMO);
        uriMatcher.addURI(DemoTable.AUTHORITY, "table1", TABLE1_DIR);
        uriMatcher.addURI(DemoTable.AUTHORITY, "table1/#", TABLE1_ITEM);
    }

    /**
     * 初始化提供器时调用，通常会在此完成对数据库的创建和升级操作
     * @return true表示提供器初始化成功，false表示失败
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        db = dbOpenHelper.getWritableDatabase();
        if (db == null){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 从内容提供器中查询数据
     * @param uri 确定查询的表
     * @param projection 确定查询的列
     * @param selection 约束查询的行
     * @param selectionArgs 约束查询的行
     * @param sortOrder 对结果进行排序
     * @return 查询结果
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DB_TABLE);
        switch (uriMatcher.match(uri)) {
            case SINGLE_DEMO:
                //查询demo表中的任意一行数据
                qb.appendWhere(DemoTable.KEY_ID + "=" + uri.getPathSegments().get(1));
                break;
            case MULTIPLE_DEMO:
                //查询demo表中的所有数据
                break;
            case TABLE1_DIR:
                //查询table1表中的所有数据
                break;
            case TABLE1_ITEM:
                //查询table1表中的单条数据
                break;
            default:
                break;
        }
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * 格式要求：
     * 1.必须以vnd开头
     * 2.如果内容uri以路径结尾，则接 android.cursor.dir/
     * 如果内容uri以id结尾，则接 android.cursor.item/
     * 3.最后街上 vnd.<authority>.<path>
     *
     * 例：对于content://com.example.demoactivity/demo这个内容uri，对应的MIME类型就可以写成 vnd.android.cursor.dir/vnd.com.example.demoactivity.demo
     * 对于content://com.example.demoactivity/demo/1这个内容uri，对应的MIME类型就可以写成 vnd.android.cursor.item/vnd.com.example.demoactivity.demo
     *
     * 根据传入的内容Uri来返回对应的MIME类型
     * @param uri 内容uri
     * @return MIME类型
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_DEMO:
                return DemoTable.MIME_TYPE_MULTIPLE;
            case SINGLE_DEMO:
                return DemoTable.MIME_TYPE_SINGLE;
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.demoactivity.table1";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.demoactivity.table1";
            default:
                throw new IllegalArgumentException("Unknow uri:" + uri);
        }
    }

    /**
     * 向内容提供器添加一条数据
     * @param uri 确定添加到的表
     * @param values 待添加的数据
     * @return 新纪录URI
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = db.insert(DB_TABLE, null, values);
        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(DemoTable.CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw  new SQLException("failed to insert row into " + uri);
    }

    /**
     * 从内容提供器删除数据
     * @param uri 确定待删除数据的表
     * @param selection 约束删除哪些行
     * @param selectionArgs 约束删除哪些行
     * @return 受影响的行数
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_DEMO:
                count = db.delete(DB_TABLE, selection, selectionArgs);
                break;
            case SINGLE_DEMO:
                String segment = uri.getPathSegments().get(1);
                count = db.delete(DB_TABLE, DemoTable.KEY_ID + "=" + segment, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * 更新内容提供器中已有的数据
     * @param uri 确定需要更新的表
     * @param values 待更新的数据
     * @param selection 约束更新哪些行
     * @param selectionArgs 约束更新哪些行
     * @return 受影响的行数
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_DEMO:
                count = db.update(DB_TABLE, values, selection, selectionArgs);
                break;
            case SINGLE_DEMO:
                String segment = uri.getPathSegments().get(1);
                count = db.update(DB_TABLE, values, DemoTable.KEY_ID + "=" + segment, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknow URI:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private static class DBOpenHelper extends SQLiteOpenHelper {

        private static final String DB_CREATE = "CREATE TABLE " + DB_TABLE +
                "(" + DemoTable.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DemoTable.KEY_NAME + " TEXT NOT NULL, " +
                DemoTable.KEY_AGE + " INTEGER," +
                DemoTable.KEY_SEX + " TEXT);";

        public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}

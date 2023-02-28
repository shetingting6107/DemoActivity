package com.example.demoactivity.content;

import android.net.Uri;

public class DemoTable {
    public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
    public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
    public static final String MIME_ITEM = "vnd.example.demo";

    public static final String MIME_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MIME_ITEM ;
    public static final String MIME_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MIME_ITEM ;

    public static final String AUTHORITY = "com.example.demoactivity";
    public static final String PATH_SINGLE = "demo/#";//匹配表中任意一行的数据内容
    public static final String PATH_MULTIPLE = "demo";
    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);


    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AGE = "age";
    public static final String KEY_SEX = "sex";
}

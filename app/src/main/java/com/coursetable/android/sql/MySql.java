package com.coursetable.android.sql;

/**
 * Created by Caihan on 2017/12/9.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.widget.Toast;

public class MySql extends SQLiteOpenHelper {
    private String CREATE_TABLE="create table tableInfo(courseName text,courseTeacher text,courseTime,courseLocation)";
    private Context mContext;
    public MySql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Toast.makeText(mContext,"由于不存在数据库 默认建立保存课表信息",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
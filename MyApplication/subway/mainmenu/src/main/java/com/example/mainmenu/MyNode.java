package com.example.mainmenu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyNode extends SQLiteOpenHelper {
    public static final int DB_version = 1;
    public MyNode(Context context){
        super(context, "station db", null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

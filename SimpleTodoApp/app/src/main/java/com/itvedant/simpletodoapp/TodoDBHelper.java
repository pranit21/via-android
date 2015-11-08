package com.itvedant.simpletodoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Pranit on 29-01-2015.
 */
public class TodoDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "com.itvedant.simpletodoapp.todos";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "todos";

    public TodoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + Columns.TODO + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public class Columns {
        public static final String TODO = "todo";
        public static final String _ID = BaseColumns._ID;
    }
}

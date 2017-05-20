package com.troshkova.portfolioprogect.examsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus-pc on 18.05.2017.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="results.db";
    private static final int VERSION=1;
    public static final String TABLE_NAME="results";
    public static final String COLUMN_RESULT="mark";
    public static final String COLUMN_SUBJECT="subject";
    public static final String COLUMN_DATE="date";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+"( id integer primary key autoincrement, "+COLUMN_SUBJECT+" text not null, "+
        COLUMN_RESULT+" integer, "+COLUMN_DATE+" text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

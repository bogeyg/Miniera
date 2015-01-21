package com.artincodes.miniera.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by jayadeep on 5/1/15.
 */

public class TodoDBHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "ToDoDatabase.db";
    public static final String CONTACTS_TABLE_NAME = "todotasks";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_TASK = "task";
    public static final String CONTACTS_COLUMN_IS_DONE = "is_done";


    private HashMap hashMap;



    public TodoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table todotasks " +
                        "(id integer primary key, task text,is_done integer)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS todotasks");
        onCreate(db);

    }

    public boolean insertTodo  (String task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("task", task);
        contentValues.put("is_done", 0);

        db.insert("todotasks", null, contentValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from todotasks", null);
    }
}

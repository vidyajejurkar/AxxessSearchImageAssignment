package com.vidya.axxesssearchimageassignment.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Image";
    public static final String CONTACTS_TABLE_NAME = "Details";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "create table " + CONTACTS_TABLE_NAME + "(id INTEGER PRIMARY KEY, name comment)"
            );
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", s);
        db.replace(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public ArrayList getAllComment() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery("select (id ||' : '||name ) as fullname from " + CONTACTS_TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if ((res != null) && (res.getCount() > 0))
                array_list.add(res.getString(res.getColumnIndex("fullname")));
            res.moveToNext();
        }
        return array_list;
    }

    public boolean update(String s, String s1) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + CONTACTS_TABLE_NAME + " SET name = " + "'" + s );
        return true;
    }

    public boolean delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from " + CONTACTS_TABLE_NAME);
        return true;
    }
}
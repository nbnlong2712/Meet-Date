package com.quintus.labs.datingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbMatchedHelper extends SQLiteOpenHelper {
    public static final String DbMatched = "Matched.db";

    public DbMatchedHelper(@Nullable Context context) {
        super(context, DbMatched, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create Table if not exists Matched (email_1 TEXT , email_2 , primary key ( email_1, email_2) )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Matched");

    }

    public boolean insertData(String email_1, String email_2)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email_1", email_1);
        contentValues.put("email_2", email_2);
        long result = MyDB.insert("Matched", null, contentValues);
        if(result == 1) return false;
        else return true;


    }

    public Boolean checkEmail(String email_1, String email_2)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Matched where email_1 = ?"+"And email_2=?", new String[]{email_1, email_2});
        if (cursor.getCount() > 0)
            return false;
        else return true;
    }


}

package com.quintus.labs.datingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class DbAccountHelper extends SQLiteOpenHelper {
    public static final String DbAccount = "Account.db";

    public DbAccountHelper(@Nullable Context context) {
        super(context, DbAccount, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table if not exists Account (email TEXT primary key, username, password, birth, avatar, gender INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Account");
    }

    public Boolean insertData(String email, String username, String password, String birth, String avatar, Integer gender)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("birth", birth);
        contentValues.put("avatar", avatar);
        contentValues.put("gender", gender);

        long result = MyDB.insert("Account", null, contentValues);
        if(result == 1) return false;
        else return true;
    }

    public Boolean checkEmail(String email)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Account where email = ?", new String[]{email});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    public void updateBirth(String email, String birth)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("birth", birth);
        MyDB.update("Account", values, "email = ?", new String[]{email});
    }

    public void updateAvatar(String email, String avatar)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar", avatar);
        MyDB.update("Account", values, "email = ?", new String[]{email});
    }

    public Boolean checkEmailPassword(String email, String password)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from Account where email = ? and password = ?", new String[]{email, password});
        if(cursor.getCount() > 0)
            return true;
        else return false;
    }
    public void updateGender(String email, Integer gender)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("gender", gender);
        MyDB.update("Account", values, "email = ?", new String[]{email});
    }
}

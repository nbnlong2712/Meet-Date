package com.quintus.labs.datingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbSettingHelper extends SQLiteOpenHelper {
    public static final String DbSetting = "Setting.db";

    public DbSettingHelper(@Nullable Context context) {
        super(context, DbSetting, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table if not exists Setting(email_s TEXT primary key, gender_prefer INTEGER, distance INTEGER, age_min INTEGER, age_max INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Setting");
    }

    public Boolean insertData(String email_s, Integer gender_prefer, Integer distance, Integer age_min, Integer age_max)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email_s", email_s);
        contentValues.put("gender_prefer", gender_prefer);
        contentValues.put("distance", distance);
        contentValues.put("age_min", age_min);
        contentValues.put("age_max", age_max);

        long result = MyDB.insert("Setting", null, contentValues);
        if(result == 1) return false;
        else return true;
    }

    public void updateGenderPrefer(String email_s, Integer gender_prefer)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("gender_prefer", gender_prefer);
        MyDB.update("Setting", values, "email_s = ?", new String[]{email_s});
    }

    public void updateDistance(String email_s, Integer distance)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("distance", distance);
        MyDB.update("Setting", values, "email_s = ?", new String[]{email_s});
    }

    public void updateAgeMin(String email_s, Integer age_min)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("age_min", age_min);
        MyDB.update("Setting", values, "email_s = ?", new String[]{email_s});
    }

    public void updateAgeMax(String email_s, Integer age_max)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("age_max", age_max);
        MyDB.update("Setting", values, "email_s = ?", new String[]{email_s});
    }
}

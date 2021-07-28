package com.quintus.labs.datingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbProfileHelper extends SQLiteOpenHelper {
    public static final String DbProfile = "Profile.db";

    public DbProfileHelper(@Nullable Context context) {
        super(context, DbProfile, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table if not exists Profile(email_p TEXT primary key, " +
                "pic1, pic2 ,pic3 ,pic4 ,pic5 ,pic6 , " +
                "describe , company , school , gender INTEGER, hobby )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Profile");
    }

    public Boolean insertData(String email_p, String pic1, String pic2, String pic3,
                              String pic4, String pic5, String pic6, String describe,
                              String company, String school, Integer gender, String hobby)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email_p", email_p);
        contentValues.put("pic1", pic1);
        contentValues.put("pic2", pic2);
        contentValues.put("pic3", pic3);
        contentValues.put("pic4", pic4);
        contentValues.put("pic5", pic5);
        contentValues.put("pic6", pic6);
        contentValues.put("describe", describe);
        contentValues.put("company", company);
        contentValues.put("school", school);
        contentValues.put("gender", gender);
        contentValues.put("hobby", hobby);

        long result = MyDB.insert("Profile", null, contentValues);
        if(result == 1) return false;
        else return true;
    }

    public void updatePic1(String email_p, String pic1)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pic1", pic1);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updatePic2(String email_p, String pic2)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pic2", pic2);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updatePic3(String email_p, String pic3)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pic3", pic3);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updatePic4(String email_p, String pic4)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pic4", pic4);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updatePic5(String email_p, String pic5)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pic5", pic5);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updatePic6(String email_p, String pic6)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pic6", pic6);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updateDescribe(String email_p, String describe)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("describe", describe);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updateSchool(String email_p, String school)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("school", school);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updateCompany(String email_p, String company)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("company", company);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updateGender(String email_p, Integer gender)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("gender", gender);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
    public void updateHobby(String email_p, String hobby)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hobby", hobby);
        MyDB.update("Profile", values, "email_p = ?", new String[]{email_p});
    }
}

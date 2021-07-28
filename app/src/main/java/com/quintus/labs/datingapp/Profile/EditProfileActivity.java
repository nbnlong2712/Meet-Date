package com.quintus.labs.datingapp.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.database.DbAccountHelper;
import com.quintus.labs.datingapp.database.DbProfileHelper;
import com.quintus.labs.datingapp.database.DbSettingHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";

    Button man, woman;
    ImageButton back;
    TextView man_text, women_text;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView;
    Bitmap myBitmap;
    Uri picUri;
    String[] permissionsRequired = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Context mContext = EditProfileActivity.this;
    private ImageView mProfileImage;
    private String userId, profileImageUri;
    private Uri resultUri;
    private String userSex;
    private EditText phoneNumber, aboutMe;
    private CheckBox sportsCheckBox, travelCheckBox, musicCheckBox, fishingCheckBox;
    private boolean isSportsClicked = false;
    private boolean isTravelClicked = false;
    private boolean isFishingClicked = false;
    private boolean isMusicClicked = false;
    private RadioGroup userSexSelection;
    private boolean sentToSettings = false;
    private int REQUEST_CAMERA = 0, SELECT_FILE_1 = 1, SELECT_FILE_2 = 2, SELECT_FILE_3 = 3, SELECT_FILE_4 = 4, SELECT_FILE_5 = 5, SELECT_FILE_6 = 6;
    private String userChoosenTask;
    private User mUser;
    private String company, school;
    private Integer gender = 1;
    private DbProfileHelper mProfileHelper;
    private DbAccountHelper mAccountHelper;
    private EditText mCompany;
    private EditText mDescribe;
    private EditText mSchool;

    private static final int PERMISSION_ALL = 100;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        mUser = (User) intent.getSerializableExtra("classUser");

        mProfileHelper = new DbProfileHelper(mContext);
        mAccountHelper = new DbAccountHelper(mContext);

        SQLiteDatabase db = openOrCreateDatabase("Profile.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from Profile where email_p = ?", new String[]{mUser.getEmail()});
        cursor.moveToFirst();
        mUser.setPic1(cursor.getString(1));
        mUser.setPic2(cursor.getString(2));
        mUser.setPic3(cursor.getString(3));
        mUser.setPic4(cursor.getString(4));
        mUser.setPic5(cursor.getString(5));
        mUser.setPic6(cursor.getString(6));
        mUser.setDescription(cursor.getString(7));
        company = cursor.getString(8);
        school = cursor.getString(9);
        gender = Integer.parseInt(cursor.getString(10));
        cursor.close();

        if(!checkPermission(permissionsRequired))
        {
            ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_ALL);
        }

        imageView1 =(ImageView) findViewById(R.id.image_view_1);
        imageView2 =(ImageView) findViewById(R.id.image_view_2);
        imageView3 =(ImageView) findViewById(R.id.image_view_3);
        imageView4 =(ImageView) findViewById(R.id.image_view_4);
        imageView5 =(ImageView) findViewById(R.id.image_view_5);
        imageView6 =(ImageView) findViewById(R.id.image_view_6);

        mDescribe = (EditText) findViewById(R.id.describe);
        mDescribe.setText(mUser.getDescription());
        mDescribe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setDescription(s.toString());
                mProfileHelper.updateDescribe(mUser.getEmail(), mUser.getDescription());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mCompany = (EditText) findViewById(R.id.company);
        mCompany.setText(company);
        mCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                company = s.toString();
                mProfileHelper.updateCompany(mUser.getEmail(), company);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mSchool = (EditText) findViewById(R.id.school);
        mSchool.setText(school);
        mSchool.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                school = s.toString();
                mProfileHelper.updateSchool(mUser.getEmail(), school);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        man = findViewById(R.id.man_button);
        woman = findViewById(R.id.woman_button);
        man_text = findViewById(R.id.man_text);
        women_text = findViewById(R.id.woman_text);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(gender == 1)
        {
            man_text.setTextColor(R.color.colorAccent);
            man.setBackgroundResource(R.drawable.ic_check_select);
            women_text.setTextColor(R.color.black);
            woman.setBackgroundResource(R.drawable.ic_check_unselect);
        }
        else{
            women_text.setTextColor(R.color.colorAccent);
            woman.setBackgroundResource(R.drawable.ic_check_select);
            man_text.setTextColor(R.color.black);
            man.setBackgroundResource(R.drawable.ic_check_unselect);
        }

        woman.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                women_text.setTextColor(R.color.colorAccent);
                woman.setBackgroundResource(R.drawable.ic_check_select);
                man_text.setTextColor(R.color.black);
                man.setBackgroundResource(R.drawable.ic_check_unselect);
                gender = 0;
                mProfileHelper.updateGender(mUser.getEmail(), gender);
                mAccountHelper.updateGender(mUser.getEmail(), gender);
            }
        });

        man.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                man_text.setTextColor(R.color.colorAccent);
                man.setBackgroundResource(R.drawable.ic_check_select);
                women_text.setTextColor(R.color.black);
                woman.setBackgroundResource(R.drawable.ic_check_unselect);
                gender = 1;
                mProfileHelper.updateGender(mUser.getEmail(), gender);
                mAccountHelper.updateGender(mUser.getEmail(), gender);
            }
        });
        /////////////////////// ----- LOAD PROFILE IMAGE ----- ////////////////////////
        if(!mUser.getPic1().equals(""))
            Glide.with(this).load(mUser.getPic1()).into(imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView1;
                galleryIntent(SELECT_FILE_1);

            }
        });
        if(!mUser.getPic2().equals(""))
            Glide.with(this).load(mUser.getPic2()).into(imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView2;
                galleryIntent(SELECT_FILE_2);
            }
        });
        if(!mUser.getPic3().equals(""))
            Glide.with(this).load(mUser.getPic3()).into(imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView3;
                galleryIntent(SELECT_FILE_3);
            }
        });
        if(!mUser.getPic4().equals(""))
            Glide.with(this).load(mUser.getPic4()).into(imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView4;
                galleryIntent(SELECT_FILE_4);
            }
        });
        if(!mUser.getPic5().equals(""))
            Glide.with(this).load(mUser.getPic5()).into(imageView5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView5;
                galleryIntent(SELECT_FILE_5);
            }
        });
        if(!mUser.getPic6().equals(""))
            Glide.with(this).load(mUser.getPic6()).into(imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView6;
                galleryIntent(SELECT_FILE_6);
            }
        });
        ///////////////////////////////////////////////////////////////////////////////
    }

    public boolean checkPermission(String... permissions)
    {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void galleryIntent(int SELECT_FILE) {
        Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == SELECT_FILE_1)
            {
                String pathfile = getRealPathFromURI(this, data.getData());
                mUser.setPic1(pathfile);
                mProfileHelper.updatePic1(mUser.getEmail(), getRealPathFromURI(this, data.getData()));
                Glide.with(this).load(getRealPathFromURI(this, data.getData())).into(imageView1);
            }
            else if(requestCode == SELECT_FILE_2)
            {
                String pathfile = getRealPathFromURI(this, data.getData());
                mUser.setPic2(pathfile);
                mProfileHelper.updatePic2(mUser.getEmail(), getRealPathFromURI(this, data.getData()));
                Glide.with(this).load(getRealPathFromURI(this, data.getData())).into(imageView2);
            }
            else if(requestCode == SELECT_FILE_3)
            {
                String pathfile = getRealPathFromURI(this, data.getData());
                mUser.setPic3(pathfile);
                mProfileHelper.updatePic3(mUser.getEmail(), getRealPathFromURI(this, data.getData()));
                Glide.with(this).load(getRealPathFromURI(this, data.getData())).into(imageView3);
            }
            else if(requestCode == SELECT_FILE_4)
            {
                mUser.setPic4(getRealPathFromURI(this, data.getData()));
                mProfileHelper.updatePic4(mUser.getEmail(), getRealPathFromURI(this, data.getData()));
                imageView4.setImageURI(Uri.fromFile(new File(mUser.getPic4())));
                Glide.with(this).load(getRealPathFromURI(this, data.getData())).into(imageView4);
            }
            else if(requestCode == SELECT_FILE_5)
            {
                mUser.setPic5(getRealPathFromURI(this, data.getData()));
                mProfileHelper.updatePic5(mUser.getEmail(), getRealPathFromURI(this, data.getData()));
                imageView5.setImageURI(Uri.fromFile(new File(mUser.getPic5())));
                Glide.with(this).load(getRealPathFromURI(this, data.getData())).into(imageView5);
            }
            else if(requestCode == SELECT_FILE_6)
            {
                mUser.setPic6(getRealPathFromURI(this, data.getData()));
                mProfileHelper.updatePic6(mUser.getEmail(), getRealPathFromURI(this, data.getData()));
                imageView6.setImageURI(Uri.fromFile(new File(mUser.getPic6())));
                Glide.with(this).load(getRealPathFromURI(this, data.getData())).into(imageView6);
            }
        }
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

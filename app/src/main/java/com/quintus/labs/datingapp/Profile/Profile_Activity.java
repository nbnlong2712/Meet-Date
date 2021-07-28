package com.quintus.labs.datingapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.quintus.labs.datingapp.Introduction.IntroductionMain;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.PreferenceUtils;
import com.quintus.labs.datingapp.Utils.PulsatorLayout;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.database.DbAccountHelper;

public class Profile_Activity extends AppCompatActivity {
    private static final String TAG = "Profile_Activity";
    private static final int ACTIVITY_NUM = 0;
    static boolean active = false;
    private DbAccountHelper mAccountHelper;
    private Context mContext;
    private ImageView imagePerson;
    private TextView name;
    String password;
    User user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = Profile_Activity.this;

        user = new User("", "", "",
                "", "", "", false, false,
                false, false, "", "", "",
                37.349642, -121.938987,"","","","","","");

        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();

        setupTopNavigationView();

        imagePerson = (ImageView) findViewById(R.id.circle_profile_image);
        name = (TextView) findViewById(R.id.profile_name);

        //  Triệu hồi database by Long
        String mail = PreferenceUtils.getEmail(mContext);
        user.setEmail(mail);

        SQLiteDatabase db = openOrCreateDatabase("Account.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from Account where email = ?", new String[]{user.getEmail()});
        cursor.moveToFirst();
        user.setUsername(cursor.getString(1));
        cursor.close();

        SQLiteDatabase db1 = openOrCreateDatabase("Profile.db", Context.MODE_PRIVATE, null);
        Cursor cursor1 = db1.rawQuery("select * from Profile where email_p = ?", new String[]{user.getEmail()});
        cursor1.moveToFirst();
        user.setPic1(cursor1.getString(1));
        cursor1.close();

        Glide.with(this).load(user.getPic1()).into(imagePerson);

        name.setText(user.getUsername());

        ImageButton edit_btn = findViewById(R.id.edit_profile);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, EditProfileActivity.class);
                intent.putExtra("classUser", user);
                startActivity(intent);
            }
        });

        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, SettingsActivity.class);
                intent.putExtra("classUser", user);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}

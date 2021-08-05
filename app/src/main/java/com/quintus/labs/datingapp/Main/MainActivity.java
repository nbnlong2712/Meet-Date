package com.quintus.labs.datingapp.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.quintus.labs.datingapp.Introduction.IntroductionMain;
import com.quintus.labs.datingapp.Profile.Profile_Activity;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.CalculateAge;
import com.quintus.labs.datingapp.Utils.PreferenceUtils;
import com.quintus.labs.datingapp.Utils.PulsatorLayout;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.database.DbAccountHelper;
import com.quintus.labs.datingapp.database.DbMatchedHelper;
import com.quintus.labs.datingapp.database.DbProfileHelper;
import com.quintus.labs.datingapp.database.DbSettingHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 1;
    final private int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    ListView listView;
    List<Cards> rowItems;
    FrameLayout cardFrame, moreFrame;
    LinearLayout cardZone, btnZone;
    private Context mContext = MainActivity.this;
    private NotificationHelper mNotificationHelper;
    private Cards cards_data[];
    private PhotoAdapter arrayAdapter;
    public User singleUser;
    private DbMatchedHelper mMachedHelper;
    private DbSettingHelper mSettingHelper;
    private DbProfileHelper mProfileHelper;
    private DbAccountHelper mAccountHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        mMachedHelper = new DbMatchedHelper(mContext);
        mMachedHelper.getWritableDatabase();

        mAccountHelper = new DbAccountHelper(mContext);
        mAccountHelper.getWritableDatabase();

        mSettingHelper = new DbSettingHelper(mContext);
        mSettingHelper.getWritableDatabase();

        mProfileHelper = new DbProfileHelper(mContext);
        mProfileHelper.getWritableDatabase();


        Intent intent = getIntent();
        singleUser = (User) intent.getSerializableExtra("classUser");

        cardFrame = findViewById(R.id.card_frame);
        cardZone = findViewById(R.id.card_zone);
        btnZone = findViewById(R.id.btn_zone);
        moreFrame = findViewById(R.id.more_frame);

        // start pulsator
        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();
        mNotificationHelper = new NotificationHelper(this);


        setupTopNavigationView();

        rowItems = new ArrayList<Cards>();
        ArrayList<Cards> c = new ArrayList<>();
        c = loadEmailCard();
        rowItems.addAll(c);
        arrayAdapter = new PhotoAdapter(this, R.layout.item, rowItems);
        checkRowItem();
        updateSwipeCard();
    }

    private void checkRowItem() {
        if (rowItems.isEmpty()) {
            moreFrame.setVisibility(View.VISIBLE);
//            cardFrame.setVisibility(View.GONE);
            cardZone.setVisibility(View.GONE);
            btnZone.setVisibility(View.GONE);
        }
    }

    private void updateLocation() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        updateLocation();
                    } else {
                        Toast.makeText(MainActivity.this, "Location Permission Denied. You have to give permission inorder to know the user range ", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void updateSwipeCard() {
        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                checkRowItem();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;

                String userId = obj.getUserId();
                String mail = PreferenceUtils.getEmail(mContext);
                if (mMachedHelper.insertData(mail, userId)) {
                    Toast.makeText(mContext, "Insert", Toast.LENGTH_LONG);
                }
                ;
                //check matches
                checkRowItem();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here


            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Cards cards = (Cards) dataObject;
                Intent intent = new Intent(mContext, ProfileCheckinMain.class);
                intent.putExtra("name", cards.getName());
                intent.putExtra("age", String.valueOf(cards.getAge()));
                intent.putExtra("photo", cards.getProfileImageUrl());
                intent.putExtra("bio", cards.getBio());
                intent.putExtra("interest", cards.getInterest());
                intent.putExtra("distance", cards.getDistance());
                mContext.startActivity(intent);
            }
        });
    }

    public void sendNotification() {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(mContext.getString(R.string.app_name), mContext.getString(R.string.match_notification));

        mNotificationHelper.getManager().notify(1, nb.build());
    }


    public void DislikeBtn(View v) {
        if (rowItems.size() != 0) {
            Cards card_item = rowItems.get(0);
            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();
            checkRowItem();

            Intent btnClick = new Intent(mContext, BtnDislikeActivity.class);
            btnClick.putExtra("url", card_item.getProfileImageUrl());
            startActivity(btnClick);
        }
    }

    public void LikeBtn(View v) {
        if (rowItems.size() != 0) {
            Cards card_item = rowItems.get(0);
            String mail = PreferenceUtils.getEmail(mContext);
            if (mMachedHelper.insertData(mail, card_item.getUserId())) {
                Toast.makeText(mContext, "Insert", Toast.LENGTH_LONG);
            }
            ;
            //check matches

            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();
            checkRowItem();

            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
            btnClick.putExtra("url", card_item.getProfileImageUrl());
            startActivity(btnClick);
        }
    }

    public ArrayList<Cards> loadEmailCard() {
        ArrayList<Cards> listcard = new ArrayList<>();
        String mail = PreferenceUtils.getEmail(mContext);
        SQLiteDatabase db = openOrCreateDatabase("Setting.db", Context.MODE_PRIVATE, null);
        if(mail.equals(null) || mail.isEmpty())
        {
            Intent i = new Intent(MainActivity.this, IntroductionMain.class);
            startActivity(i);
        }
        Cursor cursor = db.rawQuery("select * from Setting where email_s = ?", new String[]{mail});
        cursor.moveToFirst();

        String gender = cursor.getString(1);
        String agemin = cursor.getString(3);
        String agemax = cursor.getString(4);

        cursor.close();

        SQLiteDatabase db1 = openOrCreateDatabase("Account.db", Context.MODE_PRIVATE, null);
        Cursor cursor1 = db1.rawQuery("select * from Account where email!=? ", new String[]{mail});
        SQLiteDatabase db2 = openOrCreateDatabase("Profile.db", Context.MODE_PRIVATE, null);
        while (cursor1.moveToNext()) {
            String age = cursor1.getString(3);
            if (age != null) {
                String mail_ = cursor1.getString(0);
                String name = cursor1.getString(1);
                CalculateAge cal = new CalculateAge(age);
                int age_ = cal.getAge();
                String gender_ = cursor1.getString(5);

                if (age_ >= Integer.parseInt(agemin) && age_ <= Integer.parseInt(agemax) && gender_.equals(gender) && mMachedHelper.checkEmail(mail, mail_) && mMachedHelper.checkEmail(mail_, mail)) {
                    Cursor cursor2 = db2.rawQuery("select * from Profile where email_p = ?", new String[]{mail_});
                    cursor2.moveToFirst();
                    String describe = cursor2.getString(7);
                    String path = cursor2.getString(1);
                    String interest = cursor2.getString(11);
                    Cards cards = new Cards(mail_, name, age_, path, describe, interest, 800);
                    listcard.add(cards);
                }
            }

        }
        return listcard;
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

    @Override
    public void onBackPressed() {
    }
}

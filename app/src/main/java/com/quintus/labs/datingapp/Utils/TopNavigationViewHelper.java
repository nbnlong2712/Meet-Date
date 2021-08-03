package com.quintus.labs.datingapp.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.Matched.Matched_Activity;
import com.quintus.labs.datingapp.Profile.Profile_Activity;
import com.quintus.labs.datingapp.R;

public class TopNavigationViewHelper {

    private static final String TAG = "TopNavigationViewHelper";

    public static void setupTopNavigationView(BottomNavigationViewEx tv) {
        Log.d(TAG, "setupTopNavigationView: setting up navigationview");


    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_profile:
                        Intent intent2 = new Intent(context, Profile_Activity.class);
                        context.startActivity(intent2);

                        break;

                    case R.id.ic_main:
                        Intent intent1 = new Intent(context, MainActivity.class);
                        context.startActivity(intent1);

                        break;

                    case R.id.ic_matched:
                        Intent intent3 = new Intent(context, Matched_Activity.class);
                        context.startActivity(intent3);

                        break;
                    case R.id.ic_like:
                    case R.id.ic_search:
                        //do nothing

                        break;


                }

                item.setChecked(true);
                return true;
            }
        });
    }
}

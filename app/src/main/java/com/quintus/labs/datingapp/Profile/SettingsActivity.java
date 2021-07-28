package com.quintus.labs.datingapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quintus.labs.datingapp.Introduction.IntroductionMain;
import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.PreferenceUtils;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.database.DbSettingHelper;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

/**
 * Grocery App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    SeekBar distance;
    SwitchCompat man, woman;
    RangeSeekBar rangeSeekBar;
    TextView gender, distance_text, age_rnge;
    private Integer ageRange;
    private User mUser;
    private DbSettingHelper mSettingHelper;
    private Context mContext;
    private Integer gender_prefer, distance_km, age_min, age_max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mContext = SettingsActivity.this;

        Intent intent = getIntent();
        mUser = (User) intent.getSerializableExtra("classUser");

        mSettingHelper = new DbSettingHelper(mContext);

        SQLiteDatabase db = openOrCreateDatabase("Setting.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from Setting where email_s = ?", new String[]{mUser.getEmail()});
        cursor.moveToFirst();
        gender_prefer = Integer.parseInt(cursor.getString(1));
        distance_km = Integer.parseInt(cursor.getString(2));
        age_min = Integer.parseInt(cursor.getString(3));
        age_max = Integer.parseInt(cursor.getString(4));
        cursor.close();

        TextView toolbar = findViewById(R.id.toolbartag);
        toolbar.setText("Profile");
        ImageButton back = findViewById(R.id.back);
        distance = findViewById(R.id.distance);
        man = findViewById(R.id.switch_man);
        woman = findViewById(R.id.switch_woman);
        distance_text = findViewById(R.id.distance_text);
        age_rnge = findViewById(R.id.age_range);
        rangeSeekBar = findViewById(R.id.rangeSeekbar);

        distance_text.setText(distance_km+" Km");
        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                distance_km = progress;
                distance_text.setText(progress + " Km");

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mSettingHelper.updateDistance(mUser.getEmail(), distance_km);

        if(gender_prefer == 1)
        {
            man.setChecked(true);
            woman.setChecked(false);
        }
        else if(gender_prefer == 0)
        {
            woman.setChecked(true);
            man.setChecked(false);
        }
        man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    man.setChecked(true);
                    woman.setChecked(false);
                    gender_prefer = 1;
                    mSettingHelper.updateGenderPrefer(mUser.getEmail(), gender_prefer);
                }
            }
        });
        woman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    woman.setChecked(true);
                    man.setChecked(false);
                    gender_prefer = 0;
                    mSettingHelper.updateGenderPrefer(mUser.getEmail(), gender_prefer);
                }
            }
        });
        rangeSeekBar.setSelectedMaxValue(age_max);
        rangeSeekBar.setSelectedMinValue(age_min);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) { ;
                age_rnge.setText(minValue + "-" + maxValue);

                mSettingHelper.updateAgeMin(mUser.getEmail(), Integer.parseInt(minValue.toString()));
                mSettingHelper.updateAgeMax(mUser.getEmail(), Integer.parseInt(maxValue.toString()));
                age_max = Integer.parseInt(maxValue.toString());
                age_min = Integer.parseInt(minValue.toString());
            }
        });
        age_rnge.setText(age_min + "-" + age_max);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void Logout(View view) {
        PreferenceUtils.saveEmail("", this);
        PreferenceUtils.savePassword("", this);
        startActivity(new Intent(getApplicationContext(), Login.class));
        Toast.makeText(getApplicationContext(), "Mời bạn đăng nhập!", Toast.LENGTH_SHORT).show();
        finish();
    }
}

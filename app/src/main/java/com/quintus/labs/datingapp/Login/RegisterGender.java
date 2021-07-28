package com.quintus.labs.datingapp.Login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.database.DbAccountHelper;
import com.quintus.labs.datingapp.database.DbProfileHelper;


/**
 * Grocery App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class RegisterGender extends AppCompatActivity {

    private DbProfileHelper mProfileHelper;
    private DbAccountHelper mAccountHelper;
    String password;
    User user;
    boolean male = true;
    private Button genderContinueButton;
    private Button maleSelectionButton;
    private Button femaleSelectionButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gender);

        mContext = RegisterGender.this;
        mProfileHelper = new DbProfileHelper(mContext);
        mAccountHelper = new DbAccountHelper(mContext);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");
        password = intent.getStringExtra("password");

        maleSelectionButton = findViewById(R.id.maleSelectionButton);
        femaleSelectionButton = findViewById(R.id.femaleSelectionButton);
        genderContinueButton = findViewById(R.id.genderContinueButton);

        //By default male has to be selected so below code is added

        femaleSelectionButton.setAlpha(.5f);
        femaleSelectionButton.setBackgroundColor(Color.GRAY);

        mProfileHelper.insertData(user.getEmail(), "", "",
                "", "", "", "",
                "", "", "", 1, "");

        maleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileHelper.updateGender(user.getEmail(), 1);
                mAccountHelper.updateGender(user.getEmail(), 1);
                maleButtonSelected();

            }
        });

        femaleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileHelper.updateGender(user.getEmail(), 0);
                mAccountHelper.updateGender(user.getEmail(), 0);
                femaleButtonSelected();
            }
        });

        genderContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceEntryPage();
            }
        });

    }

    public void maleButtonSelected() {
        male = true;
        maleSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        maleSelectionButton.setAlpha(1.0f);
        femaleSelectionButton.setAlpha(.5f);
        femaleSelectionButton.setBackgroundColor(Color.GRAY);
    }

    public void femaleButtonSelected() {
        male = false;
        femaleSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        femaleSelectionButton.setAlpha(1.0f);
        maleSelectionButton.setAlpha(.5f);
        maleSelectionButton.setBackgroundColor(Color.GRAY);
    }

    public void openPreferenceEntryPage() {

        String ownSex = male ? "male" : "female";
        user.setSex(ownSex);
        //set default photo
        String defaultPhoto = male ? "defaultMale" : "defaultFemale";
        user.setProfileImageUrl(defaultPhoto);

        Intent intent = new Intent(this, RegisterGenderPrefection.class);
        intent.putExtra("password", password);
        intent.putExtra("classUser", user);
        startActivity(intent);
    }
}

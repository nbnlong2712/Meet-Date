package com.quintus.labs.datingapp.Introduction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.Login.RegisterBasicInfo;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.PreferenceUtils;
import com.quintus.labs.datingapp.database.DbAccountHelper;
import com.quintus.labs.datingapp.database.DbMatchedHelper;
import com.quintus.labs.datingapp.database.DbProfileHelper;
import com.quintus.labs.datingapp.database.DbSettingHelper;


/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class IntroductionMain extends AppCompatActivity {

    private Button signupButton;
    private Button loginButton;
    private Context mContext = IntroductionMain.this;
    private DbMatchedHelper mMachedHelper;
    private DbSettingHelper mSettingHelper;
    private DbProfileHelper mProfileHelper;
    private DbAccountHelper mAccountHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_main);

        mMachedHelper=new DbMatchedHelper(mContext);
        mMachedHelper.getWritableDatabase();

        mAccountHelper = new DbAccountHelper(mContext);
        mAccountHelper.getWritableDatabase();

        mSettingHelper = new DbSettingHelper(mContext);
        mSettingHelper.getWritableDatabase();

        mProfileHelper = new DbProfileHelper(mContext);
        mProfileHelper.getWritableDatabase();

        PreferenceUtils utils = new PreferenceUtils();
        if (utils.getEmail(this) != "" ){
            Intent intent = new Intent(IntroductionMain.this, MainActivity.class);
            startActivity(intent);
        }

        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmailAddressEntryPage();
            }
        });

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });
    }

    public void openLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openEmailAddressEntryPage() {
        Intent intent = new Intent(this, RegisterBasicInfo.class);
        startActivity(intent);
    }
}

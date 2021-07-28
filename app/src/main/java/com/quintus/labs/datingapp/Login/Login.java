package com.quintus.labs.datingapp.Login;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.PreferenceUtils;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.database.DbAccountHelper;


/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */
public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private User mUser;
    private DbAccountHelper mAccountHelper;
    private Context mContext;
    private EditText mEmail, mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mContext = Login.this;
        mAccountHelper = new DbAccountHelper(mContext);

        mUser = new User("", "", "",
                "", "", "", false, false,
                false, false, "", "", "",
                37.349642, -121.938987, "","","","","","");

        init();
    }

    private boolean isStringNull(String string) {
        Log.d(TAG, "isStringNull: checking string if null.");

        return string.equals("");
    }

    //----------------------------------------Firebase----------------------------------------

    private void init() {
        //initialize the button for logging in
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (isStringNull(email) || isStringNull(password)) {
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(mAccountHelper.checkEmailPassword(email, password) == true) {
                        PreferenceUtils.saveEmail(email, mContext);
                        PreferenceUtils.savePassword(password, mContext);
                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();

                        SQLiteDatabase db = openOrCreateDatabase("Account.db", Context.MODE_PRIVATE, null);
                        Cursor cursor = db.rawQuery("select * from Account where email = ?", new String[]{email});
                        cursor.moveToFirst();
                        mUser.setEmail(cursor.getString(0));
                        mUser.setUsername(cursor.getString(1));

                        mUser.setProfileImageUrl(cursor.getString(4));
                        cursor.close();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("classUser", mUser);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Login fail! If you dont have account, hãy đăng ký!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        TextView linkSignUp = findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to register screen");
                Intent intent = new Intent(Login.this, RegisterBasicInfo.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
    }
}

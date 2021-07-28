package com.quintus.labs.datingapp.Login;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GPS;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.database.DbAccountHelper;
import com.quintus.labs.datingapp.database.DbMatchedHelper;
import com.quintus.labs.datingapp.database.DbProfileHelper;
import com.quintus.labs.datingapp.database.DbSettingHelper;

public class RegisterBasicInfo extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    GPS gps;
    private Context mContext;
    private String email, username, password;
    private EditText mEmail, mPassword, mUsername;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private String append = "";
    private DbAccountHelper mAccountHelper;
    private DbSettingHelper mSettingHelper;
    private DbProfileHelper mProfileHelper;
    private DbMatchedHelper matchedHelper;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerbasic_info);

        mContext = RegisterBasicInfo.this;
        mAccountHelper = new DbAccountHelper(mContext);
        mAccountHelper.getWritableDatabase();

        mSettingHelper = new DbSettingHelper(mContext);
        mSettingHelper.getWritableDatabase();

        mProfileHelper = new DbProfileHelper(mContext);
        mProfileHelper.getWritableDatabase();
        matchedHelper=new DbMatchedHelper(mContext);
        matchedHelper.getWritableDatabase();

        gps = new GPS(getApplicationContext());

        initWidgets();
        init();
    }

    private void init() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                if (checkInputs(email, username, password))
                {
                    //find geo location
                    //find geo location
                    Location location = gps.getLocation();
                    double latitude = 37.349642;
                    double longtitude = -121.938987;
                    if (location != null) {
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();
                    }
                    Log.d("Location==>", longtitude + "   " + latitude);


                    Intent intent = new Intent(RegisterBasicInfo.this, RegisterGender.class);
                    User user = new User("", "", "", "", email, username, false, false, false, false, "", "", "", latitude, longtitude,"","","","","","");
                    intent.putExtra("password", password);
                    intent.putExtra("classUser", user);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkInputs(String email, String username, String password) {
        if (email.equals("") || username.equals("") || password.equals("")) {
            Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Below code checks if the email id is valid or not.
        else if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Invalid email address, enter valid email id and click on Continue", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            Boolean checkemail = mAccountHelper.checkEmail(email);
            if(checkemail == false)
            {
                Boolean insert = mAccountHelper.insertData(email, username, password, null, "", 1);
                if(insert == true)
                {
                    Log.i("TAG", "DATABASE thanh cong!");
                    Toast.makeText(getApplicationContext(), "Register successful!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Register sucessfull!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Email already exists! Please register with another email!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: initializing widgets");
        mEmail = (EditText) findViewById(R.id.input_email);
        mUsername = (EditText) findViewById(R.id.input_username);
        btnRegister = findViewById(R.id.btn_register);
        mPassword = (EditText) findViewById(R.id.input_password);
        mContext = RegisterBasicInfo.this;
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }
}

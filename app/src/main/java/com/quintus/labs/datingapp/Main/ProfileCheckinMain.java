package com.quintus.labs.datingapp.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quintus.labs.datingapp.R;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */
public class ProfileCheckinMain extends AppCompatActivity {

    private Context mContext;
    private String profileImageUrl;
    private String name;
    private String distance;
    private String age;
    private String describe;
    private String interest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_checkin_main);

        mContext = ProfileCheckinMain.this;


       /* ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
*/

        ImageView profileImage = findViewById(R.id.profileImage);
        TextView name_main=findViewById(R.id.name_main);
        TextView distance_main=findViewById(R.id.distance_main);
        TextView decribe_main=findViewById(R.id.describe_main);
        TextView interest_main=findViewById(R.id.interest);

        Intent intent = getIntent();

        profileImageUrl = intent.getStringExtra("photo");
        name= intent.getStringExtra("name");
        distance=intent.getStringExtra("distance");
        age= intent.getStringExtra("age");
        describe=intent.getStringExtra("bio");
        interest=intent.getStringExtra("interest");

        Glide.with(mContext).load(profileImageUrl).into(profileImage);
         name_main.setText(name+","+age);
         distance_main.setText(distance);
         decribe_main.setText(describe);
         interest_main.setText(interest);

    }


    public void DislikeBtn(View v) {

            Intent btnClick = new Intent(mContext, BtnDislikeActivity.class);
            btnClick.putExtra("url", profileImageUrl);
            startActivity(btnClick);

    }

    public void LikeBtn(View v) {
            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
            btnClick.putExtra("url", profileImageUrl);
            startActivity(btnClick);

    }

}

package com.quintus.labs.datingapp.Chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.quintus.labs.datingapp.Matched.Matched_Activity;
import com.quintus.labs.datingapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private EditText edtMessage;
    private ImageButton btnSend;
    private ImageButton btnBack;

    private RecyclerView rcvMessage;
    private MessageAdapter messageAdapter;
    private List<Message> mListMessage;

    private CircleImageView mImageView;
    private TextView mTvName;

    private String mGirlName = "";
    private String mGirlAvt = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Get info girl from MatchedActivity
        Intent info = getIntent();
        if(info!= null)
        {
            mGirlName = info.getStringExtra("name");
            mGirlAvt = info.getStringExtra("avatar");
        }

        edtMessage = findViewById(R.id.edt_message);
        btnSend = findViewById(R.id.btn_send);
        mImageView = findViewById(R.id.img_avatar);
        mTvName = findViewById(R.id.tv_name);
        btnBack = findViewById(R.id.btn_back);

        mTvName.setText(mGirlName);
        if(mGirlAvt.equals("NO"))
            mImageView.setBackgroundColor(Color.BLACK);
        else{
            Glide.with(this).load(mGirlAvt).into(mImageView);
        }

        rcvMessage = (RecyclerView) findViewById(R.id.rcv_message);
        rcvMessage.setLayoutManager(new LinearLayoutManager(this));

        mListMessage = new ArrayList<>();
        messageAdapter = new MessageAdapter();
        messageAdapter.setData(mListMessage);

        rcvMessage.setAdapter(messageAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MessageActivity.this, Matched_Activity.class);
                startActivity(i);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    public void sendMessage()
    {
        String strMessage = edtMessage.getText().toString().trim();
        if (strMessage == null || TextUtils.isEmpty(strMessage))
        {
            return;
        }
        mListMessage.add(new Message(strMessage));
        messageAdapter.notifyDataSetChanged();
        rcvMessage.scrollToPosition(mListMessage.size() - 1);

        edtMessage.setText("");
    }
}
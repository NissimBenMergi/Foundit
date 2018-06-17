package com.nl.founditapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    private ImageView mProfilePicPreview;
    private EditText mProfileNameEditText;
    private TextView mProfileGengerTextView;
    private EditText mProfileEmailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProfilePicPreview = (ImageView) findViewById(R.id.profilePicPreview);
        mProfileNameEditText = (EditText) findViewById(R.id.profileNameEditText);
        mProfileGengerTextView = (TextView) findViewById(R.id.profileGengerTextView);
        mProfileEmailEditText = (EditText) findViewById(R.id.profileEmailEditText);


        mProfileEmailEditText.setText(Main.connectedUser.getEmail());
        mProfileNameEditText.setText(Main.connectedUser.getFullName());
        mProfileGengerTextView.setText(Main.connectedUser.getGender());
        Picasso.with(this).load(Main.connectedUser.getProfilePic()).transform(new CircleTransform()).resize(136, 136).into(mProfilePicPreview);
    }

    public void MyActivitiesClick(View view) {
        Intent intent = new Intent(this, MyActivities.class);
        startActivity(intent);
    }

    public void finish(View view) {
        finish();
    }
}
package com.nl.founditapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Categories extends AppCompatActivity {


    static String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
    }

    public void AddClick(View view) {
        Intent intent;


        switch (view.getId()) {
            case R.id.partyBtn:
                category = "Party";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.socialBtn:
                category = "Social";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.sportBtn:
                category = "Sport";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.datingBtn:
                category = "Dating";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.studyBtn:
                category = "Study";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.funBtn:
                category = "Fun";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.freeEventsBtn:
                category = "Free Event";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.jobsBtn:
                category = "Job";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.CookingAndBakingBtn:
                category = "Cooking";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.volunteeringBtn:
                category = "Volunteering";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.religionBtn:
                category = "Religion";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.everythingElseBtn:
                category = "Everything else";
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                this.finish();
                break;

        }
    }

}
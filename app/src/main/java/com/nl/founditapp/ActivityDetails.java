package com.nl.founditapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityDetails extends AppCompatActivity {

    private Activity current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        TextView title = (TextView) findViewById(R.id.textViewTitle);
        EditText more = (EditText) findViewById(R.id.editTextDescription);
        TextView categoty = (TextView) findViewById(R.id.textViewCategory);
        EditText date = (EditText) findViewById(R.id.editTextDate);
        EditText time = (EditText) findViewById(R.id.editTextTime);
        TextView location = (TextView)findViewById(R.id.textViewLocation);
        current = List.activitiesList.get(position);


        title.setText(current.getTitle());
        more.setText(current.getAction());
        categoty.setText(current.getCategory());
        location.setText(current.getLocation());
        date.setText(current.getDay() + "." + current.getMonth() + "." + current.getYear());
        time.setText(current.getHour() + ":" + current.getMinute());
    }

    public void wazeClick(View view) {
        String uri = "waze://?ll="+current.getLat()+", "+current.getLng();
        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
    }
}
package com.nl.founditapp;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{

    private Activity newActivity;
    private EditText titleText;
    private EditText categoryText;
    private TextView dateText;
    private TextView timeText;
    private EditText descriptionText;
    private Button locationButton;
    private TextView locationTextView;
    private Button addActivityButton;
    private ProgressDialog progressDialog;
    private Place here;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        newActivity = new Activity();
        titleText=(EditText)findViewById(R.id.titleText);
        categoryText=(EditText)findViewById(R.id.categoryText);
        descriptionText=(EditText)findViewById(R.id.descriptionText);
        dateText = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);
        locationButton = (Button) findViewById(R.id.locationButton);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        addActivityButton = (Button) findViewById(R.id.addActivityButton);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        categoryText.setText(Categories.category);
        pickers();

        dateText.setOnClickListener(this);
        timeText.setOnClickListener(this);
        locationButton.setOnClickListener(this);
        addActivityButton.setOnClickListener(this);
    }


    private void pickers() {
        selectedCalendar = Calendar.getInstance();
        DatePicker.OnDateChangedListener onDateChangedListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, day);
                datePicker.setVisibility(View.GONE);
                dateText.setText(getDateFormat(selectedCalendar));
            }
        };

        datePicker.init(selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH), onDateChangedListener);


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                if (minute >= 0) {

                    timeText.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
                    timePicker.setVisibility(View.GONE);
                }
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateText:
                datePicker.setVisibility(View.VISIBLE);
                datePicker.isFocusableInTouchMode();
                Toast.makeText(this, "Date", Toast.LENGTH_SHORT).show();
                break;
            case R.id.timeText:
                timePicker.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Time", Toast.LENGTH_SHORT).show();
                break;
            case R.id.locationButton:
                placeIntent();
                break;
            case R.id.addActivityButton:


                if(here==null)
                {
                    Toast.makeText(this, R.string.tMustPickLocation, Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(titleText.getText().length()==0)
                {
                    Toast.makeText(this, R.string.tTitleIsEmpty, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    newActivity.setAction(descriptionText.getText().toString());
                    newActivity.setCategory(Categories.category);
                    newActivity.setTitle(titleText.getText().toString());
                    newActivity.setLocation(locationTextView.getText().toString());
                    newActivity.setDay(datePicker.getDayOfMonth());
                    newActivity.setMonth(datePicker.getMonth() + 1);
                    newActivity.setYear(datePicker.getYear());
                    newActivity.setHour(timePicker.getHour());
                    newActivity.setMinute(timePicker.getMinute());
                    newActivity.setUser(Main.connectedUser);
                    newActivity.setLat(here.getLatLng().latitude);
                    newActivity.setLng(here.getLatLng().longitude);
                    progressDialog.setMessage("Creating...");
                    progressDialog.show();
                    addActivity(newActivity);
                    this.finish();

                    break;
                }
        }
    }
    private void addActivity(Activity activity) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("activities");
        final String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(activity);

    }
    private void placeIntent(){
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 520);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 520 && resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            here=place;
            locationTextView.setText(place.getAddress());
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {}
        else if (resultCode == RESULT_CANCELED) {}
    }

    public static String getDateFormat(Calendar calendar) {
        String date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        date = simpleDateFormat.format(calendar.getTime());
        return date;
    }
}
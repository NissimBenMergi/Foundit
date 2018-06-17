package com.nl.founditapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyActivities extends AppCompatActivity {
    public static ArrayList<Activity> myActivities;
    private DatabaseReference databaseReference;
    private MyActivities window = this;
    private Activity act = new Activity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activities);


        loadDatabse();
    }



    private void loadDatabse() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("activities");
        myActivities= new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot activity : dataSnapshot.getChildren()) {
                        if(activity.getValue(Activity.class).getUser().getEmail().equals(Main.connectedUser.getEmail())) {
                            myActivities.add(activity.getValue(Activity.class));
                        }
                    }
                }
                loadView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loadView() {
        ListView listView = (ListView) findViewById(R.id.personalActivitiesList);
        MyActivities.ActivitiesListAdapter activitiesListAdapter = new MyActivities.ActivitiesListAdapter();
//
//
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent actionIntent = new Intent(window, personalDetailes.class);
                                                actionIntent.putExtra("position", position);
                                                startActivity(actionIntent);

                                            }
                                        }


        );

        //

        listView.setAdapter(activitiesListAdapter);
    }

    public class ActivitiesListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myActivities.size();
        }

        @Override
        public Object getItem(int i) {
            return myActivities.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.personal_row_design, null);


            TextView item_titleTextView = (TextView) view.findViewById(R.id.item_titleTextView);
            TextView item_categoryTextView = (TextView) view.findViewById(R.id.item_categoryTextView);
            TextView dateTextView = (TextView) view.findViewById(R.id.item_date);
            TextView timeTextView = (TextView) view.findViewById(R.id.item_time);


            dateTextView.setText(myActivities.get(i).getDay() + "." + myActivities.get(i).getMonth() + "." + myActivities.get(i).getYear());
            timeTextView.setText(myActivities.get(i).getHour() + ":" + myActivities.get(i).getMinute());
            item_titleTextView.setText(myActivities.get(i).getTitle());
            item_categoryTextView.setText(myActivities.get(i).getCategory());

            return view;
        }


    }

}

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

public class List extends AppCompatActivity {

    public static ArrayList<Activity> activitiesList;
    private DatabaseReference databaseReference;
    private List window = this;
    private Activity act=new Activity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        loadDatabse();
    }

    private void loadDatabse() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("activities");
        activitiesList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot activity : dataSnapshot.getChildren()) {
                        activitiesList.add(activity.getValue(Activity.class));
                    }
                    activitiesList=act.locSort(activitiesList);
                }
                loadView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadView() {
        ListView listView = (ListView) findViewById(R.id.activitiesList);
        ActivitiesListAdapter activitiesListAdapter = new ActivitiesListAdapter();
//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent actionIntent = new Intent(window, ActivityDetails.class);
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
            return activitiesList.size();
        }

        @Override
        public Object getItem(int i) {
            return activitiesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.row_design, null);

            ImageView itemImageView = (ImageView) view.findViewById(R.id.item_imageView);
            TextView itemTitleTextView = (TextView) view.findViewById(R.id.item_titleTextView);
            TextView itemDescriptionTextView = (TextView) view.findViewById(R.id.item_descriptionTextView);
            TextView dateTextView = (TextView) view.findViewById(R.id.item_date);
            TextView timeTextView = (TextView) view.findViewById(R.id.item_time);
            TextView nameTextView = (TextView)view.findViewById(R.id.item_nameTextView);
            TextView locationListTextView = (TextView)view.findViewById(R.id.locationListTextView);

            int full = (activitiesList.get(i).getDistance().intValue());
            int frac =(int)((activitiesList.get(i).getDistance() - activitiesList.get(i).getDistance().intValue()) *100);

            locationListTextView.setText(full+"."+frac+"Km");

            dateTextView.setText(activitiesList.get(i).getDay() + "." + activitiesList.get(i).getMonth() + "." + activitiesList.get(i).getYear());
            timeTextView.setText(activitiesList.get(i).getHour() + ":" + activitiesList.get(i).getMinute());
            Picasso.with(window).load(activitiesList.get(i).getUser().getProfilePic()).transform(new CircleTransform()).resize(136, 136)
                    .centerCrop().into(itemImageView);
            //itemImageView.setImageResource(R.drawable.profile);
            itemTitleTextView.setText(activitiesList.get(i).getTitle());
            itemDescriptionTextView.setText(activitiesList.get(i).getAction());
            nameTextView.setText(activitiesList.get(i).getUser().getFullName());
            return view;
        }


    }

}
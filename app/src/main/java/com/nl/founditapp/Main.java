package com.nl.founditapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main extends AppCompatActivity {

    public static User connectedUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        String username = settings.getString("email","anonymous");
        if (!username.equals("anonymous")) {
        }
        else
        {

            Intent LoginIntent = new Intent(this, Login.class);
            startActivity(LoginIntent);
            finish();
        }



        if (connectedUser == null)
            connectedUser = new User();

   

        userAuth();
    }

    public void AddClick(View view) {

        Intent intent = new Intent(this, Categories.class);
        startActivity(intent);
    }

    public void FindClick(View view) {
        Intent intent = new Intent(this, List.class);
        startActivity(intent);
    }

    public void LogOutClick(View view){
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().commit();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
    private void loadComponents() {
        ////set all fields from connectedUser!!

       // Toast.makeText(getApplicationContext(), connectedUser.getEmail() + connectedUser.getGender(), Toast.LENGTH_SHORT).show();
    }

    private void userAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null)
                    setUserData();
            }
        });
    }

    private void setUserData() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());
//        databaseReference.addValueEventListener
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    connectedUser = dataSnapshot.getValue(User.class);
                    loadComponents();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.profileItem:
                intent = new Intent(this, Profile.class);
                startActivity(intent);
                return true;
            case R.id.foundItItem:
                intent = new Intent(this, List.class);
                startActivity(intent);
                return true;
            case R.id.helpItem:
                intent = new Intent(this, Help.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setMessage(R.string.tAreYouSure)
                .setPositiveButton(R.string.tYesOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(Main.this, Login.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.tNoOption, null)
                .show();
    }

}

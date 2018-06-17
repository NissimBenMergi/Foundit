package com.nl.founditapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private TextView mRegistrationTextView;
    private ProgressDialog progressDialog;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        mEmailEditText = (EditText) findViewById(R.id.editText_email);
        mPasswordEditText = (EditText) findViewById(R.id.editText_password);
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mRegistrationTextView = (TextView) findViewById(R.id.registrationButton);
        mLoginButton.setOnClickListener(this);
        mRegistrationTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //progressDialog.setMessage("Loading...");
                //progressDialog.show();
                checkDetails(view);
                break;
            case R.id.registrationButton:
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                break;

        }
    }

    private void checkDetails(View view) {

        if (mEmailEditText.getText().toString().equals("") || mPasswordEditText.getText().toString().equals("")) {
            progressDialog.hide();
            Toast.makeText(getApplicationContext(), R.string.tOneOrMoreFieldsIsEmpty, Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), R.string.tWelcome,
                                Toast.LENGTH_LONG).show();
                        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("email", mEmailEditText.getText().toString());
                        editor.commit();
                        finish();
                        Intent intent = new Intent(Login.this, Main.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.tEmailOrPasswordIsWrong,
                                Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    }
}

package com.nl.founditapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button addProfilePicButton;
    private Button finishButtonSignUp;
    private ImageView profilePicPreview;
    private RadioGroup genderRadioGroup;
    private User user;
    private Uri uri;
    private ProgressDialog progressDialog;
    private int load=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user = new User();
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        addProfilePicButton = (Button) findViewById(R.id.addProfilePicButton);
        finishButtonSignUp = (Button) findViewById(R.id.finishButtonSignUp);
        profilePicPreview = (ImageView) findViewById(R.id.profilePicPreview);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        fullNameEditText = (EditText)findViewById(R.id.fullNameEditText);

        addProfilePicButton.setOnClickListener(this);
        finishButtonSignUp.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.addProfilePicButton:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, StaticValues.GALLERY_REQUEST_CODE);
                load=1;
                break;
            case R.id.profilePicPreview:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, StaticValues.GALLERY_REQUEST_CODE);
                load=1;
                break;
            case R.id.finishButtonSignUp:
                registrationCheckInput();
                break;
        }
    }

    private void registrationCheckInput() {
        if (passwordEditText.getText().toString().length() > 0 &&
                emailEditText.getText().toString().length() > 0 &&
                genderRadioGroup.getCheckedRadioButtonId() != -1) {
            if (genderRadioGroup.getCheckedRadioButtonId() == R.id.radioMale) {
                user.setGender(StaticValues.MALE_STRING);
            }
            if (genderRadioGroup.getCheckedRadioButtonId() == R.id.radioFemale) {
                user.setGender(StaticValues.FEMALE_STRING);
            }
            user.setEmail(emailEditText.getText().toString());
            user.setProfilePic("");
            user.setFullName(fullNameEditText.getText().toString());
            setFirebaseData();
        } else {
            Toast.makeText(getApplicationContext(), R.string.tOneOrMoreFieldsIsEmpty, Toast.LENGTH_SHORT).show();
        }
    }

    private void setFirebaseData() {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (uri != null) {
            firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), passwordEditText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(getApplicationContext(), R.string.tEmailExists, Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    Toast.makeText(getApplicationContext(), R.string.tPasswordTooShort, Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(getApplicationContext(), R.string.tInvalidEmailAddress, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final FirebaseUser firebaseUser = task.getResult().getUser();
                                progressDialog.show();

                                if (uri != null) {
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("usersProfileImage").child(firebaseUser.getUid());
                                    StorageReference picPath = storageReference;
                                    picPath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                            progressDialog.setMessage((int) progress + " % Signing");
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            Uri downloadUrl = task.getResult().getDownloadUrl();
                                            String finalUrl = downloadUrl.toString();
                                            user.setProfilePic(finalUrl);
                                            user.setUniqueID(firebaseUser.getUid());
                                            DatabaseReference newUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
                                            newUserReference.setValue(user);
                                            firebaseAuth.signOut();
                                            Toast.makeText(getApplicationContext(), R.string.tUserCreated, Toast.LENGTH_SHORT).show();
                                            finish();


                                        }
                                    });
                                }
                            }
                        }
                    }
            );
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.tNoPic, Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticValues.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            uri = data.getData();
            Picasso.with(getApplicationContext()).load(uri).transform(new CircleTransform()).resize(136, 136).into(profilePicPreview);

        }
    }
}

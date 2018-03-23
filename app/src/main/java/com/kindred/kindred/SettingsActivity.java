package com.kindred.kindred;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    //Firebase Variables
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    //Storage Firebase
    private StorageReference mImageStorage;


    //Android layout
    private TextView mDisplayName;
    private TextView mEmail;
    private ImageView userImageView;
    String imageId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Firebasae Storage
        mImageStorage = FirebaseStorage.getInstance().getReference();

        //User Fields
        mDisplayName = (TextView) findViewById(R.id.settings_displayName_textView);
        mEmail = (TextView) findViewById(R.id.settings_email_textview);
        userImageView = (ImageView) findViewById(R.id.profile_avatar_imageView);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                imageId = dataSnapshot.child("image_id").getValue().toString();
                mDisplayName.setText(name);
                mEmail.setText(email);
                userImageView.setImageResource(Integer.parseInt(imageId));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

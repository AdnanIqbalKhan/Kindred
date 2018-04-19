package com.kindred.kindred;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    private Button mChangeImage;
    private FloatingActionButton mBackButton;




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

        //change image
        mChangeImage = (Button) findViewById(R.id.settings_changeAvatar_btn);

        mChangeImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent startIntent = new Intent(SettingsActivity.this, SetAvatarActivity.class).putExtra("cameFrom","SettingsActivity");

                startActivity(startIntent);
            }
        });

        mBackButton = (FloatingActionButton) findViewById(R.id.settings_back_floatingBtn);
        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent startIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(startIntent);
            }
        });

    }
}

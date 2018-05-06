package com.kindred.kindred;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    //Firebase Variables
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    //Storage Firebase
    private StorageReference mImageStorage;
    private String mCurrent_uid;

    //Android layout
    private TextView mDisplayName;
    private TextView mEmail;
    private CircleImageView userImageView;
    String imageId;

    private Button mChangeImage;
    private FloatingActionButton mBackButton;
    private Button mNotifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Firebasae Storage
        mImageStorage = FirebaseStorage.getInstance().getReference();

        //User Fields
        mDisplayName = (TextView) findViewById(R.id.settings_displayName_textView);
        mEmail = (TextView) findViewById(R.id.settings_email_textview);
        userImageView = (CircleImageView) findViewById(R.id.profile_avatar_imageView);
        mNotifyBtn = findViewById(R.id.setting_notify_btn);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mCurrent_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrent_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                imageId = dataSnapshot.child("image_id").getValue().toString();
                Boolean notifyOrder = dataSnapshot.child("notifyOrder").getValue().toString().equals("true");
                if (notifyOrder) {
                    mNotifyBtn.setText("Allowed");
                } else {
                    mNotifyBtn.setText("Not Allowed");
                }
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

        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startIntent = new Intent(SettingsActivity.this, SetAvatarActivity.class).putExtra("cameFrom", "SettingsActivity");

                startActivity(startIntent);
            }
        });

        mBackButton = (FloatingActionButton) findViewById(R.id.settings_back_floatingBtn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(startIntent);
            }
        });

        mNotifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("New Order Notification")
                        .setMessage("Do you allow notification when a new order is placed?")
                        .setIcon(R.drawable.notify_)
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mUserDatabase.child("notifyOrder").setValue("true");
                            }
                        })
                        .setNegativeButton("Not Allow", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mUserDatabase.child("notifyOrder").setValue("false");
                            }
                        })
                        .show();
            }
        });
    }
}

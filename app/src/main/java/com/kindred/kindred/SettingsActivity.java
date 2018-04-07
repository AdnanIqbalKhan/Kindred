package com.kindred.kindred;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

    //Cache Variables
    String username;
    String avatarId;
    String userEmail;
    String multipleLines;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        multipleLines = readProfile();
        String[] lines = multipleLines.split(System.getProperty("line.separator"));
        username = lines[0];
        avatarId = lines[1];
        userEmail = lines[2];


        //Firebasae Storage
        mImageStorage = FirebaseStorage.getInstance().getReference();

        //User Fields
        mDisplayName = (TextView) findViewById(R.id.settings_displayName_textView);
        mDisplayName.setText(username);
        mEmail = (TextView) findViewById(R.id.settings_email_textview);
        mEmail.setText(userEmail);
        userImageView = (ImageView) findViewById(R.id.profile_avatar_imageView);
        userImageView.setImageResource(Integer.parseInt(avatarId));


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                imageId = dataSnapshot.child("image_id").getValue().toString();
                //Writing to file for caching
                writeProfile(name , imageId, email);
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



    public void writeProfile(String username, String avatarId, String email)
    {
        String file_name = "name_file";
        String profile = username + "\n" + avatarId + "\n"+ email;
        try {
            FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
            fileOutputStream.write(profile.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String readProfile()
    {
        String profile;
        try {
            FileInputStream fileInputStream = openFileInput("name_file");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer= new StringBuffer();
            while((profile=bufferedReader.readLine())!=null)
            {
                stringBuffer.append(profile + "\n");
            }
            profile =  stringBuffer.toString();
            return profile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

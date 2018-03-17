package com.kindred.kindred;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


    //ProgressBar Variables
    private TextView mtextView1;
    private TextView mtextView2;
    private ConstraintLayout mConstraintLayout;
    private ProgressBar mProgressBar;
    private Handler mHandler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //ProgressBar
        mProgressBar = (ProgressBar) findViewById(R.id.settings_loading_Image_progressBar);
        mtextView1 = (TextView) findViewById(R.id.settings_image_uploading_textview_1);
        mtextView2 = (TextView) findViewById(R.id.settings_image_uploading_textview_2);

        //Firebasae Storage
        mImageStorage = FirebaseStorage.getInstance().getReference();

        //User Fields
        mDisplayName = (TextView) findViewById(R.id.settings_displayName_textView);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mDisplayName.setText(name);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mtextView1.setVisibility(View.VISIBLE);
                        mtextView1.setTypeface(null, Typeface.BOLD);
                        mtextView2.setVisibility(View.VISIBLE);
                        mtextView2.setTypeface(null, Typeface.BOLD);
                    }
                });

                Uri resultUri = result.getUri();

                final File thumb_filePath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();

                Bitmap thumb_bitmap = null;

                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                final byte[] thumb_byte = baos.toByteArray();





                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(current_user_id+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                    if(thumb_task.isSuccessful()){

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("image", download_url);
                                        update_hashMap.put("thumb_image", thumb_downloadUrl);
                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    mHandler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            mProgressBar.setVisibility(View.GONE);
                                                            mtextView1.setVisibility(View.GONE);
                                                            mtextView1.setTypeface(null, Typeface.BOLD);
                                                            mtextView2.setVisibility(View.GONE);
                                                            mtextView2.setTypeface(null, Typeface.BOLD);
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        Toast.makeText(SettingsActivity.this,"Image Uploaded Successfuly",Toast.LENGTH_LONG).show();

                                    }
                                    else{

                                        Toast.makeText(SettingsActivity.this,"Error Occured While Uploading thumb_nail",Toast.LENGTH_LONG).show();
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setVisibility(View.GONE);
                                                mtextView1.setVisibility(View.GONE);
                                                mtextView1.setTypeface(null, Typeface.BOLD);
                                                mtextView2.setVisibility(View.GONE);
                                                mtextView2.setTypeface(null, Typeface.BOLD);
                                            }
                                        });

                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(SettingsActivity.this,"Error Occured While Uploading Image",Toast.LENGTH_LONG).show();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setVisibility(View.GONE);
                                    mtextView1.setVisibility(View.GONE);
                                    mtextView1.setTypeface(null, Typeface.BOLD);
                                    mtextView2.setVisibility(View.GONE);
                                    mtextView2.setTypeface(null, Typeface.BOLD);
                                }
                            });
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}

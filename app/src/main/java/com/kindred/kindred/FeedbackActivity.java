package com.kindred.kindred;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    private String mUserId, mUserEmail;
    private ImageButton mSubmmitBtn;
    private MultiAutoCompleteTextView mFeedbackText;
    private TextView mEmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        final Toolbar mFeedbackToolBar = findViewById(R.id.feedback_app_bar);
        setSupportActionBar(mFeedbackToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mSubmmitBtn = findViewById(R.id.feedback_submit_btn);
        mFeedbackText = findViewById(R.id.feedback_feedback_text);
        mEmailText = findViewById(R.id.feedback_email_text);

        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        mEmailText.setText(mUserEmail);

        mSubmmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubmmitBtn.setEnabled(false);
                String feedback = mFeedbackText.getText().toString().replaceAll("\\s+", " ").trim();
                mFeedbackText.setText(null);
                if (!feedback.equals("")) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("userId", mUserId);
                    data.put("feedback", feedback);
                    data.put("time", ServerValue.TIMESTAMP);

                    FirebaseDatabase.getInstance().getReference().child("Feedback").push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(FeedbackActivity.this, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FeedbackActivity.this, "An Error Occur", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(FeedbackActivity.this, "Empty feedback not allowed", Toast.LENGTH_SHORT).show();
                }
                mSubmmitBtn.setEnabled(true);
            }
        });

    }
}

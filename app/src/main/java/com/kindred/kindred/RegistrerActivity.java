package com.kindred.kindred;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegistrerActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    //ProgressBar Variables
    private TextView mtextView1;
    private TextView mtextView2;
    private ConstraintLayout mConstraintLayout;
    private ProgressBar mProgressBar;
    private Handler mHandler = new Handler();

    //Facebook login variables
    private CallbackManager mCallbackManager;
    private static final String TAG = "FACELOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrer);

        //ProgressBar
        mProgressBar = (ProgressBar) findViewById(R.id.reg_loading_progressBar);
        mtextView1 = (TextView) findViewById(R.id.reg_progressBar_textView1);
        mtextView2 = (TextView) findViewById(R.id.reg_progressBar_textView2);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.reg_background);

        //Toolbar Set
        Toolbar toolbar = findViewById(R.id.reg_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mDisplayName = (TextInputLayout) findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mCreateBtn = (Button) findViewById(R.id.reg_create_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mtextView1.setVisibility(View.VISIBLE);
                        mtextView1.setTypeface(null, Typeface.BOLD);
                        mtextView2.setVisibility(View.VISIBLE);
                        mtextView2.setTypeface(null, Typeface.BOLD);

                        mConstraintLayout.setBackgroundColor(0xff444444);
                    }
                });

                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                register_user(display_name, email, password);
            }
        });

        //Facebook login
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

    private void register_user(final String display_name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = mCurrentUser.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setVisibility(View.GONE);
                                    mtextView1.setVisibility(View.GONE);
                                    mtextView2.setVisibility(View.GONE);
                                    mConstraintLayout.setBackgroundColor(0xffffffff);

                                }
                            });

                            FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("deviceToken").setValue(FirebaseInstanceId.getInstance().getToken()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent mainIntent = new Intent(RegistrerActivity.this, SetAvatarActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            });
                        }
                    });

                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            mtextView1.setVisibility(View.GONE);
                            mtextView2.setVisibility(View.GONE);
                            mConstraintLayout.setBackgroundColor(0xffffffff);

                        }
                    });
//                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(RegistrerActivity.this, "Failed Registration: " , Toast.LENGTH_SHORT).show();
//                    Toast.makeText(RegistrerActivity.this, "Error Occured Creating Your Account",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        Toast.makeText(RegistrerActivity.this,"you are logged in", Toast.LENGTH_LONG).show();
        Intent accountIntent = new Intent(RegistrerActivity.this, SetAvatarActivity.class);
        startActivity(accountIntent);
        finish();
    }


    //Facebook login methods
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegistrerActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}

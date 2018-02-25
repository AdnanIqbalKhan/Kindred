package com.kindred.kindred;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private Button mLoginBtn;

    private FirebaseAuth mAuth;

    //ProgressBar Variables
    private TextView mtextView1;
    private TextView mtextView2;
    private ConstraintLayout mConstraintLayout;
    private ProgressBar mProgressBar;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ProgressBar
        mProgressBar = (ProgressBar) findViewById(R.id.login_progressBar);
        mtextView1 = (TextView) findViewById(R.id.login_loading_textView1);
        mtextView2 = (TextView) findViewById(R.id.login_loading_textView2);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.login_background);


        mAuth = FirebaseAuth.getInstance();

        //Toolbar set
        Toolbar toolbar = findViewById(R.id.login_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoginEmail = (TextInputLayout) findViewById(R.id.login_email);
        mLoginPassword = (TextInputLayout) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
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

                mProgressBar.setVisibility(View.VISIBLE);
                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    loginUser(email,password);
                }

            }
        });


    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            mtextView1.setVisibility(View.GONE);
                            mtextView2.setVisibility(View.GONE);
                            mConstraintLayout.setBackgroundColor(0xffffffff);

                        }
                    });

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else{
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
                    Toast.makeText(LoginActivity.this, "Failed Login: Please Try Again" , Toast.LENGTH_SHORT).show();
//                    Toast.makeText(LoginActivity.this, "Cannot Sign in. Please Check Your Credentials",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

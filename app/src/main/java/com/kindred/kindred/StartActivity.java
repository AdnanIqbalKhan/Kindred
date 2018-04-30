package com.kindred.kindred;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

public class StartActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private SignInButton mGoogleBtn;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    updateUI(user);
                }
            }
        };

        mGoogleBtn = findViewById(R.id.google_login_button);
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                .setTosUrl("https://adnaniqbalkhan.github.io/Kindred/")
                                .setPrivacyPolicyUrl("https://adnaniqbalkhan.github.io/Kindred/")
                                .setTheme(R.style.AppTheme)
                                .setIsSmartLockEnabled(false)
                                .build(),
                        RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                updateUI(FirebaseAuth.getInstance().getCurrentUser());
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(StartActivity.this, "Error in SignIn", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(StartActivity.this, "Unable to Connect to Network", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(StartActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    private void updateUI(final FirebaseUser currentUser) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(currentUser.getUid())) {
                    Toast.makeText(StartActivity.this, "you are logged in", Toast.LENGTH_SHORT).show();
                    ref.child(currentUser.getUid()).child("deviceToken").setValue(FirebaseInstanceId.getInstance().getToken());
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(StartActivity.this, "you account has created", Toast.LENGTH_SHORT).show();
                    Intent accountIntent = new Intent(StartActivity.this, SetAvatarActivity.class);
                    accountIntent.putExtra("USER_EMAIL", currentUser.getEmail());
                    accountIntent.putExtra("NAME", currentUser.getDisplayName());
                    accountIntent.putExtra("cameFrom", "StartActivity");
                    startActivity(accountIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

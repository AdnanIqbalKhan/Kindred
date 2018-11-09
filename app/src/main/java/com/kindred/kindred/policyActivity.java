package com.kindred.kindred;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import us.feras.mdv.MarkdownView;

public class policyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        final Bundle extras = getIntent().getExtras();
        MarkdownView markdownView = (MarkdownView) findViewById(R.id.markdownView);
        markdownView.loadMarkdownFile("file:///android_asset/policy.md");
        Button next = findViewById(R.id.policy_next_btn);
        if (extras == null) {
            next.setText("Back");
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accountIntent;
                if (extras == null) {
                    accountIntent = new Intent(policyActivity.this, MainActivity.class);
                } else {
                    String email = extras.getString("USER_EMAIL");
                    String name = extras.getString("NAME");
                    String cameFrom = extras.getString("cameFrom");

                    accountIntent = new Intent(policyActivity.this, SetAvatarActivity.class);
                    accountIntent.putExtra("USER_EMAIL", email);
                    accountIntent.putExtra("NAME", name);
                    accountIntent.putExtra("cameFrom", cameFrom);
                }
                startActivity(accountIntent);
                finish();
            }
        });
    }
}

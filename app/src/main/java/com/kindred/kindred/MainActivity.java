package com.kindred.kindred;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    //ViewPager
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.main_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        mTabLayout.getTabAt(0).setText("PLACE ORDER");
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_list_white);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_done_white);
        mTabLayout.getTabAt(3).setIcon(R.drawable.ic_queue_white);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    mTabLayout.getTabAt(i).setText("");
                }

                mTabLayout.getTabAt(0).setIcon(R.drawable.ic_add_box_white);
                mTabLayout.getTabAt(1).setIcon(R.drawable.ic_list_white);
                mTabLayout.getTabAt(2).setIcon(R.drawable.ic_done_white);
                mTabLayout.getTabAt(3).setIcon(R.drawable.ic_queue_white);

                mTabLayout.getTabAt(position).setIcon(null);
                switch (position) {
                    case 0:
                        mTabLayout.getTabAt(0).setText("PLACE ORDER");
                        break;
                    case 1:
                        mTabLayout.getTabAt(1).setText("ORDERS LIST");
                        break;
                    case 2:
                        mTabLayout.getTabAt(2).setText("CONFIRMED");
                        break;
                    case 3:
                        mTabLayout.getTabAt(3).setText("POSTED");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logout_btn) {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if (item.getItemId() == R.id.main_settings_btn) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }
}

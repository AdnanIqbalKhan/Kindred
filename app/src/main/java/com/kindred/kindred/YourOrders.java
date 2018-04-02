package com.kindred.kindred;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.github.pwittchen.swipe.library.rx2.SwipeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class YourOrders extends AppCompatActivity {

    //ViewPager
    private ViewPager mViewPager;
    private SectionsPagerAdapter2 mSectionsPagerAdapter;
    private TabLayout mTabLayout;

    //bottom Toolbar
    private RelativeLayout mBottomToolbar;
    //Main2 Code
    private Button mOrderListBtn;
    private Button mHomeBtn;
    private Button mYourOrdersBtn;
    private Toolbar mToolbar;

    //Swipe to change activity
    private Swipe swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        mBottomToolbar = findViewById(R.id.yourOrder_bottom_navBar);


        //Swipe to change activity

        swipe = new Swipe();
        swipe.setListener(new SwipeListener() {
            @Override public void onSwipingLeft(final MotionEvent event) {
/*                Intent startIntent = new Intent(YourOrders.this, MainActivity.class);
                startActivity(startIntent);
                finish();*/
            }
            @Override public boolean onSwipedLeft(final MotionEvent event) {
                return false;
            }
            @Override public void onSwipingRight(final MotionEvent event) {
/*                Intent startIntent = new Intent(YourOrders.this, OrderList.class);
                startActivity(startIntent);
                finish();*/
            }
            @Override public boolean onSwipedRight(final MotionEvent event) {
                return false;
            }
            @Override public void onSwipingUp(final MotionEvent event) {
            }
            @Override public boolean onSwipedUp(final MotionEvent event) {
                return false;
            }
            @Override public void onSwipingDown(final MotionEvent event) {
            }
            @Override public boolean onSwipedDown(final MotionEvent event) {
                return false;
            }
        });


        mOrderListBtn = (Button) findViewById(R.id.yourOrder_nav_orderList);
        mOrderListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(YourOrders.this, OrderList.class);
                startActivity(startIntent);
                finish();
            }
        });

        mHomeBtn = (Button) findViewById(R.id.yourOrder_nav_placeOrder);
        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(YourOrders.this, MainActivity.class);
                startActivity(startIntent);
                finish();
            }
        });

        mYourOrdersBtn = (Button) findViewById(R.id.yourOrder_nav_yourOrders);
        mYourOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(YourOrders.this, YourOrders.class);
                startActivity(startIntent);
                finish();
            }
        });

        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter2(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#433d3f"));
        mTabLayout.getTabAt(0).setText("Confirmed");
        mTabLayout.getTabAt(1).setText("Posted");


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    mTabLayout.getTabAt(i).setText("");
                }
                mTabLayout.getTabAt(0).setText("Confirmed");
                mTabLayout.getTabAt(1).setText("Posted");

                mTabLayout.getTabAt(position).setIcon(null);
                switch (position) {
                    case 0:
                        mTabLayout.getTabAt(0).setText("Confirmed");
                        break;
                    case 1:
                        mTabLayout.getTabAt(1).setText("Posted");

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

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void sendToStart() {
        Intent startIntent = new Intent(YourOrders.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logout_btn) {
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("deviceToken").removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    sendToStart();
                }
            });
        }
        if (item.getItemId() == R.id.main_settings_btn) {
            Intent settingsIntent = new Intent(YourOrders.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }

}

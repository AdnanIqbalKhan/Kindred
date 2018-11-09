package com.kindred.kindred;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderList extends AppCompatActivity {

    private RecyclerView mOrdersListRecyclerView;
    private DatabaseReference mOrdersDatabase;
    private TextView mEmptyText;
    private int mCount = 0;

    //bottom Toolbar
    private RelativeLayout mBottomToolbar;
    //Main2 Code
    private Button mOrderListBtn;
    private Button mHomeBtn;
    private Button mYourOrdersBtn;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);


        mBottomToolbar = findViewById(R.id.orderList_bottom_navBar);

        mOrderListBtn = (Button) findViewById(R.id.orderList_nav_orderList);
        mOrderListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(OrderList.this, OrderList.class);
                startActivity(startIntent);
                finish();
            }
        });

        mHomeBtn = (Button) findViewById(R.id.orderList_nav_placeOrder);
        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(OrderList.this, MainActivity.class);
                startActivity(startIntent);
                finish();
            }
        });

        mYourOrdersBtn = (Button) findViewById(R.id.orderList_nav_yourOrders);
        mYourOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(OrderList.this, YourOrders.class);
                startActivity(startIntent);
                finish();
            }
        });


        mOrdersDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        mOrdersDatabase.keepSynced(true);
        mOrdersListRecyclerView = (RecyclerView) findViewById(R.id.orders_userOrders_recyclerView);
        mOrdersListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(OrderList.this);
        mLayoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
        mLayoutManager.setStackFromEnd(true);
        mOrdersListRecyclerView.setLayoutManager(mLayoutManager);

        mEmptyText = findViewById(R.id.orderList_empty_text);
    }


    @Override
    public void onStart() {
        super.onStart();

        Query query = mOrdersDatabase.orderByChild("posted_on");
        FirebaseRecyclerAdapter<Order, OrdersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Order, OrdersViewHolder>(
                Order.class,
                R.layout.orders_singleorder_layout2,
                OrdersViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(OrdersViewHolder viewHolder, final Order model, int position) {

                if (model.getConfirmed().equals("false") && !model.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    viewHolder.setServices_Charges(model.getServices_charges());
                    viewHolder.setPurchasingLocation(model.getPurchasing_location());
                    viewHolder.setDropOffLocation(model.getDropoff_location());
                    viewHolder.setUserImage(model.getImage_id(), OrderList.this);
                    final String post_id = getRef(position).getKey();

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent itemsDetailsIntent = new Intent(OrderList.this, ItemsDetailsActivity_2.class);
                            itemsDetailsIntent.putExtra("post_id", post_id);
                            startActivity(itemsDetailsIntent);
                        }
                    });

                    mCount = mCount + 1;
                } else {
                    viewHolder.Layout_hide();
                }

                if (mCount > 0) {
                    mOrdersListRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyText.setVisibility(View.GONE);
                } else {
                    mOrdersListRecyclerView.setVisibility(View.GONE);
                    mEmptyText.setVisibility(View.VISIBLE);
                }
            }
        };

        mOrdersListRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void sendToStart() {
        Intent startIntent = new Intent(OrderList.this, StartActivity.class);
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
                    sendToStart();
                }
            });
        }
        if (item.getItemId() == R.id.main_settings_btn) {
            Intent settingsIntent = new Intent(OrderList.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        if (item.getItemId() == R.id.main_feedback_btn) {
            Intent settingsIntent = new Intent(OrderList.this, FeedbackActivity.class);
            startActivity(settingsIntent);
        }
        if (item.getItemId() == R.id.main_policy_btn) {
            Intent settingsIntent = new Intent(OrderList.this, policyActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }

}

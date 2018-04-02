package com.kindred.kindred;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// TODO add cancel order btn
public class ItemsDetailsActivity_2 extends AppCompatActivity {

    private TextView mItemsName;
    private TextView mItemsQuantity;
    private TextView mItemsPrice;
    private TextView mItemsNote;

    private QuickSandText_TextView mDropOffLocation;
    private QuickSandText_TextView mPurchasingLocation;
    private QuickSandText_TextView mDeliverUntil;
    private QuickSandText_TextView mServiceCharges;

    private LinearLayout itemsLayout;
    private LinearLayout cardInner;

    String Names;
    String Quantity;
    String Price;
    String Note;

    LinearLayout container = null;


    //Firebase Variables
    private DatabaseReference mItemsDatabase;

    private Order post;
    private String currentUid;
    private Button genBtn;
    private Button deliveredBtn;
    private TextView deliv_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_2);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(ItemsDetailsActivity_2.this, StartActivity.class));
            finish();
        }


        //Toolbar set
        Toolbar toolbar = findViewById(R.id.reg_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] item_name = new String[1];
        final String[] item_quantity = new String[1];
        final String[] item_price = new String[1];
        final String[] item_note = new String[1];

        final String post_id = getIntent().getStringExtra("post_id");

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDropOffLocation =  findViewById(R.id.drop_off_location_textview);
        mPurchasingLocation =  findViewById(R.id.pick_up_location_textview);
        mDeliverUntil =  findViewById(R.id.delivery_deadline_textview);
        mServiceCharges =  findViewById(R.id.service_tip_textview);
        itemsLayout = findViewById(R.id.items_linear_layout);


        //container = (LinearLayout) findViewById(R.id.Items_details_table_rows_container);
        //final ViewGroup finalContainer = container;


        genBtn = findViewById(R.id.place_order_btn);
       // deliveredBtn = findViewById(R.id.item_delivered_btn);
        //deliv_txt = findViewById(R.id.item_deleved_tb);
        final HashMap<String, String> itemMap = new HashMap<String, String>();

        mItemsDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_id);
        mItemsDatabase.keepSynced(true);

        mItemsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             //   finalContainer.removeAllViews();

                post = dataSnapshot.getValue(Order.class);
                // if post is deleted
                if (post == null) {
                    return;
                }
                if (!(post.getUser_id().equals(currentUid) ||
                        Objects.equals(post.getConfirmed(), "false") ||
                        (Objects.equals(post.getConfirmed(), "true") &&
                                Objects.equals(post.getProvider().getUid(), currentUid)))) {
                    startActivity(new Intent(ItemsDetailsActivity_2.this, ErrorActivity.class));
                    finish();
                }

                mDropOffLocation.setText(post.getDropoff_location());
                mPurchasingLocation.setText(post.getPurchasing_location());
                mDeliverUntil.setText(post.getDateTime());
                mServiceCharges.setText(post.getServices_charges());

                Iterable<DataSnapshot> itemsSnapshot = dataSnapshot.child("items").getChildren();



                for (DataSnapshot item : itemsSnapshot) {
                    CardView c1 = new CardView(getBaseContext());
                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    cardViewParams.setMargins(16,16,16,8);
                    c1.setCardElevation(16);
                    c1.setMaxCardElevation(15);
                    c1.setPadding(8,8,8,8);
                    c1.setRadius(9);
                    c1.setCardBackgroundColor(Color.WHITE);

                    c1.setLayoutParams(cardViewParams);


                    cardInner = new LinearLayout(getBaseContext());
                    LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    l1.setMargins(8,8,8,8);
                    cardInner.setLayoutParams(l1);
                    cardInner.setOrientation(LinearLayout.VERTICAL);
                    ;
                    c1.setPadding(8,8,8,8);





                    QuickSandText_TextView t1 = new QuickSandText_TextView(getApplicationContext());
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp1.setMargins(8, 8, 8, 8);
                    t1.setLayoutParams(lp1);
                    //t1.setText(list.get(j));
                    t1.setText((String) item.child("Item-Name").getValue());

                    //t1.setBackgroundColor(Color.WHITE);
                    t1.setPadding(8, 8, 8, 8);
                    t1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    cardInner.addView(t1);

                    QuickSandText_TextView t2 = new QuickSandText_TextView(getApplication());
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp2.setMargins(8, 8, 8, 8);
                    t2.setLayoutParams(lp2);
                    //t1.setText(list.get(j));
                    t2.setText((String) item.child("Item-Quantity").getValue());
                    //t2.setBackgroundColor(Color.WHITE);
                    t2.setPadding(8, 8, 8, 8);
                    t2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    cardInner.addView(t2);

                    QuickSandText_TextView t3 = new QuickSandText_TextView(getApplication());
                    LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp3.setMargins(8,  8   , 8, 8);
                    t3.setLayoutParams(lp3);
                    //t1.setText(list.get(j));
                    t3.setText((String) item.child("Item-Price").getValue());

                 //   t3.setBackgroundColor(Color.WHITE);
                    t3.setPadding(8, 8, 8, 8);
                    t3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    cardInner.addView(t3);

                    QuickSandText_TextView t4 = new QuickSandText_TextView(getApplication());
                    LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp4.setMargins(8, 8, 8, 8);
                    t4.setLayoutParams(lp4);
                    //t1.setText(list.get(j));
                    t4.setText((String) item.child("Item-Note").getValue());
                    t4.setPadding(8, 8, 8, 8);
                  //  t4.setBackgroundColor(Color.WHITE);
                    t4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    cardInner.addView(t4);

                    c1.addView(cardInner);



                    itemsLayout.addView(c1);
                }




                if (Objects.equals(post.getDelivered().toString(), "true")) {
//                    deliv_txt.setVisibility(View.VISIBLE);
                } else {
  //                  deliv_txt.setVisibility(View.GONE);
                }


    //            deliveredBtn.setVisibility(View.INVISIBLE);
                if (post.getConfirmed().equals("true")) {
                    genBtn.setText("Open Chat");
                    genBtn.setBackgroundColor(0xFFE81B23);
                    genBtn.setTextColor(0xFFFFFFFF);
                    if (post.getProvider().getUid().equals(currentUid) && !Objects.equals(post.getDelivered().toString(), "true")) {
                       //l deliveredBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (post.getUser_id().equals(currentUid)) {
                        genBtn.setText("Delete");
                        genBtn.setBackgroundColor(0xFFE81B23);
                        genBtn.setTextColor(0xFFFFFFFF);
                    } else {
                        genBtn.setText("Confirm");
                        genBtn.setBackgroundColor(0xFFE81B23);
                        genBtn.setTextColor(0xFFFFFFFF);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

     /*
        deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getConfirmed().equals("true")) {//open chat

                    if (post.getProvider().getUid().equals(currentUid)) {
                        FirebaseDatabase.getInstance().getReference().child("posts")
                                .child(post_id).child("delivered").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                deliveredBtn.setVisibility(View.INVISIBLE);
                                deliv_txt.setVisibility(View.VISIBLE);
                                Toast.makeText(ItemsDetailsActivity_2.this, "Delivered", Toast.LENGTH_SHORT).show();
                            }
                        });
//                        deliveredBtn.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
*/
        genBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (post.getConfirmed().equals("true")) {//open chat

                    startActivity(new Intent(ItemsDetailsActivity_2.this, ChatActivity.class)
                            .putExtra("post_id", post_id));

                } else {
                    if (post.getUser_id().equals(currentUid)) {//delete order

                        DatabaseReference deletePostRef = FirebaseDatabase.getInstance().getReference().child("posts");
                        deletePostRef.child(post_id).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Intent mainIntent = new Intent(ItemsDetailsActivity_2.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                Toast.makeText(ItemsDetailsActivity_2.this, "Order Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    } else {//confirm order
                        startActivity(getIntent());

                        final String provider_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

                        mDbRef.child("users").child(provider_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map confirmData = new HashMap<>();
                                confirmData.put("uid", provider_uid);
                                confirmData.put("name", dataSnapshot.child("name").getValue().toString());
                                confirmData.put("image", dataSnapshot.child("image_id").getValue().toString());

                                DatabaseReference userMsgPush = mDbRef.child("users/" + provider_uid + "/confirm_orders/").push();
                                String pushId = userMsgPush.getKey();

                                Map updateDB = new HashMap<>();

                                updateDB.put("posts/" + post_id + "/provider", confirmData);
                                updateDB.put("posts/" + post_id + "/confirmed", "true");
                                updateDB.put("posts/" + post_id + "/confirmation_time", ServerValue.TIMESTAMP);
                                updateDB.put("users/" + provider_uid + "/confirm_orders/" + pushId, post_id);


                                mDbRef.updateChildren(updateDB).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(ItemsDetailsActivity_2.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }
        });


    }
}
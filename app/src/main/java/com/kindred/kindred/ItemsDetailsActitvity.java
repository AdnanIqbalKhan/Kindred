package com.kindred.kindred;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ItemsDetailsActitvity extends AppCompatActivity {

    private TextView mItemsName;
    private TextView mItemsQuantity;
    private TextView mItemsPrice;
    private TextView mItemsNote;

    private TextView mDropOffLocation;
    private TextView mPurchasingLocation;
    private TextView mDeliverUntil;
    private TextView mServiceCharges;

    String Names;
    String Quantity;
    String Price;
    String Note;

    LinearLayout container;


    //Firebase Variables
    private DatabaseReference mItemsDatabase;

    private Order post;
    private String currentUid;
    private Button genBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        //Toolbar set
        Toolbar toolbar = findViewById(R.id.items_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] item_name = new String[1];
        final String[] item_quantity = new String[1];
        final String[] item_price = new String[1];
        final String[] item_note = new String[1];

        final String post_id = getIntent().getStringExtra("post_id");

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDropOffLocation = (TextView) findViewById(R.id.ItemsDetails_DropOffLocation_textView);
        mPurchasingLocation = (TextView) findViewById(R.id.itemsDetails_PurchasingLocation_textView);
        mDeliverUntil = (TextView) findViewById(R.id.ItemsDetails_DeliverUntil_textView);
        mServiceCharges = (TextView) findViewById(R.id.ItemsDetails_ServiceCharges_textView);


        genBtn = findViewById(R.id.item_details_gen_btn);

        container = (LinearLayout) findViewById(R.id.Items_details_table_rows_container);
        final ViewGroup finalContainer = container;

        final HashMap<String, String> itemMap = new HashMap<String, String>();

        mItemsDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_id);

        mItemsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Order.class);
                mDropOffLocation.setText(post.getDropoff_location());
                mPurchasingLocation.setText(post.getPurchasing_location());
                mDeliverUntil.setText(post.getTime() + " " + post.getDate());
                mServiceCharges.setText(post.getServices_charges());

                Iterable<DataSnapshot> itemsSnapshot = dataSnapshot.child("items").getChildren();

                for (DataSnapshot item : itemsSnapshot) {

                    LayoutInflater layoutInflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.items_detail_table_row, null);

                    TextView Item_Name_TextView1 = (TextView) addView.findViewById(R.id.items_details_itemName);
                    TextView Item_Quantity_TextView1 = (TextView) addView.findViewById(R.id.items_details_itemQuantity);
                    TextView Item_Price_TextView = (TextView) addView.findViewById(R.id.Items_details_itemPrice);
                    TextView Item_Note_TextView = (TextView) addView.findViewById(R.id.items_details_itemNote);

                    item_name[0] = (String) item.child("Item-Name").getValue();
                    Item_Name_TextView1.setText(item_name[0]);
//                    mItemsName.append(item_name[0] +"\n");
                    item_quantity[0] = (String) item.child("Item-Quantity").getValue();
                    Item_Quantity_TextView1.setText(item_quantity[0]);
//                    mItemsQuantity.append(item_quantity[0] +"\n");
                    item_price[0] = (String) item.child("Item-Price").getValue();
                    Item_Price_TextView.setText(item_price[0]);
//                    mItemsPrice.append(item_price[0] +"\n");
                    item_note[0] = (String) item.child("Item-Note").getValue();
                    Item_Note_TextView.setText(item_note[0]);
//                    mItemsNote.append(item_note[0] +"\n");

                    finalContainer.addView(addView);
                }
                if (post.getConfirmed().equals("true")) {
                    genBtn.setText("Open Chat");
                } else {
                    if (post.getUser_id().equals(currentUid)) {
                        genBtn.setText("Delete");
                    } else {
                        genBtn.setText("Confirm");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        genBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (post.getConfirmed().equals("true")) {//open chat
                    startActivity(new Intent(ItemsDetailsActitvity.this, ChatActivity.class)
                            .putExtra("post_id", post_id));
                } else {
                    if (post.getUser_id().equals(currentUid)) {//delete order
                        DatabaseReference deletePostRef = FirebaseDatabase.getInstance().getReference().child("posts");
                        deletePostRef.child(post_id).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(ItemsDetailsActitvity.this, "Order Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ItemsDetailsActitvity.this, MainActivity.class));
                            }
                        });

                    } else {//confirm order
                        final String provider_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

                        mDbRef.child("users").child(provider_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map confirmData = new HashMap<>();
                                confirmData.put("uid", provider_uid);
                                confirmData.put("name", dataSnapshot.child("name").getValue().toString());
                                confirmData.put("image", dataSnapshot.child("thumb_image").getValue().toString());

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

                                        Toast.makeText(ItemsDetailsActitvity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
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
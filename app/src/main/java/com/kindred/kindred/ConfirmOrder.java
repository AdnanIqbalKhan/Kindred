package com.kindred.kindred;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Fahad Saleem on 3/22/2018.
 */

public class ConfirmOrder extends AppCompatActivity {

    private int maxItems=0;
    private int state =0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set<String> fetched;
    List<String> list;
    private LinearLayout cardLayout;
    private LinearLayout cardInnerDetails;
    private LinearLayout cardOuter;
    private LinearLayout cardInnerDelete;
    private Button placeOrder;
    private QuickSandText_TextView dropOff;
    private QuickSandText_TextView pickUp;
    private QuickSandText_TextView deliveryDate;
    private QuickSandText_TextView serviceTip;
    private Button cancelButton;
    //Database
    private DatabaseReference mDatabase;
    ArrayList<String> Item_Name_Array = new ArrayList<String>();
    ArrayList<String> Item_Quantity_Array = new ArrayList<String>();
    ArrayList<String> Item_Price_Array = new ArrayList<String>();
    ArrayList<String> Item_Comment_Array = new ArrayList<String>();

    //User Data
    public static String name;
    public static String thumb_image;
    public static String uid;
    public static String image_id;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order);
        Toolbar toolbar = findViewById(R.id.reg_app_bar);
        Bundle extras = this.getIntent().getExtras();
        placeOrder = findViewById(R.id.place_order_btn);
        dropOff = findViewById(R.id.drop_off_location_textview);
        pickUp = findViewById(R.id.pick_up_location_textview);
        deliveryDate = findViewById(R.id.delivery_deadline_textview);
        serviceTip = findViewById(R.id.service_tip_textview);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        LinearLayout deliveryLayout = findViewById(R.id.delivery_linear_layout);
        QuickSandText_TextView deliveryText = findViewById(R.id.deliveryTextView);
        if (extras.getInt("ParentActivity")==1){

            deliveryLayout.setVisibility(View.GONE);
            deliveryText.setVisibility(View.GONE);
            state =0;
            placeOrder.setText("Clear Cart");

        }
        else {
            dropOff.setText(sharedPreferences.getString("DropOff",null));
            pickUp.setText(sharedPreferences.getString("PickUp", null));
            deliveryDate.setText(sharedPreferences.getString("DeliveryDate", null));
            serviceTip.setText(sharedPreferences.getString("ServiceTip", null));
            state = 1;
            Toast.makeText(this, "This was called", Toast.LENGTH_SHORT).show();
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = mCurrentUser.getUid();
        mDatabase.child("users").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("users").child(uid).child("thumb_image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thumb_image = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("users").child(uid).child("image_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                image_id = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cardLayout = findViewById(R.id.confirm_order_layout);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       maxItems = sharedPreferences.getInt("Count",0);
       Toast.makeText(this,Integer.toString(maxItems),Toast.LENGTH_SHORT).show();

       placeOrder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (state==1) {

                   String dropOff = sharedPreferences.getString("DropOff", null);
                   String pickUp = sharedPreferences.getString("PickUp", null);
                   String deliveryDate = sharedPreferences.getString("DeliveryDate", null);
                   String serviceTip = sharedPreferences.getString("ServiceTip", null);
                   post_order(name, uid, pickUp, dropOff, deliveryDate, thumb_image, image_id, serviceTip);

                   editor = sharedPreferences.edit();
                   editor.clear().apply();
                   editor.putInt("Count", 0);


               }
               else {
                   editor = sharedPreferences.edit();
                   editor.clear().apply();
                   editor.putInt("Count", 0);
                   Toast.makeText(getApplicationContext(),"Cart Cleared!", Toast.LENGTH_SHORT).show();

                   cardLayout.removeAllViews();
                   displayEmptyCart();
                   placeOrder.setVisibility(View.GONE);

               }
           }
       });


        if (maxItems==0){
            displayEmptyCart();
            placeOrder.setVisibility(View.GONE);



        }
        else {
            for (int i=0; i<maxItems; i++) {

                CardView c1 = new CardView(this);
                LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                cardViewParams.setMargins(16,16,16,8);
                c1.setCardElevation(16);
                c1.setMaxCardElevation(15);
                c1.setPadding(8,8,8,8);
                c1.setRadius(9);

                c1.setLayoutParams(cardViewParams);

                cardOuter = new LinearLayout(this);
                LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                l2.setMargins(8,8,8,8);
                cardOuter.setLayoutParams(l2);
                cardOuter.setOrientation(LinearLayout.VERTICAL);





                ;
                c1.setPadding(8,8,8,8);

                //fetched = sharedPreferences.getStringSet(Integer.toString(i+1), null);
                String [] tokens = sharedPreferences.getString(Integer.toString(i+1),null).split(",");
                //list = new ArrayList<String>(fetched);
                for(int j = 0 ; j < tokens.length ; j++){
                    QuickSandText_TextView t1 = new QuickSandText_TextView(this);
                    LinearLayout.LayoutParams l2p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    l2p.setMargins(8,8,8,8);
                    t1.setLayoutParams(l2p);
                    //t1.setText(list.get(j));
                    t1.setText(tokens[j]);
                    addItemDetails(tokens[j],j);
                    t1.setPadding(8,8,8,8);
                    t1.setTextColor(getResources().getColor(R.color.colorText));
                    cardOuter.addView(t1);
                    cardOuter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                c1.addView(cardOuter);
                cardLayout.addView(c1);
            }

        }


    }

    private void post_order(String name, String uid, String purchasing_location, String dropoff_location, String mDeliveryDateTime,  String thumb_image, String image_id, String service_charges) {

        Order order_post = new Order(name, uid, mDeliveryDateTime,
                                     purchasing_location, dropoff_location,"false",
                                    "false", ServerValue.TIMESTAMP,
                                     thumb_image, image_id, service_charges,null);
        Map<String, Object> postValues = order_post.toMap();
        String post_key = mDatabase.child("posts").push().getKey();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_key);
        mDatabase.setValue(postValues);


        Item order_items = new Item(Item_Name_Array, Item_Quantity_Array, Item_Price_Array, Item_Comment_Array);

        Map<String, HashMap<String, String>> itemValues = order_items.toMap();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_key).child("items");
        mDatabase.setValue(itemValues);

        send_notification(post_key, FirebaseAuth.getInstance().getCurrentUser().getUid(), "A new Order");


        //refresh
        Intent refreshIntent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(refreshIntent);

    }


    public void addItemDetails(String detail, int j){
        if (j==0){
            Item_Name_Array.add(detail);
        }
        else if(j==1){
            Item_Quantity_Array.add(detail);
        }
        else if(j==2){
            Item_Price_Array.add(detail);
        }
        else {
            Item_Comment_Array.add(detail);
        }


    }

    private void send_notification(String post_id, String From, String message) {
        HashMap<String, String> data = new HashMap<>();
        data.put("from", From);
        data.put("message", message);
        FirebaseDatabase.getInstance().getReference().child("Notification").child(post_id).setValue(data);
    }

    private void displayEmptyCart(){
        CardView c1 = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        cardViewParams.setMargins(16,16,16,8);
        c1.setCardElevation(16);
        c1.setMaxCardElevation(15);
        c1.setMinimumHeight(200);
        c1.setMinimumWidth(200);
        c1.setPadding(8,8,8,8);
        c1.setRadius(9);
        c1.setForegroundGravity(Gravity.CENTER);
        c1.setLayoutParams(cardViewParams);

        QuickSandText_TextView t1 = new QuickSandText_TextView(this);
        LinearLayout.LayoutParams l2p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        l2p.setMargins(8,8,8,8);
        t1.setLayoutParams(l2p);
        //t1.setText(list.get(j));
        t1.setText("There are no items in your cart!");
        t1.setPadding(8,8,8,8);
        t1.setTextSize(22);
        t1.setGravity(Gravity.CENTER);
        t1.setTextColor(getResources().getColor(R.color.colorText));
        c1.addView(t1);
        c1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        cardLayout.addView(c1);

    }
}

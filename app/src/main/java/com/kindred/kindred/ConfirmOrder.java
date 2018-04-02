package com.kindred.kindred;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Fahad Saleem on 3/22/2018.
 */

public class ConfirmOrder extends AppCompatActivity {

    private int maxItems=0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set<String> fetched;
    List<String> list;
    private LinearLayout cardLayout;
    private LinearLayout cardInner;
    private Button placeOrder;
    private QuickSandText_TextView dropOff;
    private QuickSandText_TextView pickUp;
    private QuickSandText_TextView deliveryDate;
    private QuickSandText_TextView serviceTip;


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
            placeOrder.setVisibility(View.GONE);

        }
        else {
            dropOff.setText(sharedPreferences.getString("DropOff",null));
            pickUp.setText(sharedPreferences.getString("PickUp", null));
            deliveryDate.setText(sharedPreferences.getString("DeliveryDate", null));
            serviceTip.setText(sharedPreferences.getString("ServiceTip", null));
            Toast.makeText(this, "This was called", Toast.LENGTH_SHORT).show();
        }
        cardLayout = findViewById(R.id.confirm_order_layout);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       maxItems = sharedPreferences.getInt("Count",0);
       Toast.makeText(this,Integer.toString(maxItems),Toast.LENGTH_SHORT).show();

       placeOrder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               editor = sharedPreferences.edit();
               editor.clear().apply();
               editor.putInt("Count",0);

           }
       });


        if (maxItems==0){
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
            t1.setTextColor(getResources().getColor(R.color.colorPrimary));
            c1.addView(t1);
            cardLayout.addView(c1);



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

                cardInner = new LinearLayout(this);
                LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                l1.setMargins(8,8,8,8);
                cardInner.setLayoutParams(l1);
                cardInner.setOrientation(LinearLayout.VERTICAL);
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
                    t1.setPadding(8,8,8,8);
                    t1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    cardInner.addView(t1);
                }
                c1.addView(cardInner);
                cardLayout.addView(c1);


            }

        }

    }




}

package com.kindred.kindred;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        final String[] item_name = new String[1];
        final String[] item_quantity = new String[1];
        final String[] item_price = new String[1];
        final String[] item_note = new String[1];

        final String post_id = getIntent().getStringExtra("post_id");

        mDropOffLocation = (TextView) findViewById(R.id.ItemsDetails_DropOffLocation_textView);
        mPurchasingLocation = (TextView) findViewById(R.id.itemsDetails_PurchasingLocation_textView);
        mDeliverUntil = (TextView) findViewById(R.id.ItemsDetails_DeliverUntil_textView);
        mServiceCharges = (TextView) findViewById(R.id.ItemsDetails_ServiceCharges_textView);

        container = (LinearLayout) findViewById(R.id.Items_details_table_rows_container);
        final ViewGroup finalContainer = container;




        final HashMap<String, String> itemMap = new HashMap<String, String>();


        mItemsDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_id);

        mItemsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order post = dataSnapshot.getValue(Order.class);

                mDropOffLocation.setText(post.getDropoff_location());
                mPurchasingLocation.setText(post.getPurchasing_location());
                mDeliverUntil.setText(post.getTime()+  " " + post.getDate());
                mServiceCharges.setText(post.getServices_charges());

                Iterable<DataSnapshot> itemsSnapshot = dataSnapshot.child("items").getChildren();
                for(DataSnapshot item: itemsSnapshot)
                {

                    LayoutInflater layoutInflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.items_detail_table_row, null);

                    TextView Item_Name_TextView1 = (TextView) addView.findViewById(R.id.items_details_itemName) ;
                    TextView Item_Quantity_TextView1 = (TextView) addView.findViewById(R.id.items_details_itemQuantity) ;
                    TextView Item_Price_TextView = (TextView) addView.findViewById(R.id.Items_details_itemPrice) ;
                    TextView Item_Note_TextView = (TextView) addView.findViewById(R.id.items_details_itemNote) ;
                    
                    item_name[0] = (String)item.child("Item-Name").getValue();
                    Item_Name_TextView1.setText(item_name[0]);
//                    mItemsName.append(item_name[0] +"\n");
                    item_quantity[0] = (String)item.child("Item-Quantity").getValue();
                    Item_Quantity_TextView1.setText(item_quantity[0]);
//                    mItemsQuantity.append(item_quantity[0] +"\n");
                    item_price[0] = (String)item.child("Item-Price").getValue();
                    Item_Price_TextView.setText(item_price[0]);
//                    mItemsPrice.append(item_price[0] +"\n");
                    item_note[0] = (String)item.child("Item-Note").getValue();
                    Item_Note_TextView.setText(item_note[0]);
//                    mItemsNote.append(item_note[0] +"\n");

                    finalContainer.addView(addView);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

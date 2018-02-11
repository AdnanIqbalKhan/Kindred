package com.kindred.kindred;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemsDetailsActitvity extends AppCompatActivity {

    private TextView mItemsName;
    private TextView mItemsQuantity;
    private TextView mItemsPrice;
    private TextView mItemsNote;

    String Names;
    String Quantity;
    String Price;
    String Note;


    //Firebase Variables
    private DatabaseReference mItemsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        final String post_id = getIntent().getStringExtra("post_id");

        mItemsName = (TextView) findViewById(R.id.itemDetails_itemNames_textView);
        mItemsQuantity = (TextView) findViewById(R.id.itemDetails_itemsQuantity_textView);
        mItemsPrice = (TextView) findViewById(R.id.itemDetails_itemsPrice_textView);
        mItemsNote = (TextView) findViewById(R.id.itemDetails_itemsNote_textView);


        mItemsDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_id);

        mItemsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item post_items = dataSnapshot.child("items").getValue(Item.class);
                Names = post_items.namestoString();
                Quantity = post_items.quantityToString();
                Price = post_items.priceToString();
                Note = post_items.noteToString();

                mItemsName.setText(Names);
                mItemsQuantity.setText(Quantity);
                mItemsPrice.setText(Price);
                mItemsNote.setText(Note);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

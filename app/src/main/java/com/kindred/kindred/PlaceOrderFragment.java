package com.kindred.kindred;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOrderFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    //Input Variables
    private EditText mItemName1;
    private EditText mItemQuantity1;
    private EditText mItemPrice1;
    private EditText mItemNote1;
    private EditText mPurchasingLocation;
    private EditText mDropOffLocation;
    private String mDeliveryTime;
    private String mDeliveryDate;
    private Button mPlaceOrderBtn;
    private EditText mServiceCharges;

    // All of the items related Lists



    // Date and Time Picker Variables
    private Button mDateTimePickerBtn;
    private TextView mShowDateTime;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    //Firebase Variables
    private DatabaseReference mDatabase;

    //Current User Name
    public static String name;
    public static String thumb_image;
    public static String uid;


    public PlaceOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place_order, container, false);

        mItemName1 = (EditText) v.findViewById(R.id.orderPlacing_item_plainText_1);
        mItemQuantity1 = (EditText) v.findViewById(R.id.orderPlacing_quantity_number_1);
        mItemPrice1 = (EditText) v.findViewById(R.id.orderPlacing_price_number_1);
        mItemNote1 = (EditText) v.findViewById(R.id.orderPlacing_note_plainText_1);
        mPurchasingLocation = (EditText) v.findViewById(R.id.placeOrder_purchasingLocation_editText);
        mDropOffLocation = (EditText) v.findViewById(R.id.placeOrder_dropOffLocation_editText);
        mPlaceOrderBtn = (Button) v.findViewById(R.id.PlaceOrder_btn);
        mServiceCharges = (EditText) v.findViewById(R.id.placeOrder_serviceCharges_number);

        //Date and Time Picker Code
        mDateTimePickerBtn = (Button) v.findViewById(R.id.placeOrder_dateTimePicker_btn);
        mShowDateTime = (TextView) v.findViewById(R.id.placeOrder_showDateTime_textView);
        mDateTimePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        yearFinal = i;
                        monthFinal = i1 + 1;
                        dayFinal = i2;

                        Calendar c = Calendar.getInstance();
                        hour = c.get(Calendar.HOUR_OF_DAY);
                        minute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                hourFinal = i;
                                minuteFinal = i1 + 1;

                                mDeliveryTime = hourFinal + ":" + minuteFinal;
                                mDeliveryDate = monthFinal + "/" + dayFinal + "/" + yearFinal;
                                mShowDateTime.setText("Time:" + hourFinal + ":" + minuteFinal + "\n" + "Dated:" + monthFinal + "/" + dayFinal + "/" + yearFinal
                                );
                            }
                        }, hour, minute,
                                android.text.format.DateFormat.is24HourFormat(getActivity()));
                        timePickerDialog.show();
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

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

        mPlaceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String Purchasing_Location = mPurchasingLocation.getText().toString();
                mPurchasingLocation.setText("");
                String Dropoff_Location = mDropOffLocation.getText().toString();
                mDropOffLocation.setText("");
                String Service_Charges = mServiceCharges.getText().toString();
                mServiceCharges.setText("");

                //Clear Date and Time Field
                mShowDateTime.setText("");

                post_order(name, uid, Purchasing_Location, Dropoff_Location, mDeliveryDate, mDeliveryTime, thumb_image, Service_Charges);

                Toast.makeText(getActivity(), "Order Posted Successfully", Toast.LENGTH_LONG).show();


            }

            private void post_order(String name, String uid, String purchasing_location, String dropoff_location, String mDeliveryDate, String mDeliveryTime, String thumb_image, String service_charges) {
                Order order_post = new Order(name, uid, mDeliveryDate, mDeliveryTime,
                        purchasing_location, dropoff_location, ServerValue.TIMESTAMP, thumb_image, service_charges);
                Map<String, Object> postValues = order_post.toMap();
                String post_key = mDatabase.child("posts").push().getKey();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_key);
                mDatabase.setValue(postValues);

                ArrayList<String> Item_Name = new ArrayList<String>();

                Item_Name.add("Lays");

                ArrayList<String> Item_Quantity = new ArrayList<String>();
                Item_Quantity.add("1");

                ArrayList<String> Item_Price = new ArrayList<String>();
                Item_Price.add("50");

                ArrayList<String> Item_Note = new ArrayList<String>();
                Item_Note.add("Spicy");

                Item order_items = new Item(Item_Name,Item_Quantity,Item_Price,Item_Note, 1);

                Map<String, HashMap<String, String>> itemValues = order_items.toMap();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_key).child("items");
                mDatabase.setValue(itemValues);


            }
        });


        return v;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }
}

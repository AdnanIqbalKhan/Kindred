package com.kindred.kindred;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


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


    // Date and Time Picker Variables
    private Button mDateTimePickerBtn;
    private TextView mShowDateTime;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    //Firebase Variables
    private DatabaseReference mDatabase;
    ArrayList<String> Item_Name_Array = new ArrayList<String>();
    ArrayList<String> Item_Quantity_Array = new ArrayList<String>();
    ArrayList<String> Item_Price_Array = new ArrayList<String>();
    ArrayList<String> Item_Note_Array = new ArrayList<String>();


    //Current User Name
    public static String name;
    public static String thumb_image;
    public static String uid;

    //Add Another Item
    AutoCompleteTextView inputItemName, inputItemQuantity, inputItemPrice, inputItemNote;
    Button buttonAdd;
    LinearLayout container;

    private static final String[] NUMBER = new String[] {
            "One", "Two", "Three", "Four", "Five",
            "Six", "Seven", "Eight", "Nine", "Ten"
    };
    ArrayAdapter<String> adapter;




    public PlaceOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place_order, container, false);


        mPurchasingLocation = (EditText) v.findViewById(R.id.placeOrder_purchasingLocation_editText);
        mDropOffLocation = (EditText) v.findViewById(R.id.placeOrder_dropOffLocation_editText);
        mPlaceOrderBtn = (Button) v.findViewById(R.id.PlaceOrder_btn);
        mServiceCharges = (EditText) v.findViewById(R.id.placeOrder_serviceCharges_number);


        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, NUMBER);

        inputItemName = (AutoCompleteTextView)v.findViewById(R.id.Input_Item_Name);
        inputItemQuantity = (AutoCompleteTextView)v.findViewById(R.id.Input_Item_Quantity);
        inputItemPrice = (AutoCompleteTextView)v.findViewById(R.id.Input_Item_Price);
        inputItemNote = (AutoCompleteTextView)v.findViewById(R.id.Input_Item_Note);


        buttonAdd = (Button)v.findViewById(R.id.add);
        container = (LinearLayout) v.findViewById(R.id.container);

        final ViewGroup finalContainer = container;
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);

                TextView Item_Name_TextView = (TextView) addView.findViewById(R.id.itemName) ;
                Item_Name_TextView.setText(inputItemName.getText().toString());
                if(inputItemName.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Item Name Cannot be Empty", Toast.LENGTH_LONG).show();
                    return;
                }
                Item_Name_Array.add(inputItemName.getText().toString());
                inputItemName.setText("");


                TextView Item_Quantity_TextView = (TextView) addView.findViewById(R.id.itemQuantity) ;
                Item_Quantity_TextView.setText(inputItemQuantity.getText().toString());
                Item_Quantity_Array.add(inputItemQuantity.getText().toString());
                inputItemQuantity.setText("");

                TextView Item_Price_TextView = (TextView) addView.findViewById(R.id.itemPrice) ;
                Item_Price_TextView.setText(inputItemPrice.getText().toString());
                Item_Price_Array.add(inputItemPrice.getText().toString());
                inputItemPrice.setText("");

                TextView Item_Note_TextView = (TextView) addView.findViewById(R.id.itemNote) ;
                Item_Note_TextView.setText(inputItemNote.getText().toString());
                Item_Note_Array.add(inputItemNote.getText().toString());
                inputItemNote.setText("");

                Button buttonRemove = (Button)addView.findViewById(R.id.remove);

                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Item_Name_Array.remove(inputItemName.getText().toString());
                        Item_Quantity_Array.remove(inputItemQuantity.getText().toString());
                        Item_Price_Array.remove(inputItemPrice.getText().toString());
                        Item_Note_Array.remove(inputItemNote.getText().toString());

                        ((LinearLayout)addView.getParent()).removeView(addView);

                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                finalContainer.addView(addView);


            }
        });

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

                if(Item_Name_Array.size()==0)
                {
                    Toast.makeText(getActivity(),"Item Name Cannot be Empty", Toast.LENGTH_LONG).show();
                    return;
                }

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


                Item order_items = new Item(Item_Name_Array, Item_Quantity_Array, Item_Price_Array, Item_Note_Array, 1);

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

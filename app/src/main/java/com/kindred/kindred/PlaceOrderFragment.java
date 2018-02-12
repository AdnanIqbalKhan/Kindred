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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
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


    // Add another Item Variables
    private Button mAddAnothterBtn;
    private ConstraintLayout mConstraintLayout;
    private int topMargin;
    int i=0;
    int count=0;
    int nameEditTextCount = 1;


    // All of the items related Lists
    ArrayList<EditText> ItemNames = new ArrayList<EditText>();
    ArrayList<EditText> ItemQuantity = new ArrayList<EditText>();
    ArrayList<EditText> ItemPrice = new ArrayList<EditText>();
    ArrayList<EditText> ItemNote = new ArrayList<EditText>();


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
        ItemNames.add(mItemName1);
        mItemQuantity1 = (EditText) v.findViewById(R.id.orderPlacing_quantity_number_1);
        ItemQuantity.add(mItemQuantity1);
        mItemPrice1 = (EditText) v.findViewById(R.id.orderPlacing_price_number_1);
        ItemPrice.add(mItemPrice1);
        mItemNote1 = (EditText) v.findViewById(R.id.orderPlacing_note_plainText_1);
        ItemNote.add(mItemNote1);
        mPurchasingLocation = (EditText) v.findViewById(R.id.placeOrder_purchasingLocation_editText);
        mDropOffLocation = (EditText) v.findViewById(R.id.placeOrder_dropOffLocation_editText);
        mPlaceOrderBtn = (Button) v.findViewById(R.id.PlaceOrder_btn);
        mServiceCharges = (EditText) v.findViewById(R.id.placeOrder_serviceCharges_number);



        //Add Another Item Code
        ConstraintSet constraintSet  = new ConstraintSet();


        EditText mItemName2 = new EditText(getActivity());
        mItemName2.setId(R.id.mItemName2_Id);
        ItemNames.add(mItemName2);
        EditText mItemName3 = new EditText(getActivity());
        mItemName3.setId(R.id.mItemName3_Id);
        ItemNames.add(mItemName3);
        EditText mItemName4 = new EditText(getActivity());
        mItemName4.setId(R.id.mItemName4_Id);
        ItemNames.add(mItemName4);
        EditText mItemName5 = new EditText(getActivity());
        mItemName5.setId(R.id.mItemName5_Id);
        ItemNames.add(mItemName5);

        EditText mItemQuantity2 = new EditText(getActivity());
        mItemQuantity2.setId(R.id.mItemQuantity2_Id);
        ItemQuantity.add(mItemQuantity2);
        EditText mItemQuantity3 = new EditText(getActivity());
        mItemQuantity3.setId(R.id.mItemQuantity3_Id);
        ItemQuantity.add(mItemQuantity3);
        EditText mItemQuantity4 = new EditText(getActivity());
        mItemQuantity4.setId(R.id.mItemQuantity4_Id);
        ItemQuantity.add(mItemQuantity4);
        EditText mItemQuantity5 = new EditText(getActivity());
        mItemQuantity5.setId(R.id.mItemQuantity5_Id);
        ItemQuantity.add(mItemQuantity5);

        EditText mItemPrice2 = new EditText(getActivity());
        mItemPrice2.setId(R.id.mItemPrice2_Id);
        ItemPrice.add(mItemPrice2);
        EditText mItemPrice3 = new EditText(getActivity());
        mItemPrice3.setId(R.id.mItemPrice3_Id);
        ItemPrice.add(mItemPrice3);
        EditText mItemPrice4 = new EditText(getActivity());
        mItemPrice4.setId(R.id.mItemPrice4_Id);
        ItemPrice.add(mItemPrice4);
        EditText mItemPrice5 = new EditText(getActivity());
        mItemPrice5.setId(R.id.mItemPrice5_Id);
        ItemPrice.add(mItemPrice5);

        EditText mItemNote2 = new EditText(getActivity());
        mItemNote2.setId(R.id.mItemNote2_Id);
        ItemNote.add(mItemNote2);
        EditText mItemNote3 = new EditText(getActivity());
        mItemNote3.setId(R.id.mItemNote3_Id);
        ItemNote.add(mItemNote3);
        EditText mItemNote4 = new EditText(getActivity());
        mItemNote4.setId(R.id.mItemNote4_Id);
        ItemNote.add(mItemNote4);
        EditText mItemNote5 = new EditText(getActivity());
        mItemNote5.setId(R.id.mItemNote5_Id);
        ItemNote.add(mItemNote5);


        mConstraintLayout = (ConstraintLayout) v.findViewById(R.id.placeOrderConstraintLayout);
        mAddAnothterBtn = (Button) v.findViewById(R.id.orderPlacing_addAnother_btn);
        mAddAnothterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count >= 4)
                {
                    Toast.makeText(getActivity(),"You Can't add More Elements", Toast.LENGTH_LONG).show();
                }
                else
                {
                    nameEditTextCount++;
                    ConstraintSet constraintSet = new ConstraintSet();
                    //add another Button top margin
                    topMargin = 90 * ++i;

                    //Create New Item Name Edit Text
                    ItemNames.get(i).setWidth(195);
                    ItemNames.get(i).setHint("Item");
                    //Create New Item Quantity Edit Text
                    ItemQuantity.get(i).setWidth(103);
                    ItemQuantity.get(i).setHint("Quan");

                    // Create New Item Price Edit Text
                    ItemPrice.get(i).setWidth(100);
                    ItemPrice.get(i).setHint("Price");
                    //Create New Item Note Edit Text
                    ItemNote.get(i).setWidth(195);
                    ItemNote.get(i).setHint("Note");


                    mConstraintLayout.addView(ItemNames.get(i));
                    mConstraintLayout.addView(ItemQuantity.get(i));
                    mConstraintLayout.addView(ItemPrice.get(i));
                    mConstraintLayout.addView(ItemNote.get(i));

                    constraintSet.clone(mConstraintLayout);

                    //positioning mItemName2 EditeText View
                    constraintSet.connect(ItemNames.get(i).getId(), ConstraintSet.TOP, ItemNames.get(count).getId(), ConstraintSet.BOTTOM, 8);
                    constraintSet.connect(ItemNames.get(i).getId(), ConstraintSet.START, mConstraintLayout.getId(), ConstraintSet.END, 35);
                    //positioning mItemQuantity2 EditText View
                    constraintSet.connect(ItemQuantity.get(i).getId(), ConstraintSet.TOP, ItemQuantity.get(count).getId(), ConstraintSet.BOTTOM, 8);
                    constraintSet.connect(ItemQuantity.get(i).getId(), ConstraintSet.START, ItemNames.get(i).getId(), ConstraintSet.END, 25);
                    //positioning mItemPrice2 EditText View
                    constraintSet.connect(ItemPrice.get(i).getId(), ConstraintSet.TOP, ItemPrice.get(count).getId(), ConstraintSet.BOTTOM, 8);
                    constraintSet.connect(ItemPrice.get(i).getId(), ConstraintSet.START, ItemQuantity.get(i).getId(), ConstraintSet.END, 16);

                    //positioning mItemNote2 EditText View
                    constraintSet.connect(ItemNote.get(i).getId(), ConstraintSet.TOP, ItemNote.get(count).getId(), ConstraintSet.BOTTOM, 8);
                    constraintSet.connect(ItemNote.get(i).getId(), ConstraintSet.START,  ItemPrice.get(i).getId(), ConstraintSet.END, 8);

                    //Positioning Add another button
                    constraintSet.connect(mAddAnothterBtn.getId(), ConstraintSet.TOP, mItemName1.getId(), ConstraintSet.BOTTOM, topMargin);
                    constraintSet.applyTo(mConstraintLayout);


                    ++count;
                }

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
                                mShowDateTime.setText("Time:"+hourFinal + ":" + minuteFinal + "\n"+"Dated:"+ monthFinal +"/"+ dayFinal + "/" + yearFinal
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

                for(int i=0; i < nameEditTextCount; i++){
                    if(ItemNames.get(i).getText().toString().matches(""))
                    {
                        Toast.makeText(getActivity(), "Item Name Cannot be Empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //Empty the Price of Items Fields After the placing Order
                }

                String Purchasing_Location = mPurchasingLocation.getText().toString();
                mPurchasingLocation.setText("");
                String Dropoff_Location = mDropOffLocation.getText().toString();
                mDropOffLocation.setText("");
                String Service_Charges = mServiceCharges.getText().toString();
                mServiceCharges.setText("");

                //Clear Date and Time Field
                mShowDateTime.setText("");

                post_order(name,uid,Purchasing_Location, Dropoff_Location, mDeliveryDate, mDeliveryTime,thumb_image, Service_Charges);

                Toast.makeText(getActivity(), "Order Posted Successfully", Toast.LENGTH_LONG).show();


            }

            private void post_order(String name,String uid, String purchasing_location, String dropoff_location, String mDeliveryDate, String mDeliveryTime, String thumb_image, String service_charges) {
                Order order_post = new Order(name, uid, mDeliveryDate, mDeliveryTime, purchasing_location, dropoff_location, thumb_image, service_charges);
                Map<String, Object> postValues = order_post.toMap();
                String post_key = mDatabase.child("posts").push().getKey();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_key);
                mDatabase.setValue(postValues);

                ArrayList<String> Item_Name = new ArrayList<String>();
                for(int i=0; i<ItemNames.size(); i++)
                {
                    if(ItemNames.get(i).getText().toString().matches(""))
                    {

                    }
                    else
                    {
                        Item_Name.add(ItemNames.get(i).getText().toString());
                    }
                    ItemNames.get(i).setText("");

                }

                ArrayList<String> Item_Quantity = new ArrayList<String>();
                for(EditText quantityOfItem: ItemQuantity){
                    Item_Quantity.add(quantityOfItem.getText().toString());
                    //Empty the Quantity of Items Fields After the placing Order
                    quantityOfItem.setText("");
                }
                ArrayList<String> Item_Price = new ArrayList<String>();
                for(EditText priceOfItem: ItemPrice){
                    Item_Price.add(priceOfItem.getText().toString());
                    //Empty the Price of Items Fields After the placing Order
                    priceOfItem.setText("");
                }
                ArrayList<String> Item_Note = new ArrayList<String>();
                for(EditText noteOfItem: ItemNote){
                    Item_Note.add(noteOfItem.getText().toString());
                    //Empty the Note of Items After the Placing Order
                    noteOfItem.setText("");
                }
                Item order_items = new Item(Item_Name,Item_Quantity,Item_Price,Item_Note, nameEditTextCount);
                Map<String, ArrayList<String>> itemValues = order_items.toMap();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(post_key).child("items");
                mDatabase.setValue(itemValues);

                // Reinitialize the number of items

                // empty the Data in ArrayList
                Item_Name.clear();
                Item_Quantity.clear();
                Item_Price.clear();
                Item_Note.clear();

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

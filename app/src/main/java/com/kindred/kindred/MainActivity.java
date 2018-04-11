package com.kindred.kindred;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.github.pwittchen.swipe.library.rx2.SwipeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    //Spinners
    private Spinner deliveryDeadline;
    private Spinner units;

    //QuickSandText
    private QuickSandText itemName;
    private QuickSandText itemPrice;
    private QuickSandText itemComment;
    private QuickSandText purchaseLocation;
    private QuickSand_AutoText dropOffLocation;
    private QuickSandText serviceTip;


    //SharedPreferences

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //Warning Layout
    private LinearLayout warningLayout;

    //Seekbar
    private SeekBar progBar;

    //Buttons
    private Button continueButton;
    private Button addItem;
    private Button viewCart;

    //Adapters
    private ArrayAdapter<String> dropOffAdapter;
    private ArrayAdapter<String> unitAdapter;
    private ArrayAdapter<String> deliveryAdapter;
    private ArrayAdapter<String> adapter;


    // Date and Time Picker Variables
    private String mDeliveryTime;
    private String mDeliveryDate;
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
    public static String image_id;

    FirebaseUser mCurrentUser;


    private static int count;


    private static final String[] PLACES = new String[] {
            "Ghazali Hostel", "Razi Hostel", "Attar Hostel", "Rumi Hostel",
            "Fatima Hostel", "Khadeeja Hostel", "Ayesha Hostel",
            "Zainab Hostel", "C1", "C2", "C3", "C4", "Jango", "The Wall", "309", "NBS", "SEECS", "IAEC",
            "RIMMS", "NICE/ NIT", "IESE", "SMME/SNS", "SCME", "IGIS", "SADA", "ASAB", "S3H", "C3A"
    };

    private  String[] DELIVERY = new String[]{
            "Not Specified", "Choose from Calender"
    };

    private String[] UNITS = new String[]{
            "Units", "Dozens", "KG", "Pounds", "Packs"
    };

    

    //Main2 Code
    private Button mOrderListBtn;
    private RelativeLayout mBottomToolbar;
    private Button mHomeBtn;
    private Button mYourOrdersBtn;

    //Swipe to change activity
    private Swipe swipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser==null){
            Log.d("userActivity", "this was called in OnCreate");
            sendToStart();
        }


        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Kindred");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher_logo);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);



        //Swipe to change activity

        swipe = new Swipe(20,250);

        swipe.setListener(new SwipeListener() {
            @Override public void onSwipingLeft(final MotionEvent event) {

            }
            @Override public boolean onSwipedLeft(final MotionEvent event) {
                Intent startIntent = new Intent(MainActivity.this, OrderList.class);
                startActivity(startIntent);
                finish();
                return false;
            }
            @Override public void onSwipingRight(final MotionEvent event) {

            }
            @Override public boolean onSwipedRight(final MotionEvent event) {
                Intent startIntent = new Intent(MainActivity.this, YourOrders.class);
                startActivity(startIntent);
                finish();
                return false;
            }
            @Override public void onSwipingUp(final MotionEvent event) {
            }
            @Override public boolean onSwipedUp(final MotionEvent event) {
                return false;
            }
            @Override public void onSwipingDown(final MotionEvent event) {
            }
            @Override public boolean onSwipedDown(final MotionEvent event) {
                return false;
            }
        });



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        deliveryDeadline = findViewById(R.id.spinner_delivery);
        units = findViewById(R.id.spinner_units);

        itemName = findViewById(R.id.item_name);
        itemPrice = findViewById(R.id.item_price);
        itemComment = findViewById(R.id.item_comment);
        purchaseLocation = findViewById(R.id.purchase_location);
        serviceTip = findViewById(R.id.service_tip);

        warningLayout = findViewById(R.id.warning_layout);

        progBar = findViewById(R.id.seekbar_quantity);


        continueButton = findViewById(R.id.continue_btn);
        addItem = findViewById(R.id.add_item_btn);
        viewCart = findViewById(R.id.view_cart_btn);

        dropOffLocation = findViewById(R.id.dropoff_location);
        dropOffAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, PLACES);
        dropOffLocation.setThreshold(1);
        dropOffLocation.setAdapter(dropOffAdapter);
        drawTextOnSeekbar(0);



        deliveryAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, DELIVERY);
        deliveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryDeadline.setAdapter(deliveryAdapter);
        deliveryDeadline.setOnItemSelectedListener(this);


        unitAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, UNITS);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        units.setAdapter(unitAdapter);




        progBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                drawTextOnSeekbar(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        viewCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent startIntent = new Intent(MainActivity.this, ConfirmOrder.class).putExtra("ParentActivity",1);

                startActivity(startIntent);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateDeliveryDetails() ){
                    if (sharedPreferences.getInt("Count",0)>0) {
                        editor = sharedPreferences.edit();
                        editor.putString("DropOff", dropOffLocation.getText().toString());
                        editor.putString("PickUp", purchaseLocation.getText().toString());
                        editor.putString("DeliveryDate", deliveryDeadline.getSelectedItem().toString());
                        if (!serviceTip.getText().toString().equals(null) && !serviceTip.getText().toString().isEmpty()) {
                            editor.putString("ServiceTip", serviceTip.getText().toString());
                        } else {
                            editor.putString("ServiceTip", "Not Specified");
                        }
                        editor.apply();

                        Intent startIntent = new Intent(MainActivity.this, ConfirmOrder.class).putExtra("ParentActivity", 2);
                        startActivity(startIntent);
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Please Add Any Item Before Continuing Order", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"Purchase/Drop Off Location should be valid!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dropOffLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){

                }
                else {
                    if (!Arrays.asList(PLACES).contains(dropOffLocation.getText().toString())){
                        warningLayout.setVisibility(View.VISIBLE);

                    }
                    else {
                        warningLayout.setVisibility(View.GONE);
                    }
                }
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=sharedPreferences.getInt("Count",0);
                if (validateItemsAdded()) {
                    String item;
                    item = "Name: " + itemName.getText().toString() + ",";

                    item+= "Quantity: " + Integer.toString(progBar.getProgress()+1) + " " + units.getSelectedItem().toString() + ",";
                    if (!itemPrice.getText().toString().equals(null) && !itemPrice.getText().toString().isEmpty()) {
                        item += "Price: " + itemPrice.getText().toString()+ ",";
                    }
                    else {
                        item += "Price: Not Specified" + ",";
                    }

                    if (!itemComment.getText().toString().equals(null) && !itemComment.getText().toString().isEmpty()){
                        item += "Comment: " + itemComment.getText().toString() + ",";
                    }
                    else {
                        item += "Comment: Not Specified";
                    }

                    editor = sharedPreferences.edit();
                    //editor.putStringSet(Integer.toString(++count), itemsSet);
                    editor.putString(Integer.toString(++count),item);
                    Toast.makeText(MainActivity.this,"Count value = " + Integer.toString(count), Toast.LENGTH_SHORT).show();
                    editor.putInt("Count", count);
                    editor.apply();
                    item="";
                    Toast.makeText(MainActivity.this,"Item Added!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Item name can't be empty!", Toast.LENGTH_SHORT).show();
                }

            }
        });




        //Date and Time Picker Code
        //mDateTimePickerBtn = (Button) v.findViewById(R.id.placeOrder_dateTimePicker_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mCurrentUser!=null) {
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


            mDatabase.child("users").child(uid).child("image_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    image_id = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        mBottomToolbar = findViewById(R.id.main_bottom_navBar);


        mOrderListBtn = (Button) findViewById(R.id.main_nav_orderList);
        mOrderListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, OrderList.class);
                startActivity(startIntent);
                finish();
            }
        });

        mHomeBtn = (Button) findViewById(R.id.main_nav_placeOrder);
        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(startIntent);
                finish();
            }
        });

        mYourOrdersBtn = (Button) findViewById(R.id.main_nav_yourOrders);
        mYourOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, YourOrders.class);
                startActivity(startIntent);
                finish();
            }
        });
    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Log.d("userActivity", "this was called in OnStart");
            sendToStart();
        }
    }


    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        Log.d("userActivity", "in send to Start, now starting activity");
        startActivity(startIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }

    private void send_notification(String post_id, String From, String message) {
        HashMap<String, String> data = new HashMap<>();
        data.put("from", From);
        data.put("message", message);
        FirebaseDatabase.getInstance().getReference().child("Notification").child(post_id).setValue(data);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                //Toast.makeText(getActivity(), "You chose first option", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        yearFinal = i;
                        monthFinal = i1 + 1;
                        dayFinal = i2;

                        Calendar c = Calendar.getInstance();
                        hour = c.get(Calendar.HOUR_OF_DAY);
                        minute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                hourFinal = i;
                                minuteFinal = i1 + 1;

                                mDeliveryTime = hourFinal + ":" + minuteFinal;
                                mDeliveryDate = monthFinal + "/" + dayFinal + "/" + yearFinal;
                                String date = "Time: " + hourFinal + ":" + minuteFinal + "\n" + "Dated: " + monthFinal + "/" + dayFinal + "/" + yearFinal;
                                DELIVERY[1]= date;
                                deliveryAdapter.notifyDataSetChanged();
                                //mShowDateTime.setText("Time:" + hourFinal + ":" + minuteFinal + "\n" + "Dated:" + monthFinal + "/" + dayFinal + "/" + yearFinal);
                                deliveryDeadline.setSelection(1);

                            }
                        }, hour, minute,
                                android.text.format.DateFormat.is24HourFormat(MainActivity.this));
                        timePickerDialog.show();
                    }
                }, year, month, day);
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

/*    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                deliveryDeadline.setSelection(0);
                // TODO stop audio playback
            }
        }
    }*/

    public boolean validateItemsAdded(){
        return (!itemName.getText().toString().equals(null) && !itemName.getText().toString().isEmpty());
    }
    public boolean validateDeliveryDetails(){
        boolean warning=false;
        if (!Arrays.asList(PLACES).contains(dropOffLocation.getText().toString())){
            warningLayout.setVisibility(View.VISIBLE);

        }
        else {
            warningLayout.setVisibility(View.GONE);
            warning = true;
        }

        return (!purchaseLocation.getText().toString().equals(null) && !purchaseLocation.getText().toString().isEmpty() &&
                !dropOffLocation.getText().toString().equals(null)  && !dropOffLocation.getText().toString().isEmpty() && warning);
    }

    public void drawTextOnSeekbar(int progress){
        Drawable d = ContextCompat.getDrawable(MainActivity.this, R.drawable.thum);
        Canvas c = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        c.setBitmap(bitmap);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        d.draw(c);
        //  d.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.DST);
        //Bitmap bmp = bitmap.Copy(Bitmap.Config.Argb8888, true);
        String text = Integer.toString(progress +1);
        Paint p = new Paint();
        p.setTextSize(20);
        p.setColor(Color.WHITE);
        int width = (int)p.measureText(text);
        int yPos = (int)((c.getHeight() / 2) - ((p.descent() + p.ascent()) / 2));
        c.drawText(text, (bitmap.getWidth() - width) / 2, yPos, p);
        progBar.setThumb(new BitmapDrawable(getResources(), bitmap));
    }

}

package com.kindred.kindred;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class SetAvatarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Firebase Variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    Button changeAvatarBrn;
    private int finalImageId = R.drawable.boy_1;

    String email;
    String name;
    String cameFrom;

    private Button mBackButton;

    GridView avatarGrid;
    ImageView selectedAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_avatar);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                email = null;
            } else {
                email = extras.getString("USER_EMAIL");
                name = extras.getString("NAME");
                cameFrom = extras.getString("cameFrom");
            }
        } else {
            email = (String) savedInstanceState.getSerializable("USER_EMAIL");
        }
        mBackButton = (Button) findViewById(R.id.setAvatar_back_btn);

        if (cameFrom.equals("SettingsActivity")) {
            mBackButton.setVisibility(View.VISIBLE);
        }

        //Toolbar Set
        Toolbar toolbar = findViewById(R.id.setAvatart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        avatarGrid = (GridView) findViewById(R.id.setAvatar_user_avatars_gridView);
        selectedAvatar = findViewById(R.id.setAvatar_selectedAvatar);
        avatarGrid.setAdapter(new AvatarAdapter(this));
        avatarGrid.setOnItemClickListener(this);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(SetAvatarActivity.this, SettingsActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }
        });

        changeAvatarBrn = findViewById(R.id.change_avatarBtn);

        changeAvatarBrn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = mCurrentUser.getUid();
                final String imageId = Integer.toString(finalImageId);
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                //if user came from StartActivity
                if (cameFrom.equals("StartActivity")) {
                    mDatabase.child("notifyOrder").getRef().setValue(false);
                    mDatabase.child("image_id").getRef().setValue(imageId);
                    mDatabase.child("email").getRef().setValue(email);
                    mDatabase.child("name").getRef().setValue(name);
                    mDatabase.child("deviceToken").getRef().setValue(FirebaseInstanceId.getInstance().getToken());
                    Intent mainIntent = new Intent(SetAvatarActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                //if user came from SettingsActivity
                else {
                    mDatabase.child("image_id").getRef().setValue(imageId);
                    final DatabaseReference pref = FirebaseDatabase.getInstance().getReference().child("posts");
                    pref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String User_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Order Post = child.getValue(Order.class);
                                if (User_id.equals(Post.getUser_id())) {
                                    pref.child(child.getKey()).child("image_id").setValue(imageId);
                                } else if (User_id.equals(Post.getProvider().getUid())) {
                                    pref.child(child.getKey()).child("provider").child("image_id").setValue(imageId);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Intent mainIntent = new Intent(SetAvatarActivity.this, SettingsActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Avatar avatar = (Avatar) holder.mAvatar.getTag();
        int imageId = avatar.avatarId;
        finalImageId = imageId;
        selectedAvatar.setImageResource(imageId);
//        Toast.makeText(SetAvatarActivity.this, "Image Id: " + imageId, Toast.LENGTH_SHORT).show();
    }
}

class Avatar {
    int avatarId;
    String avatarName;

    Avatar(int avatarId, String avatarName) {
        this.avatarId = avatarId;
        this.avatarName = avatarName;
    }
}

class ViewHolder {
    ImageView mAvatar;

    ViewHolder(View v) {
        mAvatar = (ImageView) v.findViewById(R.id.singleAvatar_imageView);
    }
}

class AvatarAdapter extends BaseAdapter {
    ArrayList<Avatar> list;
    Context context;

    AvatarAdapter(Context context) {
        this.context = context;
        list = new ArrayList<Avatar>();
        Resources res = context.getResources();
        String[] tempAvatarsNames = res.getStringArray(R.array.user_avatar_array);
        int[] avatars = {R.drawable.boy_1, R.drawable.boy_2, R.drawable.boy_3, R.drawable.boy_4,
                R.drawable.boy_5, R.drawable.boy_6, R.drawable.boy_7, R.drawable.boy_8,
                R.drawable.girl_1, R.drawable.girl_2, R.drawable.girl_3, R.drawable.girl_4,
                R.drawable.girl_5, R.drawable.girl_6, R.drawable.girl_7, R.drawable.girl_8};
        for (int i = 0; i < 16; i++) {
            Avatar tempAvatar = new Avatar(avatars[i], tempAvatarsNames[i]);
            list.add(tempAvatar);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_avatar, viewGroup, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Avatar temp = list.get(i);
        holder.mAvatar.setImageResource(temp.avatarId);
        holder.mAvatar.setTag(temp);

        return row;
    }
}
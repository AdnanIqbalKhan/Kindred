package com.kindred.kindred;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetAvatarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    //Firebase Variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    Button changeAvatarBrn;
    int finalImageId;

    GridView avatarGrid;
    ImageView selectedAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_avatar);

        //Toolbar Set
        Toolbar toolbar = findViewById(R.id.setAvatart_toolbar);
        setSupportActionBar(toolbar);

        avatarGrid = (GridView)findViewById(R.id.setAvatar_user_avatars_gridView);
        selectedAvatar = findViewById(R.id.setAvatar_selectedAvatar);
        avatarGrid.setAdapter(new AvatarAdapter(this));
        avatarGrid.setOnItemClickListener(this);

        changeAvatarBrn = findViewById(R.id.change_avatarBtn);

        changeAvatarBrn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = mCurrentUser.getUid();
                String imageId = Integer.toString(finalImageId);
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                mDatabase.child("image_id").getRef().setValue(imageId);
                Intent mainIntent = new Intent(SetAvatarActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();

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
        Toast.makeText(SetAvatarActivity.this, "Image Id: " + imageId, Toast.LENGTH_SHORT).show();
    }
}
class Avatar
{
    int avatarId;
    String avatarName;
    Avatar(int avatarId, String avatarName)
    {
        this.avatarId = avatarId;
        this.avatarName = avatarName;
    }
}
class ViewHolder{
    ImageView mAvatar;
    ViewHolder(View v){
        mAvatar = (ImageView) v.findViewById(R.id.singleAvatar_imageView);
    }
}
class AvatarAdapter extends BaseAdapter
{
    ArrayList<Avatar> list;
    Context context;
    AvatarAdapter(Context context){
        this.context = context;
        list = new ArrayList<Avatar>();
        Resources res = context.getResources();
        String[] tempAvatarsNames = res.getStringArray(R.array.user_avatar_array);
        int[] avatars = {R.drawable.boy_1,R.drawable.boy_2,R.drawable.boy_3,R.drawable.boy_4,
                R.drawable.boy_5,R.drawable.boy_6,R.drawable.boy_7,R.drawable.boy_8,
                R.drawable.girl_1,R.drawable.girl_2,R.drawable.girl_3,R.drawable.girl_4,
                R.drawable.girl_5,R.drawable.girl_6,R.drawable.girl_7,R.drawable.girl_8};
        for(int i=0; i<16;i++)
        {
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
        if(row==null)
        {
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_avatar,viewGroup, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }
        Avatar temp = list.get(i);
        holder.mAvatar.setImageResource(temp.avatarId);
        holder.mAvatar.setTag(temp);

        return row;
    }
}
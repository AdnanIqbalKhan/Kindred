package com.kindred.kindred;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class SetAvatarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    GridView avatarGrid;
    ImageView selectedAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_avatar);

        avatarGrid = (GridView)findViewById(R.id.setAvatar_user_avatars_gridView);
        selectedAvatar = findViewById(R.id.setAvatar_selectedAvatar);
        avatarGrid.setAdapter(new AvatarAdapter(this));
        avatarGrid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Avatar avatar = (Avatar) holder.mAvatar.getTag();
        int imageId = avatar.avatarId;
        selectedAvatar.setImageResource(imageId);
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
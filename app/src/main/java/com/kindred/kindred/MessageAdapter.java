package com.kindred.kindred;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Adnan Iqbal Khan on 18-Feb-18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Messages> MsgList;

    public MessageAdapter(List<Messages> msgList) {
        MsgList = msgList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_single_layout, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Messages c = MsgList.get(position);
        String fromId = c.getFrom();

//        if (fromId.equals(currentUserId)) {
//            holder.msgTextView.setBackgroundColor(Color.WHITE);
//            holder.msgTextView.setTextColor(Color.BLACK);
//        } else {
//            holder.msgTextView.setBackgroundResource(R.drawable.msg_txt_background);
//            holder.msgTextView.setTextColor(Color.WHITE);
//        }
        String name = ChatActivity.Users.get(fromId).get("name");
        final String image = ChatActivity.Users.get(fromId).get("image");
        holder.msgNameView.setText(name);

        Picasso.with(holder.mView.getContext()).load(image).placeholder(R.drawable.default_avatar).into(holder.cirView);

        Picasso.with(holder.mView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.default_avatar).into(holder.cirView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(holder.mView.getContext()).load(image).placeholder(R.drawable.default_avatar).into(holder.cirView);
            }
        });

        Log.d("CHAT", c.getMessage() + " : " + c.getTimestamp());

        holder.msgTimeView.setText(GetTimeAgo.getTimeAgo(c.getTimestamp()));
        holder.msgTextView.setText(c.getMessage());


    }

    @Override
    public int getItemCount() {
        return MsgList.size();
    }
}



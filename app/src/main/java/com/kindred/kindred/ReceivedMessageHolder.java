package com.kindred.kindred;

import android.view.View;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Adnan Iqbal Khan on 22-Feb-18.
 */

public class ReceivedMessageHolder extends MessageViewHolder {

    public ReceivedMessageHolder(View itemView) {
        super(itemView);
        mView = itemView;
        msgTextView = itemView.findViewById(R.id.text_message_body);
        msgTimeView = itemView.findViewById(R.id.text_message_time);
        msgNameView = itemView.findViewById(R.id.text_message_name);
        cirView = itemView.findViewById(R.id.image_message_profile);
    }

    void bind(Messages message) {

        String fromId = message.getFrom();
        String name = ChatActivity.Users.get(fromId).get("name");
        final String image = ChatActivity.Users.get(fromId).get("image");

        msgNameView.setText(name);
        msgTimeView.setText(GetTimeAgo.getTimeAgo(message.getTimestamp()));
        msgTextView.setText(message.getMessage());
        Picasso.with(mView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.default_avatar).into(cirView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(mView.getContext()).load(image).placeholder(R.drawable.default_avatar).into(cirView);
            }
        });
    }
}
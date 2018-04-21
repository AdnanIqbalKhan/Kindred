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
    }

    void bind(Messages message) {

        String fromId = message.getFrom();
        msgTimeView.setText(GetTimeAgo.getTimeAgo(message.getTimestamp()));
        msgTextView.setText(message.getMessage());
    }
}
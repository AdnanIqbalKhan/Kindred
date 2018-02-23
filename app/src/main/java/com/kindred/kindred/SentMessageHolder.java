package com.kindred.kindred;

import android.view.View;

/**
 * Created by Adnan Iqbal Khan on 22-Feb-18.
 */

public class SentMessageHolder extends MessageViewHolder {

    public SentMessageHolder(View itemView) {
        super(itemView);
        mView = itemView;
        msgTextView = itemView.findViewById(R.id.text_message_body);
        msgTimeView = itemView.findViewById(R.id.text_message_time);
        msgNameView = null;
        cirView = null;
    }

    void bind(Messages message) {
        msgTimeView.setText(GetTimeAgo.getTimeAgo(message.getTimestamp()));
        msgTextView.setText(message.getMessage());
    }
}

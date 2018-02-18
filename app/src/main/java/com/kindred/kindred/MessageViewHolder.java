package com.kindred.kindred;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Adnan Iqbal Khan on 18-Feb-18.
 */


class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView msgTextView;
    public TextView msgNameView;
    public TextView msgTimeView;
    public CircleImageView cirView;

    public View mView;

    public MessageViewHolder(View view) {
        super(view);

        mView = view;
        msgTextView = view.findViewById(R.id.msg_single_msg_txt);
        msgNameView = view.findViewById(R.id.msg_single_name_txt);
        msgTimeView = view.findViewById(R.id.msg_single_time_txt);
        cirView = view.findViewById(R.id.msg_single_profile_img);
    }
}

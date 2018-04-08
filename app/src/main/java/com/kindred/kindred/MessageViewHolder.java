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

    }
}


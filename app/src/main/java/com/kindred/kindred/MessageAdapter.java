package com.kindred.kindred;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by Adnan Iqbal Khan on 18-Feb-18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Messages> MsgList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MessageAdapter(List<Messages> msgList) {
        MsgList = msgList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Messages message = MsgList.get(position);
        String fromId = message.getFrom();

        if (fromId.equals(currentUserId)) {

            SentMessageHolder h =(SentMessageHolder)  holder;

            h.bind(message);
        } else {
            ReceivedMessageHolder h =(ReceivedMessageHolder)  holder;

            h.bind(message);
        }


    }

    @Override
    public int getItemCount() {
        return MsgList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Messages message = MsgList.get(position);
        String fromId = message.getFrom();

        if (fromId.equals(currentUserId)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
}



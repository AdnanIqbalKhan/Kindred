package com.kindred.kindred;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Adnan Iqbal Khan on 25-Feb-18.
 */

public class OrdersViewHolder extends RecyclerView.ViewHolder {

    View mView;
    private RelativeLayout layout;
    final RelativeLayout.LayoutParams params;

    public OrdersViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public void setNameVisibilityOn() {
        LinearLayout linearLayout = mView.findViewById(R.id.confirmed_orders_name_linearLayout);
        linearLayout.setVisibility(View.VISIBLE);
    }
    public void setUser_name(String userName) {
        TextView orderUserNameView = mView.findViewById(R.id.confirmed_orders_username_textview);
        orderUserNameView.setText(userName);
    }

    public void setServices_Charges(String services_charges) {
        TextView orderServiceCharges = mView.findViewById(R.id.service_tip_textview);
        orderServiceCharges.setText(services_charges);
    }

    public void setPurchasingLocation(String purLoc) {
        TextView purchasingLocation = mView.findViewById(R.id.pick_up_location_textview);
        purchasingLocation.setText(purLoc);
    }

    public void setDropOffLocation(String dropOffLoc) {
        TextView dropOffLocation = mView.findViewById(R.id.drop_off_location_textview);
        dropOffLocation.setText(dropOffLoc);
    }


    public void setUserImage(String image_id, final Context ctx) {
        CircleImageView userImageView = mView.findViewById(R.id.user_single_image);
        try {
            userImageView.setImageResource(Integer.parseInt(image_id));
        } catch (Exception e) {
            userImageView.setImageResource(R.drawable.default_avatar);
        }
    }

    public void setOrderStatus(boolean confirmed, boolean delivered, String cls) {
        TextView statusTxt = mView.findViewById(R.id.order_status_text);
        String text = "";
        if (cls.equals("Confirmed")) {
            if (confirmed) {
                text = "InProgress";
                if (delivered) {
                    text = "Completed";
                }
            } else {
                text = "WrongOrder";
            }
        } else if (cls.equals("Posted")) {
            if (confirmed) {
                text = "Confirmed";
                if (delivered) {
                    text = "Delivered";
                }
            } else {
                text = "UnConfirmed";
            }
        } else {
            text = "WrongClass";
        }
        statusTxt.setText(text);
        statusTxt.setVisibility(View.VISIBLE);
    }

    public void Layout_hide() {
        params.height = 0;
        itemView.setLayoutParams(params);
    }
}
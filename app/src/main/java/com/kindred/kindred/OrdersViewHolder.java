package com.kindred.kindred;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
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

    public void setUserImage(final String image_id, final Context ctx) {
        final CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
        userImageView.setImageResource(Integer.parseInt(image_id));
    }

    public void setOrderStatus(boolean confirmed, boolean delivered, Context ctx) {
        ImageView statusImg = mView.findViewById(R.id.order_status_img);
        if (confirmed) {
            statusImg.setImageResource(R.drawable.confrim_);
            if (delivered) {
                statusImg.setImageResource(R.drawable.delivered_);
            }
            statusImg.setVisibility(View.VISIBLE);
        } else {
            statusImg.setVisibility(View.INVISIBLE);
        }

    }

    public void Layout_hide() {
        params.height = 0;
        itemView.setLayoutParams(params); //This One.
//        layout.setLayoutParams(params);   //Or This one.

    }
}
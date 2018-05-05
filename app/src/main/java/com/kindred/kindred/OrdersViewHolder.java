package com.kindred.kindred;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public void setServices_Charges(String charges) {
        TextView orderUserNameView = mView.findViewById(R.id.service_tip_textview);
        orderUserNameView.setText(charges);
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
        itemView.setLayoutParams(params);
    }
}
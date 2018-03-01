package com.kindred.kindred;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

    public void setServices_Charges(String name) {
        TextView orderUserNameView = mView.findViewById(R.id.order_services_charges);
        orderUserNameView.setText(name);
    }

    public void setPurchasingLocation(String purLoc) {
        TextView purchasingLocation = mView.findViewById(R.id.orders_purchasingLocation_textView);
        purchasingLocation.setText(purLoc);
    }

    public void setDropOffLocation(String dropOffLoc) {
        TextView dropOffLocation = mView.findViewById(R.id.orders_dropOff_textView);
        dropOffLocation.setText(dropOffLoc);
    }

    public void setUserImage(final String thumb_image, final Context ctx) {
        final CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);

        Picasso.with(ctx).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(userImageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);
            }
        });
    }

    public void Layout_hide() {
        params.height = 0;
        itemView.setLayoutParams(params); //This One.
//        layout.setLayoutParams(params);   //Or This one.

    }
}
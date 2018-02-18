package com.kindred.kindred;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {


    private RecyclerView mOrdersListRecyclerView;
    private DatabaseReference mOrdersDatabase;


    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders, container, false);


        mOrdersDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        mOrdersListRecyclerView = (RecyclerView) v.findViewById(R.id.orders_userOrders_recyclerView);
        mOrdersListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
        mLayoutManager.setStackFromEnd(true);
        mOrdersListRecyclerView.setLayoutManager(mLayoutManager);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = mOrdersDatabase.orderByChild("posted_on");
        FirebaseRecyclerAdapter<Order, OrdersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Order, OrdersViewHolder>(
                Order.class,
                R.layout.orders_singleorder_layout,
                OrdersViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(OrdersViewHolder viewHolder, final Order model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setPurchasingLocation(model.getPurchasing_location());
                viewHolder.setDropOffLocation(model.getDropoff_location());
                viewHolder.setUserImage(model.getThumb_image(), getContext());

                final String post_id = getRef(position).getKey();
// TODO
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent itemsDetailsIntent = new Intent(getActivity(), ItemsDetailsActitvity.class);
//                        itemsDetailsIntent.putExtra("post_id", post_id);
//                        startActivity(itemsDetailsIntent);
//                    }
//                });

                final Button openChatBtn = viewHolder.mView.findViewById(R.id.orders_open_chat_btn);
//                openChatBtn.setEnabled(false);
                openChatBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getConfirmed().equals("true")) {

                            startActivity(new Intent(getContext(), ChatActivity.class)
                                    .putExtra("post_id", post_id));

                        } else {
                            Toast.makeText(getContext(), "Not valid", Toast.LENGTH_SHORT);
                        }
                    }
                });

                final Button confirmOrderBtn = viewHolder.mView.findViewById(R.id.orders_confirm_order_btn);
                confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String provider_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

                        mDbRef.child("users").child(provider_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map confirmData = new HashMap<>();
                                confirmData.put("uid", provider_uid);
                                confirmData.put("name", dataSnapshot.child("name").getValue().toString());
                                confirmData.put("image", dataSnapshot.child("thumb_image").getValue().toString());

                                DatabaseReference userMsgPush = mDbRef.child("users/" + provider_uid + "/confirm_orders/").push();
                                String pushId = userMsgPush.getKey();

                                Map updateDB = new HashMap<>();

                                updateDB.put("posts/" + post_id + "/provider", confirmData);
                                updateDB.put("posts/" + post_id + "/confirmed", "true");
                                updateDB.put("posts/" + post_id + "/confirmation_time", ServerValue.TIMESTAMP);
                                updateDB.put("users/" + provider_uid + "/confirm_orders/" + pushId, post_id);


                                mDbRef.updateChildren(updateDB).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
//                                        openChatBtn.setEnabled(true);
                                        confirmOrderBtn.setEnabled(false);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
            }
        };

        mOrdersListRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public OrdersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {
            TextView orderUserNameView = mView.findViewById(R.id.user_single_name);
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

        public void setUserImage(String thumb_image, Context ctx) {
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);
        }
    }
}

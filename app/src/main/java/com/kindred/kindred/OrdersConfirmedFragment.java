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
public class OrdersConfirmedFragment extends Fragment {


    private RecyclerView mOrdersListRecyclerView;
    private DatabaseReference mOrdersDatabase;


    public OrdersConfirmedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders_confirmed, container, false);


        mOrdersDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        mOrdersDatabase.keepSynced(true);
        mOrdersListRecyclerView = (RecyclerView) v.findViewById(R.id.confirmed_orders_recyclerView);
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
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent itemsDetailsIntent = new Intent(getActivity(), ItemsDetailsActitvity.class);
                        itemsDetailsIntent.putExtra("post_id", post_id);
                        startActivity(itemsDetailsIntent);
                    }
                });
            }
        };

        mOrdersListRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}

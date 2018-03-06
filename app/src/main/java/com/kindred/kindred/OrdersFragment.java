package com.kindred.kindred;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {


    private RecyclerView mOrdersListRecyclerView;
    private DatabaseReference mOrdersDatabase;
    private TextView mEmptyText;
    private int mCount = 0;

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders, container, false);


        mOrdersDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        mOrdersDatabase.keepSynced(true);
        mOrdersListRecyclerView = (RecyclerView) v.findViewById(R.id.orders_userOrders_recyclerView);
        mOrdersListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
        mLayoutManager.setStackFromEnd(true);
        mOrdersListRecyclerView.setLayoutManager(mLayoutManager);

        mEmptyText = v.findViewById(R.id.order_empty_text);
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

                if (model.getConfirmed().equals("false") && !model.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    viewHolder.setServices_Charges(model.getServices_charges());
                    viewHolder.setPurchasingLocation(model.getPurchasing_location());
                    viewHolder.setDropOffLocation(model.getDropoff_location());
                    viewHolder.setUserImage(model.getImage_id(), getContext());

                    final String post_id = getRef(position).getKey();

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent itemsDetailsIntent = new Intent(getActivity(), ItemsDetailsActitvity.class);
                            itemsDetailsIntent.putExtra("post_id", post_id);
                            startActivity(itemsDetailsIntent);
                        }
                    });

                    mCount = mCount + 1;
                } else {
                    viewHolder.Layout_hide();
                }

                if (mCount > 0) {
                    mOrdersListRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyText.setVisibility(View.GONE);
                } else {
                    mOrdersListRecyclerView.setVisibility(View.GONE);
                    mEmptyText.setVisibility(View.VISIBLE);
                }
            }
        };

        mOrdersListRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}

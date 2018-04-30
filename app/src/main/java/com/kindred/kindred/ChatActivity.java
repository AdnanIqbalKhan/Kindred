package com.kindred.kindred;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    private Toolbar mChatToolbar;
    private String mChatUserId;
    private String mCurrentUserId;

    private TextView mTitleView;
    private TextView LastSeenView;
    private DatabaseReference mRootRef;

    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatText;

    private RecyclerView mMsgList;
    private SwipeRefreshLayout mSwipRefreshLayout;

    private List<Messages> MessagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter messageAdapter;

    private static final int ITEMS_TO_LOAD = 5;
    private int itemPos = 0;
    private String lastItemKey = "";
    private String prevItemKey = "";

    public static Map<String, Map<String, String>> Users = new HashMap<>();
    private String mUserName;
    private String mCurrentUserName;
    private String mUserThumbImage;
    private String postID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatToolbar = findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);


        postID = getIntent().getStringExtra("post_id");


        mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.keepSynced(true);

        mRootRef.child("posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order post = dataSnapshot.getValue(Order.class);

                Map<String, String> purchaser = new HashMap<>();
                purchaser.put("name", post.getName());
                purchaser.put("image_id", post.getImage_id());

                Map<String, String> provider = new HashMap<>();
                provider.put("name", post.getProvider().getName());
                provider.put("image_id", post.getProvider().getImage_id());

                String p1 = post.getUser_id();
                String p2 = post.getProvider().getUid();

                Users.put(p1, purchaser);
                Users.put(p2, provider);

                if (Objects.equals(p1, mCurrentUserId)) {
                    mChatUserId = p2;
                    mUserName = Users.get(p2).get("name");
                    mCurrentUserName = Users.get(p1).get("name");
                    mUserThumbImage = Users.get(p2).get("image_id");
                } else if (Objects.equals(p2, mCurrentUserId)) {
                    mChatUserId = p1;
                    mUserName = Users.get(p1).get("name");
                    mCurrentUserName = Users.get(p2).get("name");
                    mUserThumbImage = Users.get(p1).get("image_id");
                } else {
                    startActivity(new Intent(ChatActivity.this, ErrorActivity.class));
                    finish();
                }

                if (Objects.equals(post.getDelivered(), "true")) {
                    LinearLayout msgLinearLayout = findViewById(R.id.chat_linear_layout_msg);
                    msgLinearLayout.setVisibility(View.GONE);
                }

                mTitleView = findViewById(R.id.custom_bar_name);
                LastSeenView = findViewById(R.id.custom_bar_lastSeen);
                final CircleImageView CirImageView = findViewById(R.id.custom_bar_image);
                mTitleView.setText(mUserName);
                CirImageView.setImageResource(Integer.parseInt(mUserThumbImage));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Error Occur", Toast.LENGTH_SHORT).show();
            }
        });


        mChatSendBtn = findViewById(R.id.chat_send_btn);
        mChatText = findViewById(R.id.chat_msg_txt);
        mMsgList = findViewById(R.id.chat_msg_list);
        mSwipRefreshLayout = findViewById(R.id.chat_swip_msg_layout);

        mLinearLayout = new LinearLayoutManager(this);
        mMsgList.setHasFixedSize(true);
        mMsgList.setLayoutManager(mLinearLayout);

        messageAdapter = new MessageAdapter(MessagesList);
        mMsgList.setAdapter(messageAdapter);

        loadMessages();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(action_bar_view);


//TODO online functionality
//        mRootRef.child("Users").child(mChatUserId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String online = dataSnapshot.child("online").getValue().toString();
//                if (online.equals("true")) {
//                    LastSeenView.setText("Online");
//                } else if (online.equals("false")) {
//                    LastSeenView.setText("Offline");
//                } else {
//                    GetTimeAgo getTimeAgo = new GetTimeAgo();
//                    long lastTime = Long.parseLong(online);
//                    String lastSeenTimeAgo = getTimeAgo.getTimeAgo(lastTime);
//                    LastSeenView.setText(lastSeenTimeAgo);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemPos = 0;
                prevItemKey = lastItemKey;
                loadMoreMessages();
            }
        });
    }

    private void loadMoreMessages() {

        String msgRef = "Messages/" + postID;
        Query messageQuery = mRootRef.child(msgRef).orderByKey().endAt(lastItemKey).limitToLast(ITEMS_TO_LOAD);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages m = dataSnapshot.getValue(Messages.class);
                String msgKey = dataSnapshot.getKey();

                if (itemPos == 0) {
                    lastItemKey = msgKey;
                }

                if (!prevItemKey.equals(msgKey)) {
                    MessagesList.add(itemPos++, m);
                    messageAdapter.notifyDataSetChanged();
                }

                mSwipRefreshLayout.setRefreshing(false);
                mLinearLayout.scrollToPositionWithOffset(ITEMS_TO_LOAD, 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadMessages() {
        String msgRef = "Messages/" + postID;

        Query messageQuery = mRootRef.child(msgRef).limitToLast(ITEMS_TO_LOAD);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages m = dataSnapshot.getValue(Messages.class);

                if (itemPos == 0) {
                    lastItemKey = dataSnapshot.getKey();
                }

                itemPos++;
                MessagesList.add(m);
                messageAdapter.notifyDataSetChanged();

                mMsgList.scrollToPosition(MessagesList.size() - 1);

                mSwipRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String msg = mChatText.getText().toString();
        if (!TextUtils.isEmpty(msg)) {
            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("message", msg);
//            msgMap.put("seen", "false");  TODO seen functionality
            msgMap.put("from", mCurrentUserId);
            msgMap.put("timestamp", ServerValue.TIMESTAMP);
            Util.sendSingleNotification(postID, mCurrentUserId, mChatUserId, "A new Message from" + mCurrentUserName, msg);
            mRootRef.child("Messages").child(postID).push().setValue(msgMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mChatText.getText().clear();
                }
            });
        }
    }
}


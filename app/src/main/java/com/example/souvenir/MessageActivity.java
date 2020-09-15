package com.example.souvenir;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.souvenir.Adapter.MessageAdapter;
import com.example.souvenir.Interface.APIservice;
import com.example.souvenir.Models.Chat;
import com.example.souvenir.Models.User;
import com.example.souvenir.NotificationsOfMsgs.Client;
import com.example.souvenir.NotificationsOfMsgs.Data;
import com.example.souvenir.NotificationsOfMsgs.MyResponse;
import com.example.souvenir.NotificationsOfMsgs.Sender;
import com.example.souvenir.NotificationsOfMsgs.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private ImageView profImage;
    private TextView username;
    private String userid;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Intent intent;
    private List<Chat> mChat;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;
    private ImageButton btn_send;
    private EditText txt_send;
    private ValueEventListener seenListener;
    private APIservice apIservice;
    private boolean notify = false;
    private ImageView back;
    private ImageView call;
    private Toolbar toolbar;
    private String phoneNum;
    public static void tryme(){

    }

    private void initiComponents() {
        call = findViewById(R.id.call);
        recyclerView = findViewById(R.id.rc);
        back = (ImageView) findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        profImage = findViewById(R.id.imageprof);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        txt_send = findViewById(R.id.txt_send);
    }

    private void makePhoneCall() {
//try {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            if (phoneNum.length() == 10 && phoneNum.startsWith("07")) {
                this.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum)));
            }
            else if (phoneNum.isEmpty())
            {   Toast.makeText(this, "this user didn't have phone number", Toast.LENGTH_LONG);}

            else
                Toast.makeText(this, "You can\'t call this user", Toast.LENGTH_LONG);

//}
//catch (Exception e){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        initiComponents();

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        apIservice = Client.getClient("https://fcm.googleapis.com/").create(APIservice.class);//api of notification
        //to link with fcm server

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        intent = getIntent();//from user adapter or other user profile

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent in = new Intent(MessageActivity.this, Profile.class);//when click on any user
                // startActivity(in);
                onBackPressed();
            }
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userid = intent.getStringExtra("userid");
        phoneNum = intent.getStringExtra("userNum");

        Log.i("mag act","line126"+phoneNum);

        System.out.println("=========================");
        System.out.println(phoneNum);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = txt_send.getText().toString();
                if (!msg.equals("")) {
                    sendmsg(firebaseUser.getUid(), userid, msg);////key

                } else {
                    Toast.makeText(MessageActivity.this, "You cannot send empty message", Toast.LENGTH_SHORT).show();
                }
                txt_send.setText("");//ba3d ersal el msg b3mal clear ll text elle katabt feyyo
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    final User user = dataSnapshot.getValue(User.class);
                    username.setText(user.getUsername());

                    //go to other user profile

                    username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MessageActivity.this, OtheruserProfile.class);
                            i.putExtra("userid", user.getId());
                            i.putExtra("username", user.getUsername());
                            i.putExtra("image", user.getImageURL());
                            i.putExtra("phone", user.getPhoneNum());/////////lujain edit
                            startActivity(i);
                        }
                    });
                    /////
///////////////////////////
                    if (user.getImageURL().equals("default")) {
                        profImage.setImageResource(R.drawable.user3);
                    } else {
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(profImage);
                    }
                    readmsg(firebaseUser.getUid(), userid, user.getImageURL());
                    //imageurl of userid,i went to userid by reference then get his imageurl

                } catch(Exception e) {
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(userid);

    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        Chat chat = ds.getValue(Chat.class);
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isseen", true);
                            ds.getRef().updateChildren(hashMap);

                        }
                    }catch(Exception e) {
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendmsg(String sender, final String receiver, String message) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Chats");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        String id = dr.push().getKey();
        hashMap.put("id", id);
        dr.child(id).setValue(hashMap);

        final String msg = message;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) { //true when send the msg by click on the arrow button
                    sendNotification(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(String receiver, final String username, final String msg) {
        Log.i("send not", "line 280");

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        //kol token btw 2 7tkoon bl id taba3 el receiver
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(),phoneNum, R.mipmap.iconn_round,
                            username + ": " + msg, "New Message", userid);
                    Sender sender = new Sender(data, token.getToken());

                    apIservice.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {//if no problem

                                if (response.body().success != 1) {//if there is a problem
                                    //do not do anything
                                    //if no notification delivered to the receiver
                                    // Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();


                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readmsg(final String myid, final String userid, final String ImageUrl) {//7tta a3mal display for msgs
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();//7tta ma yetla3o msgs metkarerat
                //list to send all ids to adapter
                ArrayList<String> ids = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        Chat chat = ds.getValue(Chat.class);
                        if (chat.getSender().equals(myid) && chat.getReceiver().equals(userid)
                                || (chat.getSender().equals(userid) && chat.getReceiver().equals(myid))) {
                            mChat.add(chat);//to show msgs in rec view
                            ids.add(chat.getId());
                        }
                    }
                    catch(Exception e) {
                    }

                    messageAdapter = new MessageAdapter(getApplicationContext(), mChat, ImageUrl);
                    recyclerView.setAdapter(messageAdapter);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("status", status);
        reference.updateChildren(hashmap);


    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

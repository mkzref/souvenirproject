package com.example.souvenir.Adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.souvenir.MessageActivity;
import com.example.souvenir.Models.Chat;
import com.example.souvenir.NotificationsOfMsgs.*;

import com.example.souvenir.R;
import com.example.souvenir.Interface.APIservice;
import com.example.souvenir.Models.User;
import com.example.souvenir.NotificationsOfMsgs.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Useradapter extends RecyclerView.Adapter<Useradapter.viewholder> {
    //useradapter for user and chat fragments
    //viewholder inner class in useradapter
    private Context mcontext;
    private List<User> musers;
    String userid;
    String phoneNum;

    private boolean isChat;//for chat fragment
    String thelastmsg;//for chat fragment


    APIservice apIservice;//for sending confirm notification from dialog
    boolean notify = false;//for sending confirm notification, flag when press confirm; if true notification sent


    public Useradapter(Context mcontext, List<User> musers, boolean isChat) {
        this.mcontext = mcontext;
        this.musers = musers;
        this.isChat = isChat;
        apIservice = Client.getClient("https://fcm.googleapis.com/").create(APIservice.class);//api of notification

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.useritem, parent, false);
        return new Useradapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {
        final User user = musers.get(position);
        holder.username.setText(user.getUsername());
        holder.dUserName.setText(user.getUsername());
        holder.dEmail.setText(user.getEmail());
        holder.dPhoneNum.setText(user.getPhoneNum());
        if (!user.getImageURL().equals("default")) {
            Glide.with(mcontext).load(user.getImageURL()).into(holder.profImage);
            Glide.with(mcontext).load(user.getImageURL()).into(holder.dImage);
        }

        if (isChat) {
            checklastmsg(user.getId(), holder.lastmsg);
        } else {
            holder.lastmsg.setVisibility(View.GONE);
        }

        if (isChat) {//eza 7ake m3ah ana
            if (user.getStatus().equals("online")) {
                Log.i("useradapter ", "line 56");
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                Log.i("useradapter ", "line 61");
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }

        } else {
            Log.i("useradapter ", "line 100");
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        userid = user.getId();
        Log.i("send not22", "line 106" + user.getId());
        //eza kabas 3ala notification image[bell]


        //eza kabas 3la item beshakel 3am
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageActivity.class);//when click on any user
                intent.putExtra("userid", user.getId());
                intent.putExtra("userNum", holder.dPhoneNum.getText());
                mcontext.startActivity(intent);
            }
        });
        holder.dChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageActivity.class);//when click on any user
                intent.putExtra("userid", user.getId());
                intent.putExtra("userNum", user.getPhoneNum());/////
                mcontext.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                holder.dialog.show();
                holder.dSendNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        notify = true;

                        notify = true;
                        userid = user.getId();
                        final String msg = "Thank You, this transaction done.";
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if (notify) {
                                    Toast.makeText(v.getContext(), "Notification Sent", Toast.LENGTH_LONG).show();
                                    sendNotification(userid, user.getUsername(), msg);
                                }
                                notify = false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                return true;
                //  mcontext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return musers.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {

        ImageView profImage;
        ImageView img_on;
        ImageView img_off;
        TextView username;
        TextView lastmsg;
        Dialog dialog;
        ImageView dImage;
        TextView dUserName;
        TextView dEmail;
        TextView dPhoneNum;
        Button dCall;
        Button dChat;
        Button dSendNotification;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            profImage = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            username = itemView.findViewById(R.id.usernametxt);
            lastmsg = itemView.findViewById(R.id.lastmsg);
            dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.user_dialogue);
            dImage = dialog.findViewById(R.id.dUserImage);
            dUserName = dialog.findViewById(R.id.dUserName);
            dEmail = dialog.findViewById(R.id.dEmail);
            dPhoneNum = dialog.findViewById(R.id.dPhoneNumber);
            dCall = dialog.findViewById(R.id.dCall);
            dCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makePhoneCall();
                }
            });
            dChat = dialog.findViewById(R.id.dChat);
            dSendNotification = dialog.findViewById(R.id.dSendNotification);
        }

        private void makePhoneCall() {
            phoneNum = dPhoneNum.getText().toString();
            if (ContextCompat.checkSelfPermission(itemView.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                if (phoneNum.length() == 10 && phoneNum.startsWith("07"))
                    mcontext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum)));
                else
                if(phoneNum.isEmpty())
                    Toast.makeText(mcontext,"this user didn't have phone number",Toast.LENGTH_LONG);
                else
                    Toast.makeText(mcontext,"You can\'t call this user",Toast.LENGTH_LONG);
        }
    }

    //check last msg


    private void checklastmsg(final String userid, final TextView lastmsg) {
        thelastmsg = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    try{
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(userid)
                                || (chat.getSender().equals(userid) && chat.getReceiver().equals(firebaseUser.getUid()))) {
                            thelastmsg = chat.getMessage();

                        }
                    }catch(Exception e) {
                    }

                }

                switch (thelastmsg) {
                    case "default":
                        lastmsg.setText("No Message");
                        break;
                    default:
                        lastmsg.setText(thelastmsg);
                        break;
                }

                thelastmsg = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void sendNotification(String receiver, final String username, final String msg) {
        Log.i("send notadapter", "line 216");

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.i("send not", "line 239");
                    Token token = ds.getValue(Token.class);

                    Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(),phoneNum, R.mipmap.iconn_round,
                            username + ": " + msg, "New Message", userid);

                    Sender sender = new Sender(data, token.getToken());
                    apIservice.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                Log.i("send notadapter", "line 250");
                                if (response.body().success != 1) {

                                    Log.i("send notadapter", "line 250");

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

}


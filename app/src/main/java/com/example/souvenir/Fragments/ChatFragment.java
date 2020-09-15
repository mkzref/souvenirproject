package com.example.souvenir.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.souvenir.R;
import com.example.souvenir.Adapter.Useradapter;
import com.example.souvenir.Models.Chat;
import com.example.souvenir.Models.User;
import com.example.souvenir.NotificationsOfMsgs.Token;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    RecyclerView recyclerView;
    Useradapter useradapter;
    List<User> musers;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    List<String>userlist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = root.findViewById(R.id.recycleview);




        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        userlist=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat =ds.getValue(Chat.class);

                    try {
                        if (chat.getSender().equals(firebaseUser.getUid())) {
                            if (!userlist.contains(chat.getReceiver()) )
                                userlist.add(chat.getReceiver());

                        }
                        //on time display other user
                        if (chat.getReceiver().equals(firebaseUser.getUid())) {
                            if (!userlist.contains(chat.getSender()) )
                                userlist.add(chat.getSender());


                        }
                    }
                    catch(Exception e) {
                    }

                    readchats();

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(getActivity(),new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        updateToken(newToken);
                    }
                });
/////


        return root;
    }




    private void updateToken(String tok) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tok);
        ref.child(firebaseUser.getUid()).setValue(token);

    }


    private void readchats() {
        musers=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user =ds.getValue(User.class);
                    //display 1 user from chats
                    for(String id :userlist) {
                        if(user.getId().equals(id)) {
                            if(musers.size()!=0) {

                                boolean flag = false;
                                for (int i = 0; i < musers.size(); i++)
                                //for (User user1 : musers)
                                {
                                    if (!user.getId().equals(musers.get(i).getId())) {
                                        for (int j = 0; j< musers.size(); j++){
                                            if(!user.getUsername().equals(musers.get(j).getUsername())) {
                                                flag=true;
                                            }
                                            else {flag=false;
                                            }
                                        }
                                        if(flag)
                                            musers.add(user);
                                        Log.i("tag","line147"+user.getUsername());
                                    }
                                }

                            }else {
                                musers.add(user);  Log.i("tag","line157"+user.getUsername());
                            }
                        }
                    }

                }

                useradapter=new Useradapter(getContext(),musers,true);
                recyclerView.setAdapter(useradapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }






}

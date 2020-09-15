package com.example.souvenir;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.souvenir.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class delete_account extends AppCompatActivity {
    EditText email;
    Button deactivebtn;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String txt_emial;
    String useremail="";
    public static String fl="ok";//not used any more
    public static void setflag(String flag)
   {
       fl=flag;
   }
    public static String getflag()
    {
        return fl;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        email = findViewById(R.id.email);
        deactivebtn = findViewById(R.id.deactivebtn);
        //fl="ok";

        deactivebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_emial=email.getText().toString();


               // setflag("ok");

                Log.i("iiiiiiiiiflll",fl);
                Log.i("iiiiiiiii",txt_emial);
                if(TextUtils.isEmpty(txt_emial))
                {
                    email.setError("Confirm is required!");

                }

                else {
                        Log.i("iiiiiiiii","nooooooooooooo");



                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            //////////////////

                            //delete products of user from view of products
                            DatabaseReference r4 = FirebaseDatabase.getInstance().getReference("Products").child("View Of Products");
                            Query query4 = r4.orderByChild("ownerID").equalTo(firebaseUser.getUid());
                            query4.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        snapshot.getRef().removeValue();
                                        Log.i("iiiiiiiii", "line96");

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });//delete from db


                            //delete reports of user
                            DatabaseReference r5 = FirebaseDatabase.getInstance().getReference("Reports");
                            Query query5 = r5.orderByChild("UserID").equalTo(firebaseUser.getUid());
                            query5.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        snapshot.getRef().removeValue();
                                        Log.i("iiiiiiiii", "line96");

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });//delete from db
                            ////////////


                            //delete locations of user
                            DatabaseReference r6 = FirebaseDatabase.getInstance().getReference("LoctionsUsers");
                            Query query6 = r6.orderByChild("ID").equalTo(firebaseUser.getUid());
                            query6.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        snapshot.getRef().removeValue();
                                        Log.i("iiiiiiiii", "line96");

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });//delete from db
                            ////////////


                            //delete products of user in cart
                            DatabaseReference r7 = FirebaseDatabase.getInstance().getReference("Cart List").child("User View");
                            Query query7 = r7.orderByKey().equalTo(firebaseUser.getUid());
                            query7.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        snapshot.getRef().removeValue();
                                        Log.i("iiiiiiiii", "line96");

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });//delete from db
                            ////////////


                            //delete chats when user is the sender
                            DatabaseReference r2 = FirebaseDatabase.getInstance().getReference("Chats");
                            Query query2 = r2.orderByChild("sender").equalTo(firebaseUser.getUid());
                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        snapshot.getRef().removeValue();
                                        Log.i("iiiiiiiii", "line95");

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });//delete from db

                            //delete chats when user is the receiver
                            DatabaseReference r3 = FirebaseDatabase.getInstance().getReference("Chats");
                            Query query3 = r3.orderByChild("receiver").equalTo(firebaseUser.getUid());
                            query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        snapshot.getRef().removeValue();
                                        Log.i("iiiiiiiii", "line115");

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });//delete from db

                            /////////////
                            //delete products of user
                            DatabaseReference r1 = FirebaseDatabase.getInstance().getReference("Products").child("Ownership Structure");
                            Query query1 = r1.orderByKey().equalTo(firebaseUser.getUid());
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        snapshot.getRef().removeValue();
                                        Log.i("iiiiiiiii", "line143");
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });//delete from db


                            ////////////////
                            ///delete user info
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            Query query = ref.orderByKey().equalTo(firebaseUser.getUid());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                                        startActivity(new Intent(delete_account.this, Entry.class));
                                        //  finish();
                                        snapshot.getRef().removeValue();
                                        Log.i("iiiiiiiii", "line161");

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });//delete from db
                            Log.i("iiiiiiiii", "line188");
                            deleteAccount();//delete email from auth,,error if delete account before delete from db
                            //coz in register we save each email with user in the db


                }//first end else

            }
        });//end onclick

    }



    private void deleteAccount() {

        setflag("ok");
        Log.i("iiiiiiiii","line105");
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "OK! Works fine!");
                            Log.i("iiiiiiiii","line210");
                        } else {
                            Log.w("TAG", "Something is wrong!");Log.i("iiiiiiiii","line212");
                        }
                    }
                });
            }

        }







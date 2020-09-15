package com.example.souvenir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.souvenir.R;
import com.example.souvenir.Models.userinfolocations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class allUsersLocations extends AppCompatActivity {
    TextView txt; DatabaseReference databaseTracks;
    ListView list;
    List<String> sp;

    FirebaseUser firebaseUser;
    ArrayAdapter<String> LISTad;

    List<String> listloctions = new ArrayList<>();

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_locations);
        list = findViewById(R.id.listtf);

        //  Toast.makeText(getApplicationContext(), intent.getStringExtra("Data"), Toast.LENGTH_SHORT).show();



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        intent=getIntent();//from user adapter

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final String userid = intent.getStringExtra("userid" );
        // Toast.makeText(getApplicationContext() , userid + " " , Toast.LENGTH_LONG).show();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


//Toast.makeText(getApplicationContext() , userid + " " , Toast.LENGTH_LONG).show();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();



        LISTad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listloctions);

        read(userid);


        databaseTracks= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String val=(String)(list.getItemAtPosition(position));
                Intent i = new Intent(allUsersLocations.this , MapsActivity.class);


                final String name = val.substring(16, val.indexOf("\n"));


                LISTad. notifyDataSetChanged();


                String[] parts = val.split("\n", 3);
                String part1 = parts[0];
                String part2 = parts[1];
                String part3 = parts[2];
                final   String p1 = part2.substring(11, part2.length());
                final  String p2 = part3.substring(11, part3.length());
                i.putExtra("points" , p1 +","+ p2);
                // Toast.makeText(getApplicationContext() ,p1+ ","+p2, Toast.LENGTH_LONG).show();
                startActivity(i);
                LISTad.notifyDataSetChanged();
            }
        });
       /* databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               // final user2 user = dataSnapshot.getValue(user2.class);




                readmsg(userid);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/

























       /* String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("USERSApp");
        DatabaseReference uidRef = rootRef.child(uid);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if(snapshot.getValue(user2.class).getEmail().equals(data)) {
                 //   startActivity(new Intent(getApplicationContext(), UserMainPageActivity.class));
                    Toast.makeText(getApplicationContext(), "Equal", Toast.LENGTH_SHORT).show();
                } else if (snapshot.getValue(user2.class).getEmail().equals("")) {

                    Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_SHORT).show();
                   // startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("", databaseError.getMessage());
            }
        };*/
        // uidRef.addListenerForSingleValueEvent(valueEventListener);


        // emailname=  intent.getStringExtra("Data");
        //Toast.makeText(getApplicationContext() , emailname , Toast.LENGTH_SHORT).show();
        // emailn = emailname;
        // Toast.makeText(getApplicationContext() , emailn + "hh" , Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext() , emailname + " " , Toast.LENGTH_LONG).show();

        //  LISTad=new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1  , sp);


        /*FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();;
        String uid = user.getUid();

        Query query3 = FirebaseDatabase.getInstance().getReference("Users")

                .orderByChild("email")

                .equalTo("GG@GMAIL.COM");

        query3.addListenerForSingleValueEvent(valueEventListener);
*/


        /*
         * this line is important
         * this time we are not getting the reference of a direct node
         * but inside the node track we are creating a new child with the artist id
         * and inside that node we will store all the tracks with unique ids
         * */



/*

String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
DatabaseReference uidRef = rootRef.child(uid);
ValueEventListener valueEventListener = new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.child("userType").getValue(String.class).equals("Customer")) {
            startActivity(new Intent(getApplicationContext(), UserMainPageActivity.class));
        } else if (dataSnapshot.child("userType").getValue(String.class).equals("Venue Owner")) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.d(TAG, databaseError.getMessage());
    }
};
uidRef.addListenerForSingleValueEvent(valueEventListener);

*/


       /* Query query3 = FirebaseDatabase.getInstance().getReference("USERSApp");



        query3.addListenerForSingleValueEvent(valueEventListener);*/


        /*Query query3 = FirebaseDatabase.getInstance().getReference("USERSApp")

                .orderByChild("Email")

                .equalTo(emailname);
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    user2 artist = snapshot.getValue(user2.class);
                    String Name = snapshot.getValue(user2.class).getId();
                    String Name2=snapshot.child("id").getValue().toString();

                    Toast.makeText(getApplicationContext(),""+artist+ Name+""+ Name2+"" , Toast.LENGTH_LONG).show();

                    // artistList.add("Email :" +artist.getEmail());
                    //Toast.makeText(getApplicationContext(),""+artist.getEmail()+"" , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/
        /*databaseTracks = FirebaseDatabase.getInstance().getReference("USERSApp");
databaseTracks.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot data2 : dataSnapshot.getChildren()) {
            //If email exists then toast shows else store the data on new key
            if (data2.getValue(user2.class).getEmail()== data) {
                Toast.makeText(getApplicationContext(), "okii.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "2 ::::: " + data2.getValue(user2.class).getEmail() , Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
*/





    }

   /* ValueEventListener valueEventListener = new ValueEventListener() {

        @Override

        public void onDataChange(DataSnapshot dataSnapshot) {

            // artistList.clear();

            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //user2 artist = snapshot.getValue(user2.class);
                    if (!snapshot.getValue(user2.class).getEmail().equals(data)) {
                        Toast.makeText(getApplicationContext(), "NO EQUAL", Toast.LENGTH_SHORT).show();
                    } else if (snapshot.getValue(user2.class).getEmail().equals("")) {
                        Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_SHORT).show();
                    } else if (snapshot.getValue(user2.class).getEmail().equals(data)) {
                        Toast.makeText(getApplicationContext(), "EQUAL", Toast.LENGTH_SHORT).show();


                    }
                }*/


    // artistList.add("Email :" +artist.getEmail());
    //Toast.makeText(getApplicationContext(),""+artist.getEmail()+"" , Toast.LENGTH_LONG).show();


              /*  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                  //  String Name = snapshot.getValue(user2.class).getName();
                   String Email = snapshot.getValue(user2.class).getEmail();
//Email2 = Email;
                   // artistList.add(" Name :" + "  "+Name +"\n"+ " Email :" + "  "+Email +" ");
                    artistList.add(Email);
                   // user2  u = snapshot.getValue(user2.class);

                   //0 artistList.add(u);
                   // Toast.makeText(getApplicationContext(),"Datasnapshot"+Name + "  " +Email  , Toast.LENGTH_LONG).show();


                }*/

    // adapter.notifyDataSetChanged();
    // Listt.setAdapter(LISTad);

    //  Listt.setAdapter(LISTad);

   /* ValueEventListener valueEventListener = new ValueEventListener() {

        @Override

        public void onDataChange(DataSnapshot dataSnapshot) {



            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String Name = snapshot.getValue(user2.class).getPhone();
                    // Latitude = snapshot.getValue(UserInformation.class).getLatitude();
//Email2 = Email;

                    Toast.makeText(getApplicationContext() , Name + " " , Toast.LENGTH_LONG).show();
                   // sp.add(" Name :" + "  "+Name +"\n"+ " Email :" + "  " +" ");
                    // user2  u = snapshot.getValue(user2.class);

                    //0 artistList.add(u);
                    // Toast.makeText(getApplicationContext(),"Datasnapshot"+Name + "  " +Email  , Toast.LENGTH_LONG).show();


                }

                // adapter.notifyDataSetChanged();
                list.setAdapter(LISTad);
            }
            list.setAdapter(LISTad);
        }



        @Override

        public void onCancelled(DatabaseError databaseError) {



        }

    };*/



  /*  ValueEventListener valueEventListener = new ValueEventListener() {

        @Override

        public void onDataChange(DataSnapshot dataSnapshot) {

            //    artistList.clear();


            //if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    user2 artist = snapshot.getValue(user2.class);
                    String Name = snapshot.getValue(user2.class).getId();
                    String Name2=snapshot.child("id").getValue().toString();

                        Toast.makeText(getApplicationContext(),""+artist+ Name+""+ Name2+"" , Toast.LENGTH_LONG).show();

                    // artistList.add("Email :" +artist.getEmail());
                    //Toast.makeText(getApplicationContext(),""+artist.getEmail()+"" , Toast.LENGTH_LONG).show();
                }
              /*  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                  //  String Name = snapshot.getValue(user2.class).getName();
                   String Email = snapshot.getValue(user2.class).getEmail();
//Email2 = Email;
                   // artistList.add(" Name :" + "  "+Name +"\n"+ " Email :" + "  "+Email +" ");
                    artistList.add(Email);
                   // user2  u = snapshot.getValue(user2.class);

                   //0 artistList.add(u);
                   // Toast.makeText(getApplicationContext(),"Datasnapshot"+Name + "  " +Email  , Toast.LENGTH_LONG).show();


                }*/

    // adapter.notifyDataSetChanged();
    //   Listt.setAdapter(LISTad);
    //}
    // Listt.setAdapter(LISTad);








    private void read( final String userid) {//7tta a3mal display for msgs

        databaseTracks =  FirebaseDatabase.getInstance().getReference("LoctionsUsers");
        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userinfolocations loc = snapshot.getValue(userinfolocations.class);
                        if (loc.getuser().equals(userid)) {
                            //  artist= new UserInformation(Name , Latitude ,Longitude );
                            // Toast.makeText(getApplicationContext(),chat.getName(), Toast.LENGTH_LONG).show();
                            listloctions.add("Name Location : " + loc.getName() + "\n" + "Longitude :" + loc.getLongitude() + "\n" + "Latitude  :" + loc.getLatitude() + "" );
                            LISTad.notifyDataSetChanged();}
                    }

                    list.setAdapter(LISTad);

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }}

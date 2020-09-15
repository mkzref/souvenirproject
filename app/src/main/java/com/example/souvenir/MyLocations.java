package com.example.souvenir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.example.souvenir.Models.userinfolocations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyLocations extends AppCompatActivity {
    ListView list;
    String uid;
    DatabaseReference myRef ;
    List<String> LISTusers = new ArrayList<>();
    ArrayAdapter<String> LISTad;
    userinfolocations loc= null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_locations);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = findViewById(R.id.list);

        LISTad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LISTusers);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        }
        Query query3 = FirebaseDatabase.getInstance().getReference("LoctionsUsers")

                .orderByChild("ID")

                .equalTo(uid);


        query3.addListenerForSingleValueEvent(valueEventListener);

        myRef=FirebaseDatabase.getInstance().getReference("LoctionsUsers");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MyLocations.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Do you want to delete this location?!")
                        .setTitle("Attempt to Delete A location")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    String val=(String)(list.getItemAtPosition(position));

                                    final String name = val.substring(16, val.indexOf("\n"));

                                    deleteArtist(name);
                                    Toast.makeText(MyLocations.this, "Deleted successfully!!", Toast.LENGTH_SHORT).show();
                                    LISTusers.remove(position);
                                    LISTad. notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });
















    }
    ValueEventListener valueEventListener = new ValueEventListener() {

        @Override

        public void onDataChange(DataSnapshot dataSnapshot) {


            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    // Toast.makeText(getApplicationContext(), "" + artist.getLongitude() + "", Toast.LENGTH_LONG).show();

                    loc = snapshot.getValue(userinfolocations.class);

                    //  artist= new UserInformation(Name , Latitude ,Longitude );
                    // Toast.makeText(getApplicationContext(),artist.getName(), Toast.LENGTH_LONG).show();
                    LISTusers.add("Name Location : " + loc.getName() + "\n" + "Longitude :" + loc.getLongitude() + "\n" + "Latitude :" + loc.getLatitude() + "" );
                    LISTad.notifyDataSetChanged(); }

                list.setAdapter(LISTad);
                LISTad.notifyDataSetChanged();
            }

        }



        @Override

        public void onCancelled(DatabaseError databaseError) {



        }

    };

    private boolean deleteArtist( String id) {
        //getting the specified artist reference
        //DatabaseReference dR = FirebaseDatabase.getInstance().getReference("LoctionsUsers").child(FirebaseAuth.getInstance().getUid());

        //removing artist
        // dR.removeValue();

        myRef.orderByChild("Name").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //databaseReference = FirebaseDatabase.getInstance().getReference("Students");
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                    String key = postsnapshot.getKey();
                    postsnapshot.getRef().removeValue();


                }
                LISTad.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











        return true;
    }

}
package com.example.souvenir;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.souvenir.Models.Reports;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewReport extends AppCompatActivity {
    TextView desc, subject,UserName;

    String id = "",userid="";
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    Toolbar toolbar;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);



        id = getIntent().getStringExtra("ID");
        userid = getIntent().getStringExtra("UserID");
        desc = (TextView) findViewById(R.id.text);
        subject = (TextView) findViewById(R.id.mainSubject);


        back = (ImageView) findViewById(R.id.back);
        getReporttDetails(id);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
//            String userid = firebaseUser.getUid();
        // reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle("New Report");

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(ViewReport.this, ReportsList.class);
                startActivity(in);



            }
        });



    }


    private void getReporttDetails(String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Reports");
        ref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Reports model = dataSnapshot.getValue(Reports.class);

                    subject.setText(model.getSubject());
                    desc.setText(model.getDescription());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

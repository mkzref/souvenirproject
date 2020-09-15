package com.example.souvenir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.souvenir.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class giveReport extends AppCompatActivity {
    String RSub,userid,RDesc,username;
    Button send;
    EditText subject,desc;
    ImageView bc;

    String ReportRandomKey,saveCurrentDate,saveCurrentTime;

    DatabaseReference databaseref;
    ProgressDialog loadingBar;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_report);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle("Give Report");
        try {
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();
            databaseref = FirebaseDatabase.getInstance().getReference().child("Reports");
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
            send = (Button) findViewById(R.id.send);
            subject = (EditText) findViewById(R.id.report_subject);
            desc = (EditText) findViewById(R.id.report_Describtion);
            bc = (ImageView) findViewById(R.id.bc);

            bc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in2 = new Intent(giveReport.this, Main.class);
                    startActivity(in2);

                }
            });

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValidateReport();
                }
            });

        }
        catch (Exception ex)
        {
            //  Toast.makeText(this,"you are not a user!",Toast.LENGTH_LONG).show();
            //  Intent in = new Intent(giveReport.this, Home.class);
            //   startActivity(in);


        }
    }

    private void ValidateReport() {


        RSub= subject.getText().toString();
        RDesc=desc.getText().toString();
        if(TextUtils.isEmpty(RSub))

        {
            Toast.makeText(this,"Please Enter your Report Subject",Toast.LENGTH_LONG).show();
        }

        else  if(TextUtils.isEmpty(RDesc))

        {
            Toast.makeText(this,"Please Enter your Report Describtion",Toast.LENGTH_LONG).show();
        }
        else
        {
            storeInformation();
        }
    }

    private void storeInformation() {




        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        ReportRandomKey=saveCurrentDate+saveCurrentTime;
        saveProductInfoToTheDataBase();


    }

    private void saveProductInfoToTheDataBase() {
        try {
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot dataSnapshot) {
                    try {

                        User user = dataSnapshot.getValue(User.class);
                        username = (user.getUsername().toString());}
                    catch(Exception e)
                    {

                        Log.i("tag in main: ","line151 main");
                    }

                }

                @Override
                public void onCancelled( DatabaseError databaseError) {

                }
            });

            if(userid!=null){
                HashMap<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("ID", ReportRandomKey);
                categoryMap.put("Date", saveCurrentDate);
                categoryMap.put("Time", saveCurrentTime);
                categoryMap.put("Subject", RSub);
                categoryMap.put("Description", RDesc);
                categoryMap.put("UserID",userid);
                categoryMap.put("UserName",username);


                databaseref.child(ReportRandomKey).updateChildren(categoryMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    Toast.makeText(giveReport.this, "Report is added successfully", Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(giveReport.this, Main.class);
                                    startActivity(in);
                                } else {

                                    String msg = task.getException().toString();
                                    Toast.makeText(giveReport.this, "Error: " + msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        });



            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"you are not a user!",Toast.LENGTH_LONG).show();



        }
    }



}














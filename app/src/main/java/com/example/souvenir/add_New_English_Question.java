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

public class add_New_English_Question extends AppCompatActivity {
    String RSub,userid,RDesc,username;
    Button add;
    EditText question, answer;
    ImageView bc;

    String QuestionRandomKey,saveCurrentDate,saveCurrentTime;

    DatabaseReference databaseref;
    ProgressDialog loadingBar;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__question);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle("Add New Question");
        try {
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();

            if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);}
            databaseref = FirebaseDatabase.getInstance().getReference().child("Help").child("English");
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
            add = (Button) findViewById(R.id.add);
            question = (EditText) findViewById(R.id.question);
            answer = (EditText) findViewById(R.id.answer);
            bc = (ImageView) findViewById(R.id.bc);

            bc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in2 = new Intent(add_New_English_Question.this, Main.class);
                    startActivity(in2);

                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValidateQuestion();
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

    private void ValidateQuestion() {


        RSub= question.getText().toString();
        RDesc= answer.getText().toString();
        if(TextUtils.isEmpty(RSub))

        {
            Toast.makeText(this,"Please Enter the question",Toast.LENGTH_LONG).show();
        }

        else  if(TextUtils.isEmpty(RDesc))

        {
            Toast.makeText(this,"Please Enter the answer",Toast.LENGTH_LONG).show();
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
        QuestionRandomKey =saveCurrentDate+saveCurrentTime;
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
                HashMap<String, Object> map = new HashMap<>();
                map.put("ID", QuestionRandomKey);
                map.put("Date", saveCurrentDate);
                map.put("Time", saveCurrentTime);
                map.put("Question", RSub.toString().toLowerCase());
                map.put("Answer", RDesc);



                databaseref.child(userid).child(QuestionRandomKey).updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    Toast.makeText(add_New_English_Question.this, " added successfully", Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(add_New_English_Question.this, Main.class);
                                    startActivity(in);
                                } else {

                                    String msg = task.getException().toString();
                                    Toast.makeText(add_New_English_Question.this, "Error: " + msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        });



            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"you are not an admin!",Toast.LENGTH_LONG).show();



        }
    }



}














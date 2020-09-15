package com.example.souvenir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class suggestNewCategory extends AppCompatActivity {
    String name,userid;
    Button send;
    EditText Cname;
    ImageView bc;
    Uri imageUri;
    String ReportRandomKey,saveCurrentDate,saveCurrentTime;

    DatabaseReference databaseref;
    ProgressDialog loadingBar;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_new_category);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle("Suggest new category");
try {

    send = (Button) findViewById(R.id.ssend);
    Cname = (EditText) findViewById(R.id.Cname);
    bc = (ImageView) findViewById(R.id.bc);

    bc.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent in2 = new Intent(suggestNewCategory.this, Main.class);
            startActivity(in2);

        }
    });
    auth = FirebaseAuth.getInstance();
    firebaseUser = auth.getCurrentUser();
    assert firebaseUser != null;
    String userid = firebaseUser.getUid();
    send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ValidateReport();
        }
    });

}
catch (Exception ex)
{
    Toast.makeText(this,"you are not a user!",Toast.LENGTH_LONG).show();
    Intent in = new Intent(suggestNewCategory.this, Main.class);
    startActivity(in);


}

    }

    private void ValidateReport() {


        name=Cname.getText().toString();

        if(TextUtils.isEmpty(name))

        {
            Toast.makeText(this,"Please Enter your subject",Toast.LENGTH_LONG).show();
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
            databaseref = FirebaseDatabase.getInstance().getReference().child("suggested Categories").child(ReportRandomKey);

            String userid = firebaseUser.getUid();
            if(userid!=null){
                HashMap<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("ID", ReportRandomKey);
                categoryMap.put("Date", saveCurrentDate);
                categoryMap.put("Time", saveCurrentTime);
                categoryMap.put("Name", name);


                databaseref.setValue(categoryMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    Toast.makeText(suggestNewCategory.this, "Category is added successfully", Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(suggestNewCategory.this, Main.class);
                                    startActivity(in);
                                } else {

                                    String msg = task.getException().toString();
                                    Toast.makeText(suggestNewCategory.this, "Error: " + msg, Toast.LENGTH_LONG).show();
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

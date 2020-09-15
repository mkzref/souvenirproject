package com.example.souvenir;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetpassActivity extends AppCompatActivity {

    EditText sendemail;
    Button btn_reset;
    FirebaseAuth firebaseAuth;

    //add email in templates in auth in firebase console,password reset
    //then pen ,add email,name,sender name(Nostalgia).
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);
        sendemail=findViewById(R.id.sendemail);
        btn_reset=findViewById(R.id.btn_reset);
        firebaseAuth= FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=sendemail.getText().toString();
                if(email.equals("")) {
                    Toast.makeText(getApplicationContext(),"Email is required",Toast.LENGTH_LONG).show();

                }
                else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Please check your Email",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ResetpassActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else {
                                String error=task.getException().getMessage();
                                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();

                            }


                        }
                    });

                }

            }
        });

    }

}

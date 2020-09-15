package com.example.souvenir;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Register extends AppCompatActivity {
    EditText username, email, password, confirmpassword, phoneNum;
    Button registerbtn;
    private ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference reference;
    ImageView imageView, imageView2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        phoneNum = findViewById(R.id.phoneNum);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        confirmpassword = findViewById(R.id.confirmpassword);
        progressBar = findViewById(R.id.progressBar);
        registerbtn = findViewById(R.id.sign_up_button);
        imageView = findViewById(R.id.hideshow);
        imageView2 = findViewById(R.id.hideshow2);
        auth = FirebaseAuth.getInstance();

        //for eye image/hide show pass
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }


        });////
        imageView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        confirmpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        confirmpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });////

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_phoneNum = phoneNum.getText().toString();
                String txt_emial = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username)) {
                    username.setError("User Name is required!");
                    // Toast.makeText(Register.this,"All fields are required",Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(txt_emial)) {
                    email.setError("Email is required!");
                    //Toast.makeText(Register.this,"All fields are required",Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(txt_password)) {
                    password.setError("Password is required!");
                    //Toast.makeText(Register.this,"All fields are required",Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(txt_phoneNum)) {
                    txt_phoneNum = "Not set yet!";
                }
                if (TextUtils.isEmpty(confirmpassword.getText())) {
                    confirmpassword.setError("Confirm password is required!");
                } else if (!txt_password.equals(confirmpassword.getText().toString())) {
                    //check confirm password
                    Toast.makeText(Register.this, "Passwords are not the same!", Toast.LENGTH_LONG).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Register.this, "Password must be at least 6 Characters", Toast.LENGTH_LONG).show();
                } else { //if everything is ok
                    register(txt_username, txt_password, txt_emial, txt_phoneNum);
                }


            }
        });
    }

    private void register(final String username, final String pasword, final String email, final String phoneNum) {
        auth.createUserWithEmailAndPassword(email, pasword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;

                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("phoneNum", phoneNum);
                            hashMap.put("email", email);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(Register.this, Main.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }


                                }
                            });


                        } else {
                            Toast.makeText(Register.this, "you cannot register with this email or password", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


}

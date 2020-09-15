package com.example.souvenir;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {
    EditText  email,password;
    Button loginbtn;
    FirebaseAuth auth;
    private ProgressBar progressBar;
    DatabaseReference reference;
    ImageView imageView;
    FirebaseUser firebaseUser;
    FingerprintManagerCompat managerCompat;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        loginbtn = findViewById(R.id.btn_login);
        progressBar =  findViewById(R.id.progressBar);
        imageView =  findViewById(R.id.hideshow);
        auth = FirebaseAuth.getInstance();

        imageView .setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_emial=email.getText().toString();
                String txt_password=password.getText().toString();

                if(TextUtils.isEmpty(txt_emial))
                {
                    email.setError("Email is required!");
                    //Toast.makeText(Register.this,"All fields are required",Toast.LENGTH_LONG).show();
                }else
                if(TextUtils.isEmpty(txt_password))
                {
                    password.setError("Password is required!");
                    //Toast.makeText(Register.this,"All fields are required",Toast.LENGTH_LONG).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(txt_emial,txt_password) .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {

                                Intent intent = new Intent(LoginActivity.this, Main.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "incorrect Email/Password!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }




            }
        });

    }




    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);//why? serarch its need
    }

    public void signUp(View view) {
        Intent intent1 = new Intent(LoginActivity.this, Register.class);
        startActivity(intent1);
    }

    public void resetpass(View view) {
        Intent intent = new Intent(LoginActivity.this, ResetpassActivity.class);
        startActivity(intent);
    }
    public void fingerprint(View view) {
        managerCompat = FingerprintManagerCompat.from(LoginActivity.this);

        if (managerCompat.isHardwareDetected() && managerCompat.hasEnrolledFingerprints()) {
            showFingerPrintDialog();

        } else {
            Toast.makeText(getApplicationContext(), "Fingerprint not supported", Toast.LENGTH_SHORT).show();
        }
    }
    private void showFingerPrintDialog() {

        FingerprintDialog fragment = new FingerprintDialog();
        fragment.setContext(this);
        fragment.show(getSupportFragmentManager(),"dialog");




    }
}


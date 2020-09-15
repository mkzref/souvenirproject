package com.example.souvenir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.souvenir.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editProfile extends AppCompatActivity {
    EditText  email , password ,name , phone , Oldpass;
    Button Edit;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        email=findViewById(R.id.Editemial);
         password=findViewById(R.id.editpass);
        name=findViewById(R.id.editusername);
         phone=findViewById(R.id.editphone);
        Edit=findViewById(R.id.Edit);
        Oldpass= findViewById(R.id.oldpass);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phonenum = phone.getText().toString().trim();
                final String Email = email.getText().toString().trim();
                final String username = name.getText().toString().trim();
                final String NewPassword = password.getText().toString().trim();
                final String oldpass = Oldpass.getText().toString().trim();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!TextUtils.isEmpty(username)  ) {

                            databaseReference.child("username").setValue(username);
                            databaseReference.child("search").setValue(username);


                        }
                        if (!TextUtils.isEmpty(phonenum)  ) {

                            databaseReference.child("phoneNum").setValue(phonenum);

                        }

                        if (!TextUtils.isEmpty(Email)  ) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.updateEmail(Email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });
//cause change in db
                            databaseReference.child("email").setValue(Email);


                        }
                        if (!TextUtils.isEmpty(oldpass) && !TextUtils.isEmpty(NewPassword)  ) {

                            //get current user
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            //before changing password re-authenticate the user
                            AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldpass);
                            user.reauthenticate(authCredential)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //successfully authenticated, begin update

                                            user.updatePassword(NewPassword)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //password updated


                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            //failed updating password, show reason

                                                            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //authentication failed, show reason

                                            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                phone.getText().clear();
                email.getText().clear();
                name.getText().clear();
                password.getText().clear();
                Oldpass.getText().clear();
            }
        });

    }


}

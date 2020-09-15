package com.example.souvenir;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;



public class Entry extends AppCompatActivity {


    //public static boolean isUser = false;//wrong coz it is static,means for all,
    //its value will not be correct

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);


    }

    public void login(View view) {
        //    isUser = true;
        Intent intent = new Intent(Entry.this, LoginActivity.class);
        startActivity(intent);
    }


    public void enterAsGuest(View view) {

        Intent intent = new Intent(Entry.this, Main.class);/////change
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}

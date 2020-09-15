package com.example.souvenir;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.souvenir.Models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int count = 0;
    private static final int image_request = 100;

    private FirebaseAuth auth;
    ActionBarDrawerToggle mtoggle;
    private FirebaseUser firebaseUser;
    private TextView username;
    private Uri imageuri;
    StorageReference storageReference;
    private StorageTask uploadTask;
    private TextView email;
    private ImageView profilePic;
    private AppBarConfiguration mAppBarConfiguration;
    private ListView list;
    private BottomNavigationView bottomNavigationView;
    private String[] title;
    private String[] desc;
    private int[] icon;
    private ImageView back;
    private String input = "";
    private Toolbar toolbar;
    private ActionBarDrawerToggle t;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DatabaseReference reference;

    NavigationView navigationView ;

    /////////////



    ////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        mDrawerLayout = findViewById(R.id.drawer_layout);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.drawer);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        setSupportActionBar(toolbar);

/////////////////////

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
        //setting the title
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            /*
             * Called when a drawer has settled in a completely closed state
             */
            public void onDrawerClosed(View view) {
                Log.d("drawerToggle", "Drawer closed");
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); //Creates call to onPrepareOptionsMenu()
            }

            /*
             * Called when a drawer has settled in a completely open state
             */
            public void onDrawerOpened(View drawerView) {
                Log.d("drawerToggle", "Drawer opened");
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);

        back = (ImageView) findViewById(R.id.back);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");






        if (firebaseUser != null) {
            assert firebaseUser != null;///?
            username = headerView.findViewById(R.id.CurrentUsername);
            email = headerView.findViewById(R.id.CurrentUserEmail);
            profilePic = headerView.findViewById(R.id.CurrentUserImage);
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    try {

                        email.setText(user.getEmail());
                        username.setText(user.getUsername());
                        if (!user.getImageURL().equals("default")) {
                            if (getApplicationContext() != null)
                                Glide.with(getApplicationContext()).load(user.getImageURL()).into(profilePic);
                        }

                    }
                    catch(Exception e)
                    {

                        Log.i("tag in main: ","line151 main");
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImage();
                }
            });
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                count++;
                switch (item.getItemId()) {
                    case R.id.Profile: {
                        if (firebaseUser != null) {
                            Intent intent = new Intent(Main.this, Profile.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "You are not a user!", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    }
                    case R.id.addProducts: {
                        if (firebaseUser != null) {
                            Intent in11 = new Intent(Main.this, selectCategory.class);
                            startActivity(in11);
                        } else {
                            Toast.makeText(getApplicationContext(), "You are not a user!", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    }
                    case R.id.addlocations: {

                        if (firebaseUser != null) {
                            Intent ap = new Intent(Main.this, MapActivity.class);
                            startActivity(ap);
                        } else {
                            Toast.makeText(getApplicationContext(), "You are not a user!", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    }
                    case R.id.myLocations: {

                        if (firebaseUser != null) {
                            Intent ap = new Intent(Main.this, MyLocations.class);
                            startActivity(ap);
                        } else {
                            Toast.makeText(getApplicationContext(), "You are not a user!", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    }


                    case R.id.tracking: {
                        if (firebaseUser != null) {
                            Intent ap = new Intent(Main.this, AllUsers.class);
                            startActivity(ap);
                        } else {
                            Toast.makeText(getApplicationContext(), "You are not a user!", Toast.LENGTH_SHORT).show();
                        }

                    }


                }
                return false;
            }
        });



        navigationView.setNavigationItemSelectedListener(this);


    }
    ///////////////profile img on click
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), image_request);
        //it will call onActivityResult

    }
    private void uploadImage() {

        if (imageuri != null) {

            final StorageReference filereference = storageReference.child(System.currentTimeMillis()
                    + "." + imageuri.getPath());
            uploadTask = filereference.putFile(imageuri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String muri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL", muri);
                        reference.updateChildren(hashMap);


                    } else {
                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        }///
        else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == image_request && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            profilePic.setImageURI(imageuri);
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), "Uploading in progress", Toast.LENGTH_SHORT).show();

            } else {
                uploadImage();
            }

        }


    }


    ////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        if (firebaseUser != null && firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.admin, menu);
        } else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu, menu);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.addCategory:
                if (firebaseUser != null) {
                    if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                        Intent in1 = new Intent(Main.this, addNewCategory.class);
                        startActivity(in1);
                    }
                } else {
                    Toast.makeText(this, "You are not an admin!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.addEnglishQuestion:
                if (firebaseUser != null) {
                    if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                        Intent in1 = new Intent(Main.this, add_New_English_Question.class);
                        startActivity(in1);
                    }
                } else {
                    Toast.makeText(this, "You are not an admin!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.addArabicQuestion:
                if (firebaseUser != null) {
                    if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                        Intent in1 = new Intent(Main.this, add_New_Arabic_Question.class);
                        startActivity(in1);
                    }
                } else {
                    Toast.makeText(this, "You are not an admin!", Toast.LENGTH_SHORT).show();
                }
                break;


           /* case R.id.menuLogout:
                if (firebaseUser != null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent in = new Intent(Main.this, Entry.class);
                    startActivity(in);

                } else{
                    Toast.makeText(this, "You are not a user!", Toast.LENGTH_SHORT).show();}
                break;
*/

            case R.id.cart:
                if (firebaseUser!=null) {
                    Intent in = new Intent(Main.this, cartList.class);
                    startActivity(in);
                    break;
                } else {
                    Toast.makeText(getApplicationContext(), "You are not a user!", Toast.LENGTH_SHORT).show();
                }

            case R.id.giveReport:
                if (firebaseUser != null) {
                    Intent in2 = new Intent(Main.this, giveReport.class);
                    startActivity(in2);
                } else{
                    Toast.makeText(this, "You are not a user!", Toast.LENGTH_SHORT).show();}
                break;

            case R.id.suggestCategory:
                if (firebaseUser != null) {
                    Intent in4 = new Intent(Main.this, suggestNewCategory.class);
                    startActivity(in4);
                } else{
                    Toast.makeText(this, "You are not a user!", Toast.LENGTH_SHORT).show();}
                break;

            case R.id.viewReports:
                if (firebaseUser != null) {
                    if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                        Intent in5 = new Intent(Main.this, ReportsList.class);
                        startActivity(in5);
                    }
                } else{
                    Toast.makeText(this, "You are not an admin!", Toast.LENGTH_SHORT).show();}
                break;

            case R.id.viewSuggestedCategory:
                if (firebaseUser != null) {
                    if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                        Intent in6 = new Intent(Main.this, viewSuggestCategories.class);
                        startActivity(in6);
                    }
                } else{
                    Toast.makeText(this, "You are not an admin!", Toast.LENGTH_SHORT).show();}
                break;

        }
        return true;
    }







    @Override
    public void onBackPressed() {
        if (count == 0)
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Main.super.onBackPressed();
                        }
                    }).create().show();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout: {
                if (firebaseUser != null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent in = new Intent(Main.this, Entry.class);
                    startActivity(in);

                } else {
                    Toast.makeText(getApplicationContext(), "You are not a user!", Toast.LENGTH_SHORT).show();
                }

                break;}

            case R.id.nav_faq: {


                Intent in = new Intent(Main.this, ActivityHelp.class);
                startActivity(in);



                break;
            }

            case R.id.Paintings: {
                if (firebaseUser != null) {


                    Intent in = new Intent(Main.this, Products_Page.class);
                    in.putExtra("Category", "Paintings");
                    startActivity(in);


                }
                break;
            }

            case R.id.Jewelry: {
                if (firebaseUser != null) {

                    Intent in = new Intent(Main.this, Products_Page.class);
                    in.putExtra("Category", "Jewelry");
                    startActivity(in);


                }
                break;
            }
            case R.id.Books: {
                if (firebaseUser != null) {

                    Intent in = new Intent(Main.this, Products_Page.class);
                    in.putExtra("Category", "Books");
                    startActivity(in);


                }
                break;
            }
            case R.id.Furniture: {
                if (firebaseUser != null) {

                    Intent in = new Intent(Main.this, Products_Page.class);
                    in.putExtra("Category", "Furniture");
                    startActivity(in);


                }
                break;
            }
            case R.id.nav_share: {


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://drive.google.com/file/d/1XHptqbcnv1XQrUeQQZgQsea9D10u1z_Q/view?usp=sharing";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Nostalgia Apk");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                break;

            }
            case R.id.deactiveitem: {

                if (firebaseUser != null) {
                    Intent intent = new Intent(Main.this, delete_account.class);
                    startActivity(intent);
                }
                break;

            }

        }
        return false;

    }


}

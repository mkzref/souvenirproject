package com.example.souvenir;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.souvenir.R;

import com.example.souvenir.Models.Products;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ViewProduct extends AppCompatActivity {
    TextView desc, name, price, category, quantity;
    ImageView image,space;


    Button addToCart, contact , Zoomin;
    String ownerID;
    String userid;
    String id = "";
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    int countfontsize = 20;
    Toolbar toolbar;
    ImageView back;
    boolean notify = false;
    String phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);


Zoomin=findViewById(R.id.zoomin);
Zoomin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        countfontsize = countfontsize+2;
        desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, countfontsize);

    }
});

        id = getIntent().getStringExtra("pid");
        desc = (TextView) findViewById(R.id.text);
        name = (TextView) findViewById(R.id.mainTitle);
        price = (TextView) findViewById(R.id.price);
        category = (TextView) findViewById(R.id.category);
        quantity = (TextView) findViewById(R.id.quantity);
        image = (ImageView) findViewById(R.id.mainIcon);
        addToCart = (Button) findViewById(R.id.addToCart);
        contact = (Button) findViewById(R.id.contact);
        back = (ImageView) findViewById(R.id.back);
        space = (ImageView) findViewById(R.id.space);
        getProdctDetails(id);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
//            String userid = firebaseUser.getUid();
        // reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle(name.getText().toString());

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ownerID.equals(firebaseUser.getUid()))
                        Toast.makeText(ViewProduct.this, "It's your product!", Toast.LENGTH_SHORT).show();
                    else {

                        ////
                        Log.i("view pro", "line94" + ownerID);
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(ownerID);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    User user = dataSnapshot.getValue(User.class);
                                    phone = user.getPhoneNum();
                                    Log.i("view pro", "line108 " + phone);
                                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);//when click on any user
                                    intent.putExtra("userid", ownerID);
                                    intent.putExtra("userNum", phone);
                                    Log.i("view pro", "line121" + phone);
                                    startActivity(intent);
                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        ////
                    }

                }catch (Exception e){  Toast.makeText(ViewProduct.this, "you are not a user!", Toast.LENGTH_SHORT).show();}
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
        if (firebaseUser != null && firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2"))
            addToCart.setVisibility(View.GONE);
        if (firebaseUser != null && !firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2"))
            space.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products").child("View Of Products");
                ref.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Products model = dataSnapshot.getValue(Products.class);
                            Intent in = new Intent(ViewProduct.this, Products_Page.class);
                            in.putExtra("Category", model.getCategory());
                            startActivity(in);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });


    }

    private void addingToCartList() {
        try {

            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String saveCurrentDate, saveCurrentTime;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());
            String userid = firebaseUser.getUid();

            final DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference().child("Cart List");
            if (!firebaseUser.getUid().equals(ownerID)) {
                HashMap<String, Object> CartMap = new HashMap<>();
                CartMap.put("pid", id);
                CartMap.put("date", saveCurrentDate);
                CartMap.put("time", saveCurrentTime);
                CartMap.put("category", category.getText().toString());
                CartMap.put("pName", name.getText().toString());
                CartMap.put("pDescription", desc.getText().toString());
                CartMap.put("pPrice", price.getText().toString());

                CartMap.put("pQuantity", quantity.getText().toString());
                databaseref.child("User View").child(userid)
                        .child("Products").child(id)
                        .updateChildren(CartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(ViewProduct.this, "added to cart successfully", Toast.LENGTH_LONG).show();
                              /*  Intent in = new Intent(ViewProduct.this, Products_Page.class);
                                startActivity(in);*/
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "you can't add your products to your cart", Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            Toast.makeText(this, "you are not a user!", Toast.LENGTH_LONG).show();


        }
    }

    private void getProdctDetails(String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products").child("View Of Products");
        ref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Products model = dataSnapshot.getValue(Products.class);
                    ownerID = model.getOwnerID();
                    name.setText(model.getPname());
                    desc.setText("Description: " + model.getPdescription());
                    Picasso.get().load(model.getImage()).into(image);
                    if(model.getCategory().equals("Donations"))
                        price.setText(model.getPprice());
                    else
                        price.setText("Price  " + " $ " +model.getPprice()  );
                    category.setText("Category: " + model.getCategory());
                    // quantity.setText("Quantity: " + 1);
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    toolbar.setTitle(name.getText().toString());

                    //placing toolbar in place of actionbar
                    setSupportActionBar(toolbar);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}

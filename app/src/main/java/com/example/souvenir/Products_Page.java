package com.example.souvenir;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.souvenir.Models.Products;
import com.example.souvenir.Adapter.productViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Products_Page extends AppCompatActivity {

    Button b, searchBtn;
    EditText text;
    ImageView back;
String category;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
String input="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products__page);
category= getIntent().getStringExtra("Category");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        text=(EditText) findViewById(R.id.searchPro);

        // list= (ListView) findViewById(R.id.list);
        back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent( Products_Page.this,Main.class);
                startActivity(in);

            }
        });


        text.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

              // input=s.toString();
               input=text.getText().toString();
               // input=s.toString();

                    onStart();
            }
        });

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child("View Of Products");
        FirebaseRecyclerOptions<Products> options;
        if(input.equals("")){
        options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("category").startAt(category).endAt(category+"\uf8ff"), Products.class)
                        .build();}
        else{
            options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(ProductsRef.orderByChild("pName").startAt(input).endAt(input+"\uf8ff"), Products.class)
                            .build();
        }


        FirebaseRecyclerAdapter<Products, productViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, productViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull productViewHolder holder, int position, @NonNull final Products model)
                    {   holder.txtProductName.setText(model.getPname());
                        //   holder.txtProductDescription.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        if(model.getCategory().equals("Donations"))
                            holder.txtProductPrice.setText(model.getPprice());
                        else
                        holder.txtProductPrice.setText("Price " + "$"+model.getPprice() );
                        if (firebaseUser != null && firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence options[] = new CharSequence[]
                                            {
                                                    "View",
                                                    "Remove"
                                            };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Products_Page.this);
                                    builder.setTitle("Options:");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == 0) {



                                                        Intent in = new Intent(Products_Page.this, ViewProduct.class);
                                                        in.putExtra("pid", model.getPid());
                                                        startActivity(in);

                                            } else if (which == 1) {
                                                String userid = firebaseUser.getUid();
                                                ProductsRef.child(model.getPid()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(Products_Page.this, "Item removed successfully", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }

                                    });
                                    builder.show();

                                }
                            });}
                        else {
                            holder.txtProductName.setText(model.getPname());
                            //   holder.txtProductDescription.setText(model.getDescription());
                            Picasso.get().load(model.getImage()).into(holder.imageView);
                            if(category.equals("Donations"))
                            holder.txtProductPrice.setText(model.getPprice());
                            else
                                holder.txtProductPrice.setText("Price  " +" $" +model.getPprice() );
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent in = new Intent(Products_Page.this, ViewProduct.class);
                                    in.putExtra("pid", model.getPid());
                                    startActivity(in);
                                }
                            });
                        }

                    }

                    @NonNull
                    @Override
                    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        productViewHolder holder = new productViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}

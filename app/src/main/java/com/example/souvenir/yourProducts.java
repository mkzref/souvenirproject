package com.example.souvenir;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.widget.Toolbar;
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

public class yourProducts extends AppCompatActivity {

    Button b, searchBtn;
    EditText text;
    ImageView back;
    String category;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String input="";
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
boolean empty=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_products);
        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle("My Products");

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        try {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        String userid = firebaseUser.getUid();
        category= getIntent().getStringExtra("Category");

        text=(EditText) findViewById(R.id.searchPro);


        back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent( yourProducts.this,Main.class);
                startActivity(in);

            }
        });


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        onStart();
        }
        catch (Exception ex)
        {
            //Toast.makeText(this,"you are not a user!",Toast.LENGTH_LONG).show();

          //  Intent in=new Intent( yourProducts.this,Main.class);
           // startActivity(in);



        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
        String userid = firebaseUser.getUid();
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        reference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Products> options;
        if (userid != null) {


            empty = false;
            options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(ProductsRef.child("View Of Products").orderByChild("ownerID").startAt(userid).endAt(userid + "\uf8ff"), Products.class)
                            .build();
        } else {
            options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(ProductsRef.child("View Of Products").orderByChild("pName").startAt(input).endAt(input + "\uf8ff"), Products.class)
                            .build();
        }


        FirebaseRecyclerAdapter<Products, productViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, productViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull productViewHolder holder, int position, @NonNull final Products model) {
                        String userid = firebaseUser.getUid();

                        empty = false;
                        holder.txtProductName.setText(model.getPname());

                        //   holder.txtProductDescription.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        if(model.getCategory().equals("Donations")){
                        holder.txtProductPrice.setText( model.getPprice() );}
                        else if(model.getCategory().equals("Art Hobbies"))
                            holder.txtProductPrice.setText( model.getPprice() );
                        else
                            holder.txtProductPrice.setText("Price  " +"$"+ model.getPprice());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options [] = new CharSequence[]
                                        {
                                                "View",
                                                "Remove"
                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(yourProducts.this);
                                builder.setTitle("Options:");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0)
                                        {
                                            Intent in = new Intent(yourProducts.this, ViewProduct.class);
                                            in.putExtra("pid", model.getPid());
                                            startActivity(in);
                                        }

                                        else if (which==1)
                                        {  final String userid = firebaseUser.getUid();
                                            ProductsRef.child("Ownership Structure").child(userid).child("View Of Products").child(model.getPid()).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        ProductsRef.child("View Of Products").child(model.getPid()).removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful())
                                                                        {    reference.child("User View")
                                                                                .child(userid).child("Products").child(model.getPid()).removeValue();
                                                                            Toast.makeText(yourProducts.this,"Item removed successfully",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }
                                            });


                                        }
                                    }
                                });
                                builder.show();

                            }
                        });




                    }

                    @NonNull
                    @Override
                    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        if (empty == true) {
                           /* TextView empty=(TextView)findViewById(R.id.empty);
                            empty.setText("You didn't add any product yet!");*/
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty, parent, false);
                            productViewHolder holder = new productViewHolder(view);
                            return holder;

                        } else {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                            productViewHolder holder = new productViewHolder(view);
                            return holder;
                        }
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
        catch (Exception ex)
        {
            Toast.makeText(this,"you are not a user!",Toast.LENGTH_LONG).show();



        }
    }

}

package com.example.souvenir;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.souvenir.Models.Products;
import com.example.souvenir.Models.User;
import com.example.souvenir.Adapter.productViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class OtheruserProfile extends AppCompatActivity {
    ImageView profImage;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    String userid;
    String phone;//lujain edit
    StorageReference storageReference;

    //
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String input="";
    boolean empty=true;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otheruser_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //////////////////lujain edit
        Intent intent=getIntent();//from user adapter
        userid = intent.getStringExtra("userid");
        phone = intent.getStringExtra("userNum");
        //////////////////////////////////

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getApplicationContext(), MessageActivity.class);//when click on any user
                //  intent.putExtra("userid",userid);

                //   intent.putExtra("userNum",phone);///lujain edit

                //   startActivity(intent);
                onBackPressed();
            }
        });

        profImage = findViewById(R.id.imageprof);
        username = findViewById(R.id.username);

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");



        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
///////////////////??
                User user = dataSnapshot.getValue(User.class);//get the user
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profImage.setImageResource(R.drawable.user2);

                } else {
                    ///////////////////////
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profImage);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //

        Start();



    }



    public void Start() {

        try {

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
                            empty = false;
                            holder.txtProductName.setText(model.getPname());
                            Picasso.get().load(model.getImage()).into(holder.imageView);
                            holder.txtProductPrice.setText("Price = " + model.getPprice() + "$");
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent in = new Intent(getApplicationContext(), ViewProduct.class);
                                    in.putExtra("pid", model.getPid());
                                    startActivity(in);

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
            //Toast.makeText(getApplicationContext(),"you are not a user!",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
package com.example.souvenir.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.souvenir.Models.Products;
import com.example.souvenir.Models.User;
import com.example.souvenir.R;
import com.example.souvenir.Adapter.productViewHolder;
import com.example.souvenir.ViewProduct;
import com.example.souvenir.editProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileFragment extends Fragment {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String input = "";
    boolean empty = true;
    ImageView profImage;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    StorageReference storageReference;
    ImageButton Editprofile;
    private static final int image_request = 100;
    private Uri imageuri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        profImage = v.findViewById(R.id.imageprof);
        username = v.findViewById(R.id.username);
//EditProfile
        Editprofile = v.findViewById(R.id.EditProfile);
        Editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity() , editProfile.class);
                startActivity(i);
            }
        });
        recyclerView = v.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//


        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        String userid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    username.setText(user.getUsername());
                    if (!user.getImageURL().equals("default"))
                        if (getContext() != null)
                            Glide.with(getContext()).load(user.getImageURL()).into(profImage);

                }
                catch(Exception e)
                {

                    Log.i("in Profile fragment: ","line151 main");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        Start();

        return v;

    }

    //3 methods to open image to change prof image from gallery
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
            profImage.setImageURI(imageuri);
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Uploading in progress", Toast.LENGTH_SHORT).show();

            } else {
                uploadImage();
            }

        }


    }


    public void Start() {

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
                            if(model.getCategory().equals("Donations"))
                                holder.txtProductPrice.setText( model.getPprice() );
                            else
                                holder.txtProductPrice.setText("Price = " + model.getPprice() + "JD");
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence options[] = new CharSequence[]
                                            {
                                                    "View",
                                                    "Remove"
                                            };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Options:");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == 0) {
                                                Intent in = new Intent(getActivity(), ViewProduct.class);
                                                in.putExtra("pid", model.getPid());
                                                startActivity(in);
                                            } else if (which == 1) {
                                                final String userid = firebaseUser.getUid();
                                                ProductsRef.child("Ownership Structure").child(userid).child("View Of Products").child(model.getPid()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    ProductsRef.child("View Of Products").child(model.getPid()).removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        reference.child("User View")
                                                                                                .child(userid).child("Products").child(model.getPid()).removeValue();
                                                                                        Toast.makeText(getActivity(), "Item removed successfully", Toast.LENGTH_LONG).show();
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
        } catch (Exception ex) {
            //Toast.makeText(getApplicationContext(),"you are not a user!",Toast.LENGTH_LONG).show();

        }
    }

}
package com.example.souvenir.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.souvenir.Models.Categories;
import com.example.souvenir.Products_Page;
import com.example.souvenir.R;
import com.example.souvenir.Adapter.categoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference CategoriesRef;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        CategoriesRef = FirebaseDatabase.getInstance().getReference().child("Categories").child("8CxyfESWn8XgpkqBlpBXLhbBVPl2");
        FirebaseRecyclerOptions<Categories> options;
        options = new FirebaseRecyclerOptions.Builder<Categories>()
                        .setQuery(CategoriesRef, Categories.class)
                        .build();
        recyclerView = root.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        FirebaseRecyclerAdapter<Categories, categoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<Categories, categoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull categoryViewHolder holder, int position, @NonNull final Categories model) {

                        holder.txtCategoryName.setText("  " + model.getName());

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //firebaseUser != null &&  ,, this if it is not here it will crash because of
                        // the second part of the if coz there is no user,when enter as guest
                        if (firebaseUser != null && firebaseUser.getUid().equals("Dpo92swUrEayfmJrtHtbVjVRaNy2")) {
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence options[] = new CharSequence[]
                                            {
                                                    "View",
                                                    "Remove"
                                            };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                                    builder.setTitle("Options:");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == 0) {
                                                Intent in = new Intent(root.getContext(), Products_Page.class);
                                                in.putExtra("Category", model.getName());
                                                startActivity(in);
                                            } else if (which == 1) {
                                                String userid = firebaseUser.getUid();
                                                CategoriesRef.child(model.getCid()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(root.getContext(), "Item removed successfully", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }

                                    });
                                    builder.show();

                                }
                            });

                        } else {
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent in = new Intent(root.getContext(), Products_Page.class);
                                    in.putExtra("Category", model.getName());
                                    startActivity(in);


                                }
                            });
                        }


                    }

                    @NonNull
                    @Override
                    public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw, parent, false);
                        categoryViewHolder holder = new categoryViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        return root;
    }
}
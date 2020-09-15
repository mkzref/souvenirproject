package com.example.souvenir;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.souvenir.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.souvenir.Models.Categories;
import com.example.souvenir.Adapter.categoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class selectCategory extends AppCompatActivity {
    ImageView p, j, b, c, k, f;
    ListView list;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    String[] title;
    String[] desc;
    int[] icon;

    ImageView back;

    private DatabaseReference CategoriesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
//getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      
        //setting the title
        toolbar.setTitle("Select Category");
        try{
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();
        back= (ImageView) findViewById(R.id.back);
        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent( selectCategory.this,Main.class);
                startActivity(in);

            }
        });
        onStart();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"you are not a user!",Toast.LENGTH_LONG).show();
            Intent in = new Intent(selectCategory.this, Main.class);
            startActivity(in);


        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        try {

            CategoriesRef = FirebaseDatabase.getInstance().getReference().child("Categories").child("8CxyfESWn8XgpkqBlpBXLhbBVPl2");
            FirebaseRecyclerOptions<Categories> options;

            options =
                    new FirebaseRecyclerOptions.Builder<Categories>()
                            .setQuery(CategoriesRef, Categories.class)
                            .build();


            FirebaseRecyclerAdapter<Categories, categoryViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Categories, categoryViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull categoryViewHolder holder, int position, @NonNull final Categories model) {

                            holder.txtCategoryName.setText("  " + model.getName());
                            //   holder.txtProductDescription.setText(model.getDescription());
                            Picasso.get().load(model.getImage()).into(holder.imageView);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent in = new Intent(selectCategory.this, addNewProduct.class);
                                    in.putExtra("Category", model.getName());
                                    startActivity(in);

                                }
                            });
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

        } catch (Exception ex) {
            Toast.makeText(this, "you are not a user!", Toast.LENGTH_LONG).show();
            Intent in = new Intent(selectCategory.this, Main.class);
            startActivity(in);


        }
    }


}





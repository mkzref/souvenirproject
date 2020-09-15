package com.example.souvenir;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.souvenir.R;
import com.example.souvenir.Models.cart;
import com.example.souvenir.Adapter.cartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cartList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button next;
    TextView total;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);


        try {
            recyclerView = findViewById(R.id.cartList);
            back = (ImageView) findViewById(R.id.back);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            total = (TextView) findViewById(R.id.cartlist);
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(cartList.this, Main.class);
                    startActivity(in);

                }
            });
        }
        catch (Exception ex)
        {
           // Toast.makeText(this,"you are not a user!",Toast.LENGTH_LONG).show();
          //  Intent in = new Intent(cartList.this, Home.class);
           // startActivity(in);


        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        try {


            String userid = firebaseUser.getUid();
            final DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
            FirebaseRecyclerOptions<cart> options;

            options = new FirebaseRecyclerOptions.Builder<cart>()
                    .setQuery(ProductsRef.child("User View")
                            .child(userid).child("Products"), cart.class).build();


            FirebaseRecyclerAdapter<cart, cartViewHolder> adapter =
                    new FirebaseRecyclerAdapter<cart, cartViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull cartViewHolder holder, int position, @NonNull final cart model) {

                            holder.txtProductName.setText(model.getPname());
                            if(model.getCategory().equals("Donations"))
                                holder.txtProductPrice.setText(model.getPprice());
                            else
                            holder.txtProductPrice.setText(model.getPprice() );

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence options [] = new CharSequence[]
                                            {
                                                    "View",
                                                    "Remove"
                                            };
                                    AlertDialog.Builder builder=new AlertDialog.Builder(cartList.this);
                                    builder.setTitle("Cart Options:");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(which==0)
                                            {
                                                Intent in = new Intent(cartList.this, ViewProduct.class);
                                                in.putExtra("pid", model.getPid());
                                                startActivity(in);
                                            }

                                            else if (which==1)
                                            {
                                                String userid = firebaseUser.getUid();
                                                ProductsRef.child("User View")
                                                        .child(userid).child("Products").child(model.getPid()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
if (task.isSuccessful())
{
    Toast.makeText(cartList.this,"Item removed successfully",Toast.LENGTH_LONG).show();
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
                        public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
                            cartViewHolder holder = new cartViewHolder(view);
                            return holder;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if( firebaseUser != null &&firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.admin, menu);

            return true;}
        else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu, menu);

            return true;}



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCategory:
                if (firebaseUser!=null) {
                    if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                        Intent in1 = new Intent(cartList.this, addNewCategory.class);
                        startActivity(in1);
                    }
                }
                else
                    Toast.makeText(this,"You are not an admin!",Toast.LENGTH_SHORT).show();

                break;



           /* case R.id.menuLogout:
                if (firebaseUser!=null) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(cartList.this, Entry.class));
                    finish();
                }
                else
                    Toast.makeText(this,"You are not a user!",Toast.LENGTH_SHORT).show();
                break;
*/



            case R.id.giveReport:
                if (firebaseUser!=null) {Intent in2 = new Intent(cartList.this, giveReport.class);
                    startActivity(in2);}
                else
                    Toast.makeText(this,"You are not a user!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.suggestCategory:
                if (firebaseUser!=null) {Intent in4 = new Intent(cartList.this, suggestNewCategory.class);
                    startActivity(in4);}
                else
                    Toast.makeText(this,"You are not a user!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.viewReports:
                if (firebaseUser!=null) {
                    if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2"))
                    {Intent in5 = new Intent(cartList.this, ReportsList.class);
                        startActivity(in5);}}
                else
                    Toast.makeText(this,"You are not an admin!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.viewSuggestedCategory:
                if (firebaseUser!=null) {if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                    Intent in6 = new Intent(cartList.this, viewSuggestCategories.class);
                    startActivity(in6);}}
                else
                    Toast.makeText(this,"You are not an admin!",Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

}
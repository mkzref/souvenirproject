package com.example.souvenir;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.example.souvenir.Models.Reports;
import com.example.souvenir.Adapter.reportViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportsList extends AppCompatActivity {
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
        setContentView(R.layout.activity_report_list);
        try {
            recyclerView = findViewById(R.id.reportList);
            back = (ImageView) findViewById(R.id.back);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            total = (TextView) findViewById(R.id.reportlist);

            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();

            if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(ReportsList.this, Main.class);
                    startActivity(in);

                }
            });


        }
            else{
            throw new Exception();
        }

        }
        catch (Exception ex)
        {
           // Toast.makeText(this,"you are not an admin!",Toast.LENGTH_LONG).show();
           // Intent in = new Intent(ReportsList.this, Home.class);
           // startActivity(in);


        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        try {


            String userid = firebaseUser.getUid();
            final DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Reports");
            FirebaseRecyclerOptions<Reports> options;

            options = new FirebaseRecyclerOptions.Builder<Reports>()
                    .setQuery(Ref, Reports.class).build();


            FirebaseRecyclerAdapter<Reports, reportViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Reports,reportViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull reportViewHolder holder, int position, @NonNull final Reports model) {

                            holder.txtSubject.setText(model.getSubject());



                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence options [] = new CharSequence[]
                                            {
                                                    "View",
                                                    "Remove"
                                            };
                                    AlertDialog.Builder builder=new AlertDialog.Builder(ReportsList.this);
                                    builder.setTitle("Report Options:");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(which==0)
                                            {
                                                Intent in = new Intent(ReportsList.this, ViewReport.class);
                                                in.putExtra("ID", model.getID());
                                                in.putExtra("UserID", model.getUserID());
                                                startActivity(in);
                                            }

                                            else if (which==1)
                                            {
                                                String userid = firebaseUser.getUid();
                                                Ref.child(model.getID()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                {
                                                                    Toast.makeText(ReportsList.this,"Item removed successfully",Toast.LENGTH_LONG).show();
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
                        public reportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports, parent, false);
                            reportViewHolder holder = new reportViewHolder(view);
                            return holder;
                        }
                    };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"you are not an admin!",Toast.LENGTH_LONG).show();



        }
    }
}
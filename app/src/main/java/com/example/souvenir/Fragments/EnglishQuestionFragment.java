package com.example.souvenir.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.souvenir.Adapter.HelpAdapter;
import com.example.souvenir.Models.help;
import com.example.souvenir.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnglishQuestionFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    ImageView back;
    String input="";
    EditText text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.english_question_fragment, container, false);
        try {
            text = (EditText) v.findViewById(R.id.searchEQ);
            recyclerView = v.findViewById(R.id.help);
            back = (ImageView) v.findViewById(R.id.back);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);


            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();


            text.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    // input=s.toString();
                    input = text.getText().toString();
                    // input=s.toString();

                    onStart();
                }
            });

        }
        catch (Exception e)
        {


        }
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        try {
            final DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Help").child("English").child("8CxyfESWn8XgpkqBlpBXLhbBVPl2");
            FirebaseRecyclerOptions<help> options;
            if(input.equals("")){

                String userid = firebaseUser.getUid();



                options = new FirebaseRecyclerOptions.Builder<help>()
                        .setQuery(Ref, help.class).build();}
            else{

                options =
                        new FirebaseRecyclerOptions.Builder<help>()
                                .setQuery(Ref.orderByChild("Question").startAt(input).endAt(input+"\uf8ff"), help.class)
                                .build();
            }

            if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                FirebaseRecyclerAdapter<help, HelpAdapter> adapter =
                        new FirebaseRecyclerAdapter<help, HelpAdapter>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull final HelpAdapter holder, int position, @NonNull final help model) {

                                holder.question.setText(model.getQuestion());

                                //holder.answer.setOnClickListener(model.getAnswer());


                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CharSequence options[] = new CharSequence[]
                                                {
                                                        "View",
                                                        "Delete"
                                                };
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Options:");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (which == 0) {

                                                    AlertDialog.Builder builder2=new AlertDialog.Builder(getContext());
                                                    builder2.setTitle(model.getQuestion());

                                                    builder2.setMessage(model.getAnswer());
                                                    builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface arg0, int arg1) {
                                                        }
                                                    }).create().show();

                                                } else if (which == 1) {

                                                    String userid = firebaseUser.getUid();
                                                    Ref.child(model.getID()).removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(getContext(), "Item removed successfully", Toast.LENGTH_LONG).show();
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
                            public HelpAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
                                HelpAdapter holder = new HelpAdapter(view);
                                return holder;
                            }
                        };
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
            else{

                FirebaseRecyclerAdapter<help, HelpAdapter> adapter =
                        new FirebaseRecyclerAdapter<help, HelpAdapter>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull final HelpAdapter holder, int position, @NonNull final help model) {

                                holder.question.setText(model.getQuestion());

                                //holder.answer.setOnClickListener(model.getAnswer());

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                        builder.setTitle(model.getQuestion());

                                        builder.setMessage(model.getAnswer());
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                            }
                                        }).create().show();


                                    }
                                });

                            }

                            @NonNull
                            @Override
                            public HelpAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
                                HelpAdapter holder = new HelpAdapter(view);
                                return holder;
                            }
                        };
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }

        }
        catch (Exception ex)
        {



        }
    }
}

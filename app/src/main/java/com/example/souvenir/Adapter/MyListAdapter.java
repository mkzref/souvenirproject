package com.example.souvenir.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.souvenir.Models.User;
import com.example.souvenir.R;
import com.example.souvenir.allUsersLocations;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.viewholder>{

    private Context mcontext;
    private List<User> listdata;
    // RecyclerView recyclerView;
    public MyListAdapter(Context mcontext , List<User> listdata) {
        this.mcontext = mcontext;
        this.listdata = listdata;
    }



    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        viewholder view = new viewholder(listItem);
        return view;



    }
/*
View view = LayoutInflater.from(mcontext).inflate(R.layout.useritem,parent,false);
        return new Useradapter.viewholder(view);
 */



    @NonNull



    @Override
    public void onBindViewHolder(viewholder holder, int position) {


        final User  user=listdata.get(position);
        holder.textView.setText("  "+user.getUsername());// holder.username.setText(user.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent = new Intent(mcontext, allUsersLocations.class);//when click on any user
                intent.putExtra("userid",user.getId());
              //  Toast.makeText(mcontext ,user.getId() + " " , Toast.LENGTH_LONG ).show();
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                mcontext.startActivity(intent);




            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }// return musers.size();




        public class viewholder extends RecyclerView.ViewHolder{

            public TextView textView;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                this.textView = (TextView) itemView.findViewById(R.id.textView);

            }
        }









}

package com.example.souvenir.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.souvenir.R;
import com.example.souvenir.Models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewholder> {
    private Context mcontext;
    private List<Chat> mChat;
    private String imageURl;
    public  Chat chat;
    FirebaseUser firebaseUser;
    ArrayList<String>ids=new ArrayList<>();
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;


    public MessageAdapter(Context mcontext, List<Chat> mChat, String imageURl) {
        this.mcontext = mcontext;
        this.mChat = mChat;
        this.imageURl = imageURl;

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if(viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.rightchatitem, parent, false);

            return new MessageAdapter.viewholder(view);

        }
        else {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.leftchatitem, parent, false);
            Log.i("in adaptermsg ","line 54");

            return new MessageAdapter.viewholder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
        //to update the RecyclerView.ViewHolder contents with the
        // item at the given position and also sets up some private fields to be used by RecyclerView.
        chat = mChat.get(position);//
        ids.add(chat.getId());
        holder.show_msg.setText(chat.getMessage());
        Log.i("in adaptermsg78",chat.getId()+"");
        if(imageURl.equals("default")) {
            holder.profImage.setImageResource(R.drawable.user3);///
        }
        else {
            Glide.with(mcontext).load(imageURl).into(holder.profImage);
        }

        if(position==mChat.size()-1) {
            if(chat.isIsseen()) {
                holder.txtseen.setText("Seen");
            }
            else {
                holder.txtseen.setText("Delivered");
            }

        }else {

            holder.txtseen.setVisibility(View.GONE);
        }
/////

        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("in adaptermsgpos ",position+"");
                Log.i("in adaptermsg104",chat.getId()+"");
                removeitem(ids.get(position),position);

            }




        });



    }

    private void removeitem(String id,int position)  {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats").child(id);
        ref.removeValue();

    }



    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class viewholder extends RecyclerView.ViewHolder{

        ImageView profImage;
        ImageView trash;
        TextView show_msg;
        TextView txtseen;

        public viewholder(@NonNull final View itemView) {
            super(itemView);
            profImage=itemView.findViewById(R.id.profile_image1);
            trash=itemView.findViewById(R.id.trash);
            show_msg=itemView.findViewById(R.id.show_msgtxt);
            txtseen=itemView.findViewById(R.id.txtseen);


        }
    }///


    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return  MSG_TYPE_RIGHT;
        }
        else{
            return  MSG_TYPE_LEFT;
        }

    }
}

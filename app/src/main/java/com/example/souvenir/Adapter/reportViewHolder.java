package com.example.souvenir.Adapter;



import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.souvenir.Interface.itemClickListener;
import com.example.souvenir.R;

public class reportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtSubject, txtDescription;
    public itemClickListener listner;


    public reportViewHolder(View itemView)
    {
        super(itemView);



        txtSubject = (TextView) itemView.findViewById(R.id.subject);

    }

    public void setItemClickListner(itemClickListener listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
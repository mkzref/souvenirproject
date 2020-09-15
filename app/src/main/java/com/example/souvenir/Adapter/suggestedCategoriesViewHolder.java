package com.example.souvenir.Adapter;



import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.souvenir.Interface.itemClickListener;
import com.example.souvenir.R;

public class suggestedCategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView name;
    public itemClickListener listner;


    public suggestedCategoriesViewHolder(View itemView)
    {
        super(itemView);



       name = (TextView) itemView.findViewById(R.id.sc);

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
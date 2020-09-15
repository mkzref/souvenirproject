package com.example.souvenir.Adapter;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.souvenir.Interface.itemClickListener;
import com.example.souvenir.R;


public class categoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtCategoryName;
    public itemClickListener listner;

    public ImageView imageView;
    public categoryViewHolder(View itemView)
    {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.mainIcon);
        txtCategoryName = (TextView) itemView.findViewById(R.id.mainTitle);

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
package com.example.souvenir.Adapter;



import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.souvenir.Interface.itemClickListener;
import com.example.souvenir.R;


public class cartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice;
        public itemClickListener listner;


    public cartViewHolder(View itemView)
    {
        super(itemView);



        txtProductName = (TextView) itemView.findViewById(R.id.card_p_name);

        txtProductPrice = (TextView) itemView.findViewById(R.id.card_p_price);
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
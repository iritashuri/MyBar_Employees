package com.example.mybaremplyees;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Items extends RecyclerView.Adapter<Adapter_Items.ViewHolder> {
    private ArrayList<Item> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    public lItemClickListener itemClickListener;

    // data is passed into the constructor
    Adapter_Items(Context context, ArrayList<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void setClickListeners(lItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // binds the data to the TextView in each row
        Log.d("pttt", "position = " + position);
        Item item = mData.get(position);
        holder.dealView_TXT_description.setText("" + item.getDescription());
        holder.dealView_TXT_price.setText(item.getPrice() + " Nis");

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    Item getItem(int position) {
        return mData.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dealView_TXT_description;
        TextView dealView_TXT_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dealView_TXT_description = itemView.findViewById(R.id.itemView_TXT_description);
            dealView_TXT_price = itemView.findViewById(R.id.itemView_TXT_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.itemClicked(getItem(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }
    }
    public interface lItemClickListener {
        void itemClicked(Item item, int position);
    }
}

package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
    OnItemClickListener onItemClickListener;
    private ArrayList<tips> dataList; // Replace YourDataModel with the actual data model you are using
    private Context context ;

    // Constructor to initialize the adapter with data
    public adapter(ArrayList<tips> dataList, Context context) {
        this.context = context;
        this.dataList = dataList;
    }

    // onCreateViewHolder is responsible for inflating the item layout and creating the ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tips, parent, false);
        return new ViewHolder(view);
    }

    // onBindViewHolder is responsible for binding the data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tips model = dataList.get(position);
        Picasso.get().load(model.getGambar()).placeholder(R.drawable.gambar).into(holder.itemgambar);
        holder.textView.setText(model.getJudul());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, listimage.class);
                intent.putExtra("singleImage",model.getGambar());
                intent.putExtra("singlejudul", model.getJudul());
                intent.putExtra("singleisi", model.getIsi());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    // getItemCount returns the number of items in the data set
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // ViewHolder class holds the views for each list item
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView; // Replace with the actual views you have in your item layout
        ImageView itemgambar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.judul);
            itemgambar = itemView.findViewById(R.id.imageitem);
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public  interface  OnItemClickListener{
        void onClick(Audio audio);
    }
}



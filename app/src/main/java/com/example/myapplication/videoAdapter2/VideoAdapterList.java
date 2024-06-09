package com.example.myapplication.videoAdapter2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.Video;

import java.util.ArrayList;

public class VideoAdapterList extends RecyclerView.Adapter<VideoAdapterList.ViewHolder>{
    Context context;
    ArrayList<Video> arrayList;
    OnItemClickListener onItemClickListener;

    public VideoAdapterList(Context context, ArrayList<Video> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_list2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(arrayList.get(position).geturl()).into(holder.imageView);
        holder.title.setText(arrayList.get(position).getjudul());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(arrayList.get(position));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  static  class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_item_image);
            title = itemView.findViewById(R.id.judul);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public  interface  OnItemClickListener{
         void onClick(Video video);
    }
}

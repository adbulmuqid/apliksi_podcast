package com.example.myapplication.AdapterAudio2;

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
import com.example.myapplication.Audio;
import com.example.myapplication.R;

import java.util.ArrayList;

public class AudioAdapter2 extends RecyclerView.Adapter<AudioAdapter2.ViewHolder>{
    Context context;
    ArrayList<Audio> arrayList;
    OnItemClickListener onItemClickListener;

    public AudioAdapter2(Context context, ArrayList<Audio> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AudioAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_list2, parent, false);
        return new ViewHolder(view);
    }



    public void onBindViewHolder(@NonNull AudioAdapter2.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (arrayList.get(position).getGambarurl() != null && !arrayList.get(position).geturl().isEmpty()) {
            Glide.with(context).load(arrayList.get(position).geturl()).into(holder.imageView);
        } else {
            // Load default image if URL is empty or null
            holder.imageView.setImageResource(R.drawable.gambar); // Replace 'default_image' with your default image resource
        }
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

    public void setData(ArrayList<Audio> audios) {
        this.arrayList = audios;
        notifyDataSetChanged();
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
        void onClick(Audio audio);
    }
}

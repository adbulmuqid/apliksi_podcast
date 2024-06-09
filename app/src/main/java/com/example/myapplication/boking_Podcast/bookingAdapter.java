package com.example.myapplication.boking_Podcast;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class bookingAdapter extends RecyclerView.Adapter<bookingAdapter.UserViewHolder> {

    private Context context;
    private List<Data> bookinglist;

    public bookingAdapter(Context context, List<Data> bookinglist) {
        this.context = context;
        this.bookinglist = bookinglist;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_booking, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Data booking = bookinglist.get(position);

        // Set data ke TextView atau elemen UI lainnya
        holder.textNama.setText("Nama Peminjam : "+booking.getNama());
        holder.textno_hp.setText("Nomor HP Peminjam : "+booking.getNomorHP());
        holder.texttanggal.setText("Tanggal Booking : "+booking.getDate());
        holder.textwaktu.setText("Waktu Booking : "+booking.getTime());
        holder.textdurasi.setText("Durasi Booking : "+booking.getDurasi());
    }

    @Override
    public int getItemCount() {
        return bookinglist.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textNama, textno_hp, texttanggal, textwaktu, textdurasi;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textNama = itemView.findViewById(R.id.textNama);
            textno_hp = itemView.findViewById(R.id.textNo_hp);
            texttanggal = itemView.findViewById(R.id.textTanggal);
            textwaktu = itemView.findViewById(R.id.textwaktu);
            textdurasi = itemView.findViewById(R.id.textdurasi);

            // Inisialisasi elemen UI lainnya jika diperlukan
        }
    }
}

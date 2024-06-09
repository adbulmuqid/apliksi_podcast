package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.myapplication.boking_Podcast.booking;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfilFragment extends Fragment {


    private EditText editTextNama, editTextEmail, editTextTelepon;
    private Button btnSimpan;
    ImageView imageView;
    FloatingActionButton button;
    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_LOGIN_STATUS = "loginStatus";


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        imageView = view.findViewById(R.id.imageView10);
        Toolbar toolbar;
        toolbar = view.findViewById(R.id.toolbar);
        DocumentReference dbReff = db.collection("Account").document(auth.getUid());
        editTextNama = view.findViewById(R.id.editTextNama);
        editTextEmail = view.findViewById(R.id.editTextTextEmail);
        editTextTelepon = view.findViewById(R.id.editTextTelepon);
        btnSimpan= view.findViewById(R.id.btnSimpan);
        //editTextEmail.setClickable(false);




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProfilFragment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        User data = new User();
        dbReff.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Mengisi objek User dengan data dari Firestore
                        data.setNama(documentSnapshot.getString("Nama"));
                        data.setEmail(documentSnapshot.getString("Email"));
                        data.setTelepon(documentSnapshot.getString("Telepon"));
                        Log.d("DEBUG", "Nama dari Firestore: " + data.getNama());
                        Log.d("DEBUG", "Email dari Firestore: " + data.getEmail());
                        Log.d("DEBUG", "Telepon dari Firestore: " + data.getTelepon());

                        // Menetapkan nilai-nilai ke EditText
                        editTextNama.setText(data.getNama());
                        editTextEmail.setText(data.getEmail());
                        editTextTelepon.setText(data.getTelepon());

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", data.getNama());
                        editor.putString("nohp", data.getTelepon());
                        editor.apply();

                    } else {
                        // Dokumen tidak ditemukan, menggunakan email dari FirebaseAuth
                        editTextEmail.setText(auth.getCurrentUser().getEmail());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Terjadi kesalahan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });




        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                auth.signOut();
                SharedPreferences preferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_LOGIN_STATUS, false);
                editor.apply();
                Intent intent = new Intent(view.getContext(), login.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nama = editTextNama.getText().toString().trim();
                String Email = editTextEmail.getText().toString().trim();
                String Telepon = editTextTelepon.getText().toString().trim();

                if (validateInputs(Nama, Email, Telepon)) {
                    Toast.makeText(getActivity(), "Lengkapi semua data terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> user = new HashMap<>();
                    user.put("Nama", Nama);
                    user.put("Email", Email);
                    user.put("Telepon", Telepon);

                    DocumentReference dbReff = db.collection("Account").document(auth.getUid());



                    dbReff.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Terjadi kesalahan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                if(validateInputs(Nama, Email, Telepon)){

                }
            }
        });


        return view;
    }

    private boolean validateInputs(@NonNull String Nama, String Email, String Telepon){
        if (Nama.isEmpty()) {
            editTextNama.setError("Name required");
            editTextNama.requestFocus();
            return true;
        }
        if (Email.isEmpty()){
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
            return true;
        }
        if (Telepon.isEmpty()){
            editTextTelepon.setError("Telepon required");
            editTextTelepon.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
        }
    }
}

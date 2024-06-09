package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendengarProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendengarProfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnLogin,btnSigup;
    private Toolbar toolbar;

    private EditText editTextNama, editTextEmail, editTextTelepon;
    private Button btnSimpan;

    public PendengarProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendengarProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PendengarProfilFragment newInstance(String param1, String param2) {
        PendengarProfilFragment fragment = new PendengarProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'
        View view = inflater.inflate(R.layout.fragment_pendengar_profil, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
       // toolbar = view.findViewById(R.id.toolbar);
      //  DocumentReference dbReff = db.collection("Account").document(auth.getUid());


        editTextNama = view.findViewById(R.id.editTextNama);
        editTextEmail = view.findViewById(R.id.editTextTextEmail);
        editTextTelepon = view.findViewById(R.id.editTextTelepon);
        btnSimpan= view.findViewById(R.id.btnSimpan);

        btnLogin = view.findViewById(R.id.btn_log);
        btnSigup = view.findViewById(R.id.btn_sigup);
        //editTextEmail.setClickable(false);
//        dbReff.get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        editTextNama.setText(documentSnapshot.getString("Nama"));
//                        editTextEmail.setText(documentSnapshot.getString("Email"));
//                        editTextTelepon.setText(documentSnapshot.getString("Telepon"));
//                    } else {
//                        editTextEmail.setText(auth.getCurrentUser().getEmail());
//                    }{
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(getActivity(), "Terjadi kesalahan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });

//        toolbar.setOnMenuItemClickListener(item -> {
//                Intent intent = new Intent(view.getContext(), login.class);
//                startActivity(intent);
//                return true;
//        });

        btnSigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), signup.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), login.class);
                startActivity(intent);
            }
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
}
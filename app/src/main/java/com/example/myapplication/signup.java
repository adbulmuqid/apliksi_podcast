package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Spinner spLevel;
    private Button btnRegister;
    private CheckBox checkBoxShowPassword,checkBoxShowPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.txt_Email);
        etPassword = findViewById(R.id.txt_password);
        etConfirmPassword = findViewById(R.id.txt_konfirmasi_password);
        spLevel = findViewById(R.id.sp_level);
        btnRegister = findViewById(R.id.btnSignup);
        checkBoxShowPassword = findViewById(R.id.checkBox1);
        checkBoxShowPassword2 = findViewById(R.id.checkBox2);

        // Set data ke spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLevel.setAdapter(adapter);

        // ...
        checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set visibilitas teks pada EditText berdasarkan kondisi CheckBox
                if (isChecked) {
                    etPassword.setTransformationMethod(null);
                   // etConfirmPassword.setTransformationMethod(null);// Menampilkan teks biasa
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                   // etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());// Menampilkan sebagai password
                }
            }
        });
        checkBoxShowPassword2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set visibilitas teks pada EditText berdasarkan kondisi CheckBox
                if (isChecked) {
                    etConfirmPassword.setTransformationMethod(null);// Menampilkan teks biasa
                } else {
                    etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());// Menampilkan sebagai password
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                String level = spLevel.getSelectedItem().toString();

                // Cek validitas input
                if (username.isEmpty()) {
                    Toast.makeText(signup.this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(signup.this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(signup.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register pengguna
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(signup.this, task -> {
                            if (task.isSuccessful()) {
                                // Registrasi berhasil
                                Toast.makeText(signup.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();

                                // Kirim verifikasi email
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    firebaseUser.sendEmailVerification()
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    // Verifikasi email berhasil dikirim
                                                    Toast.makeText(signup.this, "Verifikasi email berhasil dikirim!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Verifikasi email gagal dikirim
                                                    Toast.makeText(signup.this, "Gagal mengirim verifikasi email. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                // Simpan level ke database Firestore
                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                                // Data yang akan disimpan di Firestore
                                Map<String, Object> user = new HashMap<>();
                                user.put("login_level", level);

                                // Menentukan path koleksi dan dokumen
                                String userId = firebaseUser.getUid();
                                String path = "users/" + userId;

                                // Menyimpan data ke Firestore
                                firebaseFirestore.document(path)
                                        .set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            // Sukses menyimpan data ke Firestore
                                            Toast.makeText(signup.this, "Data disimpan di Firestore", Toast.LENGTH_SHORT).show();

                                            // Kembali ke halaman login
                                            Intent intent = new Intent(signup.this, login.class);
                                            startActivity(intent);
                                        })
                                        .addOnFailureListener(e -> {
                                            // Gagal menyimpan data ke Firestore
                                            Toast.makeText(signup.this, "Gagal menyimpan data ke Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // Registrasi gagal
                                Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }
}

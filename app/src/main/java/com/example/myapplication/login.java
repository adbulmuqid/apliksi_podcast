package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputBinding;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin,btnSigup;
    private TextView forgotPassword;
    private FirebaseAuth firebaseAuth;
    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_LOGIN_STATUS = "loginStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.txt_username);
        etPassword = findViewById(R.id.txt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnSigup = findViewById(R.id.btn_sign_up);
        forgotPassword = findViewById(R.id.forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();
        CheckBox checkBoxShowPassword = findViewById(R.id.checkBox);


        btnSigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                View dialogView = getLayoutInflater().inflate(R.layout.activity_lupapassword, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(login.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(login.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(login.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

        // ...
        checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set visibilitas teks pada EditText berdasarkan kondisi CheckBox
                if (isChecked) {
                    etPassword.setTransformationMethod(null);  // Menampilkan teks biasa
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());  // Menampilkan sebagai password
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // Cek validitas input
                if (username.isEmpty()) {
                    Toast.makeText(login.this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(login.this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Login pengguna
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(login.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                                    // Email sudah diverifikasi
                                    Toast.makeText(login.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                                    // Dapatkan level pengguna dari Firestore
                                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                    String userId = firebaseUser.getUid();

                                    firebaseFirestore.collection("users").document(userId)
                                            .get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {
                                                    // Dokumen ditemukan, ambil level
                                                    String level = documentSnapshot.getString("login_level");
                                                    SharedPreferences preferences = login.this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();

                                                    // Periksa status sebelumnya, default value adalah false jika belum ada
                                                    boolean previousStatus = preferences.getBoolean(KEY_LOGIN_STATUS, false);

                                                    // Simpan status login ke SharedPreferences
                                                    editor.putBoolean(KEY_LOGIN_STATUS, true);
                                                    editor.apply();


                                                    // Buka homepage sesuai level
                                                    if (!previousStatus) {
                                                        if ("Pembicara".equals(level)) {
                                                            Intent intent = new Intent(login.this, home_pembicara.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Intent intent = new Intent(login.this, home_pendengar.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                } else {
                                                    // Dokumen tidak ditemukan
                                                    Toast.makeText(login.this, "Dokumen tidak ditemukan di Firestore", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                // Gagal mengambil data dari Firestore
                                                Toast.makeText(login.this, "Gagal mengambil data dari Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });

                                } else {
                                    // Email belum diverifikasi
                                    Toast.makeText(login.this, "Email belum diverifikasi. Silakan cek email Anda.", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut(); // Log out pengguna agar tidak masuk ke aplikasi sebelum verifikasi email
                                }
                            } else {
                                // Login gagal
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        checkCurrentUser();
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()){
            startActivity(new Intent(login.this, home_pembicara.class));
            finish();
        }
    }
}
package com.example.myapplication;
public class User {
    private String nama;
    private String email;
    private String telepon;

    // Buat constructor kosong untuk deserialisasi Firestore
    public User() {
    }

    // Getter dan setter

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }
}

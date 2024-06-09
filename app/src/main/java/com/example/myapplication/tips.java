package com.example.myapplication;

public class tips {
    private  String judul, isi;
    private  String gambar;

    public tips() {
    }

    public tips(String judul, String isi, String gambar) {
        this.judul = judul;
        this.isi = isi;
        this.gambar = gambar;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}

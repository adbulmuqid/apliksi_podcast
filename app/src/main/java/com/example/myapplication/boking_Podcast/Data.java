package com.example.myapplication.boking_Podcast;

public class Data {
    String nama, nomorHP, durasi, date, time;
    public Data(String nama, String nomorHP, String date, String time, String durasi){
        this.nama =nama;
        this.nomorHP = nomorHP;
        this.date = date;
        this.time = time;
        this.durasi = durasi;
    }

    public String getNomorHP() {
        return nomorHP;
    }

    public String getNama() {
        return nama;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNomorHP(String nomorHP) {
        this.nomorHP = nomorHP;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }
}

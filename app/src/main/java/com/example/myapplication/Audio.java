package com.example.myapplication;
public class Audio {
    private String judul, url, gambarurl;
    private int penonton;
    public Audio() {
        // Default constructor required for calls to DataSnapshot.getValue(Video.class)
    }

    public Audio(String judul, String url, int penonton, String gambarurl) {
        this.judul = judul;
        this.url = url;
        this.penonton = penonton;
        this.gambarurl = gambarurl;
    }

    public String getGambarurl() {
        return gambarurl;
    }

    public void setGambarurl(String gambarurl) {
        this.gambarurl = gambarurl;
    }

    public String getjudul() {
        return judul;
    }

    public void setjudul(String judul) {
        this.judul = judul;
    }

    public int getPenonton() {
        return penonton;
    }

    public void setPenonton(int penonton) {
        this.penonton = penonton;
    }

    public String geturl() {
        return url;
    }

    public String seturl(String url) {
        this.url= url;
        return  url;
    }
}


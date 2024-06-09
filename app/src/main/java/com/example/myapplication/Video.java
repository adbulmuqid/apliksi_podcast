package com.example.myapplication;

public class Video {
    String judul, url;
    private int penonton;
    public Video() {
        // Default constructor required for calls to DataSnapshot.getValue(Video.class)
    }

    public Video(String judul, String url, int penonton) {
        this.judul = judul;
        this.url = url;
        this.penonton = penonton;
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

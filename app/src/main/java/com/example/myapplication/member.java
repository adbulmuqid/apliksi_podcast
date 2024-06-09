package com.example.myapplication;

public class member {
    private String VideoName;
    private String VideoUrl;

    public member(String nama, String url){
        if ((nama.trim().equals(""))){
            nama = "tidak boleh kosong";
        }
        VideoName = nama;
        VideoUrl=url;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }
}

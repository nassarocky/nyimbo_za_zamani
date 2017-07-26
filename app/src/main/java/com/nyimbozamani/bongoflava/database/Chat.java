package com.nyimbozamani.nyimbozazamani.database;

public class Chat {
    private String duration;
    private String name;
    private String artist;
    private String downloadUrl;


    public Chat() {
        // Needed for Firebase
    }

    public Chat(String namee, String durationn, String direct,String artistt) {
        name = namee;
        duration = durationn;
        artist=artistt;
        downloadUrl = direct;
    }

    public String getName() {
        return name;
    }

    public void setName(String namee) {
        name = namee;
    }

    public String getDownloadUrl() {
        return downloadUrl ;
    }
public void setDownloadUrl(String direct){
    downloadUrl=direct;
}
    public void setArtist(String artistss) {
        artist = artistss;
    }
public String getArtist(){
    return  artist;
}
public String getDuration(){
    return duration;
}

public  void setDuration(String durations){
    duration=durations;
}


}

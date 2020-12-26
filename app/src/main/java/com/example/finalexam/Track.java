package com.example.finalexam;

import com.google.firebase.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Track implements Serializable {
    String trackTitle, albumTitle, albumCoverImageMedium, artistName;
    long id;
    String trackDuration, mp3Link;
    Timestamp timestamp;

    public Track(JSONObject jsonObject, String albumTitles, String albumCoverImageMedium) throws JSONException{
        this.trackTitle = jsonObject.getString("title_short");
        long min = (jsonObject.getInt("duration"))/60;
        int sec = (jsonObject.getInt("duration"))%60;
        this.trackDuration = min+":"+sec;
        this.mp3Link = jsonObject.getString("preview");
        this.id = jsonObject.getLong("id");
        this.artistName = jsonObject.getJSONObject("artist").getString("name");
        this.albumTitle = albumTitles;
        this.albumCoverImageMedium = albumCoverImageMedium;

    }

    public Track() {
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackTitle='" + trackTitle + '\'' +
                ", trackDuration='" + trackDuration + '\'' +
                ", mp3Link='" + mp3Link + '\'' +
                '}';
    }
}

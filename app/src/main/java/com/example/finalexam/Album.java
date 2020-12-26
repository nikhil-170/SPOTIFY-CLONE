package com.example.finalexam;

import com.google.firebase.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Album implements Serializable {
    long id,nb_tracks;
    String albumTitles, artisName, alblumCoverImageMedium, artistCoverImageSmall,
            albumCoverImageBig, trackDetalis, sharedUserName;
    Timestamp timestamp;


    public  Album(JSONObject jsonObject)throws JSONException{
        this.id = jsonObject.getLong("id");
        this.albumTitles = jsonObject.getString("title");
        this.artisName = jsonObject.getJSONObject("artist").getString("name");
        this.nb_tracks = jsonObject.getLong("nb_tracks");
        this.alblumCoverImageMedium = jsonObject.getString("cover_medium");
        this.artistCoverImageSmall = jsonObject.getJSONObject("artist").getString("picture_small");
        this.albumCoverImageBig = jsonObject.getString("cover_big");
        this.trackDetalis = jsonObject.getString("tracklist");


    }

    public Album(long id, long nb_tracks, String albumTitles, String artisName, String alblumCoverImageMedium, String artistCoverImageSmall) {
        this.id = id;
        this.nb_tracks = nb_tracks;
        this.albumTitles = albumTitles;
        this.artisName = artisName;
        this.alblumCoverImageMedium = alblumCoverImageMedium;
        this.artistCoverImageSmall = artistCoverImageSmall;
    }

    public Album() {
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", nb_tracks=" + nb_tracks +
                ", albumTitles='" + albumTitles + '\'' +
                ", artisName='" + artisName + '\'' +
                ", alblumCoverImage='" + alblumCoverImageMedium + '\'' +
                ", artistCoverImage='" + artistCoverImageSmall + '\'' +
                '}';
    }
}

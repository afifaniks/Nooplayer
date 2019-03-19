package com.example.nooplayer.entity;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * @author: Afif Al Mamun
 * @created_in: 3/18/19
 * @project_name: Nooplayer
 **/
public class Track implements Comparable<Track>{
    private String trackName;
    private String albumName;
    private String artistName;
    private String path;
   // private Bitmap albumArt;
//    private String genre;
//    private String duration;

    // TODO get image


    public Track(String trackName, String albumName, String artistName, String path) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.artistName = artistName;
        this.path = path;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(Track o) {
        return this.trackName.compareTo(o.trackName);
    }
}

package com.example.nooplayer.entity;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

/**
 * @author: Afif Al Mamun
 * @created_in: 3/18/19
 * @project_name: Nooplayer
 **/
public class Track implements Comparable<Track>, Serializable {
    private String trackName;
    private String albumName;
    private String artistName;
    private String genre;
    private String path;
    private String composerName;
    private String duration;
    private String year;

    public Track(String trackName, String albumName, String artistName, String genre, String path, String composerName, String duration, String year) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.artistName = artistName;
        this.genre = genre;
        this.path = path;
        this.composerName = composerName;
        this.duration = duration;
        this.year = year;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComposerName() {
        return composerName;
    }

    public void setComposerName(String composerName) {
        this.composerName = composerName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public int compareTo(Track o) {
        return this.trackName.compareTo(o.trackName);
    }
}

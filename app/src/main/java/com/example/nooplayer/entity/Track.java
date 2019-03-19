package com.example.nooplayer.entity;

/**
 * @author: Afif Al Mamun
 * @created_in: 3/18/19
 * @project_name: Nooplayer
 **/
public class Track {
    private String trackName;
    private String albumName;
    private String artistName;
    private String path;
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
}

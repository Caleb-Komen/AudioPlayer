package com.audioplay.musica.models;

public class Album {
    long id;
    int numberOfTracks;
    String albumName;

    public Album(long id, int numberOfTracks, String albumName) {
        this.id = id;
        this.numberOfTracks = numberOfTracks;
        this.albumName = albumName;
    }

    public long getId() {
        return id;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public String getAlbumName() {
        return albumName;
    }
}

package com.audioplay.musica.models;

import android.graphics.Bitmap;

public class Album {
    private long id;
    private int numberOfTracks;
    private String albumName;

    private Bitmap bitmap;

    public Album(long id, int numberOfTracks, String albumName) {
        this.id = id;
        this.numberOfTracks = numberOfTracks;
        this.albumName = albumName;
    }

    public Album(long id, int numberOfTracks, String albumName, Bitmap bitmap) {
        this.id = id;
        this.numberOfTracks = numberOfTracks;
        this.albumName = albumName;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
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

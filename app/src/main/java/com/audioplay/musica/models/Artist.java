package com.audioplay.musica.models;

import android.graphics.Bitmap;

public class Artist {

    long id;
    int numberOfTracks;
    String artistName;
    Bitmap artistCoverArt;

    public Artist(long id, int numberOfTracks, String artistName) {
        this.id = id;
        this.numberOfTracks = numberOfTracks;
        this.artistName = artistName;
    }

    public Artist(long id, int numberOfTracks, String artistName, Bitmap artistCoverArt) {
        this.id = id;
        this.numberOfTracks = numberOfTracks;
        this.artistName = artistName;
        this.artistCoverArt = artistCoverArt;
    }

    public long getId() {
        return id;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public String getArtistName() {
        return artistName;
    }

    public Bitmap getArtistCoverArt() {
        return artistCoverArt;
    }
}

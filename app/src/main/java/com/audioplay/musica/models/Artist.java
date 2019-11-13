package com.audioplay.musica.models;

public class Artist {

    long id;
    int numberOfTracks;
    String artistName;

    public Artist(long id, int numberOfTracks, String artistName) {
        this.id = id;
        this.numberOfTracks = numberOfTracks;
        this.artistName = artistName;
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
}

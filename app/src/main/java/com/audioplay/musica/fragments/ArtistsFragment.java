package com.audioplay.musica.fragments;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.audioplay.musica.R;
import com.audioplay.musica.adapters.ArtistAdapter;
import com.audioplay.musica.models.Artist;
import com.audioplay.musica.models.Song;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistsFragment extends Fragment {

    private RecyclerView rvArtistsList;
    private ArtistAdapter artistAdapter;
    private List<Artist> artists = new ArrayList<>();

    public ArtistsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        rvArtistsList = view.findViewById(R.id.rv_artists_list);
        getArtists();
        artistAdapter = new ArtistAdapter(getActivity(), artists);

        //prepareArtistsData();

        rvArtistsList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvArtistsList.setHasFixedSize(true);
        rvArtistsList.setAdapter(artistAdapter);

        return view;
    }

    private void getArtists() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri artistsUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(artistsUri, null, null, null, null);

        Uri coverArtUri = Uri.parse("content://media/external/audio/albumart");
        if (cursor != null && cursor.moveToFirst()){
            do {

                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                int numberOfTracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));

                artists.add(new Artist(id, numberOfTracks, artistName));

            } while (cursor.moveToNext());

            cursor.close();
        }


    }

    private void prepareArtistsData() {
        artists.add(new Artist(1, 3, "James Blunt"));
        artists.add(new Artist(2, 1, "Future Island"));
        artists.add(new Artist(3, 8, "Maroon 5"));
        artists.add(new Artist(4, 4, "Don Williams"));
        artists.add(new Artist(5, 1, "Kenny Rodgers"));
        artists.add(new Artist(6, 2, "Lee Brice"));
        artists.add(new Artist(7, 2, "Alan Jackson"));
        artists.add(new Artist(8, 11, "Unknown"));
    }

}

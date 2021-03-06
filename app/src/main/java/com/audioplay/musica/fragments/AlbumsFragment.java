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
import com.audioplay.musica.adapters.AlbumAdapter;
import com.audioplay.musica.models.Album;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsFragment extends Fragment {

    private RecyclerView rvAlbumsList;
    private AlbumAdapter albumAdapter;
    private List<Album> albums = new ArrayList<>();

    public AlbumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        rvAlbumsList = view.findViewById(R.id.rv_albums_list);
        getAlbums();
        albumAdapter = new AlbumAdapter(getActivity(), albums);

        rvAlbumsList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvAlbumsList.setHasFixedSize(true);
        rvAlbumsList.setAdapter(albumAdapter);
        return view;
    }

    private void getAlbums() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri albumsUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(albumsUri, null, null, null, null);

        Uri coverArtUri = Uri.parse("content://media/external/audio/albumart");
        if (cursor != null && cursor.moveToFirst()){
            do {
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                int numberOfTracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));

                Uri uri = ContentUris.withAppendedId(coverArtUri, id);
                Bitmap coverArt = null;
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                    coverArt = BitmapFactory.decodeStream(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                albums.add(new Album(id, numberOfTracks, albumName, coverArt));

            } while (cursor.moveToNext());

            cursor.close();
        }
    }

}

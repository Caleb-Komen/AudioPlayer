package com.audioplay.musica.fragments;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.audioplay.musica.R;
import com.audioplay.musica.adapters.SongAdapter;
import com.audioplay.musica.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongsFragment extends Fragment {

    private List<Song> songs = new ArrayList<>();

    public SongsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        RecyclerView recyclerViewSongs = view.findViewById(R.id.songs_list);

        getSongs();

        SongAdapter songAdapter = new SongAdapter(getActivity(), songs);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSongs.setHasFixedSize(true);
        recyclerViewSongs.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewSongs.setAdapter(songAdapter);

        songAdapter.SetOnMoreButtonClickListener(new SongAdapter.OnMoreButtonClickListener() {
            @Override
            public void onMoreButtonClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_add_to_favourites:
                        Toast.makeText(getActivity(), "Added to favourites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_add_to_playlist:
                        Toast.makeText(getActivity(), "Added to playlist", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_delete:
                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_details:
                        Toast.makeText(getActivity(), "Details", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_set_as_ringtone:
                        Toast.makeText(getActivity(), "Set as ringtone", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_share:
                        Toast.makeText(getActivity(), "Share", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return view;
    }

    private void getSongs() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cusor = musicResolver.query(musicUri, null, null, null, null);

        if (cusor != null && cusor.moveToFirst()){
            while (cusor.moveToNext()){

                long songId = cusor.getLong(cusor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cusor.getString(cusor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cusor.getString(cusor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cusor.getString(cusor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

                songs.add(new Song(songId, title, artist, album));
            }
        }

        cusor.close();
    }

}

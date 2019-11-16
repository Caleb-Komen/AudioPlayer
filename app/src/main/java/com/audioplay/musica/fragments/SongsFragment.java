package com.audioplay.musica.fragments;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.audioplay.musica.services.MusicService;
import com.audioplay.musica.services.MusicService.MusicBinder;
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
    private boolean isBound = false;
    private MusicService musicService;
    private Intent playbackIntent;

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

        songAdapter.setOnMoreButtonClickListener(new SongAdapter.OnMoreButtonClickListener() {
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

        songAdapter.setOnRecyclerListClickListener(new SongAdapter.OnRecyclerListClickListener() {
            @Override
            public void onRecyclerListClick(int position) {
                musicService.setCurrentSong(position);
                musicService.playSong();
            }
        });
        return view;
    }

    private void getSongs() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(musicUri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()){
            while (cursor.moveToNext()){

                long songId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

                songs.add(new Song(songId, title, artist, album));
            }
        }

        cursor.close();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicBinder musicBinder = (MusicBinder) iBinder;
            musicService = musicBinder.getService();
            musicService.setSongsList(songs);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        if (playbackIntent == null) {
            playbackIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playbackIntent, serviceConnection, getActivity().BIND_AUTO_CREATE);
            getActivity().startService(playbackIntent);
        }
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(playbackIntent);
        musicService = null;
        super.onDestroy();
    }
}

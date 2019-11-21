package com.audioplay.musica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.audioplay.musica.R;
import com.audioplay.musica.models.Album;
import com.bumptech.glide.Glide;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private Context context;
    private List<Album> albums;

    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {

        Album album = albums.get(position);

        holder.albumName.setText(album.getAlbumName());
        holder.numberOfTracks.setText(String.valueOf(album.getNumberOfTracks()));

        if (album.getBitmap() != null) {
            Glide.with(context)
                    .load(album.getBitmap())
                    .into(holder.coverArt);
        }else {
            Glide.with(context)
                    .load(R.drawable.ic_headset)
                    .into(holder.coverArt);
        }

    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView albumName, numberOfTracks;
        ImageView coverArt;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            albumName = itemView.findViewById(R.id.tv_album_name);
            numberOfTracks = itemView.findViewById(R.id.tv_no_of_songs);
            coverArt = itemView.findViewById(R.id.album_cover_art);
        }
    }
}

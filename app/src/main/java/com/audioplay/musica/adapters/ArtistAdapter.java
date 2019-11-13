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
import com.audioplay.musica.models.Artist;
import com.bumptech.glide.Glide;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private Context context;
    private List<Artist> artists;

    public ArtistAdapter(Context context, List<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {

        Artist artist = artists.get(position);

        holder.artistName.setText(artist.getArtistName());
        holder.numberOfTracks.setText(String.valueOf(artist.getNumberOfTracks()));
        Glide.with(context)
                .load(R.drawable.musica)
                .into(holder.coverPhoto);

    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistName, numberOfTracks;
        ImageView coverPhoto;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.tv_artist_name);
            numberOfTracks = itemView.findViewById(R.id.tv_no_of_songs);
            coverPhoto = itemView.findViewById(R.id.artist_cover_photo);
        }
    }
}

package com.audioplay.musica.adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.audioplay.musica.R;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context context;
    private Cursor cursor;
    private OnMoreButtonClickListener onMoreButtonClickListener;
    private OnRecyclerListClickListener onRecyclerListClickListener;
    private int songIdPostion;
    private int titlePosition;
    private int artistPosition;
    private int albumPosition;

    public void swapCursor(Cursor cursor){
        this.cursor = cursor;

        populateColumnPosition();
    }

    private void populateColumnPosition() {

        if (cursor == null)
            return;

        songIdPostion = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
        titlePosition = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        artistPosition = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        albumPosition = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
    }

    public SongAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;

        populateColumnPosition();
        notifyDataSetChanged();
    }

    public interface OnMoreButtonClickListener{
        void onMoreButtonClick(MenuItem menuItem);
    }

    public void setOnMoreButtonClickListener(OnMoreButtonClickListener onMoreButtonClickListener){
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public interface OnRecyclerListClickListener{
        void onRecyclerListClick(int position);
    }

    public void setOnRecyclerListClickListener (OnRecyclerListClickListener onRecyclerListClickListener){
        this.onRecyclerListClickListener = onRecyclerListClickListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sound_track, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

        cursor.moveToPosition(position);
        String title = cursor.getString(titlePosition);
        String artist = cursor.getString(artistPosition);

        holder.songTitle.setText(title);
        holder.songArtist.setText(artist);
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();

    }

    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView songTitle, songArtist;
        ImageButton buttonMore;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.song_title);
            songArtist = itemView.findViewById(R.id.song_artist);
            buttonMore = itemView.findViewById(R.id.btn_more);

            buttonMore.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == R.id.btn_more){
                displayPopupMenu(view);
            }else {
                int position = getAdapterPosition();
                onRecyclerListClickListener.onRecyclerListClick(position);
            }
        }
    }

    private void displayPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.sound_track_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onMoreButtonClick(item);
                return true;
            }
        });

        popupMenu.show();
    }
}

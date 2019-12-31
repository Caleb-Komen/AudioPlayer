package com.audioplay.musica.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import com.audioplay.musica.R;
import com.audioplay.musica.activities.MainActivity;
import com.audioplay.musica.models.Song;
import com.chibde.visualizer.CircleBarVisualizer;

import java.io.IOException;
import java.util.List;


public class MusicService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener {

    public static final int NOTIFICATION_ID = 1;
    private MediaPlayer mediaPlayer;
    private List<Song> songs;
    private int songPosition;
    private CircleBarVisualizer circleBarVisualizer;
    private String songTitle;
    private String songArtist;
    private int resumePosition;
    public static final String TAG = MusicService.class.getSimpleName();

    public MusicService() {
    }

    public void setSongsList(List<Song> songs){
        this.songs = songs;
    }

    public void setCircleBarVisualizer(CircleBarVisualizer circleBarVisualizer){
        this.circleBarVisualizer = circleBarVisualizer;
    }



    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    private final IBinder iBinder = new MusicBinder();
    @Override
    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        mediaPlayer = new MediaPlayer();

        initMusicPlayer();
    }

    private void initMusicPlayer() {
        //configure player with some properties
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    public void playMedia(){

        mediaPlayer.reset();
        Song song = songs.get(songPosition);
        songTitle = song.getTitle();
        songArtist = song.getArtist();
        long songId = song.getId();

        //get uri as the data source
        Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), songUri);
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }

        mediaPlayer.prepareAsync();
    }

    public void pauseMedia(){
        //pause media if it's playing
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    public void stopMedia(){
        if (mediaPlayer == null) {
            return;
        }

        //stop media if it's playing
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    public void resumeMedia(){

        if (!mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }
    }


    public void setCurrentSong(int position){
        songPosition = position;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playNext();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

        Log.d(TAG, "onError: an error occurred");
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //start playback
        mediaPlayer.start();
        circleBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
        displayPlayerNotification();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onAudioFocusChange(int i) {

    }

    private void displayPlayerNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentText(songArtist)
                .setContentTitle(songTitle)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer(){
        mediaPlayer.pause();
    }

    public void seekTo(int position){
        mediaPlayer.seekTo(position);
    }

    public void start(){
        mediaPlayer.start();
    }

    public void playNext(){
        songPosition++;
        if (songPosition > songs.size()){
            songPosition = 0;
        }
        playMedia();
    }

    public void playPrevious(){
        songPosition--;
        if (songPosition < 0){
            songPosition = songs.size() - 1;
        }
        playMedia();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}

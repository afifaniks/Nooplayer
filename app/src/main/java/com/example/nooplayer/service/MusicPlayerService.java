package com.example.nooplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.nooplayer.entity.Track;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {
    static final String TAG = "MusicPlayerService";
    private final IBinder MPSBinder = new MusicPlayerServiceBinder();
    private MediaPlayer musicPlayer;
    private static boolean playing = false;
    private static boolean started = true;
    private static int currentPosition = 0;
    public static final String BROADCAST_PLAYER_STARTED = "com.example.nooplayer.started";
    public static final String BROADCAST_PLAYER_CHANGE_TRACK = "com.example.nooplayer.started";
    Handler handler = new Handler();

    Intent seekIntent;
    ArrayList<Track> trackList;

    // Binder
    public class MusicPlayerServiceBinder extends Binder {
        public MusicPlayerService getMusicPlayerService() {
            return MusicPlayerService.this;
        }
    }

    public MusicPlayerService() {
    }

    public static int getTrackPosition() {
        return currentPosition;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return MPSBinder;
    }

    //Releasing resources on unbind
//    @Override
//    public boolean onUnbind(Intent intent){
//        musicPlayer.stop();
//        musicPlayer.release();
//        return false;
//    }


    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new MediaPlayer();

        musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                musicPlayer.start();
                playing = true;
                started = true;

                sendBroadcastMessage(BROADCAST_PLAYER_STARTED);

            }
        });

        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });

       // seekIntent = new Intent(BROADCAST_PLAYER_STARTED);
    }

    public void setTrackList (ArrayList<Track> list) {
        trackList = new ArrayList<>();
        trackList.clear();
        trackList = list;
    }

    public void play(int position) {
        currentPosition = position;

        try {
            if (started)
                musicPlayer.reset();
            Track currentTrack = trackList.get(position);
            musicPlayer.setDataSource(currentTrack.getPath());
            Log.d(TAG, "onPlay() -> Track: " + currentTrack.getTrackName());
            musicPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying () {
        return playing;
    }

    public void pause() {
         if (playing) {
             musicPlayer.pause();
             playing = false;
         } else {
             musicPlayer.start();
             sendBroadcastMessage(BROADCAST_PLAYER_STARTED);
             playing = true;
         }
    }

    public int getCurrentPosition() {
        return musicPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return musicPlayer.getDuration();
    }

    public void seekTo(int pos) {
        musicPlayer.seekTo(pos);
    }

    public void stopMusic() {
        musicPlayer.stop();
    }

    public void playNext() {
        currentPosition++;

        if (currentPosition == trackList.size() - 1) {
            play(0); // Returning to start
        } else {
            play(currentPosition);
        }

        sendBroadcastMessage(BROADCAST_PLAYER_STARTED);
    }


    public void playPrev() {
        currentPosition--;

        if (currentPosition == -1) {
            play(trackList.size() - 1); // Returning to last
        } else {
            play(currentPosition);
        }

        sendBroadcastMessage(BROADCAST_PLAYER_STARTED);

    }

    private void sendBroadcastMessage(String action) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        sendBroadcast(broadcastIntent);
    }
}

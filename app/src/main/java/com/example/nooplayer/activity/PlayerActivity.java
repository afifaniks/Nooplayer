package com.example.nooplayer.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.nooplayer.R;
import com.example.nooplayer.entity.Track;
import com.example.nooplayer.service.MusicPlayerService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    static final String TAG = "PlayerActivity";
    private MediaPlayer player;
    private TextView songTitle;
    private TextView songArtist;
    private TextView songAlbum;
    private ImageButton playButton;
    private Track currentTrack;
    private Button nextButton;
    private Button prevButton;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable runnable;
    private TextView currentPosition;
    private TextView totalDuration;
    private boolean serviceBound = false;
    private MusicPlayerService musicPlayerService;
    Intent playerIntent = null;
    ArrayList<Track> trackList = new ArrayList<>();
    int trackPosition;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getSetFields(trackList.get(MusicPlayerService.getTrackPosition()));
            setUpSeekBar();
            updateSeekBar();
        }
    };

    private ServiceConnection musicPlayerCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.MusicPlayerServiceBinder binder =
                    (MusicPlayerService.MusicPlayerServiceBinder) service;
            musicPlayerService = binder.getMusicPlayerService();
            serviceBound = true;

            Bundle trackListBundle = getIntent().getBundleExtra("TRACK_LIST");
            trackList = (ArrayList<Track>) trackListBundle.getSerializable("TRACK_LIST");
            trackPosition = getIntent().getIntExtra("TRACK_POS", 0);

            Log.d(TAG, "on ServiceConnected");
            playSong();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
            Log.d(TAG, "on ServiceDisconnected");
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (playerIntent == null)
        {
            playerIntent = new Intent(this, MusicPlayerService.class);
            bindService(playerIntent, musicPlayerCon, Context.BIND_AUTO_CREATE);
            startService(playerIntent);
        }

        registerReceiver(broadcastReceiver, new IntentFilter(MusicPlayerService.BROADCAST_PLAYER_STARTED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private void playSong() {
        musicPlayerService.setTrackList(trackList);
        musicPlayerService.play(trackPosition);
        getSetFields(trackList.get(trackPosition));
        playButton.setBackgroundResource(R.drawable.pause);

//        setUpSeekBar();
//        updateSeekBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serviceBound) {
            unbindService(musicPlayerCon);
            serviceBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        handler = new Handler();

    }

    private String timeConverter(int duration) {
        int inSeconds = duration / 1000;
        int toMinutes = inSeconds / 60;
        int toSeconds = inSeconds - 60 * toMinutes;

        String resultTime = "";

        if (toMinutes <= 9)
            resultTime += "0";

        resultTime += Integer.toString(toMinutes) + ":";

        if (toSeconds <= 9)
            resultTime += "0";

        resultTime += Integer.toString(toSeconds);

        return resultTime;
    }


    private void updateSeekBar() {

        int duration = musicPlayerService.getCurrentPosition();

        seekBar.setProgress(duration);


        currentPosition.setText(timeConverter(duration));

        if (musicPlayerService.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };

            handler.postDelayed(runnable, 100);

        }
    }

    private void getSetFields(Track currentTrack) {
        songTitle = findViewById(R.id.songTitle);
        songArtist = findViewById(R.id.songArtist);
        songAlbum = findViewById(R.id.songAlbum);
        playButton = findViewById(R.id.play);
        seekBar = findViewById(R.id.seekBar);
        currentPosition = findViewById(R.id.timePassed);
        totalDuration = findViewById(R.id.timeTotal);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // Seeking to the position
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicPlayerService.seekTo(progress);
                    updateSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // songTitle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.moving_text));
        // Setting fields
        songTitle.setText(currentTrack.getTrackName());
        songArtist.setText(currentTrack.getArtistName());
        songAlbum.setText(currentTrack.getAlbumName());
    }

    private void setUpSeekBar() {
        seekBar.setMax(musicPlayerService.getDuration());

        String duration = timeConverter(musicPlayerService.getDuration());
        totalDuration.setText(duration);
    }

    public void playTrack(View view) {
        if (musicPlayerService.isPlaying()) {
            try {
                musicPlayerService.pause();
                playButton.setBackgroundResource(R.drawable.play);
            } catch (Exception e) {

            }
        } else {
            try {
                musicPlayerService.pause();
                playButton.setBackgroundResource(R.drawable.pause);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void playNext(View view) {
        if (musicPlayerService.isPlaying()) {
            musicPlayerService.playNext();
        }
    }

    public void playPrevious(View view) {
        if (musicPlayerService.isPlaying()) {
            musicPlayerService.playPrev();
        }
    }
}

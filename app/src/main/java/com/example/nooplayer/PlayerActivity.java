package com.example.nooplayer;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.nooplayer.entity.Track;

import java.io.IOException;
import java.text.DecimalFormat;

public class PlayerActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Getting Track
        currentTrack = (Track) getIntent().getSerializableExtra("TRACK");
        player = new MediaPlayer();
        handler = new Handler();


        getSetFields(currentTrack);

        try {
            player.setDataSource(currentTrack.getPath());
            playButton.setBackgroundResource(R.drawable.pause);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(player.getDuration());

                String duration = timeConverter(player.getDuration());
                totalDuration.setText(duration);

                player.start();
                updateSeekBar();
            }
        });

//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                playButton.setBackgroundResource(R.drawable.play);
//            }
//        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // Seeking to the position
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
        int duration = player.getCurrentPosition();

        seekBar.setProgress(duration);
        currentPosition.setText(timeConverter(duration));

        if (player.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };

            handler.postDelayed(runnable, 1000);

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

        //songTitle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.moving_text));
        // Setting fields
        songTitle.setText(currentTrack.getTrackName());
        songArtist.setText(currentTrack.getArtistName());
        songAlbum.setText(currentTrack.getAlbumName());
    }

    public void playTrack(View view) {
        if (player.isPlaying()) {
            try {
                player.pause();
                playButton.setBackgroundResource(R.drawable.play);
            } catch (Exception e) {

            }
        } else {
            try {
                player.start();
                playButton.setBackgroundResource(R.drawable.pause);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

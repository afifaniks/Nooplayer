package com.example.nooplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nooplayer.R;
import com.example.nooplayer.TrackListAdapter;
import com.example.nooplayer.entity.Track;
import com.example.nooplayer.system.GenreCursor;
import com.example.nooplayer.system.MusicCursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;

/**
 * @author: Afif Al Mamun
 * @created_in: 4/5/19
 * @project_name: Nooplayer-master
 **/
public class NoopySuggestionFragment extends Fragment {
    private static final int RECOMMENDATION_SIZE = 10;
    View view;
    TextView txtMessage;
    ListView trackList;
    static ArrayList<Track> tracks;
    static ArrayList<Track> recommendation = new ArrayList<>();
    PlayerActivity playerActivity;
    ArrayAdapter<Track> adapter;
    private static boolean moodSet = false;
    private static String mood = "";
    private static int NEW_RECOMMENDATION = 0;
    private static boolean isStarted = false;
    private static boolean isVisible = false;

    public NoopySuggestionFragment() {

    }

    public static void setMood(String m) {
        moodSet = true;
        mood = m;
        NEW_RECOMMENDATION = 1;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;

        if (isVisible && isStarted) {
            loadContents();
        } else {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;

        if (isStarted && isVisible) {
            loadContents();
        }
    }

    private void loadContents() {
        txtMessage = view.findViewById(R.id.txtMessage);
        trackList = view.findViewById(R.id.trackListView);
        playerActivity = PlayerActivity.getThisActivity();

        if (moodSet) {
            txtMessage.setText("Some songs for " + mood + " mood");

            if (NEW_RECOMMENDATION == 1) {
                tracks = GenreCursor.getRecommendation(getContext(), mood);

                int size = tracks.size();

                // Unique random songs
                LinkedHashSet<Integer> indexes = new LinkedHashSet<>();

                if (size > 0) {
                    while (indexes.size() < RECOMMENDATION_SIZE) {
                        indexes.add(new Random().nextInt(size));
                    }
                }

                for (Integer i: indexes) {
                    System.out.println(i);
                    recommendation.add(tracks.get(i));
                }

                Collections.sort(recommendation);
                //Getting listView
                adapter = new TrackListAdapter(view.getContext(), R.layout.list_view,  recommendation);
                trackList.setAdapter(adapter);
                NEW_RECOMMENDATION = 0; // Resetting
            } else {
                adapter = new TrackListAdapter(view.getContext(), R.layout.list_view,  recommendation);
                trackList.setAdapter(adapter);
            }

        }

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playerActivity.setTrackList(recommendation);
                playerActivity.setPosition(position);
                playerActivity.playSong();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_noopy_suggestion, container, false);

//            txtMessage = view.findViewById(R.id.txtMessage);
//            trackList = view.findViewById(R.id.trackListView);
//            playerActivity = PlayerActivity.getThisActivity();
//
//            if (moodSet) {
//                adapter = new TrackListAdapter(view.getContext(), R.layout.list_view,  recommendation);
//                trackList.setAdapter(adapter);
//            }

        return view;
    }
}

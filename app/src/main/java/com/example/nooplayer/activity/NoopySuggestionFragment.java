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
    private static final int RECOMMENDATION_NUMBER = 10;
    View view;
    TextView txtMessage;
    ListView trackList;
    ArrayList<Track> tracks;
    PlayerActivity playerActivity;
    ArrayAdapter<Track> adapter;
    private static boolean moodSet = false;
    private static String mood = "";

    public NoopySuggestionFragment() {

    }

    public static void setMood(String m) {
        moodSet = true;
        mood = m;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (moodSet) {
                txtMessage.setText("Some songs for " + mood + " mood");

                tracks = GenreCursor.getRecommendation(getContext(), mood);

                ArrayList<Track> recommendation = new ArrayList<>();
                int size = tracks.size();

                // Unique random songs
                LinkedHashSet<Integer> indexes = new LinkedHashSet<>();

                while (indexes.size() < RECOMMENDATION_NUMBER) {
                    indexes.add(new Random().nextInt(size));
                }

                for (Integer i: indexes) {
                    System.out.println(i);
                    recommendation.add(tracks.get(i));
                }

                Collections.sort(recommendation);
                    //Getting listView
                adapter = new TrackListAdapter(view.getContext(), R.layout.list_view,  recommendation);
                trackList.setAdapter(adapter);

                playerActivity.setTrackList(recommendation);

            } else {
                System.out.println("YYY");
            }

        } else {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_noopy_suggestion, container, false);

            txtMessage = view.findViewById(R.id.txtMessage);
            trackList = view.findViewById(R.id.trackListView);
            playerActivity = PlayerActivity.getThisActivity();

            if (!moodSet) {
                txtMessage.setText("Use Noopy to get suggestion");
            }

            trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    playerActivity.setPosition(position);
                    playerActivity.playSong();
                }
            });

        return view;
    }
}

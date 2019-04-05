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

import com.example.nooplayer.R;
import com.example.nooplayer.TrackListAdapter;
import com.example.nooplayer.entity.Track;
import com.example.nooplayer.system.GenreCursor;
import com.example.nooplayer.system.MusicCursor;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author: Afif Al Mamun
 * @created_in: 4/4/19
 * @project_name: Nooplayer-master
 **/
public class TrackListFragment extends Fragment {
    View view;
    ArrayList<Track> tracks;
    private boolean _hasLoadedOnce= false; // your boolean field

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (!isFragmentVisible_ && !_hasLoadedOnce) {
                _hasLoadedOnce = true;
            }
        }
    }

    public TrackListFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_track_list, container, false);

        String requiredPermission = "android.permission.READ_EXTERNAL_STORAGE";
        int checkValue = getContext().checkCallingOrSelfPermission(requiredPermission);

        System.out.println(checkValue + "Chk");

        if (checkValue == 0) {
            tracks = new MusicCursor().getMusic(getContext());
            GenreCursor.setMap(getContext());
//            GenreCursor.genre(getContext(), 10);
            Collections.sort(tracks);
            //Getting listView
            ListView trackList = view.findViewById(R.id.trackListView);

            ArrayAdapter<Track> adapter = new TrackListAdapter(view.getContext(), R.layout.list_view,  tracks);

            trackList.setAdapter(adapter);
            checkValue = getContext().checkCallingOrSelfPermission(requiredPermission);

        }

        ListView listView = view.findViewById(R.id.trackListView);
        final PlayerActivity playerActivity = PlayerActivity.getThisActivity();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playerActivity.setPosition(position);
                playerActivity.playSong();
            }
        });

        playerActivity.setTrackList(tracks);
        playerActivity.getSetFields(tracks.get(0));

        return view;
    }
}

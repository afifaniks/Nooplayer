package com.example.nooplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nooplayer.entity.Track;

import java.util.ArrayList;

/**
 * @author: Afif Al Mamun
 * @created_in: 3/18/19
 * @project_name: Nooplayer
 **/
public class TrackListAdapter extends ArrayAdapter<Track> {

    public TrackListAdapter(Context context, int res, ArrayList<Track> trackList) {
        super(context, res, trackList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_view, parent, false);

        Track track = getItem(position);

        TextView trackName = convertView.findViewById(R.id.songTitle);
        TextView albumName = convertView.findViewById(R.id.songAlbum);
        TextView artistName = convertView.findViewById(R.id.songArtist);
        ImageView albumArt = convertView.findViewById(R.id.albumArt);
        // TODO gotta add album art

        trackName.setText(track.getTrackName());
        albumName.setText(track.getAlbumName());
        artistName.setText(track.getArtistName());


        System.out.println(track.getPath());

        return convertView;
    }
}

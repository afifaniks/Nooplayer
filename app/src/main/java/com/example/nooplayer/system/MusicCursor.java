package com.example.nooplayer.system;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nooplayer.entity.Track;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author: Afif Al Mamun
 * @created_in: 3/19/19
 * @project_name: Nooplayer
 **/
public class MusicCursor{
    ArrayList<Track> tracks = new ArrayList<>();

    public ArrayList<Track> getMusic(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        int pos = 0;

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int location = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentAlbum = songCursor.getString(songAlbum);
                String songPath = songCursor.getString(location);
                ;

                tracks.add(new Track(currentTitle, currentAlbum, currentArtist, songPath));

            } while (songCursor.moveToNext());
        }

        return tracks;
    }

}

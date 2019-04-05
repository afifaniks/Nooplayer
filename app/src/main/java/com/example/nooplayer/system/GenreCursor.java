package com.example.nooplayer.system;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.nooplayer.entity.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * @author: Afif Al Mamun
 * @created_in: 4/5/19
 * @project_name: Nooplayer-master
 **/
public class GenreCursor {
    ArrayList<Track> tracks = new ArrayList<>();
    TreeMap<Integer, String> genreMap = new TreeMap<>();

    public static void setMap(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME};

        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int indexID = cursor.getColumnIndex(MediaStore.Audio.Genres._ID);
            int indexName = cursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
            do {
                String genreID = cursor.getString(indexID);
                String genreName = cursor.getString(indexName);


                System.out.println(genreID + " " + genreName);

            } while (cursor.moveToNext());
        }

    }

    public static void genre(Context context, int genreID) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreID);


        String[] projection = new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID};

        Cursor songCursor = contentResolver.query(uri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int indexSongTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            do {
                String currentTitle = songCursor.getString(indexSongTitle);
                System.out.println("Test" + currentTitle);
            } while (songCursor.moveToNext());
        }

        songCursor.close();
    }
}

package com.example.nooplayer.system;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.nooplayer.entity.Track;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author: Afif Al Mamun
 * @created_in: 4/5/19
 * @project_name: Nooplayer-master
 **/

public class GenreCursor {
    static ArrayList<Track> tracks = new ArrayList<>();
    static TreeMap<String, Integer> genreMap = new TreeMap<>();
    static TreeMap<String, String[]> suggestionMap = new TreeMap<>();

    static String[] sadMusic = {
            "Alternative",
            "Classick Rock",
            "Country Rock",
            "Post Rock",
            "Blues Rock"
    };

    static String[] happyMusic = {
            "Metal",
            "Heavy Metal",
            "Pop",
            "Progressive Rock"
    };

    static String[] disgustMusic = {
            "Black Metal",
            "Metal"
    };

    static String[] angryMusic = {
            "Death Metal",
            "Heavy Metal",
            "Black Metal",
            "Metal"
    };

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

                genreMap.put(genreName, Integer.valueOf(genreID));

                System.out.println(genreID + " " + genreName);

            } while (cursor.moveToNext());
        }

        suggestionMap.put("SAD", sadMusic);
        suggestionMap.put("HAPPY", happyMusic);
        suggestionMap.put("DISGUST", disgustMusic);
        suggestionMap.put("ANGRY", angryMusic);
    }

    public static ArrayList<Track> getRecommendation(Context context, String mood) {
        tracks.clear();

        if (suggestionMap.containsKey(mood)) { // Checking if mood is listed on map
            for (String genreName: suggestionMap.get(mood)) { // Getting genre for mood
                if (genreMap.containsKey(genreName)) { // Generating list for each genre
                    Integer genreId = genreMap.get(genreName);
                    genre(context, genreId);
                }
            }
        }

        return tracks;
    }

    private static void genre(Context context, int genreID) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreID);

        Cursor songCursor = contentResolver.query(uri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int location = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int albumId = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int songComposer = songCursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER);
            int releaseYear = songCursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
            int durationId = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentAlbum = songCursor.getString(songAlbum);
                String songPath = songCursor.getString(location);
                String composer = songCursor.getString(songComposer);
                String duration = songCursor.getString(durationId);
                String year = songCursor.getString(releaseYear);

                tracks.add(new Track(currentTitle,
                        currentAlbum,
                        currentArtist,
                        null,
                        songPath,
                        composer,
                        duration,
                        year));

                System.out.println("Test" + currentTitle);

            } while (songCursor.moveToNext());
        }

        songCursor.close();
    }
}

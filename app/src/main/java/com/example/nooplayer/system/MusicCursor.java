package com.example.nooplayer.system;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nooplayer.entity.Track;

import java.io.File;
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
            int albumId = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentAlbum = songCursor.getString(songAlbum);
                String songPath = songCursor.getString(location);
//                Uri albumArt = getAlbumArtUri(albumId);
//
//                byte[] art = null;
//                Bitmap bitmap = null;
//
//                try {
//                    mediaMetadataRetriever.setDataSource(songPath);
//                    art = mediaMetadataRetriever.getEmbeddedPicture();
//                    if (art != null)
//                        bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
//                } catch (Exception e) {}
//
//
//                System.out.println(songPath + " " + art);
                // System.out.println("Check"+getAlbumArtUri(albumId));

                tracks.add(new Track(currentTitle, currentAlbum, currentArtist, songPath));

            } while (songCursor.moveToNext());

            mediaMetadataRetriever.release();
        }

        songCursor.close();

        return tracks;
    }

    public static Uri getAlbumArtUri(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }


    public Uri getArtUriFromMusicFile(File file, Context context) {
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = { MediaStore.Audio.Media.ALBUM_ID };

        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1 AND " + MediaStore.Audio.Media.DATA + " = '"
                + file.getAbsolutePath() + "'";
        final Cursor cursor = context.getContentResolver().query(uri, cursor_cols, where, null, null);
       // Log.d(TAG, "Cursor count:" + cursor.getCount());
        /*
         * If the cusor count is greater than 0 then parse the data and get the art id.
         */
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
            cursor.close();
            return albumArtUri;
        }
        return Uri.EMPTY;
    }

}

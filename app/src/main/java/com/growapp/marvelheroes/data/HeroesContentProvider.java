package com.growapp.marvelheroes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;

import com.growapp.marvelheroes.data.Contract.*;

public class HeroesContentProvider extends ContentProvider {

    final String LOG_TAG = HeroesContentProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private HeroesDBOpenHelper mDBOpenHelper;

    static final int HEROES = 1;
    static final int IMAGES = 2;


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        matcher.addURI(authority, Contract.PATH_HEROES, HEROES);
        matcher.addURI(authority, Contract.PATH_IMAGES, IMAGES);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDBOpenHelper = new HeroesDBOpenHelper(getContext());
        Log.d(LOG_TAG, "HeroesContentProvider onCreate");
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case HEROES: return HeroEntry.CONTENT_TYPE;
            case IMAGES: return ImageEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case HEROES:
            {
                retCursor = mDBOpenHelper.getReadableDatabase().query(
                        HeroEntry.TABLE_NAME,
                        new String[] {HeroEntry.COLUMN_NAME},
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case IMAGES:
            {
                retCursor = mDBOpenHelper.getReadableDatabase().query(
                        ImageEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}

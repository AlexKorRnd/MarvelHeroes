package com.growapp.marvelheroes.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Алексей on 06.08.2015.
 */
public class Contract {

    public static final String CONTENT_AUTHORITY = "com.growapp.marvelheroes";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_HEROES = "heroes";
    public static final String PATH_IMAGES = "images";



    public static final class HeroEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HEROES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEROES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEROES;


        public static final String TABLE_NAME = "heroesTable";

        public static final String COLUMN_HERO_ID = "hero_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";

        public static Uri buildHeroUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class ImageEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_IMAGES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IMAGES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IMAGES;

        public static final String TABLE_NAME = "images";

        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_EXTENSION = "extension";

        // foreign key
        public static final String COLUMN_HERO_ID = "hero_id";


        public static Uri buildImageUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}

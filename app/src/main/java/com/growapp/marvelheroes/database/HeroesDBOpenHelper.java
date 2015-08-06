package com.growapp.marvelheroes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class HeroesDBOpenHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "heroesDB";

    static final String HEROES_TABLE = "heroesTable";

    static final String KEY_ID = "_id";
    static final String KEY_HERO_ID = "hero_id";
    static final String KEY_NAME = "name";
    static final String KEY_DESCRIPTION = "description";
    //static final String KEY_PHOTO_URL = "KEY_PHOTO_URL";

    static final String URL_PHOTO_HERO_TABLE = "photoTable";
    static final String KEY_PATH = "path";
    static final String KEY_EXTENSION = "extension";

    private static final int DATABASE_VERSION = 8;

    private static final String LOG_TAG = HeroesDBOpenHelper.class.getSimpleName();


    public HeroesDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d(LOG_TAG, "HeroesDBOpenHelper");


    }

    @Override
    //Вызывается при создании базы на устройстве
    public void onCreate(SQLiteDatabase db) {

        Log.d(LOG_TAG, "db onCreate");
        // Посроим стандартный sql-запрос для создания таблицы
        final String CREATE_HEROES_TABLE = "CREATE TABLE " + HEROES_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_HERO_ID + " INTEGER, "
                + KEY_NAME + " TEXT NOT NULL, "
                + KEY_DESCRIPTION + " TEXT NOT NULL);";
        db.execSQL(CREATE_HEROES_TABLE);

        final String CREATE_IMAGE_URL_TABLE = "CREATE TABLE " + URL_PHOTO_HERO_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_HERO_ID + " INTEGER, "
                + KEY_PATH + " INTEGER, "
                + KEY_EXTENSION + " TEXT NOT NULL);";
        db.execSQL(CREATE_IMAGE_URL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HEROES_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + URL_PHOTO_HERO_TABLE);
        onCreate(sqLiteDatabase);
    }
}

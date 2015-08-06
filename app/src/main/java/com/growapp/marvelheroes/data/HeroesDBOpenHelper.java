package com.growapp.marvelheroes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.growapp.marvelheroes.data.Contract.*;


public class HeroesDBOpenHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "heroesDB.db";
    private static final int DATABASE_VERSION = 9;


    private static final String LOG_TAG = HeroesDBOpenHelper.class.getSimpleName();


    public HeroesDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOG_TAG, "HeroesDBOpenHelper");
    }

    @Override
    //Вызывается при создании базы на устройстве
    public void onCreate(SQLiteDatabase db) {

        Log.d(LOG_TAG, "db onCreate");

        final String CREATE_HEROES_TABLE = "CREATE TABLE " + HeroEntry.TABLE_NAME + " ("
                + HeroEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HeroEntry.COLUMN_HERO_ID + " INTEGER UNIQUE NOT NULL, "
                + HeroEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + HeroEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL);";

        db.execSQL(CREATE_HEROES_TABLE);

        final String CREATE_IMAGE_URL_TABLE = "CREATE TABLE " + ImageEntry.TABLE_NAME + " ("
                + ImageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ImageEntry.COLUMN_HERO_ID + " INTEGER, "
                + ImageEntry.COLUMN_PATH + " TEXT NOT NULL, "
                + ImageEntry.COLUMN_EXTENSION + " TEXT NOT NULL);";

        db.execSQL(CREATE_IMAGE_URL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HeroEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

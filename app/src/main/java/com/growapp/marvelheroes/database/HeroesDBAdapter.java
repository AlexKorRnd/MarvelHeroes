package com.growapp.marvelheroes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.growapp.marvelheroes.data.Character;
import com.growapp.marvelheroes.data.ImageItem;

public class HeroesDBAdapter {


    private static final String LOG_TAG = HeroesDBOpenHelper.class.getSimpleName();

    private Cursor cursor;
    private SQLiteDatabase database;
    private HeroesDBOpenHelper dbOpenHelper;
    private Context context;



    public HeroesDBAdapter(Context context) {
        super();
        this.context = context;
        dbOpenHelper = new HeroesDBOpenHelper(context);
    }

    public HeroesDBAdapter open() throws SQLException {
        dbOpenHelper = new HeroesDBOpenHelper(context);
        database = dbOpenHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbOpenHelper.close();
    }


    public int getCount() {
        return cursor.getCount();
    }



    public Character getItem(int heroID) {

        String selection = HeroesDBOpenHelper.KEY_HERO_ID + "=" + heroID;

        Cursor cursor = database.query(HeroesDBOpenHelper.HEROES_TABLE, null,
                selection, null, null, null, null);

        cursor.moveToFirst();

        Character character = new Character();

        String name = cursor.getString(cursor.getColumnIndex(HeroesDBOpenHelper.KEY_NAME));
        Log.d(LOG_TAG, "name = " + name);
        character.setName(name);

        String description = cursor.getString(cursor.getColumnIndex(HeroesDBOpenHelper.KEY_DESCRIPTION));
        Log.d(LOG_TAG, "description = " + description);
        character.setDescription(description);

        cursor.close();

        Cursor cursorImage = database.query(HeroesDBOpenHelper.URL_PHOTO_HERO_TABLE, null,
                selection, null, null, null, null);
        cursorImage.moveToFirst();

        String path = cursorImage.getString(cursorImage.getColumnIndex(HeroesDBOpenHelper.KEY_PATH));
        Log.d(LOG_TAG, "path = " + path);

        String extension = cursorImage.getString(cursorImage.getColumnIndex(HeroesDBOpenHelper.KEY_EXTENSION));
        Log.d(LOG_TAG, "extension = " + extension);

        cursorImage.close();

        ImageItem imageItem = new ImageItem(path, extension);
        character.setThumbnail(imageItem);

        return character;
    }


    public long addItem(Character name) {
        ContentValues valuesHeroTable = new ContentValues();

        valuesHeroTable.put(HeroesDBOpenHelper.KEY_HERO_ID, name.getId());
        //Log.d("LOG_TAG", "hero_id = " + name.getId());
        valuesHeroTable.put(HeroesDBOpenHelper.KEY_NAME, name.getName());
        //Log.d("LOG_TAG", "name = " + name.getName());

        //Log.d("LOG_TAG", "database == null is " + (database == null));
        valuesHeroTable.put(HeroesDBOpenHelper.KEY_DESCRIPTION, name.getDescription());

        database.insert(HeroesDBOpenHelper.HEROES_TABLE, null, valuesHeroTable);


        ContentValues valuesImageTable = new ContentValues();
        valuesImageTable.put(HeroesDBOpenHelper.KEY_HERO_ID, name.getId());
        valuesImageTable.put(HeroesDBOpenHelper.KEY_PATH, name.getThumbnail().getPath());
        valuesImageTable.put(HeroesDBOpenHelper.KEY_EXTENSION, name.getThumbnail().getExtension());

        return database.insert(HeroesDBOpenHelper.URL_PHOTO_HERO_TABLE, null, valuesImageTable);
    }

}

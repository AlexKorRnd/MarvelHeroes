package com.growapp.marvelheroes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.growapp.marvelheroes.model.Character;
import com.growapp.marvelheroes.model.ImageItem;

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

        String selection = Contract.HeroEntry.COLUMN_HERO_ID + "=" + heroID;

        Cursor cursor = database.query(Contract.HeroEntry.TABLE_NAME, null,
                selection, null, null, null, null);

        cursor.moveToFirst();

        Character character = new Character();

        String name = cursor.getString(cursor.getColumnIndex(Contract.HeroEntry.COLUMN_NAME));
        Log.d(LOG_TAG, "name = " + name);
        character.setName(name);

        String description =
                cursor.getString(cursor.getColumnIndex(Contract.HeroEntry.COLUMN_DESCRIPTION));
        Log.d(LOG_TAG, "description = " + description);
        character.setDescription(description);

        cursor.close();

        Cursor cursorImage = database.query(Contract.ImageEntry.TABLE_NAME, null,
                selection, null, null, null, null);
        cursorImage.moveToFirst();

        String path =
                cursorImage.getString(cursorImage.getColumnIndex(Contract.ImageEntry.COLUMN_PATH));
        Log.d(LOG_TAG, "path = " + path);

        String extension =
                cursorImage.getString(cursorImage.getColumnIndex(Contract.ImageEntry.COLUMN_EXTENSION));
        Log.d(LOG_TAG, "extension = " + extension);

        cursorImage.close();

        ImageItem imageItem = new ImageItem(path, extension);
        character.setThumbnail(imageItem);

        return character;
    }


    public long addItem(Character name) {
        ContentValues valuesHeroTable = new ContentValues();

        valuesHeroTable.put(Contract.HeroEntry.COLUMN_HERO_ID, name.getId());
        //Log.d("LOG_TAG", "hero_id = " + name.getId());
        valuesHeroTable.put(Contract.HeroEntry.COLUMN_NAME, name.getName());
        //Log.d("LOG_TAG", "name = " + name.getName());

        //Log.d("LOG_TAG", "database == null is " + (database == null));
        valuesHeroTable.put(Contract.HeroEntry.COLUMN_DESCRIPTION, name.getDescription());

        database.insert(Contract.HeroEntry.TABLE_NAME, null, valuesHeroTable);


        ContentValues valuesImageTable = new ContentValues();
        valuesImageTable.put(Contract.ImageEntry.COLUMN_HERO_ID, name.getId());
        valuesImageTable.put(Contract.ImageEntry.COLUMN_PATH, name.getThumbnail().getPath());
        valuesImageTable.put(Contract.ImageEntry.COLUMN_EXTENSION, name.getThumbnail().getExtension());

        return database.insert(Contract.ImageEntry.TABLE_NAME, null, valuesImageTable);
    }

}

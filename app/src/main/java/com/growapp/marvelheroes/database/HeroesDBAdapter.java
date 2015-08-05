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

    //private static final String DB_NAME = "heroesDB";
    //private static final String TABLE_NAME = "heroesTable";

    //private static final int DB_VERSION = 1;

    private static final String LOG_TAG = "HeroesDBAdapter";

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

    /*public long getItemId(int position) {
        Character nameOnPosition = getItem(position);
        return nameOnPosition.getId();
    }*/


    public int getCount() {
        return cursor.getCount();
    }

    /*public Character getItem(int position) {
        Log.d(LOG_TAG + ".LOG_TAG", "position = " + position);
        if (cursor.moveToPosition(position)) {
            Character character = new Character();
            character.setName(cursor.getString(1));
            character.setDescription(cursor.getString(2));
            return character;
        } else {
            throw new CursorIndexOutOfBoundsException(
                    "Cant move cursor to postion");
        }
    }*/

    public Character getItem(int heroID) {
        Log.d(LOG_TAG + ".LOG_TAG", "heroID = " + heroID);

        String selection = HeroesDBOpenHelper.KEY_HERO_ID + "=" + heroID;
        Log.d(LOG_TAG + ".LOG_TAG", "selection = " + selection);

        Cursor cursor = database.query(HeroesDBOpenHelper.HEROES_TABLE, null,
                selection, null, null, null, null);

        cursor.moveToFirst();


        Log.d(LOG_TAG + ".LOG_TAG", "cursor.toString() = " + cursor.toString());

        Character character = new Character();

        String name = cursor.getString(cursor.getColumnIndex(HeroesDBOpenHelper.KEY_NAME));
        Log.d(LOG_TAG + ".LOG_TAG", "name = " + name);
        character.setName(name);

        String description = cursor.getString(cursor.getColumnIndex(HeroesDBOpenHelper.KEY_DESCRIPTION));
        Log.d(LOG_TAG + ".LOG_TAG", "description = " + description);
        character.setDescription(description);

        /*selection = HeroesDBOpenHelper.KEY_HERO_ID + "=" + heroID;
        Cursor cursor2 = database.query(HeroesDBOpenHelper.URL_PHOTO_HERO_TABLE, null,
                selection, null, null, null, null);
        cursor2.moveToFirst();
        String path = cursor2.getString(cursor2.getColumnIndex(HeroesDBOpenHelper.KEY_PATH));
        String extension = cursor2.getString(cursor2.getColumnIndex(HeroesDBOpenHelper.KEY_EXTENSION));

        ImageItem imageItem = new ImageItem(path, extension);
        character.setThumbnail(imageItem);*/

        return character;
    }


    public Cursor getAllEntries() {
        //Список колонок базы, которые следует включить в результат
        String[] columnsToTake = { HeroesDBOpenHelper.KEY_ID,
                HeroesDBOpenHelper.KEY_HERO_ID,
                HeroesDBOpenHelper.KEY_NAME, HeroesDBOpenHelper.KEY_DESCRIPTION };
        // составляем запрос к базе
        return database.query(HeroesDBOpenHelper.HEROES_TABLE, columnsToTake,
                null, null, null, null, null);
    }

    public long addItem(Character name) {
        ContentValues values = new ContentValues();

        values.put(HeroesDBOpenHelper.KEY_HERO_ID, name.getId());
        Log.d("LOG_TAG", "hero_id = " + name.getId());
        values.put(HeroesDBOpenHelper.KEY_NAME, name.getName());
        Log.d("LOG_TAG", "name = " + name.getName());

        Log.d("LOG_TAG", "database == null is " + (database == null));
        values.put(HeroesDBOpenHelper.KEY_DESCRIPTION, name.getDescription());

        long id = database.insert(HeroesDBOpenHelper.HEROES_TABLE, null, values);

        /*values = new ContentValues();
        values.put(HeroesDBOpenHelper.KEY_HERO_ID, name.getId());
        values.put(HeroesDBOpenHelper.KEY_PATH, name.getThumbnail().getPath());
        values.put(HeroesDBOpenHelper.KEY_EXTENSION, name.getThumbnail().getExtension());*/

        //id = database.insert(HeroesDBOpenHelper.URL_PHOTO_HERO_TABLE, null, values);

        return id;
    }

}

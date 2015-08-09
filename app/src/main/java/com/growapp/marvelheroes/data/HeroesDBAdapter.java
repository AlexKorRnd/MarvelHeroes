package com.growapp.marvelheroes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.growapp.marvelheroes.model.Character;
import com.growapp.marvelheroes.model.ImageItem;

import java.util.ArrayList;

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




    public Character getItem(int heroID) throws EmptyCursorException {

        String selection = Contract.HeroEntry.COLUMN_HERO_ID + "=" + heroID;

        Cursor cursor = database.query(Contract.HeroEntry.TABLE_NAME, null,
                selection, null, null, null, null);

        if (cursor.getCount() == 0)
            throw new EmptyCursorException("Cursor is empty");


        cursor.moveToFirst();

        Character character = new Character();

        int heroId = cursor.getInt(cursor.getColumnIndex(Contract.HeroEntry.COLUMN_HERO_ID));
        character.setId(heroId);

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

    public ArrayList<Character> getAll(){

        Cursor cursor = database.query(Contract.HeroEntry.TABLE_NAME, null,
                null, null, null, null, null);

        ArrayList<Character> characters = new ArrayList<>(cursor.getCount());

        cursor.moveToFirst();

        for (int i=0; i<cursor.getCount(); ++i){
            int heroID = cursor.getInt(cursor.getColumnIndex(Contract.HeroEntry.COLUMN_HERO_ID));
            try {
                characters.add(getItem(heroID));
            } catch (EmptyCursorException e) {
                Log.e(LOG_TAG, "EmptyCursorException");
                Crashlytics.log(Log.ERROR, LOG_TAG, " EmptyCursorException");
                e.printStackTrace();
            }
            cursor.moveToNext();
        }

        cursor.close();

        return characters;
    }

    public long addItem(Character character) {

        try {
            getItem(character.getId()); // проверка на существование записи в БД
        } catch (EmptyCursorException e) {
            // такой записи в БД еще нет

            ContentValues valuesHeroTable = new ContentValues();

            valuesHeroTable.put(Contract.HeroEntry.COLUMN_HERO_ID, character.getId());

            valuesHeroTable.put(Contract.HeroEntry.COLUMN_NAME, character.getName());

            valuesHeroTable.put(Contract.HeroEntry.COLUMN_DESCRIPTION, character.getDescription());

            database.insert(Contract.HeroEntry.TABLE_NAME, null, valuesHeroTable);


            ContentValues valuesImageTable = new ContentValues();
            valuesImageTable.put(Contract.ImageEntry.COLUMN_HERO_ID, character.getId());
            valuesImageTable.put(Contract.ImageEntry.COLUMN_PATH, character.getThumbnail().getPath());
            valuesImageTable.put(Contract.ImageEntry.COLUMN_EXTENSION, character.getThumbnail().getExtension());

            return database.insert(Contract.ImageEntry.TABLE_NAME, null, valuesImageTable);
        }


        return 0;

    }

}

package com.growapp.marvelheroes.data;


/**
 * Created by Алексей on 04.08.2015.
 */
public class Character {
    int id;         //The unique ID of the character resource.
    String name;    // The name of the character.
    String description;     //A short bio or description of the character.
    ImageItem thumbnail;   //The representative image for this character.

    public Character() {
    }

    public Character(int id, String name, String description, ImageItem thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageItem getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageItem thumbnail) {
        this.thumbnail = thumbnail;
    }
}

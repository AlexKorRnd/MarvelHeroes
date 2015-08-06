package com.growapp.marvelheroes.data;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


class CharacterDeserializer implements JsonDeserializer {

    @Override
    public Character deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Character character = new Character();

        character.setId(json.getAsJsonObject().get("id").getAsInt());
        character.setName(json.getAsJsonObject().get("name").getAsString());
        character.setDescription(json.getAsJsonObject().get("description").getAsString());

        Gson gson = new Gson();
        ImageItem imageItem = gson.fromJson(json.getAsJsonObject().get("thumbnail"), ImageItem.class);
        character.setThumbnail(imageItem);

        return character;
    }
}

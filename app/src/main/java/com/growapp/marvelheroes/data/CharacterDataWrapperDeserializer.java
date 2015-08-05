package com.growapp.marvelheroes.data;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Алексей on 05.08.2015.
 */
public class CharacterDataWrapperDeserializer implements JsonDeserializer {
    @Override
    public CharacterDataWrapper deserialize(JsonElement json, Type typeOfT,
           JsonDeserializationContext context) throws JsonParseException {


        CharacterDataWrapper characterDataWrapper = new CharacterDataWrapper();

        characterDataWrapper.setCode(json.getAsJsonObject().get("code").getAsInt());
        characterDataWrapper.setStatus(json.getAsJsonObject().get("status").getAsString());
        characterDataWrapper.setCopyright(json.getAsJsonObject().get("copyright").getAsString());

        Gson gson = new Gson();
        CharacterDataContainer characterDataContainer =
                gson.fromJson(json.getAsJsonObject().get("data"), CharacterDataContainer.class);

        characterDataWrapper.setData(characterDataContainer);

        return characterDataWrapper;
    }
}

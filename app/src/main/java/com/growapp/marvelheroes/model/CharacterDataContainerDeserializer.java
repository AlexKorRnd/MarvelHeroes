package com.growapp.marvelheroes.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;


class CharacterDataContainerDeserializer implements JsonDeserializer {

    @Override
    public CharacterDataContainer deserialize(JsonElement json, Type typeOfT,
                                                        JsonDeserializationContext context) throws JsonParseException {


        CharacterDataContainer container = new CharacterDataContainer();

        container.setOffset(json.getAsJsonObject().get("offset").getAsInt());
        container.setLimit(json.getAsJsonObject().get("limit").getAsInt());
        container.setTotal(json.getAsJsonObject().get("total").getAsInt());
        container.setCount(json.getAsJsonObject().get("count").getAsInt());

        //container.setResults(json.getAsJsonObject().getAsJsonObject("results"));

        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Character>>(){}.getType();
        Collection<Character> characters =
                gson.fromJson(json.getAsJsonObject().getAsJsonArray("results"), collectionType);


        container.setResults((ArrayList<Character>)characters);

        return container;
    }

}

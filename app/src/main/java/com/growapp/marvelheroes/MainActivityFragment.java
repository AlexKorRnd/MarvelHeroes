package com.growapp.marvelheroes;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.GridView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.growapp.marvelheroes.data.*;
import com.growapp.marvelheroes.data.Character;
import com.growapp.marvelheroes.database.HeroesDBAdapter;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    //private final static String LOG_TAG = MainActivityFragment.class.getSimpleName();
    static final String TAG_STRING_URL = "TAG_STRING_URL";
    static final String TAG_HERO_ID = "TAG_HERO_ID";
    /*static final String TAG_NAME = "name";
    static final String TAG_DESCRIPTION = "description";*/

    private Context mContext;

    private HeroesDBAdapter mDBAdapter;

    volatile ArrayList<Character> mCharacters;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mContext = getActivity();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = buildUrl();



        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        CharacterDataWrapper dataWrapper = gson.fromJson(response.toString(), CharacterDataWrapper.class);

                        CharacterDataContainer container = dataWrapper.getData();
                        mCharacters = dataWrapper.getData().getResults();


                        for (Character character : mCharacters){
                            mDBAdapter.addItem(character);
                        }

                        GridViewAdapter gridAdapter = new GridViewAdapter(mContext, R.id.gridView, mCharacters);
                        gridView.setAdapter(gridAdapter);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });

        VolleyController.getInstance().addToRequestQueue(jsObjRequest);

        mDBAdapter = new HeroesDBAdapter(getActivity());
        mDBAdapter.open();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailInfo.class);
                /*intent.putExtra(TAG_NAME, mCharacters.get(position).getName());
                intent.putExtra(TAG_DESCRIPTION,
                        mCharacters.get(position).getDescription());

                ImageItem imageItem = mCharacters.get(position).getThumbnail();
                intent.putExtra(TAG_STRING_URL, imageItem.getPath() + "." +imageItem.getExtension());*/

                ImageItem imageItem = mCharacters.get(position).getThumbnail();
                intent.putExtra(TAG_STRING_URL, imageItem.getPath() + "." +imageItem.getExtension());
                intent.putExtra(TAG_HERO_ID, mCharacters.get(position).getId());
                startActivity(intent);
            }
        });

        return rootView;
    }



    private String buildUrl() {
        final String BASE_URL = "http://gateway.marvel.com/v1/public/characters?";
        final String LIMIT_PARAM = "limit";
        final String TS_PARAM = "ts";
        final String API_KEY_PARAM = "apikey";
        final String HASH_PARAM = "hash";

        String ts = "timestamp";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LIMIT_PARAM, Constants.limit)
                .appendQueryParameter(TS_PARAM, ts)
                .appendQueryParameter(API_KEY_PARAM, Constants.apikey)
                .appendQueryParameter(HASH_PARAM, Md5.Md5(ts + Constants.privateKey + Constants.apikey))
                .build();

        return builtUri.toString();
    }

    private String md5(String ts) {

        try {

        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(ts.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
        return "";

    }


}

package com.growapp.marvelheroes.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.growapp.marvelheroes.GridViewAdapter;
import com.growapp.marvelheroes.R;
import com.growapp.marvelheroes.Utils;
import com.growapp.marvelheroes.VolleyController;
import com.growapp.marvelheroes.activity.DetailInfoActivity;
import com.growapp.marvelheroes.activity.MainActivity;
import com.growapp.marvelheroes.model.*;
import com.growapp.marvelheroes.model.Character;
import com.growapp.marvelheroes.data.HeroesDBAdapter;
import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public static final String TAG_HERO_ID = "TAG_HERO_ID";


    private Context mContext;

    private HeroesDBAdapter mDBAdapter;

    private volatile ArrayList<Character> mCharacters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final GridView gridView = (GridView) rootView.findViewById(R.id.gridView);

        mContext = getActivity();


        RequestQueue queue = Volley.newRequestQueue(getActivity());


        String url = buildUrl();

        mDBAdapter = new HeroesDBAdapter(getActivity());
        mDBAdapter.open();



        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        CharacterDataWrapper dataWrapper = gson.fromJson(response.toString(), CharacterDataWrapper.class);

                        CharacterDataContainer container = dataWrapper.getData();

                        mCharacters = dataWrapper.getData().getResults();

                        final GridViewAdapter gridAdapter = new GridViewAdapter(mContext, R.id.gridView, mCharacters);
                        gridView.setAdapter(gridAdapter);



                        for (Character character : mCharacters){
                            mDBAdapter.addItem(character);

                        }

                        mDBAdapter.close();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                mCharacters = mDBAdapter.getAll();
                final GridViewAdapter gridAdapter = new GridViewAdapter(mContext, R.id.gridView, mCharacters);
                gridView.setAdapter(gridAdapter);
                gridAdapter.setNotifyOnChange(true);



                if (mCharacters.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.title_no_internet_connection)
                            .setMessage(R.string.message_no_internet_connection);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }



                mDBAdapter.close();
            }
        });


        queue.add(jsObjRequest);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), DetailInfoActivity.class);
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
                .appendQueryParameter(LIMIT_PARAM, getString(R.string.limit))
                .appendQueryParameter(TS_PARAM, ts)
                .appendQueryParameter(API_KEY_PARAM, getString(R.string.apikey))
                .appendQueryParameter(HASH_PARAM, Utils.md5(ts + getString(R.string.privateKey)
                        + getString(R.string.apikey))).build();



        return builtUri.toString();
    }


}

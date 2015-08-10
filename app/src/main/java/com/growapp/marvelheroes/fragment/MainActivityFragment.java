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

    private HeroesDBAdapter mDBAdapter;

    private ArrayList<Character> mCharacters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final GridView gridView = (GridView) rootView.findViewById(R.id.gridView);

        mCharacters = new ArrayList<>();

        final GridViewAdapter gridAdapter = new GridViewAdapter(getActivity(), R.id.gridView, mCharacters);
        gridView.setAdapter(gridAdapter);

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






}
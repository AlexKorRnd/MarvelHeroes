package com.growapp.marvelheroes.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.growapp.marvelheroes.R;
import com.growapp.marvelheroes.model.Character;
import com.growapp.marvelheroes.model.ImageItem;
import com.growapp.marvelheroes.data.HeroesDBAdapter;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.io.File;
import java.net.URI;


public class DetailInfoFragment extends Fragment {

    public static final String LOG_TAG = "LOG_TAG";

    private ShareActionProvider mShareActionProvider;
    private Character mCharacter;

    public DetailInfoFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_info, container, false);

        Intent intent = getActivity().getIntent();

        int hero_id = -2;
        if (intent != null){
            hero_id = intent.getIntExtra(MainActivityFragment.TAG_HERO_ID, -1);

        }

        HeroesDBAdapter adapter = new HeroesDBAdapter(getActivity());
        adapter.open();

        mCharacter = adapter.getItem(hero_id);

        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.imageView_detail);


        ImageItem imageItem = mCharacter.getThumbnail();
        Uri uri = Uri.parse(imageItem.toString());
        draweeView.setImageURI(uri);

        TextView textView = (TextView) view.findViewById(R.id.heroe_name);
        textView.setText(mCharacter.getName());

        textView = (TextView) view.findViewById(R.id.heroe_description);
        textView.setText(mCharacter.getDescription());

        adapter.close();

        return view;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_detail_info, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        //mShareActionProvider.onPrepareSubMenu();
        //mShareActionProvider.setShareIntent(initShareIntent());

    }



}

package com.growapp.marvelheroes;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.growapp.marvelheroes.data.Character;
import com.growapp.marvelheroes.data.ImageItem;
import com.growapp.marvelheroes.database.HeroesDBAdapter;



public class DetailInfoFragment extends Fragment {

    public DetailInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_info, container, false);

        Intent intent = getActivity().getIntent();

        //String urlPhoto = "";
        //String name = "";
        //String description = "";
        //int id = -2;
        int hero_id = -2;
        if (intent != null){
            //name = intent.getStringExtra(MainActivityFragment.TAG_NAME);
            //description = intent.getStringExtra(MainActivityFragment.TAG_DESCRIPTION);
            //urlPhoto = intent.getStringExtra(MainActivityFragment.TAG_STRING_URL);
            hero_id = intent.getIntExtra(MainActivityFragment.TAG_HERO_ID, -1);

            Log.d("LOG_TAG", "hero_id =" + hero_id);
            /*Log.d("LOG_TAG", "name =" + name);
            Log.d("LOG_TAG", "description =" + description);
            Log.d("LOG_TAG", "urlPhoto =" + urlPhoto);*/
        }

        HeroesDBAdapter adapter = new HeroesDBAdapter(getActivity());
        adapter.open();

        Character character = adapter.getItem(hero_id);

        Log.d("LOG_TAG", "character == null is " + (character == null));

        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.imageView_detail);
        ImageItem imageItem = character.getThumbnail();

        //Log.d("LOG_TAG_DET", imageItem.toString());

        Uri uri = Uri.parse(imageItem.toString());
        Log.d("LOG_TAG", "uri = " + uri.toString());
        draweeView.setImageURI(uri);

        TextView textView = (TextView) view.findViewById(R.id.heroe_name);
        textView.setText(character.getName());

        textView = (TextView) view.findViewById(R.id.heroe_description);
        textView.setText(character.getDescription());

        return view;
    }
}

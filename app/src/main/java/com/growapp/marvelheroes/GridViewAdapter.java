package com.growapp.marvelheroes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.growapp.marvelheroes.data.*;
import com.growapp.marvelheroes.data.Character;


import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Character> mCharacters;

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Character> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.mCharacters = data;
    }

    public int getCount() {
        return mCharacters.size();
    }

    public Object getItem(int position) {
        return mCharacters.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        grid =(View) inflater.inflate(R.layout.grid_item_layout, parent, false);
        Log.d("TEST", "parent = " + parent.getClass().getSimpleName());

        /*if (convertView == null) {
            grid = new View(mContext);

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid =(View) inflater.inflate(R.layout.grid_item_layout, parent, false);
            Log.d("TEST", "parent = " + parent.getClass().getSimpleName());
            //Log.d("TEST", "convertView = " + convertView.getClass().getSimpleName());
        } else {
            grid = (View) convertView;
            Log.d("TEST", "convertView = " + convertView.getClass().getSimpleName());
            Log.d("TEST", "convertView = " + convertView.toString());
        }*/


        SimpleDraweeView draweeView = (SimpleDraweeView) grid.findViewById(R.id.image_hero);
        ImageItem imageItem = mCharacters.get(position).getThumbnail();
        Uri uri = Uri.parse(imageItem.getPath() + "." + imageItem.getExtension());

        draweeView.setImageURI(uri);

        TextView textView = (TextView) grid.findViewById(R.id.name_hero);
        textView.setText(mCharacters.get(position).getName());




        return draweeView;
    }


}
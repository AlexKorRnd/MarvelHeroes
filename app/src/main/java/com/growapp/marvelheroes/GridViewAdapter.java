package com.growapp.marvelheroes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.facebook.drawee.view.SimpleDraweeView;
import com.growapp.marvelheroes.model.*;
import com.growapp.marvelheroes.model.Character;


import java.util.ArrayList;


public class GridViewAdapter extends ArrayAdapter {
    private final Context mContext;
    private final int layoutResourceId;
    private final ArrayList<Character> mCharacters;

    static class ViewHolder {
        SimpleDraweeView draweeView;
    }

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


        ViewHolder mViewHolder;

        if(convertView == null){
            mViewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
            mViewHolder.draweeView = (SimpleDraweeView) convertView.findViewById(R.id.image_hero);

            convertView.setTag(mViewHolder);

        }else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ImageItem imageItem = mCharacters.get(position).getThumbnail();
        Uri uri = Uri.parse(imageItem.getPath() + "." + imageItem.getExtension());
        mViewHolder.draweeView.setImageURI(uri);



        return convertView;
    }


}
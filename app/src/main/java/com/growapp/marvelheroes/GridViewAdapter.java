package com.growapp.marvelheroes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.growapp.marvelheroes.data.HeroesDBAdapter;
import com.growapp.marvelheroes.model.*;
import com.growapp.marvelheroes.model.Character;


import org.json.JSONObject;

import java.util.ArrayList;


public class GridViewAdapter extends ArrayAdapter {
    private final Context mContext;
    private final int layoutResourceId;
    private ArrayList<Character> mCharacters;

    private GridViewAdapter self;

    private static final String LOG_TAG = "LOG_TAG";

    private HeroesDBAdapter mDBAdapter;

    static class ViewHolder {
        SimpleDraweeView draweeView;
    }

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Character> data) {
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        mContext = context;
        mCharacters = data;

        this.self = this;

        RequestQueue queue = Volley.newRequestQueue(mContext);

        String url = Utils.buildUrl(mContext);

        mDBAdapter = new HeroesDBAdapter(mContext);
        mDBAdapter.open();


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        CharacterDataWrapper dataWrapper = gson.fromJson(response.toString(), CharacterDataWrapper.class);

                        mCharacters.addAll(dataWrapper.getData().getResults());
                        self.notifyDataSetChanged();

                        for (Character character : mCharacters){
                            mDBAdapter.addItem(character);
                        }

                        mDBAdapter.close();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                mCharacters.addAll(mDBAdapter.getAll());
                self.notifyDataSetChanged();

                if (mCharacters.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(R.string.title_no_internet_connection)
                            .setMessage(R.string.message_no_internet_connection)
                            .setNeutralButton(R.string.button_ok,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                mDBAdapter.close();
            }
        });


        queue.add(jsObjRequest);

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
package com.growapp.marvelheroes.fragment;


import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import com.crashlytics.android.Crashlytics;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.growapp.marvelheroes.R;
import com.growapp.marvelheroes.model.Character;
import com.growapp.marvelheroes.model.ImageItem;
import com.growapp.marvelheroes.data.HeroesDBAdapter;



public class DetailInfoFragment extends Fragment {

    public static final String LOG_TAG = DetailInfoFragment.class.getSimpleName();

    private ShareActionProvider mShareActionProvider;
    private Character mCharacter;

    private Bitmap mBitmap;
    private Context mContext;

    public DetailInfoFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity();

        final View view = inflater.inflate(R.layout.fragment_detail_info, container, false);

        Intent intent = getActivity().getIntent();

        int hero_id = -2;
        if (intent != null){
            hero_id = intent.getIntExtra(MainActivityFragment.TAG_HERO_ID, -1);

        }

        HeroesDBAdapter adapter = new HeroesDBAdapter(getActivity());
        adapter.open();

        mCharacter = adapter.getItem(hero_id);

        //SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.imageView_detail);

        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        final ImageItem imageItem = mCharacter.getThumbnail();
        Uri uri = Uri.parse(imageItem.toString());

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setRequestPriority(Priority.HIGH)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();

        DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchDecodedImage(imageRequest, getActivity());



        try {
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                    if (bitmap == null) {
                        Crashlytics.log(Log.ERROR, LOG_TAG,
                                "Bitmap data source returned success, but bitmap null.");

                        return;
                    } else {
                        mBitmap = bitmap;
                        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_detail);
                        imageView.setImageBitmap(mBitmap);
                    }
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {
                    Crashlytics.log(Log.ERROR, LOG_TAG,
                            "Bitmap data source returned error");

                }
            }, CallerThreadExecutor.getInstance());
        } finally {
            if (dataSource != null) {
                dataSource.close();
            }
        }



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
        mShareActionProvider.setShareIntent(initShareIntent());

    }

    public Intent initShareIntent() {
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                mBitmap, "Image Description", null);
        Uri bmpUri = Uri.parse(path);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mCharacter.getName() + "\n" + mCharacter.getDescription());
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");

        return shareIntent;
    }

}

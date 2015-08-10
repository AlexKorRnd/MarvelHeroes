package com.growapp.marvelheroes.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
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
import com.growapp.marvelheroes.data.EmptyCursorException;
import com.growapp.marvelheroes.model.Character;
import com.growapp.marvelheroes.model.ImageItem;
import com.growapp.marvelheroes.data.HeroesDBAdapter;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;


public class DetailInfoFragment extends Fragment {

    //public static final String LOG_TAG = DetailInfoFragment.class.getSimpleName();
    public static final String LOG_TAG = DetailInfoFragment.class.getSimpleName() + " LOG_TAG";

    private ShareActionProvider mShareActionProvider;
    private Character mCharacter;

    private Bitmap mBitmap;
    private Context mContext;

    private String userID;

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

        try {
            mCharacter = adapter.getItem(hero_id);
        } catch (EmptyCursorException e) {
            Log.e(LOG_TAG, " EmptyCursorException");
            Crashlytics.log(Log.ERROR, LOG_TAG, " EmptyCursorException");
            e.printStackTrace();
        }


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

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share){
            shareVK();
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareVK(){
        String[] sMyScope = new String[]
                {VKScope.MESSAGES, VKScope.WALL, VKScope.NOHTTPS, VKScope.PHOTOS};

        VKSdk.login(this, sMyScope);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                userID = res.userId;

                Log.d(LOG_TAG, " onResult");

                final Bitmap photo =mBitmap;

                VKRequest request2 = VKApi.uploadWallPhotoRequest(new VKUploadImage(photo,
                        VKImageParameters.jpgImage(0.9f)), 0, 0);

                request2.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        photo.recycle();
                        VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                        //Make post with photo

                        Log.d(LOG_TAG, " photoModel.getId() = " + photoModel.getId());

                        makePost(new VKAttachments(photoModel),
                                mCharacter.getName() + "\n" + mCharacter.getDescription());
                        Log.d(LOG_TAG, " onComplete");

                    }
                    @Override
                    public void onError(VKError error) {
                        Log.d(LOG_TAG, " onError");
                    }
                });


            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                Log.d(LOG_TAG, " VKError onError");
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void makePost(VKAttachments attachments, String message) {

        final VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, message));

        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {
                try {

                    super.onComplete(response);

                    Log.d("LOG_TAG", "post ok");

                    VKWallPostResult result = (VKWallPostResult) response.parsedModel;

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(String.format("https://vk.com/wall" +
                                    userID + "_" + result.post_id)));
                    startActivity(intent);


                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "error");
                }
            }
        });
    }

    public void initShareIntent() {
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                mBitmap, "Image Description", null);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //shareIntent.setType("image/*");
        try{

            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    mCharacter.getName() + "\n" + mCharacter.getDescription());
            Uri bmpUri = Uri.parse(path);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            //startActivity(Intent.createChooser(shareIntent, "How do you want to share?"));

            //return shareIntent;
        }
        catch (NullPointerException e){
            //return  shareIntent;
            startActivity(Intent.createChooser(shareIntent, "How do you want to share?"));
        }

    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        popup.inflate(R.menu.share_menu);
        popup.show();
    }


}

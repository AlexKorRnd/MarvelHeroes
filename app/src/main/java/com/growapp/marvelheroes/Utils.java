package com.growapp.marvelheroes;

import android.content.Context;
import android.net.Uri;

import com.growapp.marvelheroes.data.Contract;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Utils {

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {

            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();


            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String buildUrl(Context context) {
        final String BASE_URL = "http://gateway.marvel.com/v1/public/characters?";
        final String LIMIT_PARAM = "limit";
        final String TS_PARAM = "ts";
        final String API_KEY_PARAM = "apikey";
        final String HASH_PARAM = "hash";

        String ts = "timestamp";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LIMIT_PARAM, context.getString(R.string.limit))
                .appendQueryParameter(TS_PARAM, ts)
                .appendQueryParameter(API_KEY_PARAM, context.getString(R.string.apikey))
                .appendQueryParameter(HASH_PARAM, Utils.md5(ts +context.getString(R.string.privateKey)
                        + context.getString(R.string.apikey))).build();



        return builtUri.toString();
    }
}

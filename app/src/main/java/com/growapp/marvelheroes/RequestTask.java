package com.growapp.marvelheroes;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


class RequestTask extends AsyncTask<String, Void, String> {
    private final String LOG_TAG = RequestTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;
        String privateKey = "5d7d7598f2b6dfb06731114c517ada47dacfce5f";
        String apiKey = "4aab5874a0e157ce820533faf8b6915d";

        try {

            final String BASE_URL = "http://gateway.marvel.com/v1/public/characters?";
            final String TS_PARAM = "ts";
            final String API_KEY_PARAM = "apikey";
            final String HASH_PARAM = "hash";


            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(TS_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    //.appendQueryParameter(HASH_PARAM, Md5.Md5(params[0] + privateKey + apiKey))
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        Log.i(LOG_TAG, jsonStr);
        return jsonStr;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
}
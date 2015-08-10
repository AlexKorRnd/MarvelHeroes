package com.growapp.marvelheroes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.crashlytics.android.*;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.growapp.marvelheroes.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(getApplicationContext());
        VKSdk.initialize(getApplicationContext());

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());

        for (String s: fingerprints){
            Log.d("LOG_TAG", "fingerprints = " + s);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

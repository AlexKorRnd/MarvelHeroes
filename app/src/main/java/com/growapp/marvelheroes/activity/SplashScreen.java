package com.growapp.marvelheroes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.crashlytics.android.Crashlytics;
import com.growapp.marvelheroes.R;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_screen);

        Thread logoTimer = new Thread()
        {
            public void run()
            {
                try
                {
                    int logoTimer = 0;
                    while(logoTimer < 3000)
                    {
                        sleep(100);
                        logoTimer = logoTimer +100;
                    }
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
                finally
                {
                    finish();
                }
            }
        };
        logoTimer.start();
    }

}

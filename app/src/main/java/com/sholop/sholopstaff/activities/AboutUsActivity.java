package com.sholop.sholopstaff.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.config.AppConfig;


public class AboutUsActivity extends Activity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(AboutUsActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, AppConfig.SPLASH_DISPLAY_LENGTH);
    }

}
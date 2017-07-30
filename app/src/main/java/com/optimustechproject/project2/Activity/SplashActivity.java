package com.optimustechproject.project2.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.DbHandler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2500);
                } catch (Exception e) {

                } finally {
                    if(DbHandler.getBoolean(SplashActivity.this,"isLoggedIn",false)){
                        Intent intent = new Intent(SplashActivity.this,NavigationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
        };
        splashTread.start();
    }
}

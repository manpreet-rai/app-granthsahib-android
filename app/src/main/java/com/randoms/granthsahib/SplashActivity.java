package com.randoms.granthsahib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    int mode, time;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            View v1 = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            v1.setSystemUiVisibility(uiOptions);
            time = 1300;
        } else{
            time = 2000;
        }

        mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.AppTheme);

        setTheme(mode);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        }, time);
    }
}

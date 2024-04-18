package com.randoms.granthsahib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

public class HelpActivity extends AppCompatActivity {

    int mode;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    View creditsView, privacyPolicyView;
    ImageButton helpBackBtn, infoBtn, closePrivacyViewBtn;
    WebView creditsWebView, helpWebView, privacyPolicyWebView;
    Button closeCreditsBtn, privacyPolicyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.AppTheme);

        setTheme(mode);
        setContentView(R.layout.activity_help);

        helpBackBtn = findViewById(R.id.helpBackBtn);
        infoBtn = findViewById(R.id.infoBtn);
        creditsView = findViewById(R.id.creditsView);
        creditsWebView = findViewById(R.id.creditsWebView);
        helpWebView = findViewById(R.id.helpWebView);
        closeCreditsBtn = findViewById(R.id.closeCreditsBtn);
        privacyPolicyBtn = findViewById(R.id.privacyPolicyBtn);

        privacyPolicyView = findViewById(R.id.privacyPolicyView);
        privacyPolicyWebView = findViewById(R.id.privacyPolicyWebView);
        closePrivacyViewBtn = findViewById(R.id.closePrivacyViewBtn);

        initializeHelpWebView();
        initializeCreditsWebView();

        if(mode==R.style.AppTheme){
            helpWebView.loadUrl("file:///android_asset/help_day.html");
        } else {
            helpWebView.loadUrl("file:///android_asset/help_night.html");
        }

        helpBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode==R.style.AppTheme){
                    creditsWebView.loadUrl("file:///android_asset/credits_day.html");
                } else {
                    creditsWebView.loadUrl("file:///android_asset/credits_night.html");
                }
                creditsView.setAlpha(0.0f);
                creditsView.setVisibility(View.VISIBLE);
                creditsView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(1.0f).setDuration(180);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        creditsView.setAlpha(1.0f);
                    }
                }, 180);
            }
        });

        closeCreditsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creditsView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(180);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        creditsView.setVisibility(View.INVISIBLE);
                    }
                }, 180);
            }
        });
        privacyPolicyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializePrivacyWebView();
                if(mode==R.style.AppTheme){
                    privacyPolicyWebView.loadUrl("file:///android_asset/granthsahib_privacypolicy_day.html");
                } else {
                    privacyPolicyWebView.loadUrl("file:///android_asset/granthsahib_privacypolicy_night.html");
                }
                privacyPolicyView.setAlpha(0.0f);
                privacyPolicyView.setVisibility(View.VISIBLE);
                privacyPolicyView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(1.0f).setDuration(180);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        privacyPolicyView.setAlpha(1.0f);
                    }
                }, 180);
            }
        });
        closePrivacyViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privacyPolicyView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(180);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        privacyPolicyView.setVisibility(View.INVISIBLE);
                    }
                }, 180);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (privacyPolicyView.getVisibility() == View.VISIBLE) {
            privacyPolicyView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(180);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    privacyPolicyView.setVisibility(View.INVISIBLE);
                }
            }, 180);
            return;
        }
        if (creditsView.getVisibility() == View.VISIBLE) {
            creditsView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(180);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    creditsView.setVisibility(View.INVISIBLE);
                }
            }, 180);
        } else finish();
    }

    void initializeHelpWebView(){
        helpWebView.setBackgroundColor(Color.TRANSPARENT);
        helpWebView.getSettings().setAllowFileAccess(true);
        helpWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        helpWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        helpWebView.getSettings().setAllowContentAccess(true);
        helpWebView.setWebViewClient(new WebViewClient());
    }

    void initializeCreditsWebView(){
        creditsWebView.setBackgroundColor(Color.TRANSPARENT);
        creditsWebView.getSettings().setAllowFileAccess(true);
        creditsWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        creditsWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        creditsWebView.getSettings().setAllowContentAccess(true);
        creditsWebView.setWebViewClient(new WebViewClient());
    }

    void initializePrivacyWebView(){
        privacyPolicyWebView.setBackgroundColor(Color.TRANSPARENT);
        privacyPolicyWebView.getSettings().setAllowFileAccess(true);
        privacyPolicyWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        privacyPolicyWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        privacyPolicyWebView.getSettings().setAllowContentAccess(true);
        privacyPolicyWebView.setWebViewClient(new WebViewClient());
    }
}

package com.randoms.granthsahib;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static androidx.core.content.FileProvider.getUriForFile;

public class ShareHukamnamas extends AppCompatActivity {

    int mode, gurmukhiRed, gurmukhiGreen, gurmukhiBlue, englishRed, englishGreen, englishBlue, punjabiRed, punjabiGreen, punjabiBlue, colorViewTrigger, tempRed, tempGreen, tempBlue, defaultRed, defaultGreen, defaultBlue, MY_PERMISSIONS_REQUEST_WRITE_STORAGE;
    String quoteHeadingString, ShareAsHeadingString, ShareAsSourceString, GurmukhiHeadingString, EnglishTranslationString, PunjabiTranslationString, lineIDtxtString, sourceIDtxtString, navScreenshotColor;

    View mainView, shabadToolBar, settingsMenuView, settingsMenuHandlerView, settingsAreaView, gurmukhiSeekView, englishSettingsView, englishSeekView, punjabiSettingsView, punjabiSeekView, colorPickerView, showColorCreatedView, colorPickerBackgroundView, colorPickerArea, colorGurmukhiBtnView, colorEnglishBtnView, colorPunjabiBtnView;
    TextView quoteHeading, ShareAsHeading, ShareAsSource, GurmukhiHeading, EnglishTranslation, PunjabiTranslation, lineIDtxt, sourceIDtxt, gurmukhiSeekBarProgress, englishSeekBarProgress, punjabiSeekBarProgress, redSeekBarProgress, greenSeekBarProgress, blueSeekBarProgress;
    ImageButton quoteBackBtn, settingsBtn, closeSettingsViewBtn, resetSettingsViewBtn, closeColorPickerViewBtn, screenshotBtn;
    Button onOffGurmukhiBtn, onOffEnglishBtn, onOffPunjabiBtn, cancelColorPickerViewBtn, okayColorPickerViewBtn, restoreDefaultsBtn;
    SeekBar gurmukhiSizeSeekBar, englishSizeSeekBar, punjabiSizeSeekBar, redSeekBar, greenSeekBar, blueSeekBar;

    ScrollView quoteView;

    Boolean gurmukhiOn, englishOn, punjabiOn;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    ArrayList<String> sizeChangeSettings;
    ArrayList <Integer> gurmukhiSizeChange, englishSizeChange, punjabiSizeChange;

    GradientDrawable drawableGurmukhi, drawableEnglish, drawablePunjabi;

    Intent intentShare;
    Uri contentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.AppTheme);

        setTheme(mode);
        setContentView(R.layout.activity_share_hukamnamas);

        mainView = findViewById(R.id.mainView);
        shabadToolBar = findViewById(R.id.shabadToolBar);
        quoteView = findViewById(R.id.quoteView);
        quoteHeading = findViewById(R.id.quoteHeading);
        ShareAsHeading = findViewById(R.id.ShareAsHeading);
        ShareAsSource = findViewById(R.id.ShareAsSource);
        GurmukhiHeading = findViewById(R.id.GurmukhiHeading);
        EnglishTranslation = findViewById(R.id.EnglishTranslation);
        PunjabiTranslation = findViewById(R.id.PunjabiTranslation);
        sourceIDtxt = findViewById(R.id.sourceIDtxt);
        lineIDtxt = findViewById(R.id.lineIDtxt);
        quoteBackBtn = findViewById(R.id.quoteBackBtn);
        screenshotBtn = findViewById(R.id.screenShotBtn);
        settingsBtn = findViewById(R.id.settingsBtn);
        settingsMenuView = findViewById(R.id.settingsMenuView);
        settingsMenuHandlerView = findViewById(R.id.settingsMenuHandlerView);
        settingsAreaView = findViewById(R.id.settingsAreaView);
        gurmukhiSeekView = findViewById(R.id.gurmukhiSeekView);
        englishSettingsView = findViewById(R.id.englishSettingsView);
        englishSeekView = findViewById(R.id.englishSeekView);
        punjabiSettingsView = findViewById(R.id.punjabiSettingsView);
        punjabiSeekView = findViewById(R.id.punjabiSeekView);
        closeSettingsViewBtn = findViewById(R.id.closeSettingsViewBtn);
        resetSettingsViewBtn = findViewById(R.id.resetSettingsViewBtn);
        restoreDefaultsBtn = findViewById(R.id.restoreDefaultsBtn);
        onOffGurmukhiBtn = findViewById(R.id.onOffGurmukhiBtn);
        colorGurmukhiBtnView = findViewById(R.id.colorGurmukhiBtnView);
        onOffEnglishBtn = findViewById(R.id.onOffEnglishBtn);
        colorEnglishBtnView = findViewById(R.id.colorEnglishBtnView);
        onOffPunjabiBtn = findViewById(R.id.onOffPunjabiBtn);
        colorPunjabiBtnView = findViewById(R.id.colorPunjabiBtnView);
        gurmukhiSeekBarProgress = findViewById(R.id.gurmukhiSeekBarProgress);
        englishSeekBarProgress = findViewById(R.id.englishSeekBarProgress);
        punjabiSeekBarProgress = findViewById(R.id.punjabiSeekBarProgress);
        gurmukhiSizeSeekBar = findViewById(R.id.gurmukhiSizeSeekBar);
        englishSizeSeekBar = findViewById(R.id.englishSizeSeekBar);
        punjabiSizeSeekBar = findViewById(R.id.punjabiSizeSeekBar);

        colorPickerView = findViewById(R.id.colorPickerView);
        showColorCreatedView = findViewById(R.id.showColorCreatedView);
        closeColorPickerViewBtn = findViewById(R.id.closeColorPickerViewBtn);
        colorPickerBackgroundView = findViewById(R.id.colorPickerBackgroundView);
        colorPickerArea = findViewById(R.id.colorPickerArea);
        redSeekBar = findViewById(R.id.redSeekBar);
        greenSeekBar = findViewById(R.id.greenSeekBar);
        blueSeekBar = findViewById(R.id.blueSeekBar);
        redSeekBarProgress = findViewById(R.id.redSeekBarProgress);
        greenSeekBarProgress = findViewById(R.id.greenSeekBarProgress);
        blueSeekBarProgress = findViewById(R.id.blueSeekBarProgress);
        cancelColorPickerViewBtn = findViewById(R.id.cancelColorPickerViewBtn);
        okayColorPickerViewBtn = findViewById(R.id.okayColorPickerViewBtn);

        Intent intent = getIntent();
        quoteHeadingString = intent.getStringExtra("quoteHeadingString");
        ShareAsHeadingString = intent.getStringExtra("ShareAsHeadingString");
        ShareAsSourceString = intent.getStringExtra("ShareAsSourceString");
        GurmukhiHeadingString = intent.getStringExtra("GurmukhiHeadingString");
        EnglishTranslationString = intent.getStringExtra("EnglishTranslationString");
        PunjabiTranslationString = intent.getStringExtra("PunjabiTranslationString");
        lineIDtxtString = intent.getStringExtra("lineIDtxtString");
        sourceIDtxtString = intent.getStringExtra("sourceIDtxtString");


        if(mode==R.style.AppTheme){
            gurmukhiRed = gurmukhiGreen = gurmukhiBlue = englishRed = englishGreen = englishBlue = punjabiRed = punjabiGreen = punjabiBlue = defaultRed = defaultGreen = defaultBlue = 51;
            navScreenshotColor = "#FFFFFF";
        } else{
            gurmukhiRed = englishRed = punjabiRed = defaultRed= 199;
            gurmukhiGreen = englishGreen = punjabiGreen = defaultGreen = 210;
            gurmukhiBlue = englishBlue = punjabiBlue = defaultBlue = 223;
            navScreenshotColor = "#151E27";
        }

        intentShare = new Intent();
        intentShare.setAction(Intent.ACTION_SEND);
        intentShare.setType("image/jpeg");
        intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;

        sizeChangeSettings = new ArrayList<String>();
        gurmukhiSizeChange = new ArrayList<Integer>();
        englishSizeChange = new ArrayList<Integer>();
        punjabiSizeChange = new ArrayList<Integer>();

        drawableGurmukhi = (GradientDrawable) colorGurmukhiBtnView.getBackground();
        drawableEnglish = (GradientDrawable) colorEnglishBtnView.getBackground();
        drawablePunjabi = (GradientDrawable) colorPunjabiBtnView.getBackground();

        quoteHeading.setText(quoteHeadingString);
        ShareAsHeading.setText(ShareAsHeadingString);
        ShareAsSource.setText(ShareAsSourceString);
        GurmukhiHeading.setText(GurmukhiHeadingString);
        EnglishTranslation.setText(EnglishTranslationString);
        PunjabiTranslation.setText(PunjabiTranslationString);
        lineIDtxt.setText(lineIDtxtString);
        sourceIDtxt.setText(sourceIDtxtString);

        setDefaultSettings();

        quoteBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsAreaView.setTranslationY(settingsAreaView.getHeight());
                settingsAreaView.setAlpha(0.0f);
                settingsMenuHandlerView.setAlpha(0.0f);
                settingsMenuView.setVisibility(View.VISIBLE);
                settingsMenuHandlerView.setVisibility(View.VISIBLE);
                settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).alpha(1.0f).setDuration(220);
                settingsMenuHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.8f).setDuration(220);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingsAreaView.setAlpha(1.0f);
                        settingsMenuHandlerView.setAlpha(0.8f);
                    }
                }, 220);
            }
        });

        closeSettingsViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(settingsAreaView.getHeight()).alpha(0.0f).setDuration(220);
                settingsMenuHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(220);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingsMenuView.setVisibility(View.INVISIBLE);
                        settingsMenuHandlerView.setAlpha(1.0f);
                    }
                }, 220);
            }
        });

        settingsMenuHandlerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(settingsAreaView.getHeight()).alpha(0.0f).setDuration(220);
                settingsMenuHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(220);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingsMenuView.setVisibility(View.INVISIBLE);
                        settingsMenuHandlerView.setAlpha(1.0f);
                    }
                }, 220);
            }
        });

        screenshotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Permission not granted
                if (ContextCompat.checkSelfPermission(ShareHukamnamas.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ShareHukamnamas.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(ShareHukamnamas.this).setTitle("Permission Needed").setMessage("This permission is needed to save Quote as an Image in your External Storage before Sharing").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(ShareHukamnamas.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                    } else {
                        ActivityCompat.requestPermissions(ShareHukamnamas.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        Toast.makeText(ShareHukamnamas.this, "External Storage access is required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Permission has already been granted
                    if(isExternalStorageWritable()){
                        shabadToolBar.setVisibility(View.INVISIBLE);
                        int navColor = getWindow().getNavigationBarColor();
                        getWindow().setNavigationBarColor(Color.parseColor(navScreenshotColor));
                        View v1 = getWindow().getDecorView().getRootView();
                        v1.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                        v1.setDrawingCacheEnabled(false);
                        getWindow().setNavigationBarColor(navColor);
                        shabadToolBar.setVisibility(View.VISIBLE);

                        //String mPath = Environment.getExternalStorageDirectory().toString() + "/GranthSahibShares/" +"GranthSahib " + sourceIDtxtString +".jpg";
                        String mPath = getExternalFilesDir(null) + "/GranthSahibShares/" +"GranthSahib " + sourceIDtxtString +".jpg";
                        File imageFile = new File(mPath);
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(imageFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        finally {
                            if(outputStream!=null){
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                contentUri = getUriForFile(ShareHukamnamas.this, "com.randoms.granthsahib.fileprovider", imageFile);
                                intentShare.putExtra(Intent.EXTRA_STREAM, contentUri);
                                startActivity(Intent.createChooser(intentShare, "Share as Quote"));
                                try {
                                    outputStream.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    outputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            return;
                        }
                    }
                    else Toast.makeText(ShareHukamnamas.this, "External Storage is not writable at the moment, please try again later", Toast.LENGTH_LONG).show();
                }
            }
        });

        resetSettingsViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sizeChangeSettings.get(sizeChangeSettings.size()-1).equals("Gurmukhi")){
                    int i = gurmukhiSizeChange.get(gurmukhiSizeChange.size()-1);
                    gurmukhiSizeSeekBar.setProgress(i);
                    gurmukhiSeekBarProgress.setText(""+i);
                    GurmukhiHeading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i);
                    gurmukhiSizeChange.remove(gurmukhiSizeChange.size()-1);
                }
                else if(sizeChangeSettings.get(sizeChangeSettings.size()-1).equals("English")){
                    int i = englishSizeChange.get(englishSizeChange.size()-1);
                    englishSizeSeekBar.setProgress(i);
                    englishSeekBarProgress.setText(""+i);
                    EnglishTranslation.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i);
                    englishSizeChange.remove(englishSizeChange.size()-1);
                }
                else {
                    int i = punjabiSizeChange.get(punjabiSizeChange.size()-1);
                    punjabiSizeSeekBar.setProgress(i);
                    punjabiSeekBarProgress.setText(""+i);
                    PunjabiTranslation.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i);
                    punjabiSizeChange.remove(punjabiSizeChange.size()-1);
                }
                sizeChangeSettings.remove(sizeChangeSettings.size()-1);

                if(sizeChangeSettings.isEmpty()){
                    resetSettingsViewBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        restoreDefaultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultSettings();
                resetSettingsViewBtn.setVisibility(View.INVISIBLE);
                GurmukhiHeading.setTextColor(Color.rgb(defaultRed, defaultGreen, defaultBlue));
                EnglishTranslation.setTextColor(Color.rgb(defaultRed, defaultGreen, defaultBlue));
                PunjabiTranslation.setTextColor(Color.rgb(defaultRed, defaultGreen, defaultBlue));
                sizeChangeSettings.clear();
                gurmukhiSizeChange.clear();
                englishSizeChange.clear();
                punjabiSizeChange.clear();
            }
        });

        gurmukhiSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                gurmukhiSeekBarProgress.setText(""+i);
                GurmukhiHeading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                gurmukhiSizeChange.add(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress()==gurmukhiSizeChange.get(gurmukhiSizeChange.size()-1)){
                    gurmukhiSizeChange.remove(gurmukhiSizeChange.size()-1);
                }else{
                    sizeChangeSettings.add("Gurmukhi");
                    resetSettingsViewBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        englishSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                englishSeekBarProgress.setText(""+i);
                EnglishTranslation.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                englishSizeChange.add(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress()==englishSizeChange.get(englishSizeChange.size()-1)){
                    englishSizeChange.remove(englishSizeChange.size()-1);
                }else{
                    sizeChangeSettings.add("English");
                    resetSettingsViewBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        punjabiSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                punjabiSeekBarProgress.setText(""+i);
                PunjabiTranslation.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                punjabiSizeChange.add(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress()==punjabiSizeChange.get(punjabiSizeChange.size()-1)){
                    punjabiSizeChange.remove(punjabiSizeChange.size()-1);
                }else{
                    sizeChangeSettings.add("Punjabi");
                    resetSettingsViewBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        onOffGurmukhiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gurmukhiOn){
                    onOffGurmukhiBtn.setText("On");
                    onOffGurmukhiBtn.setSelected(false);
                    GurmukhiHeading.setVisibility(View.GONE);
                    gurmukhiSeekView.setVisibility(View.GONE);
                    colorGurmukhiBtnView.setVisibility(View.GONE);
                } else {
                    onOffGurmukhiBtn.setText("Off");
                    onOffGurmukhiBtn.setSelected(true);
                    GurmukhiHeading.setVisibility(View.VISIBLE);
                    gurmukhiSeekView.setVisibility(View.VISIBLE);
                    colorGurmukhiBtnView.setVisibility(View.VISIBLE);
                }
                gurmukhiOn = !gurmukhiOn;
            }
        });

        onOffEnglishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(englishOn){
                    onOffEnglishBtn.setText("On");
                    onOffEnglishBtn.setSelected(false);
                    EnglishTranslation.setVisibility(View.GONE);
                    englishSeekView.setVisibility(View.GONE);
                    colorEnglishBtnView.setVisibility(View.GONE);
                } else {
                    onOffEnglishBtn.setText("Off");
                    onOffEnglishBtn.setSelected(true);
                    EnglishTranslation.setVisibility(View.VISIBLE);
                    englishSeekView.setVisibility(View.VISIBLE);
                    colorEnglishBtnView.setVisibility(View.VISIBLE);
                }
                englishOn = !englishOn;
            }
        });

        onOffPunjabiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(punjabiOn){
                    onOffPunjabiBtn.setText("On");
                    onOffPunjabiBtn.setSelected(false);
                    PunjabiTranslation.setVisibility(View.GONE);
                    punjabiSeekView.setVisibility(View.GONE);
                    colorPunjabiBtnView.setVisibility(View.GONE);
                } else {
                    onOffPunjabiBtn.setText("Off");
                    onOffPunjabiBtn.setSelected(true);
                    PunjabiTranslation.setVisibility(View.VISIBLE);
                    punjabiSeekView.setVisibility(View.VISIBLE);
                    colorPunjabiBtnView.setVisibility(View.VISIBLE);
                }
                punjabiOn = !punjabiOn;
            }
        });

        colorGurmukhiBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorViewTrigger = 0;
                redSeekBar.setProgress(gurmukhiRed);
                redSeekBarProgress.setText(""+gurmukhiRed);
                tempRed = gurmukhiRed;

                greenSeekBar.setProgress(gurmukhiGreen);
                greenSeekBarProgress.setText(""+gurmukhiGreen);
                tempGreen = gurmukhiGreen;

                blueSeekBar.setProgress(gurmukhiBlue);
                blueSeekBarProgress.setText(""+gurmukhiBlue);
                tempBlue = gurmukhiBlue;

                showColorCreatedView.setBackgroundColor(Color.rgb(gurmukhiRed, gurmukhiGreen, gurmukhiBlue));
                showColorPicker();
            }
        });

        colorEnglishBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorViewTrigger = 1;
                redSeekBar.setProgress(englishRed);
                redSeekBarProgress.setText(""+englishRed);

                greenSeekBar.setProgress(englishGreen);
                greenSeekBarProgress.setText(""+englishGreen);

                blueSeekBar.setProgress(englishBlue);
                blueSeekBarProgress.setText(""+englishBlue);

                showColorCreatedView.setBackgroundColor(Color.rgb(englishRed, englishGreen, englishBlue));
                showColorPicker();
            }
        });

        colorPunjabiBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorViewTrigger = 2;
                redSeekBar.setProgress(punjabiRed);
                redSeekBarProgress.setText(""+punjabiRed);

                greenSeekBar.setProgress(punjabiGreen);
                greenSeekBarProgress.setText(""+punjabiGreen);

                blueSeekBar.setProgress(punjabiBlue);
                blueSeekBarProgress.setText(""+punjabiBlue);

                showColorCreatedView.setBackgroundColor(Color.rgb(punjabiRed, punjabiGreen, punjabiBlue));
                showColorPicker();
            }
        });

        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                redSeekBarProgress.setText(""+i);
                tempRed = i;
                showColorCreatedView.setBackgroundColor(Color.rgb(i, tempGreen, tempBlue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                greenSeekBarProgress.setText(""+i);
                tempGreen = i;
                showColorCreatedView.setBackgroundColor(Color.rgb(tempRed, i, tempBlue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blueSeekBarProgress.setText(""+i);
                tempBlue = i;
                showColorCreatedView.setBackgroundColor(Color.rgb(tempRed, tempGreen, i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        closeColorPickerViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissColorPicker();
            }
        });

        cancelColorPickerViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissColorPicker();
            }
        });

        okayColorPickerViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorViewTrigger==0){
                    GurmukhiHeading.setTextColor(Color.rgb(tempRed, tempGreen, tempBlue));
                    gurmukhiRed = tempRed;
                    gurmukhiGreen = tempGreen;
                    gurmukhiBlue = tempBlue;
                    drawableGurmukhi.setColor(Color.rgb(tempRed, tempGreen, tempBlue));
                } else if(colorViewTrigger==1){
                    EnglishTranslation.setTextColor(Color.rgb(tempRed, tempGreen, tempBlue));
                    englishRed = tempRed;
                    englishGreen = tempGreen;
                    englishBlue = tempBlue;
                    drawableEnglish.setColor(Color.rgb(tempRed, tempGreen, tempBlue));
                } else{
                    PunjabiTranslation.setTextColor(Color.rgb(tempRed, tempGreen, tempBlue));
                    punjabiRed = tempRed;
                    punjabiGreen = tempGreen;
                    punjabiBlue = tempBlue;
                    drawablePunjabi.setColor(Color.rgb(tempRed, tempGreen, tempBlue));
                }
                dismissColorPicker();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (colorPickerView.getVisibility() == View.VISIBLE) {
            dismissColorPicker();
            return;
        }
        if (settingsMenuView.getVisibility() == View.VISIBLE) {
            settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(settingsAreaView.getHeight()).alpha(0.0f).setDuration(220);
            settingsMenuHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(220);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    settingsMenuView.setVisibility(View.INVISIBLE);
                    settingsMenuHandlerView.setAlpha(1.0f);
                }
            }, 220);
            return;
        } else {
            finish();
        }
    }

    void setDefaultSettings(){
        gurmukhiSizeSeekBar.setMax(48);
        gurmukhiSizeSeekBar.setProgress(24);
        gurmukhiSeekBarProgress.setText("24");

        englishSizeSeekBar.setMax(48);
        englishSizeSeekBar.setProgress(15);
        englishSeekBarProgress.setText("15");

        punjabiSizeSeekBar.setMax(48);
        punjabiSizeSeekBar.setProgress(17);
        punjabiSeekBarProgress.setText("17");

        gurmukhiOn = englishOn = punjabiOn = true;
        onOffGurmukhiBtn.setSelected(true);
        onOffEnglishBtn.setSelected(true);
        onOffPunjabiBtn.setSelected(true);

        redSeekBar.setMax(255);
        greenSeekBar.setMax(255);
        blueSeekBar.setMax(255);

        drawableGurmukhi.setColor(Color.rgb(defaultRed, defaultGreen, defaultBlue));
        drawableEnglish.setColor(Color.rgb(defaultRed, defaultGreen, defaultBlue));
        drawablePunjabi.setColor(Color.rgb(defaultRed, defaultGreen, defaultBlue));
    }

    void showColorPicker(){
        colorPickerView.setAlpha(0.0f);
        colorPickerArea.setTranslationX(colorPickerArea.getWidth());
        colorPickerBackgroundView.setTranslationX(colorPickerBackgroundView.getWidth());
        showColorCreatedView.setTranslationX(-showColorCreatedView.getWidth());
        colorPickerView.setVisibility(View.VISIBLE);
        colorPickerArea.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationX(0).setDuration(180);
        colorPickerBackgroundView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationX(0).setDuration(180);
        showColorCreatedView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationX(0).setDuration(180);
        colorPickerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(1.0f).setDuration(180);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                colorPickerView.setAlpha(1.0f);
                colorPickerArea.setTranslationX(0);
                colorPickerBackgroundView.setTranslationX(0);
                showColorCreatedView.setTranslationX(0);
            }
        },180);
    }

    void dismissColorPicker(){
        colorPickerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(180);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                colorPickerView.setVisibility(View.INVISIBLE);
            }
        },180);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //String mPath = Environment.getExternalStorageDirectory().toString() + "/GranthSahibShares";
            String mPath = getExternalFilesDir(null) + "/GranthSahibShares";
            File folder = new File(mPath);
            if(!folder.isDirectory() || !folder.exists()){
                folder.mkdir();
            }
            return true;
        }
        return false;
    }
}

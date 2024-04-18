package com.randoms.granthsahib;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    View dummyBarView, dummySearchArea, realBarView, realSearchArea, numericBarView, numericSearchArea, keyboardView, numericView, settingsMenuView, settingsMenuHandlerView, settingsAreaView;
    View keyboardTouchHandlerView, toolBar, mainScreenView, hukamnamaScreenView, changeSourceView, changeSourcePanel, hukamnamaView, randomShabadView, itemViewOptionsView, itemViewBackgroundHandlerView, itemViewOptionsMenu;
    TextView realSearchBox, numericSearchBox, connectionMessage, currentSource, savedText, itemViewOptionsLineID, itemViewOptionsShabadID, itemViewOptionsSave;
    TextView hukamOnlineHeading, hukamOnlineContent, hukamOnlineHeadingEnglish, hukamOnlineContentEnglish, hukamOnlineAng, hukamOnlineDate, hukamOnlineViakhiaHeading, hukamOnlineViakhiaContent;
    TextView randomShabadHeading, randomShabadContent, randomShabadHeadingEnglish, randomShabadContentEnglish, randomShabadAng, randomShabadDate, randomShabadViakhiaHeading, randomShabadViakhiaContent;
    Button cancelSearch, cancelNumericSearch, shabadBtn, searchENBtn, searchPBBtn, shabadSearchBtn, searchNumericENBtn, punjabiBtn, refreshShabadBtn, archiveBtn, lineIDBtn, shabadIDBtn, cancelSourceChangeBtn, okaySourceChangeBtn;
    Button aB, dayModeBtn, nightModeBtn, fontSizeMinus5Btn, fontSizeMinus3Btn, fontSizeBtn, fontSizePlus3Btn, fontSizePlus7Btn, fontSizePlus11Btn, fontSizePlus15Btn, showViakhiaBtn, showEnglishBtn;
    ImageButton sggsBtn, sundarGutkaBtn, settingsBtn, realBarUndoBtn, numericBarUndoBtn, closeSettingsViewBtn, helpMenuBtn, closeItemViewOptionsViewBtn, sendHukamnamaBtn, sendRandomShabadBtn;
    ImageView searchBoxIcon, numericSearchBoxIcon;
    NumberPicker sourceNumberPicker;
    Boolean setKeyboardView;
    StringBuilder gurmukhiString;
    StringBuilder shabadIDString;
    String source, sourceFullName, uniqueIDToRemove;
    String [] sourceListFullNames;
    String [] sourceListAbbr;
    int [] shabadLimits;
    int tintActive, tintInactive, cancelColorActive, cancelColorInactive, hukamnamaFontSize, hukamnamaViakhiaVisibility, hukamnamaEnglishVisibility;

    RecyclerView gurmukhiSearchResultsRV, savedListRV;

    int mode, upperShabadLimit, sourcePickerLastValue;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    private BroadcastReceiver networkReceiver;

    ArrayList<String> hukamFetch;
    ArrayList<String> randomShabadFetch;

    String randomShabadCurrentDate;
    Boolean setRefreshCalled, shabadMode, restoreSettings, englishSearched;

    Cursor gurmukhiResultsCursor;
    Cursor cursorRandom;
    LinearLayoutManagerWithSmoothScroller linearLayoutManagerWithSmoothScroller, savedLayoutManager;

    ListAdapter listAdapter;
    ListAdapter2 gurmukhiResultAdapter;

    ArrayList<String> savedBani, savedSource, savedAng, savedShabadID, savedUniqueID, sourceComplete, nullUniqueIDs;

    InitializeRandomShabad initializeRandomShabad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = this.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.AppTheme);

        setTheme(mode);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        dummyBarView = findViewById(R.id.dummyBarView);
        dummySearchArea = findViewById(R.id.dummySearchArea);
        realBarView = findViewById(R.id.realBarView);
        realSearchArea = findViewById(R.id.realSearchArea);
        realSearchBox = findViewById(R.id.realSearchBox);

        hukamnamaView = findViewById(R.id.hukamnamaView);
        hukamOnlineHeading = findViewById(R.id.hukamOnlineHeading);
        hukamOnlineContent = findViewById(R.id.hukamOnlineContent);
        hukamOnlineViakhiaHeading = findViewById(R.id.hukamOnlineViakhiaHeading);
        hukamOnlineViakhiaContent = findViewById(R.id.hukamOnlineViakhiaContent);
        hukamOnlineHeadingEnglish = findViewById(R.id.hukamOnlineHeadingEnglish);
        hukamOnlineContentEnglish = findViewById(R.id.hukamOnlineContentEnglish);
        hukamOnlineAng = findViewById(R.id.hukamOnlineAng);
        hukamOnlineDate = findViewById(R.id.hukamOnlineDate);
        connectionMessage = findViewById(R.id.connectionMessage);

        randomShabadView = findViewById(R.id.randomShabadView);
        randomShabadHeading = findViewById(R.id.randomShabadHeading);
        randomShabadContent = findViewById(R.id.randomShabadContent);
        randomShabadViakhiaHeading = findViewById(R.id.randomShabadViakhiaHeading);
        randomShabadViakhiaContent = findViewById(R.id.randomShabadViakhiaContent);
        randomShabadHeadingEnglish = findViewById(R.id.randomShabadHeadingEnglish);
        randomShabadContentEnglish = findViewById(R.id.randomShabadContentEnglish);
        randomShabadAng = findViewById(R.id.randomShabadAng);
        randomShabadDate = findViewById(R.id.randomShabadDate);

        sggsBtn = findViewById(R.id.sggsBtn);
        sundarGutkaBtn = findViewById(R.id.sundarGutkaBtn);
        hukamnamaScreenView = findViewById(R.id.hukamnamaScreen);
        refreshShabadBtn = findViewById(R.id.refreshShabadBtn);
        sendHukamnamaBtn = findViewById(R.id.sendHukamnamaBtn);
        sendRandomShabadBtn = findViewById(R.id.sendRandomShabadBtn);
        realBarUndoBtn = findViewById(R.id.realBarUndoBtn);
        numericBarUndoBtn = findViewById(R.id.numericBarUndoBtn);

        settingsBtn = findViewById(R.id.settingsBtn);
        settingsMenuView = findViewById(R.id.settingsMenuView);
        settingsMenuHandlerView = findViewById(R.id.settingsMenuHandlerView);
        settingsAreaView = findViewById(R.id.settingsAreaView);
        closeSettingsViewBtn = findViewById(R.id.closeSettingsViewBtn);
        helpMenuBtn = findViewById(R.id.helpMenuBtn);
        dayModeBtn = findViewById(R.id.dayModeBtn);
        nightModeBtn = findViewById(R.id.nightModeBtn);
        fontSizeMinus5Btn = findViewById(R.id.fontSizeMinus5Btn);
        fontSizeMinus3Btn = findViewById(R.id.fontSizeMinus3Btn);
        fontSizeBtn = findViewById(R.id.fontSizeBtn);
        fontSizePlus3Btn = findViewById(R.id.fontSizePlus3Btn);
        fontSizePlus7Btn = findViewById(R.id.fontSizePlus7Btn);
        fontSizePlus11Btn = findViewById(R.id.fontSizePlus11Btn);
        fontSizePlus15Btn = findViewById(R.id.fontSizePlus15Btn);
        showViakhiaBtn = findViewById(R.id.showViakhiaBtn);
        showEnglishBtn = findViewById(R.id.showEnglishBtn);

        numericBarView = findViewById(R.id.numericBarView);
        numericSearchArea = findViewById(R.id.numericSearchArea);
        numericSearchBox = findViewById(R.id.numericSearchBox);
        cancelSearch = findViewById(R.id.cancelBtn);
        cancelNumericSearch = findViewById(R.id.numericCancelBtn);
        keyboardView = findViewById(R.id.keyboardView);
        aB = findViewById(R.id.aB);
        numericView = findViewById(R.id.numericView);
        keyboardTouchHandlerView = findViewById(R.id.keyboardTouchHandlerView);
        mainScreenView = findViewById(R.id.mainScreenView);
        toolBar = findViewById(R.id.toolBar);
        searchBoxIcon = findViewById(R.id.searchBoxIcon);
        numericSearchBoxIcon = findViewById(R.id.numericSearchBoxIcon);
        shabadBtn = findViewById(R.id.shabadBtn);
        searchENBtn = findViewById(R.id.searchENBtn);
        searchPBBtn = findViewById(R.id.searchPBBtn);
        shabadSearchBtn = findViewById(R.id.shabadSearchBtn);
        archiveBtn = findViewById(R.id.archiveBtn);
        searchNumericENBtn = findViewById(R.id.searchNumericENBtn);
        punjabiBtn = findViewById(R.id.punjabiBtn);
        lineIDBtn = findViewById(R.id.lineIDBtn);
        shabadIDBtn = findViewById(R.id.shabadIDBtn);
        changeSourceView = findViewById(R.id.changeSourceView);
        changeSourcePanel = findViewById(R.id.changeSourcePanel);
        cancelSourceChangeBtn = findViewById(R.id.cancelSourceChangeBtn);
        okaySourceChangeBtn = findViewById(R.id.okaySourceChangeBtn);
        sourceNumberPicker = findViewById(R.id.sourceNumberPicker);
        currentSource = findViewById(R.id.currentSource);
        savedListRV = findViewById(R.id.savedListRV);
        savedText = findViewById(R.id.savedText);
        gurmukhiSearchResultsRV = findViewById(R.id.gurmukhiSearchResultsRV);

        itemViewOptionsView = findViewById(R.id.itemViewOptionsView);
        itemViewBackgroundHandlerView = findViewById(R.id.itemViewBackgroundHandlerView);
        itemViewOptionsMenu = findViewById(R.id.itemViewOptionsMenu);
        closeItemViewOptionsViewBtn = findViewById(R.id.closeItemViewOptionsViewBtn);
        itemViewOptionsLineID = findViewById(R.id.itemViewOptionsLineID);
        itemViewOptionsShabadID = findViewById(R.id.itemViewOptionsShabadID);
        itemViewOptionsSave = findViewById(R.id.itemViewOptionsSave);

        hukamnamaFontSize = mPreferences.getInt(getString(R.string.hukamnamaFontSize), 19);
        updateFontSize();
        setSelectedFontBtn();
        englishSearched = false;

        hukamnamaViakhiaVisibility = mPreferences.getInt(getString(R.string.hukamnamaViakhiaVisibility), 0);
        hukamnamaEnglishVisibility = mPreferences.getInt(getString(R.string.hukamnamaEnglishVisibility), 0);

        if(hukamnamaViakhiaVisibility==0){
            showViakhiaBtn.setSelected(true);
            randomShabadViakhiaHeading.setVisibility(View.VISIBLE);
            randomShabadViakhiaContent.setVisibility(View.VISIBLE);
        } else {
            showViakhiaBtn.setSelected(false);
            randomShabadViakhiaHeading.setVisibility(View.GONE);
            randomShabadViakhiaContent.setVisibility(View.GONE);
        }

        if(hukamnamaEnglishVisibility==0){
            showEnglishBtn.setSelected(true);
            randomShabadHeadingEnglish.setVisibility(View.VISIBLE);
            randomShabadContentEnglish.setVisibility(View.VISIBLE);
        }else {
            showEnglishBtn.setSelected(false);
            randomShabadHeadingEnglish.setVisibility(View.GONE);
            randomShabadContentEnglish.setVisibility(View.GONE);
        }

        if(mode==R.style.AppTheme){
            tintActive = Color.parseColor("#333333");
            tintInactive = Color.parseColor("#8E8E8E");
            cancelColorActive = Color.parseColor("#C02F1D");
            cancelColorInactive = Color.parseColor("#8E8E8E");
            dayModeBtn.setSelected(true);
        } else {
            tintActive = Color.parseColor("#C7D2DF");
            tintInactive = Color.parseColor("#4B647C");
            cancelColorActive = Color.parseColor("#C7D2DF");
            cancelColorInactive = Color.parseColor("#4B647C");
            nightModeBtn.setSelected(true);
        }

        restoreSettings = mPreferences.getBoolean(getString(R.string.restoreSettings), false);

        if(restoreSettings){
            settingsAreaView.setTranslationY(settingsAreaView.getHeight());
            settingsAreaView.setAlpha(0.0f);
            settingsMenuHandlerView.setAlpha(0.0f);
            settingsMenuView.setVisibility(View.VISIBLE);
            settingsMenuHandlerView.setVisibility(View.VISIBLE);
            settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).alpha(1.0f).setDuration(220);
            settingsMenuHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.9f).setDuration(220);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    settingsAreaView.setAlpha(1.0f);
                    settingsMenuHandlerView.setAlpha(0.9f);
                    mEditor.putBoolean(getString(R.string.restoreSettings), false).commit();
                }
            }, 220);
        }

        linearLayoutManagerWithSmoothScroller = new LinearLayoutManagerWithSmoothScroller(this);
        savedLayoutManager = new LinearLayoutManagerWithSmoothScroller(this);

        linearLayoutManagerWithSmoothScroller.setSmoothScrollbarEnabled(true);
        savedLayoutManager.setSmoothScrollbarEnabled(true);

        gurmukhiSearchResultsRV.setLayoutManager(linearLayoutManagerWithSmoothScroller);

        hukamOnlineHeading.setText(mPreferences.getString(getString(R.string.currentOnlineHukamHeading), ""));
        hukamOnlineContent.setText(mPreferences.getString(getString(R.string.currentOnlineHukamContent), ""));
        hukamOnlineViakhiaContent.setText(mPreferences.getString(getString(R.string.currentOnlineHukamViakhiaContent), ""));
        hukamOnlineHeadingEnglish.setText(mPreferences.getString(getString(R.string.currentOnlineHukamHeadingEnglish), ""));
        hukamOnlineContentEnglish.setText(mPreferences.getString(getString(R.string.currentOnlineHukamContentEnglish), ""));
        hukamOnlineAng.setText(mPreferences.getString(getString(R.string.currentOnlineHukamAng), ""));
        hukamOnlineDate.setText(mPreferences.getString(getString(R.string.lastUpdatedOnlineHukam), ""));

        randomShabadHeading.setText(mPreferences.getString(getString(R.string.randomShabadHeading), ""));
        randomShabadContent.setText(mPreferences.getString(getString(R.string.randomShabadContent), ""));
        randomShabadViakhiaHeading.setText(mPreferences.getString(getString(R.string.randomShabadViakhiaHeading), ""));
        randomShabadViakhiaContent.setText(mPreferences.getString(getString(R.string.randomShabadViakhiaContent), ""));
        randomShabadHeadingEnglish.setText(mPreferences.getString(getString(R.string.randomShabadHeadingEnglish), ""));
        randomShabadContentEnglish.setText(mPreferences.getString(getString(R.string.randomShabadContentEnglish), ""));
        randomShabadAng.setText(mPreferences.getString(getString(R.string.randomShabadAng), ""));
        randomShabadDate.setText(mPreferences.getString(getString(R.string.randomShabadID), ""));

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkHukamIfExists();
            }
        });

        setKeyboardView = true;
        setRefreshCalled = false;
        shabadMode = true;

        shabadIDBtn.setSelected(true);
        gurmukhiString = new StringBuilder();
        shabadIDString = new StringBuilder();
        source = "SGGS";
        sourceFullName = "Sri Guru Granth Sahib Ji";
        sourceListFullNames = new String[]{"Sri Guru Granth Sahib Ji", "Vaar Bhai Gurdas Ji", "Dasam Granth Ji", "Prose Bhai Nand Lal Ji", "Kabit Bhai Gurdas Ji"};
        sourceListAbbr = new String[]{"SGGS", "Vaar", "DasamGranth", "proseBhaiNandLalJi", "kabitBhaiGurdasJi"};
        shabadLimits = new int[]{4238, 940, 3089, 176, 674};
        upperShabadLimit = 4238;
        sourcePickerLastValue = 0;
        randomShabadCurrentDate = mPreferences.getString(getString(R.string.randomShabadDate), "");

        nullUniqueIDs = new ArrayList<String>(Arrays.asList("60400", "60401", "60402", "60403", "60404", "60405", "60406", "60407", "60408", "60409", "60410", "60411", "68203", "88676", "94436"));
        hukamFetch = new ArrayList<String>(6);
        randomShabadFetch = new ArrayList<String>(7);

        getSavedData();

        savedListRV.setLayoutManager(savedLayoutManager);
        listAdapter = new ListAdapter();

        if(savedBani!=null){
            savedListRV.setAdapter(listAdapter);
        }

        sourceNumberPicker.setMinValue(0);
        sourceNumberPicker.setMaxValue(4);
        sourceNumberPicker.setValue(0);
        sourceNumberPicker.setDisplayedValues(sourceListFullNames);
        sourceNumberPicker.setWrapSelectorWheel(false);

        archiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ArchiveActivity.class);
                startActivity(intent);
            }
        });
        refreshShabadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRefreshCalled = true;
                if(initializeRandomShabad!=null){
                    initializeRandomShabad.cancel(true);
                }
                //cursorRandom = null;
                initializeRandomShabad = new InitializeRandomShabad();
                initializeRandomShabad.execute();
                refreshShabadBtn.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(220);
                refreshShabadBtn.setVisibility(View.INVISIBLE);
                sendRandomShabadBtn.setVisibility(View.INVISIBLE);
                refreshShabadBtn.setAlpha(0.0f);
            }
        });

        shabadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setKeyboardView = false;
                cancelNumericSearch.setText("Cancel");
                cancelNumericSearch.setTextColor(cancelColorActive);
                numericSearchBoxIcon.setColorFilter(tintActive);
                numericBarView.setVisibility(View.VISIBLE);
                realBarView.setVisibility(View.INVISIBLE);
                keyboardView.setVisibility(View.INVISIBLE);
                numericView.setVisibility(View.VISIBLE);
                cancelSearch.setText("");
                if(!shabadIDString.toString().equals("")){
                    numericBarUndoBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        punjabiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setKeyboardView = true;
                cancelSearch.setText("Cancel");
                cancelSearch.setTextColor(cancelColorActive);
                searchBoxIcon.setColorFilter(tintActive);
                realBarView.setVisibility(View.VISIBLE);
                numericBarView.setVisibility(View.INVISIBLE);
                numericView.setVisibility(View.INVISIBLE);
                keyboardView.setVisibility(View.VISIBLE);
                cancelNumericSearch.setText("");
                if(!gurmukhiString.toString().equals("")){
                    realBarUndoBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        dummySearchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaScreenView.setVisibility(View.INVISIBLE);
                mainScreenView.setVisibility(View.VISIBLE);
                if(setKeyboardView){
                    realBarView.setVisibility(View.VISIBLE);
                    searchBoxIcon.setColorFilter(tintActive);
                    cancelSearch.setText("Cancel");
                    cancelSearch.setTextColor(cancelColorActive);
                    dummyBarView.setVisibility(View.INVISIBLE);
                    numericBarView.setVisibility(View.INVISIBLE);

                    numericView.setVisibility(View.INVISIBLE);
                    keyboardView.setAlpha(0.0f);
                    keyboardView.setVisibility(View.VISIBLE);
                    keyboardView.setTranslationY(keyboardView.getHeight());
                    keyboardView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).alpha(1.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            keyboardView.setTranslationY(0);
                            keyboardView.setAlpha(1.0f);
                            keyboardTouchHandlerView.setVisibility(View.VISIBLE);
                            keyboardTouchHandlerView.setTranslationY(toolBar.getHeight());
                        }
                    }, 220);
                } else {
                    numericBarView.setVisibility(View.VISIBLE);
                    numericSearchBoxIcon.setColorFilter(tintActive);
                    cancelNumericSearch.setText("Cancel");
                    cancelNumericSearch.setTextColor(cancelColorActive);
                    dummyBarView.setVisibility(View.INVISIBLE);
                    realBarView.setVisibility(View.INVISIBLE);

                    keyboardView.setVisibility(View.INVISIBLE);
                    numericView.setAlpha(0.0f);
                    numericView.setVisibility(View.VISIBLE);
                    numericView.setTranslationY(numericView.getHeight());
                    numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).alpha(1.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            numericView.setTranslationY(0);
                            numericView.setAlpha(1.0f);
                            keyboardTouchHandlerView.setVisibility(View.VISIBLE);
                            keyboardTouchHandlerView.setTranslationY(toolBar.getHeight());
                        }
                    }, 220);
                }
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dummyBarView.setVisibility(View.VISIBLE);
                realBarView.setVisibility(View.INVISIBLE);
                numericBarView.setVisibility(View.INVISIBLE);
                mainScreenView.setVisibility(View.INVISIBLE);
                hukamnamaScreenView.setVisibility(View.VISIBLE);
                cancelSearch.setText("");
                cancelNumericSearch.setText("");

                keyboardTouchHandlerView.setTranslationY(0);
                keyboardTouchHandlerView.setVisibility(View.INVISIBLE);

                if(keyboardView.getVisibility() == View.VISIBLE){
                    keyboardView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(keyboardView.getHeight()).alpha(0.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            keyboardView.setVisibility(View.INVISIBLE);
                        }
                    }, 220);
                }
                gurmukhiString.setLength(0);
                shabadIDString.setLength(0);
                realSearchBox.setText("");
                numericSearchBox.setText("");
                realBarUndoBtn.setVisibility(View.INVISIBLE);
                gurmukhiSearchResultsRV.setVisibility(View.INVISIBLE);
            }
        });
        cancelNumericSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dummyBarView.setVisibility(View.VISIBLE);
                numericBarView.setVisibility(View.INVISIBLE);
                realBarView.setVisibility(View.INVISIBLE);
                mainScreenView.setVisibility(View.INVISIBLE);
                hukamnamaScreenView.setVisibility(View.VISIBLE);
                cancelNumericSearch.setText("");
                cancelSearch.setText("");

                keyboardTouchHandlerView.setTranslationY(0);
                keyboardTouchHandlerView.setVisibility(View.INVISIBLE);

                if(numericView.getVisibility() == View.VISIBLE){
                    numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(numericView.getHeight()).alpha(0.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            numericView.setVisibility(View.INVISIBLE);
                        }
                    }, 220);
                }
                gurmukhiString.setLength(0);
                shabadIDString.setLength(0);
                realSearchBox.setText("");
                numericSearchBox.setText("");
                numericBarUndoBtn.setVisibility(View.INVISIBLE);
                gurmukhiSearchResultsRV.setVisibility(View.INVISIBLE);
            }
        });
        keyboardTouchHandlerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBoxIcon.setColorFilter(tintInactive);
                cancelSearch.setTextColor(cancelColorInactive);
                realBarUndoBtn.setVisibility(View.INVISIBLE);
                numericBarUndoBtn.setVisibility(View.INVISIBLE);
                keyboardTouchHandlerView.setTranslationY(0);
                keyboardTouchHandlerView.setVisibility(View.INVISIBLE);
                if(setKeyboardView){
                    keyboardView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(keyboardView.getHeight()).alpha(0.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            keyboardView.setVisibility(View.INVISIBLE);
                        }
                    }, 220);
                } else{
                    numericSearchBoxIcon.setColorFilter(tintInactive);
                    cancelNumericSearch.setTextColor(cancelColorInactive);
                    numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(numericView.getHeight()).alpha(0.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            numericView.setVisibility(View.INVISIBLE);
                        }
                    }, 220);
                }
            }
        });
        realSearchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(keyboardView.getVisibility() == View.INVISIBLE){
                    searchBoxIcon.setColorFilter(tintActive);
                    cancelSearch.setTextColor(cancelColorActive);
                    keyboardView.setAlpha(0.0f);
                    keyboardView.setVisibility(View.VISIBLE);
                    keyboardView.setTranslationY(keyboardView.getHeight());
                    keyboardView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).alpha(1.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            keyboardView.setTranslationY(0);
                            keyboardView.setAlpha(1.0f);
                            keyboardTouchHandlerView.setVisibility(View.VISIBLE);
                            keyboardTouchHandlerView.setTranslationY(toolBar.getHeight());
                        }
                    }, 220);
                    if(!gurmukhiString.toString().equals("")){
                        realBarUndoBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        numericSearchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numericView.getVisibility() ==  View.INVISIBLE){
                    numericSearchBoxIcon.setColorFilter(tintActive);
                    cancelNumericSearch.setTextColor(cancelColorActive);
                    numericView.setAlpha(0.0f);
                    numericView.setVisibility(View.VISIBLE);
                    numericView.setTranslationY(numericView.getHeight());
                    numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).alpha(1.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            numericView.setTranslationY(0);
                            numericView.setAlpha(1.0f);
                            keyboardTouchHandlerView.setVisibility(View.VISIBLE);
                            keyboardTouchHandlerView.setTranslationY(toolBar.getHeight());
                        }
                    }, 220);
                    if(!shabadIDString.toString().equals("")){
                        numericBarUndoBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        shabadSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shabadMode){
                    if(shabadIDString.toString().equals("") || Integer.parseInt(shabadIDString.toString())>upperShabadLimit){
                        if(shabadIDString.toString().equals("")){
                            Toast.makeText(MainActivity.this, "Enter Shabad ID to Search", Toast.LENGTH_LONG).show();
                        } else Toast.makeText(MainActivity.this, "For "+sourceListFullNames[sourceNumberPicker.getValue()]+" Shabad ID should not exceed "+upperShabadLimit, Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(MainActivity.this, ShabadActivity.class);
                    intent.putExtra("searchShabad", shabadIDString.toString());
                    intent.putExtra("source", source);
                    intent.putExtra("sourceFullName", sourceFullName);
                    intent.putExtra("upperShabadLimit", upperShabadLimit);
                    intent.putExtra("uniqueID", 0);
                    intent.putExtra("searchMode", "Shabad");
                    intent.putExtra("BaniString", "NA");
                    startActivity(intent);
                }
                else{
                    if(shabadIDString.toString().equals("") || nullUniqueIDs.contains(shabadIDString.toString())){
                        if(shabadIDString.toString().equals("")){
                            Toast.makeText(MainActivity.this, "Enter Shabad ID to Search", Toast.LENGTH_LONG).show();
                        } else Toast.makeText(MainActivity.this, "Invalid Line ID, Please Recheck", Toast.LENGTH_LONG).show();
                        return;
                    }
                    DatabaseHelper mHelper = new DatabaseHelper(MainActivity.this);
                    SQLiteDatabase db = mHelper.openDatabase();
                    gurmukhiResultsCursor = db.rawQuery("SELECT * FROM Shabad_ID WHERE _ID=?", new String[]{shabadIDString.toString()});
                    Boolean check = gurmukhiResultsCursor.moveToFirst();
                    if(!check){
                        Toast.makeText(MainActivity.this, "Invalid Line ID, Please Recheck", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(MainActivity.this, ShabadActivity.class);
                    String source = gurmukhiResultsCursor.getString(8);
                    String shabadID = Integer.toString(gurmukhiResultsCursor.getInt(3));
                    String sourceFullName;
                    int upperLimit, uniqueID;
                    uniqueID = gurmukhiResultsCursor.getInt(0);

                    switch (source){
                        case "SGGS":
                            sourceFullName = "Sri Guru Granth Sahib Ji";
                            upperLimit = 4238;
                            break;
                        case "Vaar":
                            sourceFullName = "Vaar Bhai Gurdas Ji";
                            upperLimit = 940;
                            break;
                        case "DasamGranth":
                            sourceFullName = "Dasam Granth Ji";
                            upperLimit = 3089;
                            break;
                        case "proseBhaiNandLalJi":
                            sourceFullName = "Prose Bhai Nand Lal Ji";
                            upperLimit = 176;
                            break;
                        case "kabitBhaiGurdasJi":
                            sourceFullName = "Kabit Bhai Gurdas Ji";
                            upperLimit = 674;
                            break;
                        default:
                            sourceFullName = "Sri Guru Granth Sahib Ji";
                            upperLimit = 4238;
                            break;
                    }

                    intent.putExtra("searchShabad", shabadID);
                    intent.putExtra("source", source);
                    intent.putExtra("sourceFullName", sourceFullName);
                    intent.putExtra("upperShabadLimit", upperLimit);
                    intent.putExtra("uniqueID", uniqueID);
                    intent.putExtra("searchMode", "Gurmukhi");
                    intent.putExtra("BaniString", gurmukhiResultsCursor.getString(5));
                    intent.putExtra("Ang", ""+gurmukhiResultsCursor.getInt(1));
                    startActivity(intent);
                    db.close();
                }
            }
        });
        realBarUndoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gurmukhiString.length()!=0) {
                    gurmukhiString.deleteCharAt(gurmukhiString.length() - 1);
                    realSearchBox.setText(gurmukhiString.toString());
                }
                if(gurmukhiString.length()==0) {
                    realSearchBox.setText("");
                    realBarUndoBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        numericBarUndoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shabadIDString.length()!=0) {
                    shabadIDString.deleteCharAt(shabadIDString.length() - 1);
                    numericSearchBox.setText(shabadIDString.toString());
                }
                if(shabadIDString.length()==0) {
                    numericSearchBox.setText("");
                    numericBarUndoBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        realBarUndoBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                gurmukhiString.setLength(0);
                realSearchBox.setText("");
                realBarUndoBtn.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        numericBarUndoBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                shabadIDString.setLength(0);
                numericSearchBox.setText("");
                numericBarUndoBtn.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        searchENBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                englishSearched = true;
                Intent intent = new Intent(MainActivity.this, EnglishSearch.class);
                startActivity(intent);
            }
        });

        searchNumericENBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                englishSearched = true;
                Intent intent = new Intent(MainActivity.this, EnglishSearch.class);
                startActivity(intent);
            }
        });

        changeSourcePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSourceView.setVisibility(View.VISIBLE);
            }
        });
        sggsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AngActivity.class);
                intent.putExtra("mode", mode);
                startActivity(intent);
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
                settingsMenuHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.9f).setDuration(220);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingsAreaView.setAlpha(1.0f);
                        settingsMenuHandlerView.setAlpha(0.9f);
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

        dayModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode==R.style.DarkTheme){
                    dayModeBtn.setSelected(true);
                    nightModeBtn.setSelected(false);
                    settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(settingsAreaView.getHeight()).alpha(0.0f).setDuration(220);
                    settingsMenuHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            settingsMenuView.setVisibility(View.INVISIBLE);
                            settingsMenuHandlerView.setAlpha(1.0f);
                            mEditor.putInt(getString(R.string.mode), R.style.AppTheme).commit();
                            mEditor.putBoolean(getString(R.string.restoreSettings), true).commit();
                            recreate();
                        }
                    }, 220);
                }
            }
        });

        nightModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == R.style.AppTheme){
                    dayModeBtn.setSelected(false);
                    nightModeBtn.setSelected(true);
                    settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(settingsAreaView.getHeight()).alpha(0.0f).setDuration(220);
                    settingsMenuHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            settingsMenuView.setVisibility(View.INVISIBLE);
                            settingsMenuHandlerView.setAlpha(1.0f);
                            mEditor.putInt(getString(R.string.mode), R.style.DarkTheme).commit();
                            mEditor.putBoolean(getString(R.string.restoreSettings), true).commit();
                            recreate();
                        }
                    }, 220);
                }
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

        okaySourceChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                source = sourceListAbbr[sourceNumberPicker.getValue()];
                upperShabadLimit = shabadLimits[sourceNumberPicker.getValue()];
                currentSource.setText("Source : "+sourceListFullNames[sourceNumberPicker.getValue()]);
                sourcePickerLastValue = sourceNumberPicker.getValue();
                sourceFullName = sourceListFullNames[sourceNumberPicker.getValue()];
                changeSourceView.setVisibility(View.INVISIBLE);
            }
        });
        cancelSourceChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sourceNumberPicker.setValue(sourcePickerLastValue);
                changeSourceView.setVisibility(View.INVISIBLE);
            }
        });

        searchPBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper mHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = mHelper.openDatabase();

                String search = "%"+gurmukhiString.toString()+"%";
                gurmukhiResultsCursor = db.rawQuery("SELECT * FROM Shabad_ID WHERE FIRST_LETTERS LIKE ?", new String[]{search});

                Boolean check = gurmukhiResultsCursor.moveToNext();
                if(!check){
                    Toast.makeText(MainActivity.this, "No Results Found", Toast.LENGTH_LONG).show();
                    return;
                }
                searchBoxIcon.setColorFilter(tintInactive);
                cancelSearch.setTextColor(cancelColorInactive);
                realBarUndoBtn.setVisibility(View.INVISIBLE);
                keyboardTouchHandlerView.setTranslationY(0);
                keyboardTouchHandlerView.setVisibility(View.INVISIBLE);

                if(keyboardView.getVisibility() == View.VISIBLE){
                    keyboardView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(keyboardView.getHeight()).alpha(1.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            keyboardView.setVisibility(View.INVISIBLE);
                        }
                    }, 220);
                }

                gurmukhiResultAdapter = new ListAdapter2();
                gurmukhiSearchResultsRV.setAdapter(gurmukhiResultAdapter);
                mainScreenView.setVisibility(View.INVISIBLE);
                gurmukhiSearchResultsRV.setVisibility(View.VISIBLE);
                db.close();
            }
        });

        lineIDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shabadMode = false;
                lineIDBtn.setSelected(true);
                shabadIDBtn.setSelected(false);
                if(changeSourcePanel.getVisibility()!=View.GONE){
                    changeSourcePanel.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleY(0.0f).scaleX(0.0f).setDuration(180);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            changeSourcePanel.setScaleY(0.0f);
                            changeSourcePanel.setScaleX(0.0f);
                            changeSourcePanel.setVisibility(View.GONE);
                        }
                    }, 180);
                }
            }
        });

        shabadIDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shabadMode = true;
                shabadIDBtn.setSelected(true);
                lineIDBtn.setSelected(false);
                if(changeSourcePanel.getVisibility()!=View.VISIBLE){
                    changeSourcePanel.setVisibility(View.VISIBLE);
                    changeSourcePanel.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleY(1.0f).scaleX(1.0f).setDuration(180);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            changeSourcePanel.setScaleY(1.0f);
                            changeSourcePanel.setScaleX(1.0f);
                        }
                    }, 180);
                }
            }
        });

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
                    if(hukamOnlineContent.getText().toString().equals("")){
                        connectionMessage.setText("Loading... Please wait!!!");
                    }
                    checkHukamIfExists();
                    new InitializeHukamnamaOnline().execute();
                    // Other Stuff
                }
                else {
                    checkHukamIfExists();
                    if(hukamOnlineContent.getText().toString().equals("")){
                        connectionMessage.setText("Please, Connect to the Internet");
                    }
                }
            }
        };

        fontSizeMinus5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 14;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.hukamnamaFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizeMinus3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 16;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.hukamnamaFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 19;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.hukamnamaFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizePlus3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 22;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.hukamnamaFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizePlus7Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 26;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.hukamnamaFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizePlus11Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 30;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.hukamnamaFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizePlus15Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 34;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.hukamnamaFontSize), hukamnamaFontSize).commit();
            }
        });
        showViakhiaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hukamnamaViakhiaVisibility==0){
                    showViakhiaBtn.setSelected(false);
                    hukamnamaViakhiaVisibility=8;
                    checkHukamIfExists();
                    randomShabadViakhiaHeading.setVisibility(View.GONE);
                    randomShabadViakhiaContent.setVisibility(View.GONE);
                    mEditor.putInt(getString(R.string.hukamnamaViakhiaVisibility), 8).commit();
                } else {
                    showViakhiaBtn.setSelected(true);
                    hukamnamaViakhiaVisibility=0;
                    checkHukamIfExists();
                    randomShabadViakhiaHeading.setVisibility(View.VISIBLE);
                    randomShabadViakhiaContent.setVisibility(View.VISIBLE);
                    mEditor.putInt(getString(R.string.hukamnamaViakhiaVisibility), 0).commit();
                }
            }
        });
        showEnglishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hukamnamaEnglishVisibility==0){
                    showEnglishBtn.setSelected(false);
                    hukamnamaEnglishVisibility=8;
                    checkHukamIfExists();
                    randomShabadHeadingEnglish.setVisibility(View.GONE);
                    randomShabadContentEnglish.setVisibility(View.GONE);
                    mEditor.putInt(getString(R.string.hukamnamaEnglishVisibility), 8).commit();
                } else {
                    showEnglishBtn.setSelected(true);
                    hukamnamaEnglishVisibility=0;
                    checkHukamIfExists();
                    randomShabadHeadingEnglish.setVisibility(View.VISIBLE);
                    randomShabadContentEnglish.setVisibility(View.VISIBLE);
                    mEditor.putInt(getString(R.string.hukamnamaEnglishVisibility), 0).commit();
                }
            }
        });

        itemViewBackgroundHandlerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outsideItemViewCloseOptionsView();
            }
        });

        closeItemViewOptionsViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outsideItemViewCloseOptionsView();
            }
        });
        itemViewOptionsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = savedUniqueID.indexOf(uniqueIDToRemove);
                savedBani.remove(index);
                savedSource.remove(index);
                savedAng.remove(index);
                savedShabadID.remove(index);
                savedUniqueID.remove(index);
                sourceComplete.remove(index);
                saveData();

                Toast.makeText(MainActivity.this, "Removed from Saved", Toast.LENGTH_SHORT).show();
                listAdapter.notifyDataSetChanged();
                outsideItemViewCloseOptionsView();
            }
        });

        aB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                searchData("E");
                return true;
            }
        });

        helpMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        sundarGutkaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SundarGutka.class);
                startActivity(intent);
            }
        });

        sendHukamnamaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mPreferences.getString(getString(R.string.currentOnlineHukamContent), "").equals("")){
                    Toast.makeText(MainActivity.this, "Hukamnama not found, Try again later", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ShareHukamnamas.class);

                intent.putExtra("quoteHeadingString", "Share Hukamnama");
                intent.putExtra("ShareAsHeadingString", "Hukamnama");
                intent.putExtra("ShareAsSourceString", "Daily From Sri Darbar Sahib");
                String line = mPreferences.getString(getString(R.string.currentOnlineHukamHeading), "")+"\n"+mPreferences.getString(getString(R.string.currentOnlineHukamContent), "");
                intent.putExtra("GurmukhiHeadingString", line);
                line = mPreferences.getString(getString(R.string.currentOnlineHukamHeadingEnglish), "")+"\n"+mPreferences.getString(getString(R.string.currentOnlineHukamContentEnglish), "");
                intent.putExtra("EnglishTranslationString", line);
                line = mPreferences.getString(getString(R.string.currentOnlineHukamViakhiaContent), "");
                intent.putExtra("PunjabiTranslationString", line);
                intent.putExtra("lineIDtxtString", mPreferences.getString(getString(R.string.currentOnlineHukamAng), ""));
                intent.putExtra("sourceIDtxtString", mPreferences.getString(getString(R.string.lastUpdatedOnlineHukam), ""));

                startActivity(intent);
            }
        });

        sendRandomShabadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mPreferences.getString(getString(R.string.currentOnlineHukamContent), "").equals("")){
                    Toast.makeText(MainActivity.this, "Shabad For You not found, Try again later", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ShareHukamnamas.class);

                intent.putExtra("quoteHeadingString", "Share Shabad For You");
                intent.putExtra("ShareAsHeadingString", "Shabad For You");
                intent.putExtra("ShareAsSourceString", "Generated In-App Randomly");
                String line = mPreferences.getString(getString(R.string.randomShabadHeading), "")+"\n"+mPreferences.getString(getString(R.string.randomShabadContent), "");
                intent.putExtra("GurmukhiHeadingString", line);
                line = mPreferences.getString(getString(R.string.randomShabadHeadingEnglish), "")+"\n"+mPreferences.getString(getString(R.string.randomShabadContentEnglish), "");
                intent.putExtra("EnglishTranslationString", line);
                line = mPreferences.getString(getString(R.string.randomShabadViakhiaContent), "");
                intent.putExtra("PunjabiTranslationString", line);
                intent.putExtra("lineIDtxtString", mPreferences.getString(getString(R.string.randomShabadAng), ""));
                intent.putExtra("sourceIDtxtString", mPreferences.getString(getString(R.string.randomShabadID), ""));

                startActivity(intent);
            }
        });
    }

    public void checkHukamIfExists(){
        if(!hukamOnlineContent.getText().toString().equals("")){
            connectionMessage.setVisibility(View.GONE);
            hukamOnlineHeading.setVisibility(View.VISIBLE);
            hukamOnlineContent.setVisibility(View.VISIBLE);
            hukamOnlineViakhiaHeading.setVisibility(hukamnamaViakhiaVisibility);
            hukamOnlineViakhiaContent.setVisibility(hukamnamaViakhiaVisibility);
            hukamOnlineHeadingEnglish.setVisibility(hukamnamaEnglishVisibility);
            hukamOnlineContentEnglish.setVisibility(hukamnamaEnglishVisibility);
            hukamOnlineAng.setVisibility(View.VISIBLE);
            hukamOnlineDate.setVisibility(View.VISIBLE);
        } else{
            connectionMessage.setVisibility(View.VISIBLE);
            hukamOnlineHeading.setVisibility(View.GONE);
            hukamOnlineContent.setVisibility(View.GONE);
            hukamOnlineViakhiaHeading.setVisibility(View.GONE);
            hukamOnlineViakhiaContent.setVisibility(View.GONE);
            hukamOnlineHeadingEnglish.setVisibility(View.GONE);
            hukamOnlineContentEnglish.setVisibility(View.GONE);
            hukamOnlineAng.setVisibility(View.GONE);
            hukamOnlineDate.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.aB:
                searchData("a");
                break;
            case R.id.AB:
                searchData("A");
                break;
            case R.id.eB:
                searchData("e");
                break;
            case R.id.sB:
                searchData("s");
                break;
            case R.id.hB:
                searchData("h");
                break;
            case R.id.kB:
                searchData("k");
                break;
            case R.id.KB:
                searchData("K");
                break;
            case R.id.gB:
                searchData("g");
                break;
            case R.id.GB:
                searchData("G");
                break;
            case R.id.ghB:
                searchData("|");
                break;
            case R.id.cB:
                searchData("c");
                break;
            case R.id.CB:
                searchData("C");
                break;
            case R.id.jB:
                searchData("j");
                break;
            case R.id.JB:
                searchData("J");
                break;
            case R.id.jhB:
                searchData("\\");
                break;
            case R.id.tB:
                searchData("t");
                break;
            case R.id.TB:
                searchData("T");
                break;
            case R.id.fB:
                searchData("f");
                break;
            case R.id.FB:
                searchData("F");
                break;
            case R.id.xB:
                searchData("x");
                break;
            case R.id.qB:
                searchData("q");
                break;
            case R.id.QB:
                searchData("Q");
                break;
            case R.id.dB:
                searchData("d");
                break;
            case R.id.DB:
                searchData("D");
                break;
            case R.id.nB:
                searchData("n");
                break;
            case R.id.pB:
                searchData("p");
                break;
            case R.id.PB:
                searchData("P");
                break;
            case R.id.bB:
                searchData("b");
                break;
            case R.id.BB:
                searchData("B");
                break;
            case R.id.mB:
                searchData("m");
                break;
            case R.id.XB:
                searchData("X");
                break;
            case R.id.rB:
                searchData("r");
                break;
            case R.id.lB:
                searchData("l");
                break;
            case R.id.vB:
                searchData("v");
                break;
            case R.id.VB:
                searchData("V");
                break;
            case R.id.numberOne:
                searchShabad("1");
                break;
            case R.id.numberTwo:
                searchShabad("2");
                break;
            case R.id.numberThree:
                searchShabad("3");
                break;
            case R.id.numberFour:
                searchShabad("4");
                break;
            case R.id.numberFive:
                searchShabad("5");
                break;
            case R.id.numberSix:
                searchShabad("6");
                break;
            case R.id.numberSeven:
                searchShabad("7");
                break;
            case R.id.numberEight:
                searchShabad("8");
                break;
            case R.id.numberNine:
                searchShabad("9");
                break;
            case R.id.numberZero:
                searchShabad("0");
                break;
            default:
                break;
        }
    }
    private void searchData(String str) {
        if (gurmukhiString.length() < 15) {
            if (str.contentEquals("")) {
                gurmukhiString.setLength(0);
                gurmukhiString.append(str);
                realSearchBox.setText(gurmukhiString.toString());
                realBarUndoBtn.setVisibility(View.INVISIBLE);
            } else {
                gurmukhiString.append(str);
                realSearchBox.setText(gurmukhiString.toString());
                realBarUndoBtn.setVisibility(View.VISIBLE);
            }
        }else Toast.makeText(MainActivity.this, "Search Limit Reached", Toast.LENGTH_SHORT).show();
    }
    private void searchShabad(String str) {
        if (shabadIDString.length() < 7) {
            if (str.contentEquals("")) {
                shabadIDString.setLength(0);
                shabadIDString.append(str);
                numericSearchBox.setText(shabadIDString.toString());
                numericBarUndoBtn.setVisibility(View.INVISIBLE);
            } else {
                shabadIDString.append(str);
                numericSearchBox.setText(shabadIDString.toString());
                numericBarUndoBtn.setVisibility(View.VISIBLE);
            }
        }else Toast.makeText(MainActivity.this, "Search Limit Reached", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if(itemViewOptionsView.getVisibility()==View.VISIBLE){
            outsideItemViewCloseOptionsView();
            return;
        }
        if(settingsMenuView.getVisibility()==View.VISIBLE){
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
        }
        if(gurmukhiSearchResultsRV.getVisibility() == View.VISIBLE){
            if(keyboardView.getVisibility()==View.VISIBLE || numericView.getVisibility()==View.VISIBLE ){
                if(setKeyboardView){
                    searchBoxIcon.setColorFilter(tintInactive);
                    cancelSearch.setTextColor(cancelColorInactive);
                    realBarUndoBtn.setVisibility(View.INVISIBLE);
                    keyboardView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(keyboardView.getHeight()).alpha(0.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            keyboardView.setVisibility(View.INVISIBLE);
                            keyboardTouchHandlerView.setTranslationY(0);
                            keyboardTouchHandlerView.setVisibility(View.INVISIBLE);
                        }
                    }, 220);
                }else{
                    numericSearchBoxIcon.setColorFilter(tintInactive);
                    cancelNumericSearch.setTextColor(cancelColorInactive);
                    numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(numericView.getHeight()).alpha(0.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            numericView.setVisibility(View.INVISIBLE);
                            keyboardTouchHandlerView.setTranslationY(0);
                            numericBarUndoBtn.setVisibility(View.VISIBLE);
                            keyboardTouchHandlerView.setVisibility(View.INVISIBLE);
                        }
                    }, 220);
                }
            } else if(keyboardView.getVisibility()==View.INVISIBLE && numericView.getVisibility()==View.INVISIBLE){
                gurmukhiString.setLength(0);
                realSearchBox.setText("");
                realBarUndoBtn.setVisibility(View.INVISIBLE);
                numericBarUndoBtn.setVisibility(View.INVISIBLE);
                mainScreenView.setVisibility(View.VISIBLE);
                gurmukhiSearchResultsRV.setVisibility(View.INVISIBLE);
            }
            return;
        }

        if (changeSourceView.getVisibility() == View.VISIBLE){
            sourceNumberPicker.setValue(sourcePickerLastValue);
            changeSourceView.setVisibility(View.INVISIBLE);
            return;
        }
        if(dummyBarView.getVisibility()==View.VISIBLE){
            super.onBackPressed();
        }
        else if(keyboardView.getVisibility()==View.VISIBLE || numericView.getVisibility()==View.VISIBLE){
            if(setKeyboardView){
                searchBoxIcon.setColorFilter(tintInactive);
                cancelSearch.setTextColor(cancelColorInactive);
                realBarUndoBtn.setVisibility(View.INVISIBLE);
                keyboardView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(keyboardView.getHeight()).alpha(0.0f).setDuration(220);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        keyboardView.setVisibility(View.INVISIBLE);
                        keyboardTouchHandlerView.setTranslationY(0);
                        keyboardTouchHandlerView.setVisibility(View.INVISIBLE);
                    }
                }, 220);
            }else{
                numericSearchBoxIcon.setColorFilter(tintInactive);
                cancelNumericSearch.setTextColor(cancelColorInactive);
                numericBarUndoBtn.setVisibility(View.INVISIBLE);
                numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(numericView.getHeight()).alpha(0.0f).setDuration(220);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        numericView.setVisibility(View.INVISIBLE);
                        keyboardTouchHandlerView.setTranslationY(0);
                        keyboardTouchHandlerView.setVisibility(View.INVISIBLE);
                    }
                }, 220);
            }
        } else if(keyboardView.getVisibility()==View.INVISIBLE && numericView.getVisibility()==View.INVISIBLE){
            dummyBarView.setVisibility(View.VISIBLE);
            realBarView.setVisibility(View.INVISIBLE);
            numericBarView.setVisibility(View.INVISIBLE);
            mainScreenView.setVisibility(View.INVISIBLE);
            hukamnamaScreenView.setVisibility(View.VISIBLE);
            cancelSearch.setText("");
            cancelNumericSearch.setText("");
            gurmukhiString.setLength(0);
            shabadIDString.setLength(0);
            realSearchBox.setText("");
            numericSearchBox.setText("");
            realBarUndoBtn.setVisibility(View.INVISIBLE);
            numericBarUndoBtn.setVisibility(View.INVISIBLE);
        }

    }

    public class InitializeHukamnamaOnline extends AsyncTask<Void, Void, Void> {
        Boolean errorOccured = false;
        String jsonData = "";
        ArrayList<String> hukamFetch = new ArrayList<String>(7);
        @Override
        protected Void doInBackground(Void... params) {
            try {
                hukamFetch.clear();
                URL url = new URL("https://api.gurbaninow.com/v2/hukamnama/today");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";

                while (line !=null){
                    line = bufferedReader.readLine();
                    jsonData += line;
                }

                JSONObject jsonObject = new JSONObject(jsonData);

                JSONObject dateObject = (JSONObject) jsonObject.get("date");
                JSONObject gregorianDateObject = (JSONObject) dateObject.get("gregorian");
                hukamFetch.add(gregorianDateObject.get("date")+" "+gregorianDateObject.get("month")+", "+gregorianDateObject.get("year"));

                if(jsonObject.isNull("hukamnamainfo")){
                    return null;
                }

                dateObject = (JSONObject) jsonObject.get("hukamnamainfo");
                hukamFetch.add("Ang : "+dateObject.get("pageno"));

                JSONArray hukamnamaArray = (JSONArray) jsonObject.get("hukamnama");
                if(hukamnamaArray==null){
                    return null;
                }

                JSONObject hukamnamaLine = (JSONObject) hukamnamaArray.get(0);
                JSONObject hukamnamaInnerLine = (JSONObject) hukamnamaLine.get("line");
                JSONObject hukamnamaGurmukhi = (JSONObject) hukamnamaInnerLine.get("gurmukhi");
                hukamFetch.add((String) hukamnamaGurmukhi.get("akhar"));

                JSONObject translation = (JSONObject) hukamnamaInnerLine.get("translation");
                JSONObject hukamnamaEnglish = (JSONObject) translation.get("english");
                hukamFetch.add((String) hukamnamaEnglish.get("default"));

                JSONObject punjabi;
                JSONObject defaultPunjabi;

                StringBuilder gurmukhiHukam = new StringBuilder();
                StringBuilder englishHukam = new StringBuilder();
                StringBuilder punjabiHukam = new StringBuilder();

                for(int i = 1; i<hukamnamaArray.length(); i++){
                    hukamnamaLine = (JSONObject) hukamnamaArray.get(i);
                    hukamnamaInnerLine = (JSONObject) hukamnamaLine.get("line");
                    hukamnamaGurmukhi = (JSONObject) hukamnamaInnerLine.get("gurmukhi");
                    gurmukhiHukam.append(hukamnamaGurmukhi.get("akhar")+" ");

                    translation = (JSONObject) hukamnamaInnerLine.get("translation");
                    hukamnamaEnglish = (JSONObject) translation.get("english");
                    englishHukam.append(hukamnamaEnglish.get("default") + " ");

                    punjabi = (JSONObject) translation.get("punjabi");
                    defaultPunjabi = (JSONObject) punjabi.get("default");
                    punjabiHukam.append(defaultPunjabi.get("akhar")+" ");
                }
                gurmukhiHukam.deleteCharAt(gurmukhiHukam.length()-1);
                englishHukam.deleteCharAt(englishHukam.length()-1);
                punjabiHukam.deleteCharAt(punjabiHukam.length()-1);

                hukamFetch.add(gurmukhiHukam.toString());
                hukamFetch.add(englishHukam.toString());
                hukamFetch.add(punjabiHukam.toString());

            } catch (MalformedURLException e) {
                checkHukamIfExists();
                errorOccured = true;
            } catch (IOException e) {
                checkHukamIfExists();
                errorOccured = true;
            } catch (JSONException e) {
                checkHukamIfExists();
                errorOccured = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(hukamFetch.size()<7){
                return;
            }
            if(hukamOnlineContent.getText().toString().equals(hukamFetch.get(4)) && hukamOnlineDate.equals(hukamFetch.get(0)) || errorOccured){
                checkHukamIfExists();
                errorOccured = false;
                return;
            }
            hukamOnlineHeading.setText(hukamFetch.get(2));
            hukamOnlineContent.setText(hukamFetch.get(4));
            hukamOnlineViakhiaContent.setText(hukamFetch.get(6));
            hukamOnlineHeadingEnglish.setText(hukamFetch.get(3));
            hukamOnlineContentEnglish.setText(hukamFetch.get(5));
            hukamOnlineAng.setText(hukamFetch.get(1));
            hukamOnlineDate.setText(hukamFetch.get(0));

            connectionMessage.setVisibility(View.GONE);
            hukamOnlineHeading.setVisibility(View.VISIBLE);
            hukamOnlineContent.setVisibility(View.VISIBLE);
            hukamOnlineViakhiaHeading.setVisibility(hukamnamaViakhiaVisibility);
            hukamOnlineViakhiaContent.setVisibility(hukamnamaViakhiaVisibility);
            hukamOnlineHeadingEnglish.setVisibility(hukamnamaEnglishVisibility);
            hukamOnlineContentEnglish.setVisibility(hukamnamaEnglishVisibility);
            hukamOnlineAng.setVisibility(View.VISIBLE);
            hukamOnlineDate.setVisibility(View.VISIBLE);

            mEditor.putString(getString(R.string.currentOnlineHukamHeading), hukamFetch.get(2)).commit();
            mEditor.putString(getString(R.string.currentOnlineHukamContent), hukamFetch.get(4)).commit();
            mEditor.putString(getString(R.string.currentOnlineHukamViakhiaContent), hukamFetch.get(6)).commit();
            mEditor.putString(getString(R.string.currentOnlineHukamHeadingEnglish), hukamFetch.get(3)).commit();
            mEditor.putString(getString(R.string.currentOnlineHukamContentEnglish), hukamFetch.get(5)).commit();
            mEditor.putString(getString(R.string.currentOnlineHukamAng), hukamFetch.get(1)).commit();
            mEditor.putString(getString(R.string.lastUpdatedOnlineHukam), hukamFetch.get(0)).commit();
            this.cancel(true);
        }
    }

    public class InitializeRandomShabad extends AsyncTask<Void, Void, Void> {
        DatabaseHelper mHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = mHelper.openDatabase();
        Boolean check = true;

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... params) {
            randomShabadFetch.clear();
            cursorRandom = null;
            if(!randomShabadCurrentDate.equals(new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime())) || setRefreshCalled){
                while(check){
                    cursorRandom = db.rawQuery("SELECT * FROM Shabad_ID WHERE SHABAD_ID = ? AND SOURCE_ID = ?", new String[]{String.valueOf(new Random().nextInt(4239)), "SGGS"});
                    if(cursorRandom.moveToFirst()){
                        if(cursorRandom.isNull(11)){
                            continue;
                        }
                        if(cursorRandom.getString(5).contains(cursorRandom.getString(11)) && !cursorRandom.getString(11).isEmpty()){
                            int indexOne =0, indexRahao=0, indexTwo=0;
                            for(int i = 0; i< cursorRandom.getCount(); i++){
                                cursorRandom.moveToPosition(i);

                                if(cursorRandom.getString(5).contains("]2]")) {
                                    if (indexTwo == 0) {
                                        indexTwo = i;
                                    }
                                }
                                else if(cursorRandom.getString(5).contains("rhwau")) {
                                    if (indexRahao == 0) {
                                        indexRahao = i;
                                    }
                                }

                                else if(cursorRandom.getString(5).contains("]1]")){
                                    if(indexOne==0){
                                        indexOne = i;
                                    }
                                }

                                else continue;
                            }
                            int limits = Math.max(((indexOne > indexRahao) ? indexOne : indexRahao), indexTwo);
                            String ang = String.valueOf(cursorRandom.getInt(1));
                            String shabad_id = String.valueOf(cursorRandom.getInt(3));
                            StringBuilder heading = new StringBuilder();
                            StringBuilder headingEnglish = new StringBuilder();
                            StringBuilder gurmukhiData = new StringBuilder();
                            StringBuilder englishData = new StringBuilder();
                            StringBuilder viakhiaContent = new StringBuilder();
                            for(int i = 0; i<=limits;i++){
                                cursorRandom.moveToPosition(i);
                                if(i==0){
                                    heading.append(cursorRandom.getString(5));
                                    headingEnglish.append(cursorRandom.getString(4));
                                    continue;
                                }
                                gurmukhiData.append(cursorRandom.getString(5));
                                gurmukhiData.append(" ");
                                englishData.append(cursorRandom.getString(4));
                                englishData.append(" ");
                                viakhiaContent.append(cursorRandom.getString(6));
                                viakhiaContent.append(" ");
                            }
                            gurmukhiData.deleteCharAt(gurmukhiData.length()-1);
                            englishData.deleteCharAt(englishData.length()-1);
                            viakhiaContent.deleteCharAt(viakhiaContent.length()-1);
                            randomShabadFetch.add(heading.toString());
                            randomShabadFetch.add(gurmukhiData.toString());
                            randomShabadFetch.add(viakhiaContent.toString());
                            randomShabadFetch.add(headingEnglish.toString());
                            randomShabadFetch.add(englishData.toString());
                            randomShabadFetch.add("Ang : "+ang);
                            randomShabadFetch.add("Shabad ID : "+ shabad_id);
                            randomShabadFetch.add(new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime()));
                            check = false;
                            cursorRandom.close();
                        }
                    }
                    else {
                        continue;
                    }
                }
            } else {
                randomShabadExceptionOccured();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setRefreshCalled = false;
            randomShabadHeading.setText(randomShabadFetch.get(0));
            randomShabadContent.setText(randomShabadFetch.get(1));
            randomShabadViakhiaHeading.setText("ivAwiKAw");
            randomShabadViakhiaContent.setText(randomShabadFetch.get(2));
            randomShabadHeadingEnglish.setText(randomShabadFetch.get(3));
            randomShabadContentEnglish.setText(randomShabadFetch.get(4));
            randomShabadAng.setText(randomShabadFetch.get(5));
            randomShabadDate.setText(randomShabadFetch.get(6));

            refreshShabadBtn.setVisibility(View.VISIBLE);
            sendRandomShabadBtn.setVisibility(View.VISIBLE);
            refreshShabadBtn.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(1.0f).setDuration(220);
            refreshShabadBtn.setAlpha(1.0f);

            mEditor.putString(getString(R.string.randomShabadHeading), randomShabadFetch.get(0)).commit();
            mEditor.putString(getString(R.string.randomShabadContent), randomShabadFetch.get(1)).commit();
            mEditor.putString(getString(R.string.randomShabadViakhiaHeading), "ivAwiKAw").commit();
            mEditor.putString(getString(R.string.randomShabadViakhiaContent), randomShabadFetch.get(2)).commit();
            mEditor.putString(getString(R.string.randomShabadHeadingEnglish), randomShabadFetch.get(3)).commit();
            mEditor.putString(getString(R.string.randomShabadContentEnglish), randomShabadFetch.get(4)).commit();
            mEditor.putString(getString(R.string.randomShabadAng), randomShabadFetch.get(5)).commit();
            mEditor.putString(getString(R.string.randomShabadID), randomShabadFetch.get(6)).commit();
            mEditor.putString(getString(R.string.randomShabadDate), randomShabadFetch.get(7)).commit();
            this.cancel(true);
            db.close();
        }
    }

    public void randomShabadExceptionOccured(){
        randomShabadHeading.setText(mPreferences.getString(getString(R.string.randomShabadHeading), ""));
        randomShabadContent.setText(mPreferences.getString(getString(R.string.randomShabadContent), ""));
        randomShabadViakhiaContent.setText(mPreferences.getString(getString(R.string.randomShabadViakhiaContent), ""));
        randomShabadHeadingEnglish.setText(mPreferences.getString(getString(R.string.randomShabadHeadingEnglish), ""));
        randomShabadContentEnglish.setText(mPreferences.getString(getString(R.string.randomShabadContentEnglish), ""));
        randomShabadAng.setText(mPreferences.getString(getString(R.string.randomShabadAng), ""));
        randomShabadDate.setText(mPreferences.getString(getString(R.string.randomShabadID), ""));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(englishSearched){
            calledEnglish();
        }
        getSavedData();

        savedListRV.setLayoutManager(savedLayoutManager);
        listAdapter = new ListAdapter();

        if(savedBani!=null){
            savedListRV.setAdapter(listAdapter);
        }

        shabadIDString.setLength(0);
        numericSearchBox.setText("");
        numericBarUndoBtn.setVisibility(View.INVISIBLE);
        //gurmukhiString.setLength(0);
        //realSearchBox.setText("");
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        if(!randomShabadCurrentDate.equals(new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime()))){
            new InitializeRandomShabad().execute();
        }
    }

    public class ListAdapter2 extends RecyclerView.Adapter<ListAdapter2.ListViewHolder2>{

        @NonNull
        @Override
        public ListViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
            return new ListViewHolder2(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder2 holder, int position) {
            if (gurmukhiResultsCursor.getCount()<1){
                return;
            }

            gurmukhiResultsCursor.moveToPosition(position);

            holder.banitxt.setText(gurmukhiResultsCursor.getString(5));
            holder.sourceInfotxt.setText(gurmukhiResultsCursor.getString(20));
            if(gurmukhiResultsCursor.getString(8).contentEquals("SGGS")){
                holder.angIDtxt.setText("AMg "+gurmukhiResultsCursor.getInt(1));
            } else holder.angIDtxt.setText("");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gurmukhiResultsCursor.moveToPosition(gurmukhiSearchResultsRV.getChildAdapterPosition(view));
                    Intent intent = new Intent(MainActivity.this, ShabadActivity.class);
                    String source = gurmukhiResultsCursor.getString(8);
                    String shabadID = Integer.toString(gurmukhiResultsCursor.getInt(3));
                    String sourceFullName;
                    int upperLimit, uniqueID;
                    uniqueID = gurmukhiResultsCursor.getInt(0);

                    switch (source){
                        //case "SGGS":
                        //    sourceFullName = "Sri Guru Granth Sahib Ji";
                        //    upperLimit = 4238;
                        //    break;
                        case "Vaar":
                            sourceFullName = "Vaar Bhai Gurdas Ji";
                            upperLimit = 940;
                            break;
                        case "DasamGranth":
                            sourceFullName = "Dasam Granth Ji";
                            upperLimit = 3089;
                            break;
                        case "proseBhaiNandLalJi":
                            sourceFullName = "Prose Bhai Nand Lal Ji";
                            upperLimit = 176;
                            break;
                        case "kabitBhaiGurdasJi":
                            sourceFullName = "Kabit Bhai Gurdas Ji";
                            upperLimit = 674;
                            break;
                        default:
                            sourceFullName = "Sri Guru Granth Sahib Ji";
                            upperLimit = 4238;
                            break;
                    }

                    intent.putExtra("searchShabad", shabadID);
                    intent.putExtra("source", source);
                    intent.putExtra("sourceFullName", sourceFullName);
                    intent.putExtra("upperShabadLimit", upperLimit);
                    intent.putExtra("uniqueID", uniqueID);
                    intent.putExtra("searchMode", "Gurmukhi");
                    intent.putExtra("BaniString", gurmukhiResultsCursor.getString(5));
                    intent.putExtra("Ang", ""+gurmukhiResultsCursor.getInt(1));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return gurmukhiResultsCursor.getCount();
        }

        public class ListViewHolder2 extends RecyclerView.ViewHolder{
            TextView sourceInfotxt;
            TextView angIDtxt;
            TextView banitxt;
            View cellView;
            View border;
            public ListViewHolder2(@NonNull View itemView) {
                super(itemView);
                sourceInfotxt = itemView.findViewById(R.id.sourceInfotxt);
                angIDtxt = itemView.findViewById(R.id.angIDtxt);
                banitxt = itemView.findViewById(R.id.banitxt);
                border = itemView.findViewById(R.id.border);
                cellView = itemView.findViewById(R.id.cellView);
            }
        }
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_new, null, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.sourceInfotxt.setText(savedSource.get(position));
            holder.banitxt.setText(savedBani.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ShabadActivity.class);
                    String source = sourceComplete.get(position);
                    String shabadID = savedShabadID.get(position);
                    String fullName;
                    int upperLimit, uniqueID;
                    uniqueID = Integer.parseInt(savedUniqueID.get(position));

                    switch (source){
                        case "SGGS":
                            fullName = "Sri Guru Granth Sahib Ji";
                            upperLimit = 4238;
                            break;
                        case "Vaar":
                            fullName = "Vaar Bhai Gurdas Ji";
                            upperLimit = 940;
                            break;
                        case "DasamGranth":
                            fullName = "Dasam Granth Ji";
                            upperLimit = 3089;
                            break;
                        case "proseBhaiNandLalJi":
                            fullName = "Prose Bhai Nand Lal Ji";
                            upperLimit = 176;
                            break;
                        case "kabitBhaiGurdasJi":
                            fullName = "Kabit Bhai Gurdas Ji";
                            upperLimit = 674;
                            break;
                        default:
                            fullName = "Sri Guru Granth Sahib Ji";
                            upperLimit = 4238;
                            break;
                    }

                    intent.putExtra("searchShabad", shabadID);
                    intent.putExtra("source", source);
                    intent.putExtra("sourceFullName", fullName);
                    intent.putExtra("upperShabadLimit", upperLimit);
                    intent.putExtra("uniqueID", uniqueID);
                    intent.putExtra("searchMode", "Gurmukhi");

                    startActivity(intent);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int positionOFClick = savedListRV.getChildAdapterPosition(view);
                    itemViewOptionsLineID.setText("Line ID : "+savedUniqueID.get(positionOFClick));
                    itemViewOptionsShabadID.setText("Shabad ID : "+savedShabadID.get(positionOFClick));
                    uniqueIDToRemove = savedUniqueID.get(positionOFClick);
                    outsideOnLongClick();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return savedBani.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            TextView sourceInfotxt;
            TextView angIDtxt;
            TextView banitxt;
            View cellView;
            View border;
            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                sourceInfotxt = itemView.findViewById(R.id.sourceInfotxt);
                banitxt = itemView.findViewById(R.id.banitxt);
                border = itemView.findViewById(R.id.border);
                cellView = itemView.findViewById(R.id.cellView);
            }
        }
    }
    void getSavedData(){
        savedBani = new ArrayList<>();
        savedSource = new ArrayList<>();
        savedAng = new ArrayList<>();
        savedShabadID = new ArrayList<>();
        savedUniqueID = new ArrayList<>();
        sourceComplete = new ArrayList<>();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        String json = mPreferences.getString(getString(R.string.savedBani), null);
        savedBani = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.savedSource), null);
        savedSource = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.savedAng), null);
        savedAng = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.savedShabadID), null);
        savedShabadID = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.savedUniqueID), null);
        savedUniqueID = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.sourceComplete), null);
        sourceComplete = gson.fromJson(json, type);
    }

    void updateFontSize(){
        hukamOnlineHeading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize+2);
        hukamOnlineContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        hukamOnlineViakhiaHeading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        hukamOnlineViakhiaContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        hukamOnlineHeadingEnglish.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        hukamOnlineContentEnglish.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize-1);

        randomShabadHeading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize+2);
        randomShabadContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        randomShabadViakhiaHeading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        randomShabadViakhiaContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        randomShabadHeadingEnglish.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        randomShabadContentEnglish.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize-1);
    }
    void deselectAllFontBtns(){
        fontSizeMinus5Btn.setSelected(false);
        fontSizeMinus3Btn.setSelected(false);
        fontSizeBtn.setSelected(false);
        fontSizePlus3Btn.setSelected(false);
        fontSizePlus7Btn.setSelected(false);
        fontSizePlus11Btn.setSelected(false);
        fontSizePlus15Btn.setSelected(false);

        setSelectedFontBtn();
    }

    void setSelectedFontBtn(){
        switch (hukamnamaFontSize){
            case 14:
                fontSizeMinus5Btn.setSelected(true);
                break;
            case 16:
                fontSizeMinus3Btn.setSelected(true);
                break;
            case 19:
                fontSizeBtn.setSelected(true);
                break;
            case 22:
                fontSizePlus3Btn.setSelected(true);
                break;
            case 26:
                fontSizePlus7Btn.setSelected(true);
                break;
            case 30:
                fontSizePlus11Btn.setSelected(true);
                break;
            case 34:
                fontSizePlus15Btn.setSelected(true);
                break;
            default:
                break;
        }
    }

    public void outsideOnLongClick(){
        itemViewOptionsMenu.setTranslationY(itemViewOptionsMenu.getHeight());
        itemViewOptionsMenu.setAlpha(0.0f);
        itemViewBackgroundHandlerView.setAlpha(0.0f);
        itemViewOptionsView.setVisibility(View.VISIBLE);
        itemViewBackgroundHandlerView.setVisibility(View.VISIBLE);
        itemViewOptionsMenu.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).alpha(1.0f).setDuration(220);
        itemViewBackgroundHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.9f).setDuration(220);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemViewOptionsMenu.setAlpha(1.0f);
                itemViewBackgroundHandlerView.setAlpha(0.9f);
            }
        }, 220);
    }

    public void outsideItemViewCloseOptionsView(){
        itemViewOptionsMenu.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(itemViewOptionsMenu.getHeight()).alpha(0.0f).setDuration(220);
        itemViewBackgroundHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(220);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemViewOptionsView.setVisibility(View.INVISIBLE);
                itemViewBackgroundHandlerView.setAlpha(1.0f);
            }
        }, 220);
    }
    public void saveData(){
        Gson gson = new Gson();
        String json = gson.toJson(savedBani);
        mEditor.putString(getString(R.string.savedBani), json).commit();
        json = gson.toJson(savedSource);
        mEditor.putString(getString(R.string.savedSource), json).commit();
        json = gson.toJson(savedAng);
        mEditor.putString(getString(R.string.savedAng), json).commit();
        json = gson.toJson(savedShabadID);
        mEditor.putString(getString(R.string.savedShabadID), json).commit();
        json = gson.toJson(savedUniqueID);
        mEditor.putString(getString(R.string.savedUniqueID), json).commit();
        json = gson.toJson(sourceComplete);
        mEditor.putString(getString(R.string.sourceComplete), json).commit();
    }
    public void calledEnglish(){
        searchBoxIcon.setColorFilter(tintInactive);
        cancelSearch.setTextColor(cancelColorInactive);
        realBarUndoBtn.setVisibility(View.INVISIBLE);
        keyboardView.setVisibility(View.INVISIBLE);
        numericSearchBoxIcon.setColorFilter(tintInactive);
        cancelNumericSearch.setTextColor(cancelColorInactive);
        numericBarUndoBtn.setVisibility(View.INVISIBLE);
        numericView.setVisibility(View.INVISIBLE);
        keyboardTouchHandlerView.setTranslationY(0);
        keyboardTouchHandlerView.setVisibility(View.INVISIBLE);
        englishSearched = false;
    }
}

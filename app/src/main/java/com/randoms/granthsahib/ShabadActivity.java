package com.randoms.granthsahib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShabadActivity extends AppCompatActivity {
    int mode, filled, timer, upperShabadLimit, uniqueID, positionToMove, uniqueIDToSave;
    String searchShabad, source, sourceFullName, searchMode, BaniStringToSave, BaniString, AngToSave, Ang, quoteSource, quoteShabadID, quoteLineID;
    ImageButton shabadBackBtn, bookmarksBtn, closeItemViewOptionsViewBtn, settingsBtn, closeSettingsViewBtn;
    TextView shabadHeading, shabadExtras, itemViewOptionsGurmukhiID, itemViewOptionsLineID, itemViewOptionsShabadID, itemViewOptionsShareQuote, itemViewOptionsShareShabad, itemViewOptionsSave;
    RecyclerView shabadRV;
    View navigationBtnView, navigationBtnBlurView, shabadToolbar,  nextBtnView, prevBtnView, shabadMainView, itemViewOptionsView, itemViewBackgroundHandlerView, itemViewOptionsMenu;
    View settingsMenuView, settingsMenuHandlerView, settingsAreaView, GurbaniView;
    Runnable timerClock;
    Handler timeHandler;
    Cursor cursor;
    ListAdapter dataAdapter;
    DatabaseHelper shabadHelper;
    SQLiteDatabase database;
    Button fontSizeMinus5Btn, fontSizeMinus3Btn, fontSizeBtn, fontSizePlus3Btn, fontSizePlus7Btn, fontSizePlus11Btn, fontSizePlus15Btn, showTransliterationBtn, showEnglishBtn, showShabadArthBtn, showBhavArthBtn, onModeBtn, offModeBtn;
    Boolean showTransliteration, showEnglish, showShabadArth, showBhavArth, kirtanMode;
    int transVisible, englishVisible, shabadArthVisible, bhavArthVisible, hukamnamaFontSize;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    ArrayList<String> savedBani, savedSource, savedAng, savedShabadID, savedUniqueID, sourceComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.AppTheme);

        setTheme(mode);
        setContentView(R.layout.activity_shabad);

        Intent intent = getIntent();

        searchShabad = intent.getStringExtra("searchShabad");
        source = intent.getStringExtra("source");
        sourceFullName = intent.getStringExtra("sourceFullName");
        upperShabadLimit = intent.getIntExtra("upperShabadLimit", 4238);
        uniqueID = intent.getIntExtra("uniqueID", 0);
        searchMode = intent.getStringExtra("searchMode");
        BaniString = intent.getStringExtra("BaniString");
        Ang = intent.getStringExtra("Ang");
        uniqueIDToSave = uniqueID;
        BaniStringToSave = BaniString;
        AngToSave = Ang;

        hukamnamaFontSize = mPreferences.getInt(getString(R.string.shabadFontSize), 19);
        kirtanMode = mPreferences.getBoolean(getString(R.string.kirtanMode), false);

        shabadToolbar = findViewById(R.id.shabadToolBar);
        shabadHeading = findViewById(R.id.shabadHeading);
        shabadExtras = findViewById(R.id.shabadExtras);
        shabadRV = findViewById(R.id.shabadRV);
        navigationBtnView = findViewById(R.id.navigationBtnView);
        navigationBtnBlurView = findViewById(R.id.navigationBtnBlurView);
        nextBtnView = findViewById(R.id.nextBtnView);
        prevBtnView = findViewById(R.id.prevBtnView);
        shabadMainView = findViewById(R.id.shabadMainView);
        shabadBackBtn = findViewById(R.id.shabadBackBtn);
        bookmarksBtn = findViewById(R.id.bookmarksBtn);
        itemViewOptionsView = findViewById(R.id.itemViewOptionsView);
        itemViewBackgroundHandlerView = findViewById(R.id.itemViewBackgroundHandlerView);
        itemViewOptionsMenu = findViewById(R.id.itemViewOptionsMenu);
        closeItemViewOptionsViewBtn = findViewById(R.id.closeItemViewOptionsViewBtn);
        itemViewOptionsGurmukhiID = findViewById(R.id.itemViewOptionsGurmukhiID);
        itemViewOptionsLineID = findViewById(R.id.itemViewOptionsLineID);
        itemViewOptionsShabadID = findViewById(R.id.itemViewOptionsShabadID);
        itemViewOptionsShareQuote = findViewById(R.id.itemViewOptionsShareQuote);
        itemViewOptionsShareShabad = findViewById(R.id.itemViewOptionsShareShabad);
        itemViewOptionsSave = findViewById(R.id.itemViewOptionsSave);

        GurbaniView = findViewById(R.id.GurbaniView);
        settingsBtn = findViewById(R.id.settingsBtn);
        closeSettingsViewBtn = findViewById(R.id.closeSettingsViewBtn);
        settingsMenuView = findViewById(R.id.settingsMenuView);
        settingsMenuHandlerView = findViewById(R.id.settingsMenuHandlerView);
        settingsAreaView = findViewById(R.id.settingsAreaView);
        closeSettingsViewBtn = findViewById(R.id.closeSettingsViewBtn);
        onModeBtn = findViewById(R.id.onModeBtn);
        offModeBtn = findViewById(R.id.offModeBtn);
        fontSizeMinus5Btn = findViewById(R.id.fontSizeMinus5Btn);
        fontSizeMinus3Btn = findViewById(R.id.fontSizeMinus3Btn);
        fontSizeBtn = findViewById(R.id.fontSizeBtn);
        fontSizePlus3Btn = findViewById(R.id.fontSizePlus3Btn);
        fontSizePlus7Btn = findViewById(R.id.fontSizePlus7Btn);
        fontSizePlus11Btn = findViewById(R.id.fontSizePlus11Btn);
        fontSizePlus15Btn = findViewById(R.id.fontSizePlus15Btn);
        showTransliterationBtn = findViewById(R.id.showTransliterationBtn);
        showEnglishBtn = findViewById(R.id.showEnglishBtn);
        showShabadArthBtn = findViewById(R.id.showShabadArthBtn);
        showBhavArthBtn = findViewById(R.id.showBhavArthBtn);

        if(!kirtanMode){
            GurbaniView.setVisibility(View.VISIBLE);
            showTransliteration = mPreferences.getBoolean(getString(R.string.shabadShowTransliteration), false);
            showEnglish = mPreferences.getBoolean(getString(R.string.shabadShowEnglish), true);
            showShabadArth = mPreferences.getBoolean(getString(R.string.shabadShowShabadArth), false);
            showBhavArth = mPreferences.getBoolean(getString(R.string.shabadShowBhavArth), true);
            offModeBtn.setSelected(true);
            onModeBtn.setSelected(false);
            shabadRV.setKeepScreenOn(false);
        } else{
            GurbaniView.setVisibility(View.GONE);
            showTransliteration = false;
            showEnglish = false;
            showShabadArth = false;
            showBhavArth = false;
            onModeBtn.setSelected(true);
            offModeBtn.setSelected(false);
            hukamnamaFontSize = 30;
            shabadRV.setKeepScreenOn(true);
        }

        setSelectedFontBtn();

        getSavedData();
        if(savedUniqueID==null){
            savedBani = new ArrayList<>();
            savedSource = new ArrayList<>();
            savedAng = new ArrayList<>();
            savedShabadID = new ArrayList<>();
            savedUniqueID = new ArrayList<>();
            sourceComplete = new ArrayList<>();
        }

        if(searchMode.equals("Gurmukhi")){
            bookmarksBtn.setVisibility(View.VISIBLE);
        } else bookmarksBtn.setVisibility(View.GONE);

        if (savedUniqueID.contains(Integer.toString(uniqueIDToSave))) {
            bookmarksBtn.setSelected(true);
        } else {
            bookmarksBtn.setSelected(false);
        }

        LinearLayoutManagerWithSmoothScroller layoutManager = new LinearLayoutManagerWithSmoothScroller(ShabadActivity.this);
        layoutManager.setSmoothScrollbarEnabled(true);
        shabadRV.setLayoutManager(layoutManager);

        shabadHelper = new DatabaseHelper(this);
        database = shabadHelper.openDatabase();

        filled = 0;
        timer = 3000;
        timeHandler = new Handler();

        cursor = database.rawQuery("SELECT * FROM Shabad_ID WHERE SHABAD_ID = ? AND SOURCE_ID = ?", new String[]{searchShabad, source});
        cursor.moveToFirst();
        database.close();

        updateShabadHeading();
        updateViewVisiblity();
        enableGurbaniBtns();

        switch (source){
            case "Vaar":
                if(Integer.parseInt(searchShabad)>=914 && Integer.parseInt(searchShabad)<=940 || kirtanMode){
                    GurbaniView.setVisibility(View.GONE);
                } else {
                    GurbaniView.setVisibility(View.VISIBLE);
                }
                showShabadArthBtn.setVisibility(View.GONE);
                showBhavArthBtn.setVisibility(View.GONE);
                break;
            case "DasamGranth":
                showTransliterationBtn.setVisibility(View.VISIBLE);
                showEnglishBtn.setVisibility(View.VISIBLE);
                showShabadArthBtn.setVisibility(View.GONE);
                showBhavArthBtn.setVisibility(View.GONE);
                break;
            case "proseBhaiNandLalJi":
                if(Integer.parseInt(searchShabad)>=63){
                    showTransliterationBtn.setVisibility(View.VISIBLE);
                } else showTransliterationBtn.setVisibility(View.GONE);
                showEnglishBtn.setVisibility(View.VISIBLE);
                showShabadArthBtn.setVisibility(View.GONE);
                showBhavArthBtn.setVisibility(View.GONE);
                break;
            case "kabitBhaiGurdasJi":
                showTransliterationBtn.setVisibility(View.GONE);
                showEnglishBtn.setVisibility(View.VISIBLE);
                showShabadArthBtn.setVisibility(View.GONE);
                showBhavArthBtn.setVisibility(View.GONE);
                break;
            default:
                showTransliterationBtn.setVisibility(View.VISIBLE);
                showEnglishBtn.setVisibility(View.VISIBLE);
                showShabadArthBtn.setVisibility(View.VISIBLE);
                showBhavArthBtn.setVisibility(View.VISIBLE);
                break;
        }

        shabadExtras.setText(sourceFullName + ", Shabad : "+searchShabad);
        dataAdapter = new ListAdapter();
        shabadRV.setAdapter(dataAdapter);

        if(uniqueID!=0){
            for(int i = 0; i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                if(cursor.getInt(0)==uniqueID){
                    positionToMove = i;
                }
            }
            shabadRV.smoothScrollToPosition(positionToMove);
        }

        if(Integer.parseInt(searchShabad)==0){
            prevBtnView.setVisibility(View.INVISIBLE);
        }
        if(Integer.parseInt(searchShabad)==upperShabadLimit){
            nextBtnView.setVisibility(View.INVISIBLE);
        }

        timerClock = new Runnable() {
            @Override
            public void run() {
                navigationBtnView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(navigationBtnView.getHeight()).setDuration(200);
                navigationBtnBlurView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(navigationBtnView.getHeight()).setDuration(200);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filled = navigationBtnView.getHeight();
                        navigationBtnView.setTranslationY(navigationBtnView.getHeight());
                        navigationBtnBlurView.setTranslationY(navigationBtnView.getHeight());
                    }
                }, 200);
            }
        };
        timeHandler.postDelayed(timerClock, timer);

        shabadRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                timeHandler.removeCallbacks(timerClock);
                if(filled>=0 && filled<=navigationBtnView.getHeight()){
                    hideNavigationBtnView(dy);
                }
            }
        });

        shabadBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMode = "Shabad";
                bookmarksBtn.setSelected(false);
                bookmarksBtn.setVisibility(View.INVISIBLE);
                searchShabad = String.valueOf(Integer.parseInt(searchShabad)+1);
                if(Integer.parseInt(searchShabad)==upperShabadLimit){
                    nextBtnView.setVisibility(View.INVISIBLE);
                }
                switch (source){
                    case "Vaar":
                        if(Integer.parseInt(searchShabad)>=914 && Integer.parseInt(searchShabad)<=940){
                            GurbaniView.setVisibility(View.GONE);
                        } else {
                            GurbaniView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "proseBhaiNandLalJi":
                        if(Integer.parseInt(searchShabad)>=63){
                            showTransliterationBtn.setVisibility(View.VISIBLE);
                        } else showTransliterationBtn.setVisibility(View.GONE);
                        break;
                }
                database = shabadHelper.openDatabase();
                cursor = database.rawQuery("SELECT * FROM Shabad_ID WHERE SHABAD_ID = ? AND SOURCE_ID = ?", new String[]{searchShabad, source});
                cursor.moveToFirst();
                database.close();
                updateShabadHeading();
                shabadExtras.setText(sourceFullName + ", Shabad : "+searchShabad);
                dataAdapter.notifyDataSetChanged();
                shabadRV.smoothScrollToPosition(0);
                prevBtnView.setVisibility(View.VISIBLE);
                timeHandler.removeCallbacks(timerClock);
                timeHandler.postDelayed(timerClock, timer);
            }
        });
        prevBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMode = "Shabad";
                bookmarksBtn.setSelected(false);
                bookmarksBtn.setVisibility(View.INVISIBLE);
                searchShabad = String.valueOf(Integer.parseInt(searchShabad)-1);
                if(Integer.parseInt(searchShabad)==0){
                    prevBtnView.setVisibility(View.INVISIBLE);
                }
                switch (source){
                    case "Vaar":
                        if(Integer.parseInt(searchShabad)>=914 && Integer.parseInt(searchShabad)<=940){
                            GurbaniView.setVisibility(View.GONE);
                        } else {
                            GurbaniView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "proseBhaiNandLalJi":
                        if(Integer.parseInt(searchShabad)>=63){
                            showTransliterationBtn.setVisibility(View.VISIBLE);
                        } else showTransliterationBtn.setVisibility(View.GONE);
                        break;
                }
                database = shabadHelper.openDatabase();
                cursor = database.rawQuery("SELECT * FROM Shabad_ID WHERE SHABAD_ID = ? AND SOURCE_ID = ?", new String[]{searchShabad, source});
                cursor.moveToFirst();
                database.close();
                updateShabadHeading();
                shabadExtras.setText(sourceFullName + ", Shabad : "+searchShabad);
                dataAdapter.notifyDataSetChanged();
                shabadRV.smoothScrollToPosition(0);
                nextBtnView.setVisibility(View.VISIBLE);
                timeHandler.removeCallbacks(timerClock);
                timeHandler.postDelayed(timerClock, timer);
            }
        });

        navigationBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeHandler.removeCallbacks(timerClock);
                timeHandler.postDelayed(timerClock, timer);
            }
        });

        bookmarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(savedUniqueID.contains(""+uniqueIDToSave)){
                    int index = savedUniqueID.indexOf(""+uniqueIDToSave);
                    savedBani.remove(index);
                    savedSource.remove(index);
                    savedAng.remove(index);
                    savedShabadID.remove(index);
                    savedUniqueID.remove(index);
                    sourceComplete.remove(index);
                    saveData();
                    bookmarksBtn.setSelected(false);
                    Toast.makeText(ShabadActivity.this, "Removed from Saved", Toast.LENGTH_SHORT).show();
                } else {
                    savedBani.add(0, BaniStringToSave);
                    savedSource.add(0, shabadHeading.getText().toString());
                    if(source.equals("SGGS")){
                        savedAng.add(0, "AMg : "+AngToSave);
                    } else savedAng.add(0, "0");
                    savedShabadID.add(0, searchShabad);
                    savedUniqueID.add(0, ""+uniqueIDToSave);
                    sourceComplete.add(0, source);
                    saveData();
                    bookmarksBtn.setSelected(true);
                    Toast.makeText(ShabadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
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
                if(savedUniqueID.contains(""+uniqueID)){
                    int index = savedUniqueID.indexOf(""+uniqueID);
                    savedBani.remove(index);
                    savedSource.remove(index);
                    savedAng.remove(index);
                    savedShabadID.remove(index);
                    savedUniqueID.remove(index);
                    sourceComplete.remove(index);
                    saveData();
                    if(uniqueID==uniqueIDToSave){
                        bookmarksBtn.setSelected(false);
                    }
                    Toast.makeText(ShabadActivity.this, "Removed from Saved", Toast.LENGTH_SHORT).show();
                } else {
                    savedBani.add(0, BaniString);
                    savedSource.add(0, shabadHeading.getText().toString());
                    if(source.equals("SGGS")){
                        savedAng.add(0, "AMg : "+Ang);
                    } else savedAng.add(0, "0");
                    savedShabadID.add(0, searchShabad);
                    savedUniqueID.add(0, ""+uniqueID);
                    sourceComplete.add(0, source);
                    saveData();
                    if(uniqueID==uniqueIDToSave){
                        bookmarksBtn.setSelected(true);
                    }
                    Toast.makeText(ShabadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                }
                outsideItemViewCloseOptionsView();
            }
        });

        itemViewOptionsShareQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShabadActivity.this, QuoteActivity.class);
                intent.putExtra("quoteSource", quoteSource);
                intent.putExtra("quoteShabadID", quoteShabadID);
                intent.putExtra("quoteLineID", quoteLineID);
                startActivity(intent);
            }
        });

        itemViewOptionsShareShabad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShabadActivity.this, ShabadArtActivity.class);
                intent.putExtra("quoteSource", quoteSource);
                intent.putExtra("quoteShabadID", quoteShabadID);
                intent.putExtra("quoteLineID", quoteLineID);
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

        fontSizeMinus5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 14;
                shabadRV.setAdapter(null);
                dataAdapter.notifyDataSetChanged();
                shabadRV.setAdapter(dataAdapter);
                deselectAllFontBtns();
                if(!kirtanMode){
                    mEditor.putInt(getString(R.string.shabadFontSize), hukamnamaFontSize).commit();
                }

            }
        });
        fontSizeMinus3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 16;
                shabadRV.setAdapter(null);
                dataAdapter.notifyDataSetChanged();
                shabadRV.setAdapter(dataAdapter);
                deselectAllFontBtns();
                if(!kirtanMode){
                    mEditor.putInt(getString(R.string.shabadFontSize), hukamnamaFontSize).commit();
                }
            }
        });
        fontSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 19;
                shabadRV.setAdapter(null);
                dataAdapter.notifyDataSetChanged();
                shabadRV.setAdapter(dataAdapter);
                deselectAllFontBtns();
                if(!kirtanMode){
                    mEditor.putInt(getString(R.string.shabadFontSize), hukamnamaFontSize).commit();
                }
            }
        });
        fontSizePlus3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 22;
                shabadRV.setAdapter(null);
                dataAdapter.notifyDataSetChanged();
                shabadRV.setAdapter(dataAdapter);
                deselectAllFontBtns();
                if(!kirtanMode){
                    mEditor.putInt(getString(R.string.shabadFontSize), hukamnamaFontSize).commit();
                }
            }
        });
        fontSizePlus7Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 26;
                shabadRV.setAdapter(null);
                dataAdapter.notifyDataSetChanged();
                shabadRV.setAdapter(dataAdapter);
                deselectAllFontBtns();
                if(!kirtanMode){
                    mEditor.putInt(getString(R.string.shabadFontSize), hukamnamaFontSize).commit();
                }
            }
        });
        fontSizePlus11Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 30;
                shabadRV.setAdapter(null);
                dataAdapter.notifyDataSetChanged();
                shabadRV.setAdapter(dataAdapter);
                deselectAllFontBtns();
                if(!kirtanMode){
                    mEditor.putInt(getString(R.string.shabadFontSize), hukamnamaFontSize).commit();
                }
            }
        });
        fontSizePlus15Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 34;
                shabadRV.setAdapter(null);
                dataAdapter.notifyDataSetChanged();
                shabadRV.setAdapter(dataAdapter);
                deselectAllFontBtns();
                if(!kirtanMode){
                    mEditor.putInt(getString(R.string.shabadFontSize), hukamnamaFontSize).commit();
                }
            }
        });

        showTransliterationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showTransliteration){
                    showTransliteration = true;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.shabadShowTransliteration), true).commit();
                } else {
                    showTransliteration = false;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.shabadShowTransliteration), false).commit();
                }
            }
        });

        showEnglishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showEnglish){
                    showEnglish = true;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.shabadShowEnglish), true).commit();
                } else {
                    showEnglish = false;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.shabadShowEnglish), false).commit();
                }
            }
        });

        showShabadArthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showShabadArth){
                    showShabadArth = true;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.shabadShowShabadArth), true).commit();
                } else {
                    showShabadArth = false;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.shabadShowShabadArth), false).commit();
                }
            }
        });

        showBhavArthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showBhavArth){
                    showBhavArth = true;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.shabadShowBhavArth), true).commit();
                } else {
                    showBhavArth = false;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.shabadShowBhavArth), false).commit();
                }
            }
        });

        onModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kirtanMode){
                    GurbaniView.setVisibility(View.GONE);
                    showTransliteration = false;
                    showEnglish = false;
                    showShabadArth = false;
                    showBhavArth = false;
                    hukamnamaFontSize = 30;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    deselectAllFontBtns();
                    enableGurbaniBtns();
                    onModeBtn.setSelected(true);
                    offModeBtn.setSelected(false);
                    mEditor.putBoolean(getString(R.string.kirtanMode), true).commit();
                    kirtanMode = true;
                    shabadRV.setKeepScreenOn(true);
                }
            }
        });

        offModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kirtanMode){
                    hukamnamaFontSize = mPreferences.getInt(getString(R.string.shabadFontSize), 19);
                    showTransliteration = mPreferences.getBoolean(getString(R.string.shabadShowTransliteration), false);
                    showEnglish = mPreferences.getBoolean(getString(R.string.shabadShowEnglish), true);
                    showShabadArth = mPreferences.getBoolean(getString(R.string.shabadShowShabadArth), false);
                    showBhavArth = mPreferences.getBoolean(getString(R.string.shabadShowBhavArth), true);
                    switch (source){
                        case "Vaar":
                            if(Integer.parseInt(searchShabad)>=914 && Integer.parseInt(searchShabad)<=940){
                                GurbaniView.setVisibility(View.GONE);
                            } else {
                                GurbaniView.setVisibility(View.VISIBLE);
                            }
                            break;
                        case "proseBhaiNandLalJi":
                            if(Integer.parseInt(searchShabad)>=63){
                                showTransliterationBtn.setVisibility(View.VISIBLE);
                            } else showTransliterationBtn.setVisibility(View.GONE);
                            GurbaniView.setVisibility(View.VISIBLE);
                            break;
                        default:
                            GurbaniView.setVisibility(View.VISIBLE);
                            break;
                    }
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    deselectAllFontBtns();
                    enableGurbaniBtns();
                    offModeBtn.setSelected(true);
                    onModeBtn.setSelected(false);
                    mEditor.putBoolean(getString(R.string.kirtanMode), false).commit();
                    kirtanMode = false;
                    shabadRV.setKeepScreenOn(false);
                }
            }
        });
    }

    void hideNavigationBtnView(int dy){
        filled += dy;
        if(filled>navigationBtnView.getHeight()){
            filled = navigationBtnView.getHeight();
        } else if(filled<0){
            filled = 0;
        }
        navigationBtnView.setTranslationY(filled);
        navigationBtnBlurView.setTranslationY(filled);
        timeHandler.postDelayed(timerClock, timer);
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_shabad_layout, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            cursor.moveToPosition(position);

            holder.shabadGurmukhi.setText(cursor.getString(5));

            holder.shabadTransliteration.setText(cursor.getString(9));
            holder.shabadTransliteration.setVisibility(cursor.getInt(transVisible));

            holder.shabadEnglish.setText(cursor.getString(4));
            holder.shabadEnglish.setVisibility(cursor.getInt(englishVisible));

            holder.shabadArth.setText(cursor.getString(7));
            holder.shabadArth.setVisibility(cursor.getInt(shabadArthVisible));

            holder.shabadBhav.setText(cursor.getString(6));
            holder.shabadBhav.setVisibility(cursor.getInt(bhavArthVisible));

            if(position == positionToMove && searchMode.equals("Gurmukhi")){
                holder.shabadCellView.setSelected(true);
            } else holder.shabadCellView.setSelected(false);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    outsideOnClick();
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int positionOFClick = shabadRV.getChildAdapterPosition(view);
                    cursor.moveToPosition(positionOFClick);
                    itemViewOptionsGurmukhiID.setText("gurmuKI kof : "+cursor.getString(10));
                    itemViewOptionsLineID.setText("Line ID : "+cursor.getInt(0));
                    itemViewOptionsShabadID.setText("Shabad ID : "+cursor.getInt(3));

                    quoteSource = cursor.getString(8);
                    quoteLineID = ""+cursor.getInt(2);
                    quoteShabadID = ""+cursor.getInt(3);

                    int unique = cursor.getInt(0);
                    if(savedUniqueID!=null && savedUniqueID.contains(""+unique)){
                        itemViewOptionsSave.setText("Remove From Saved");
                        uniqueID = unique;
                    } else {
                        itemViewOptionsSave.setText("Save");
                        BaniString = cursor.getString(5);
                        uniqueID = unique;
                    }
                    outsideOnLongClick();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            View shabadCellView;
            TextView shabadGurmukhi, shabadTransliteration, shabadEnglish, shabadArth, shabadBhav;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                shabadCellView = itemView.findViewById(R.id.shabadCellView);
                shabadGurmukhi = itemView.findViewById(R.id.shabadGurmukhi);
                shabadTransliteration = itemView.findViewById(R.id.shabadTransliteration);
                shabadEnglish = itemView.findViewById(R.id.shabadEnglish);
                shabadArth = itemView.findViewById(R.id.shabadArth);
                shabadBhav = itemView.findViewById(R.id.shabadBhav);

                shabadGurmukhi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize+2);
                shabadTransliteration.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize-1);
                shabadEnglish.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize-1);
                shabadArth.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
                shabadBhav.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize+1);
            }
        }
    }
    void updateShabadHeading(){
        switch (source){
            case "SGGS":
                if(cursor.getString(11)==null){
                    shabadHeading.setText((cursor.getString(12)==null)?"":cursor.getString(12)+" AMg : "+cursor.getInt(1));
                } else shabadHeading.setText(cursor.getString(11)+" ".concat((cursor.getString(12)==null)?"":cursor.getString(12))+" AMg : "+cursor.getInt(1));
                break;
            case "DasamGranth":
                if(cursor.getString(14)==null){
                    shabadHeading.setText((cursor.getString(12)==null)?"":cursor.getString(12));
                } else shabadHeading.setText(cursor.getString(14)+" ".concat((cursor.getString(12)==null || cursor.getString(12).equals(""))?"":cursor.getString(12)));
                break;
            default:
                if(cursor.getString(11)==null){
                    shabadHeading.setText((cursor.getString(12)==null)?"":cursor.getString(12));
                } else shabadHeading.setText(cursor.getString(11)+" ".concat((cursor.getString(12)==null )?"":cursor.getString(12)));
                break;
        }
    }

    public void outsideOnClick(){
        if(navigationBtnView.getTranslationY()==navigationBtnView.getHeight()){
            navigationBtnView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).setDuration(200);
            navigationBtnBlurView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).setDuration(200);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    filled = 0;
                    navigationBtnView.setTranslationY(0);
                    navigationBtnBlurView.setTranslationY(0);
                    timeHandler.postDelayed(timerClock, timer);
                }
            }, 200);
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

    void updateViewVisiblity(){
        if(showTransliteration){
            transVisible = 15;
        } else transVisible = 19;
        if(showEnglish){
            englishVisible = 16;
        } else englishVisible = 19;
        if(showShabadArth){
            shabadArthVisible = 17;
        } else shabadArthVisible = 19;
        if(showBhavArth){
            bhavArthVisible = 18;
        } else bhavArthVisible = 19;
    }
    void getSavedData(){
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

    void saveData(){
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        }
        if (itemViewOptionsView.getVisibility() == View.VISIBLE) {
            outsideItemViewCloseOptionsView();
        } else {
            finish();
        }
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
    void enableGurbaniBtns(){
        if(showTransliteration){
            showTransliterationBtn.setSelected(true);
        } else showTransliterationBtn.setSelected(false);
        if(showEnglish){
            showEnglishBtn.setSelected(true);
        } else showEnglishBtn.setSelected(false);
        if(showShabadArth){
            showShabadArthBtn.setSelected(true);
        } else  showShabadArthBtn.setSelected(false);
        if(showBhavArth){
            showBhavArthBtn.setSelected(true);
        } else showBhavArthBtn.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        outsideItemViewCloseOptionsView();
    }
}

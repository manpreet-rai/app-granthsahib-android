package com.randoms.granthsahib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AngActivity extends AppCompatActivity implements View.OnClickListener{
    int mode, filled, timer, lastAng;
    ImageButton shabadBackBtn, goToAngBtn, settingsBtn, closeItemViewOptionsViewBtn, closeSettingsViewBtn;
    TextView shabadHeading, shabadExtras, angTextView, angTextViewDummy, itemViewOptionsGurmukhiID, itemViewOptionsLineID, itemViewOptionsShabadID, itemViewOptionsShareShabad, itemViewOptionsShareQuote, itemViewOptionsSave;
    RecyclerView shabadRV;
    View navigationBtnView, navigationBtnBlurView, shabadToolbar,  nextBtnView, prevBtnView, shabadMainView, angKeyboardHandlerView, angSearchArea, numericView, shabadBarView, itemViewOptionsView, itemViewBackgroundHandlerView, itemViewOptionsMenu;
    View settingsMenuView, settingsMenuHandlerView, settingsAreaView;
    Runnable timerClock;
    Handler timeHandler;
    Cursor cursor;
    ListAdapter dataAdapter;
    DatabaseHelper angHelper;
    SQLiteDatabase database;
    Boolean showTransliteration, showEnglish, showShabadArth, showBhavArth;
    Button undoBtn, okayBtn, fontSizeMinus5Btn, fontSizeMinus3Btn, fontSizeBtn, fontSizePlus3Btn, fontSizePlus7Btn, fontSizePlus11Btn, fontSizePlus15Btn;
    Button showTransliterationBtn, showEnglishBtn, showShabadArthBtn, showBhavArthBtn;
    StringBuilder angIDString;
    ImageView searchIcon;
    int tintActive, tintInactive, cancelColorActive, cancelColorInactive;
    int transVisible, englishVisible, shabadArthVisible, bhavArthVisible, uniqueID, hukamnamaFontSize;
    String BaniString, SourceString, Ang, ShabadID, SourceComplete, quoteSource, quoteShabadID, quoteLineID;

    ArrayList<String> savedBani, savedSource, savedAng, savedShabadID, savedUniqueID, sourceComplete;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = this.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.AppTheme);

        setTheme(mode);
        setContentView(R.layout.activity_ang);

        if(mode==R.style.AppTheme){
            tintActive = Color.parseColor("#333333");
            tintInactive = Color.parseColor("#8E8E8E");
            cancelColorActive = Color.parseColor("#C02F1D");
            cancelColorInactive = Color.parseColor("#8E8E8E");
        } else {
            tintActive = Color.parseColor("#C7D2DF");
            tintInactive = Color.parseColor("#4B647C");
            cancelColorActive = Color.parseColor("#C7D2DF");
            cancelColorInactive = Color.parseColor("#4B647C");
        }

        lastAng = mPreferences.getInt(getString(R.string.lastAng), 1);

        hukamnamaFontSize = mPreferences.getInt(getString(R.string.angFontSize), 19);

        shabadBarView = findViewById(R.id.shabadBarView);
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

        goToAngBtn = findViewById(R.id.goToAngBtn);
        searchIcon = findViewById(R.id.searchIcon);
        angTextView = findViewById(R.id.angTextView);
        angTextViewDummy = findViewById(R.id.angTextViewDummy);
        angKeyboardHandlerView = findViewById(R.id.angKeyboardHandlerView);
        angSearchArea = findViewById(R.id.angSearchArea);
        numericView = findViewById(R.id.numericView);
        undoBtn = findViewById(R.id.searchENBtn);
        okayBtn = findViewById(R.id.okayBtn);

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

        settingsBtn = findViewById(R.id.settingsBtn);
        closeSettingsViewBtn = findViewById(R.id.closeSettingsViewBtn);
        settingsMenuView = findViewById(R.id.settingsMenuView);
        settingsMenuHandlerView = findViewById(R.id.settingsMenuHandlerView);
        settingsAreaView = findViewById(R.id.settingsAreaView);
        closeSettingsViewBtn = findViewById(R.id.closeSettingsViewBtn);
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

        LinearLayoutManagerWithSmoothScroller layoutManager = new LinearLayoutManagerWithSmoothScroller(AngActivity.this);
        layoutManager.setSmoothScrollbarEnabled(true);
        shabadRV.setLayoutManager(layoutManager);

        angHelper = new DatabaseHelper(this);
        database = angHelper.openDatabase();

        filled = 0;
        timer = 3000;
        timeHandler = new Handler();
        angIDString = new StringBuilder();

        showTransliteration = mPreferences.getBoolean(getString(R.string.angShowTransliteration), false);
        showEnglish = mPreferences.getBoolean(getString(R.string.angShowEnglish), true);
        showShabadArth = mPreferences.getBoolean(getString(R.string.angShowShabadArth), false);
        showBhavArth = mPreferences.getBoolean(getString(R.string.angShowBhavArth), true);

        cursor = database.rawQuery("SELECT * FROM Shabad_ID WHERE ANG_ID = ? AND SOURCE_ID = ?", new String[]{String.valueOf(lastAng), "SGGS"});
        cursor.moveToFirst();
        database.close();

        if(cursor.isNull(1)){
            return;
        }

        shabadHeading.setText("AMg : "+cursor.getInt(1));

        updateViewVisiblity();
        enableGurbaniBtns();

        dataAdapter = new ListAdapter();
        shabadRV.setAdapter(dataAdapter);

        if(lastAng==1){
            prevBtnView.setVisibility(View.INVISIBLE);
        }
        if(lastAng==1430){
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
                if (angSearchArea.getVisibility() == View.VISIBLE) {
                    numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(numericView.getHeight()).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            angSearchArea.setVisibility(View.GONE);
                            angTextView.setText("");
                            angIDString.setLength(0);
                            searchIcon.setColorFilter(tintInactive);
                            numericView.setVisibility(View.INVISIBLE);
                            angKeyboardHandlerView.setVisibility(View.INVISIBLE);
                            angTextViewDummy.setVisibility(View.VISIBLE);
                        }
                    }, 220);
                    return;
                } else {
                    finish();
                }
            }
        });

        nextBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastAng +=1;
                if(lastAng==1430){
                    nextBtnView.setVisibility(View.INVISIBLE);
                }
                mEditor.putInt(getString(R.string.lastAng), lastAng).commit();
                database = angHelper.openDatabase();
                cursor = database.rawQuery("SELECT * FROM Shabad_ID WHERE ANG_ID = ? AND SOURCE_ID = ?", new String[]{String.valueOf(lastAng), "SGGS"});
                cursor.moveToFirst();
                database.close();
                if(cursor.isNull(1)){
                    return;
                }
                shabadHeading.setText("AMg : "+cursor.getInt(1));
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
                lastAng -= 1;
                if(lastAng==1){
                    prevBtnView.setVisibility(View.INVISIBLE);
                }
                mEditor.putInt(getString(R.string.lastAng), lastAng).commit();
                database = angHelper.openDatabase();
                cursor = database.rawQuery("SELECT * FROM Shabad_ID WHERE ANG_ID = ? AND SOURCE_ID = ?", new String[]{String.valueOf(lastAng), "SGGS"});
                cursor.moveToFirst();
                database.close();
                if(cursor.isNull(1)){
                    return;
                }
                shabadHeading.setText("AMg : "+cursor.getInt(1));
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

        goToAngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(angSearchArea.getVisibility()==View.GONE){
                    angSearchArea.setVisibility(View.VISIBLE);
                    angKeyboardHandlerView.setVisibility(View.VISIBLE);
                    numericView.setTranslationY(numericView.getHeight());
                    numericView.setVisibility(View.VISIBLE);
                    numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            numericView.setTranslationY(0);
                        }
                    }, 220);
                }
            }
        });

        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int angNo;
                try{
                    angNo = Integer.parseInt(angIDString.toString());
                }
                catch (Exception e){
                    return;
                }
                if(angNo > 0 && angNo <=1430){
                    database = angHelper.openDatabase();
                    cursor = database.rawQuery("SELECT * FROM Shabad_ID WHERE ANG_ID = ? AND SOURCE_ID = ?", new String[]{String.valueOf(angNo), "SGGS"});
                    cursor.moveToFirst();
                    database.close();
                    shabadHeading.setText("AMg : "+cursor.getInt(1));
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.smoothScrollToPosition(0);

                    numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(numericView.getHeight()).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            angSearchArea.setVisibility(View.GONE);
                            angTextView.setText("");
                            angIDString.setLength(0);
                            searchIcon.setColorFilter(tintInactive);
                            numericView.setVisibility(View.INVISIBLE);
                            angKeyboardHandlerView.setVisibility(View.INVISIBLE);
                            angTextViewDummy.setVisibility(View.VISIBLE);
                        }
                    }, 220);

                    timeHandler.removeCallbacks(timerClock);
                    timeHandler.postDelayed(timerClock, timer);
                    lastAng = angNo;
                    mEditor.putInt(getString(R.string.lastAng), lastAng).commit();
                    if(lastAng==1){
                        prevBtnView.setVisibility(View.INVISIBLE);
                    } else prevBtnView.setVisibility(View.VISIBLE);
                    if(lastAng==1430){
                        nextBtnView.setVisibility(View.INVISIBLE);
                    } else nextBtnView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(AngActivity.this, "Please enter a number between 1 and 1430 only", Toast.LENGTH_SHORT).show();
                    angIDString.setLength(0);
                    angTextView.setText("");
                    angTextViewDummy.setVisibility(View.VISIBLE);
                    searchIcon.setColorFilter(tintInactive);
                }
            }
        });

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (angIDString.length()!=0) {
                    angIDString.deleteCharAt(angIDString.length() - 1);
                    angTextView.setText(angIDString.toString());
                }
                if(angTextView.length()==0) {
                    angTextView.setText("");
                    angTextViewDummy.setVisibility(View.VISIBLE);
                    searchIcon.setColorFilter(tintInactive);
                }
            }
        });

        undoBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                angIDString.setLength(0);
                angTextView.setText("");
                angTextViewDummy.setVisibility(View.VISIBLE);
                searchIcon.setColorFilter(tintInactive);
                return true;
            }
        });

        angKeyboardHandlerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(numericView.getHeight()).setDuration(220);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        angSearchArea.setVisibility(View.GONE);
                        angTextView.setText("");
                        angIDString.setLength(0);
                        searchIcon.setColorFilter(tintInactive);
                        numericView.setVisibility(View.INVISIBLE);
                        angKeyboardHandlerView.setVisibility(View.INVISIBLE);
                        angTextViewDummy.setVisibility(View.VISIBLE);
                    }
                }, 220);
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
                    Toast.makeText(AngActivity.this, "Removed from Saved", Toast.LENGTH_SHORT).show();
                } else {
                    savedBani.add(0, BaniString);
                    savedSource.add(0, SourceString);
                    savedAng.add(0, "AMg : "+Ang);
                    savedShabadID.add(0, ShabadID);
                    savedUniqueID.add(0, ""+uniqueID);
                    sourceComplete.add(0, "SGGS");
                    saveData();
                    Toast.makeText(AngActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                }
                outsideItemViewCloseOptionsView();
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

        itemViewOptionsShareQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AngActivity.this, QuoteActivity.class);
                intent.putExtra("quoteSource", quoteSource);
                intent.putExtra("quoteShabadID", quoteShabadID);
                intent.putExtra("quoteLineID", quoteLineID);
                startActivity(intent);
            }
        });

        itemViewOptionsShareShabad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AngActivity.this, ShabadArtActivity.class);
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
                mEditor.putInt(getString(R.string.angFontSize), hukamnamaFontSize).commit();
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
                mEditor.putInt(getString(R.string.angFontSize), hukamnamaFontSize).commit();
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
                mEditor.putInt(getString(R.string.angFontSize), hukamnamaFontSize).commit();
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
                mEditor.putInt(getString(R.string.angFontSize), hukamnamaFontSize).commit();
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
                mEditor.putInt(getString(R.string.angFontSize), hukamnamaFontSize).commit();
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
                mEditor.putInt(getString(R.string.angFontSize), hukamnamaFontSize).commit();
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
                mEditor.putInt(getString(R.string.angFontSize), hukamnamaFontSize).commit();
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
                    mEditor.putBoolean(getString(R.string.angShowTransliteration), true).commit();
                } else {
                    showTransliteration = false;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.angShowTransliteration), false).commit();
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
                    mEditor.putBoolean(getString(R.string.angShowEnglish), true).commit();
                } else {
                    showEnglish = false;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.angShowEnglish), false).commit();
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
                    mEditor.putBoolean(getString(R.string.angShowShabadArth), true).commit();
                } else {
                    showShabadArth = false;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.angShowShabadArth), false).commit();
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
                    mEditor.putBoolean(getString(R.string.angShowBhavArth), true).commit();
                } else {
                    showBhavArth = false;
                    updateViewVisiblity();
                    shabadRV.setAdapter(null);
                    dataAdapter.notifyDataSetChanged();
                    shabadRV.setAdapter(dataAdapter);
                    enableGurbaniBtns();
                    mEditor.putBoolean(getString(R.string.angShowBhavArth), false).commit();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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

    private void searchShabad(String str) {
        angTextViewDummy.setVisibility(View.GONE);
        searchIcon.setColorFilter(tintActive);
        if (angIDString.length() < 15) {
            if (str.contentEquals("")) {
                angIDString.setLength(0);
                angIDString.append(str);
                angTextView.setText(angIDString.toString());
            } else {
                angIDString.append(str);
                angTextView.setText(angIDString.toString());
            }
        }else Toast.makeText(AngActivity.this, "Search Limit Reached", Toast.LENGTH_SHORT).show();
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
                        if (cursor.getString(11) == null) {
                            SourceString = (cursor.getString(12) == null) ? "" : cursor.getString(12) + " AMg : " + cursor.getInt(1);
                        } else
                            SourceString = cursor.getString(11) + " ".concat((cursor.getString(12) == null) ? "" : cursor.getString(12)) + " AMg : " + cursor.getInt(1);
                        Ang = ""+cursor.getInt(1);
                        ShabadID = ""+cursor.getInt(3);
                        uniqueID = unique;
                        SourceComplete = "SGGS";
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
            return;
        }
        if (angSearchArea.getVisibility() == View.VISIBLE) {
            numericView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(numericView.getHeight()).setDuration(220);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    angSearchArea.setVisibility(View.GONE);
                    angTextView.setText("");
                    angIDString.setLength(0);
                    searchIcon.setColorFilter(tintInactive);
                    numericView.setVisibility(View.INVISIBLE);
                    angKeyboardHandlerView.setVisibility(View.INVISIBLE);
                    angTextViewDummy.setVisibility(View.VISIBLE);
                }
            }, 220);
            return;
        } else {
            finish();
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

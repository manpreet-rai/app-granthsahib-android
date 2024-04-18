package com.randoms.granthsahib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SundarGutka extends AppCompatActivity implements Serializable {
    int mode, sundarGutkaFontSize;
    Boolean quicklistMode, slimFont, regularFont, mediumFont, boldFont;
    Button allBanisBtn, quickListBtn, slimFontStyleBtn, regularFontStyleBtn, mediumFontStyleBtn, boldFontStyleBtn;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    TextView nitnemHeading, baniHeading, itemViewOptionsSave, quickListEmptyMessage, fontSizeSeekBarProgress;
    ImageButton sundarGutkaBackBtn, sundarGuktaGoToBtn, settingsBtn, closeItemViewOptionsViewBtn, closeSettingsViewBtn;
    SeekBar fontSizeSeekBar;

    DrawerLayout sundarGutkaDrawer;
    View sundarGutkaBtnsView, emptyMarginView, itemViewOptionsView, itemViewBackgroundHandlerView, itemViewOptionsMenu, settingsMenuView, settingsMenuHandlerView, settingsAreaView;
    RecyclerView baniListRV, quickBaniListRV, gurbaniRV, gotoRV;

    ArrayList<ArrayList<String>> GurbaniArrayList;
    ArrayList<ArrayList<Boolean>> HeadingArrayList;
    ArrayList<ArrayList<Integer>> JumpToIndexArrayList;
    ArrayList<ArrayList<String>> JumpToLabelsArrayList;

    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManagerQ;
    LinearLayoutManagerWithSmoothScroller linearLayoutManager2;

    int loadBaniIndex, indexCheckInQuickList, sundarGutkaLastPosition, sundarGutkaLastBaniIndex, gurbaniLayout;
    String sundarGutkaLastBani, sundarGutkaCurrentBani;
    String [] BaniNames;
    String [] BaniNamesEnglish;

    ArrayList<Integer> quickListArray;

    GurbaniListAdapter gurbaniListAdapter;
    GurbaniQuickListAdapter gurbaniQuickListAdapter;
    GurbaniViewAdapter gurbaniViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.AppTheme);

        setTheme(mode);
        setContentView(R.layout.activity_sundar_gutka);

        sundarGutkaDrawer = findViewById(R.id.sundarGutkaDrawer);
        sundarGutkaBtnsView = findViewById(R.id.sundarGutkaBtnsView);
        allBanisBtn = findViewById(R.id.allBanisBtn);
        quickListBtn = findViewById(R.id.quickListBtn);
        sundarGutkaBackBtn = findViewById(R.id.sundarGutkaBackBtn);
        sundarGuktaGoToBtn = findViewById(R.id.sundarGuktaGoToBtn);
        settingsBtn = findViewById(R.id.settingsBtn);
        nitnemHeading = findViewById(R.id.nitnemHeading);
        baniHeading = findViewById(R.id.baniHeading);
        quickListEmptyMessage = findViewById(R.id.quickListEmptyMessage);
        baniListRV = findViewById(R.id.baniListRV);
        quickBaniListRV = findViewById(R.id.quickBaniListRV);
        gurbaniRV = findViewById(R.id.gurbaniRV);
        gotoRV = findViewById(R.id.gotoRV);
        emptyMarginView = findViewById(R.id.emptyMarginView);
        itemViewOptionsSave = findViewById(R.id.itemViewOptionsSave);
        closeItemViewOptionsViewBtn = findViewById(R.id.closeItemViewOptionsViewBtn);
        itemViewOptionsView = findViewById(R.id.itemViewOptionsView);
        itemViewBackgroundHandlerView = findViewById(R.id.itemViewBackgroundHandlerView);
        itemViewOptionsMenu = findViewById(R.id.itemViewOptionsMenu);

        settingsMenuView = findViewById(R.id.settingsMenuView);
        settingsMenuHandlerView = findViewById(R.id.settingsMenuHandlerView);
        settingsAreaView = findViewById(R.id.settingsAreaView);
        closeSettingsViewBtn = findViewById(R.id.closeSettingsViewBtn);

        slimFontStyleBtn = findViewById(R.id.slimFontStyleBtn);
        regularFontStyleBtn = findViewById(R.id.regularFontStyleBtn);
        mediumFontStyleBtn = findViewById(R.id.mediumFontStyleBtn);
        boldFontStyleBtn = findViewById(R.id.boldFontStyleBtn);
        fontSizeSeekBarProgress = findViewById(R.id.fontSizeSeekBarProgress);
        fontSizeSeekBar = findViewById(R.id.fontSizeSeekBar);

        GurbaniArrayList = new ArrayList<ArrayList<String>>();
        HeadingArrayList = new ArrayList<ArrayList<Boolean>>();
        JumpToIndexArrayList = new ArrayList<ArrayList<Integer>>();
        JumpToLabelsArrayList = new ArrayList<ArrayList<String>>();

        InputStream inputStream;
        ObjectInputStream ois;
        try {
            AssetManager assetManager = this.getAssets();
            inputStream = assetManager.open("g0");
            ois = new ObjectInputStream(inputStream);
            GurbaniArrayList = (ArrayList<ArrayList<String>>) ois.readObject();
            inputStream.close();

            inputStream = assetManager.open("g1");
            ois = new ObjectInputStream(inputStream);
            HeadingArrayList = (ArrayList<ArrayList<Boolean>>) ois.readObject();
            inputStream.close();

            inputStream = assetManager.open("g2");
            ois = new ObjectInputStream(inputStream);
            JumpToIndexArrayList = (ArrayList<ArrayList<Integer>>) ois.readObject();
            inputStream.close();

            inputStream = assetManager.open("g3");
            ois = new ObjectInputStream(inputStream);
            JumpToLabelsArrayList = (ArrayList<ArrayList<String>>) ois.readObject();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(SundarGutka.this, "Error Occurred, Try Again Later", Toast.LENGTH_LONG).show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(SundarGutka.this, "Error Occurred, Try Again Later", Toast.LENGTH_LONG).show();
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(SundarGutka.this, "Error Occurred, Try Again Later", Toast.LENGTH_LONG).show();
            finish();
        }

        BaniNames = new String[]{"jpujI swihb", "jwpu swihb", "qÍpRswid sÍXy", "cOpeI swihb", "AnMdu swihb", "rhrwis swihb", "r`iKAw Sbd", "kIrqn soihlw", "Sbd hjwry", "bwrh mwhw mWJ",
                "Sbd hjwry pw 10", "sÍXy dInn", "AwrqI", "Ardws", "suKmnI swihb", "Awsw dI vwr", "dKxI EAMkwr", "isD gosit", "bwvn AKrI", "jYqsrI dI vwr",
                "rwmklI kI vwr", "bsMq kI vwr", "bwrh mwhw quKwrI", "lwvW", "slok mhlw 9", "kucjI", "sucjI", "guxvMqI", "rwgmwlw", "Akwl ausqq cOpeI", "cMfI dI vwr"};

        BaniNamesEnglish = new String[]{"Japji Sahib", "Jaap Sahib", "Tav Prasad Sawaiye", "Chaupai Sahib", "Anand Sahib", "Rehraas Sahib", "Rakhya Shabad", "Kirtan Sohila", "Shabad Hazaare", "Barah Maha Maaj",
                "Shabad Hazaare Paatshahi 10", "Swaiye Deenan", "Aarti", "Ardaas", "Sukhmani Sahib", "Asa Di Vaar", "Dakhnee Oankaar", "Sidh Gosat", "Baavan Akhree", "Jaitsari Di Vaar",
                "Ramkali Ki Vaar", "Basant Ki Vaar", "Barah Maha Tukhari", "Laavan", "Salok Mehal 9", "Kuchaji", "Suchaji", "Gunvanti", "Raag Maala", "Akaal Ustat Chaupai", "Chandi Di Vaar"};

        quicklistMode = mPreferences.getBoolean(getString(R.string.quicklistMode), false);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        String json = mPreferences.getString(getString(R.string.quickListArray), null);
        quickListArray = gson.fromJson(json, type);

        sundarGutkaLastBani = mPreferences.getString(getString(R.string.sundarGutkaLastBani), "");
        sundarGutkaLastPosition = mPreferences.getInt(getString(R.string.sundarGutkaLastPosition), 0);
        sundarGutkaLastBaniIndex = mPreferences.getInt(getString(R.string.sundarGutkaLastBaniIndex), -1);

        if(quickListArray==null){
            quickListArray = new ArrayList<Integer>();
        }

        linearLayoutManager = new LinearLayoutManager(SundarGutka.this);
        linearLayoutManagerQ = new LinearLayoutManager(SundarGutka.this);

        linearLayoutManager2 = new LinearLayoutManagerWithSmoothScroller(SundarGutka.this);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(SundarGutka.this);

        gotoRV.setLayoutManager(linearLayoutManager3);

        gurbaniListAdapter = new GurbaniListAdapter();

        baniListRV.setLayoutManager(linearLayoutManager);
        baniListRV.setAdapter(gurbaniListAdapter);

        gurbaniQuickListAdapter = new GurbaniQuickListAdapter();
        quickBaniListRV.setLayoutManager(linearLayoutManagerQ);

        gurbaniRV.setLayoutManager(linearLayoutManager2);
        gurbaniRV.setKeepScreenOn(true);
        sundarGutkaDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if(quicklistMode){
            quickListBtn.setSelected(true);
            if(quickListArray.size()!=0){
                quickBaniListRV.setAdapter(gurbaniQuickListAdapter);
                quickBaniListRV.setVisibility(View.VISIBLE);
                quickListEmptyMessage.setVisibility(View.GONE);
            } else {
                quickBaniListRV.setAdapter(null);
                quickBaniListRV.setVisibility(View.GONE);
                quickListEmptyMessage.setVisibility(View.VISIBLE);
            }
            baniListRV.setVisibility(View.GONE);
        } else {
            allBanisBtn.setSelected(true);
        }

        sundarGutkaFontSize = mPreferences.getInt(getString(R.string.sundarGutkaFontSize), 19);

        slimFont = mPreferences.getBoolean(getString(R.string.slimFont), false);
        regularFont = mPreferences.getBoolean(getString(R.string.regularFont), true);
        mediumFont = mPreferences.getBoolean(getString(R.string.mediumFont), false);
        boldFont = mPreferences.getBoolean(getString(R.string.boldFont), false);

        fontSizeSeekBar.setMax(48);
        fontSizeSeekBar.setProgress(sundarGutkaFontSize);
        fontSizeSeekBarProgress.setText(""+sundarGutkaFontSize);

        updateFontSize();
        setSelectedFontStyleBtn();

        quickListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!quickListBtn.isSelected()){
                    quickListBtn.setSelected(true);
                    allBanisBtn.setSelected(false);
                    if(quickListArray.size()!=0){
                        quickBaniListRV.setAdapter(gurbaniQuickListAdapter);
                        quickBaniListRV.setVisibility(View.VISIBLE);
                        quickListEmptyMessage.setVisibility(View.GONE);
                    } else {
                        quickBaniListRV.setAdapter(null);
                        quickBaniListRV.setVisibility(View.GONE);
                        quickListEmptyMessage.setVisibility(View.VISIBLE);
                    }
                    baniListRV.setVisibility(View.GONE);
                    mEditor.putBoolean(getString(R.string.quicklistMode), true).commit();
                }
            }
        });

        allBanisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!allBanisBtn.isSelected()){
                    allBanisBtn.setSelected(true);
                    quickListBtn.setSelected(false);
                    baniListRV.setVisibility(View.VISIBLE);
                    quickBaniListRV.setVisibility(View.GONE);
                    mEditor.putBoolean(getString(R.string.quicklistMode), false).commit();
                }
            }
        });

        itemViewBackgroundHandlerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        closeItemViewOptionsViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        itemViewOptionsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quickListArray.contains(indexCheckInQuickList)){
                    int index = quickListArray.indexOf(indexCheckInQuickList);
                    quickListArray.remove(index);
                    gurbaniQuickListAdapter.notifyDataSetChanged();
                    Toast.makeText(SundarGutka.this, "Removed from Quick List", Toast.LENGTH_SHORT).show();
                } else {
                    quickListArray.add(indexCheckInQuickList);
                    Toast.makeText(SundarGutka.this, "Added to Quick List", Toast.LENGTH_SHORT).show();
                }
                if(quickListArray.size()!=0){
                    quickListEmptyMessage.setVisibility(View.GONE);
                } else {
                    quickBaniListRV.setVisibility(View.GONE);
                    quickListEmptyMessage.setVisibility(View.VISIBLE);
                }

                Gson gson = new Gson();
                String json = gson.toJson(quickListArray);
                mEditor.putString(getString(R.string.quickListArray), json).commit();
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
        });

        sundarGuktaGoToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sundarGutkaDrawer.openDrawer(GravityCompat.END);
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

        sundarGutkaBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SundarGutka.this.onBackPressed();
            }
        });

        slimFontStyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!slimFont){
                    slimFont = true;
                    regularFont = false;
                    mediumFont  = false;
                    boldFont = false;

                    setSelectedFontStyleBtn();
                    mEditor.putBoolean(getString(R.string.slimFont), slimFont).commit();
                    mEditor.putBoolean(getString(R.string.regularFont), regularFont).commit();
                    mEditor.putBoolean(getString(R.string.mediumFont), mediumFont).commit();
                    mEditor.putBoolean(getString(R.string.boldFont), boldFont).commit();
                }
            }
        });

        regularFontStyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!regularFont){
                    slimFont = false;
                    regularFont = true;
                    mediumFont  = false;
                    boldFont = false;

                    setSelectedFontStyleBtn();
                    mEditor.putBoolean(getString(R.string.slimFont), slimFont).commit();
                    mEditor.putBoolean(getString(R.string.regularFont), regularFont).commit();
                    mEditor.putBoolean(getString(R.string.mediumFont), mediumFont).commit();
                    mEditor.putBoolean(getString(R.string.boldFont), boldFont).commit();
                }
            }
        });

        mediumFontStyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediumFont){
                    slimFont = false;
                    regularFont = false;
                    mediumFont  = true;
                    boldFont = false;

                    setSelectedFontStyleBtn();
                    mEditor.putBoolean(getString(R.string.slimFont), slimFont).commit();
                    mEditor.putBoolean(getString(R.string.regularFont), regularFont).commit();
                    mEditor.putBoolean(getString(R.string.mediumFont), mediumFont).commit();
                    mEditor.putBoolean(getString(R.string.boldFont), boldFont).commit();
                }
            }
        });

        boldFontStyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!boldFont){
                    slimFont = false;
                    regularFont = false;
                    mediumFont  = false;
                    boldFont = true;

                    setSelectedFontStyleBtn();
                    mEditor.putBoolean(getString(R.string.slimFont), slimFont).commit();
                    mEditor.putBoolean(getString(R.string.regularFont), regularFont).commit();
                    mEditor.putBoolean(getString(R.string.mediumFont), mediumFont).commit();
                    mEditor.putBoolean(getString(R.string.boldFont), boldFont).commit();
                }
            }
        });

        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                fontSizeSeekBarProgress.setText(""+i);
                sundarGutkaFontSize = i;
                updateFontSize();
                mEditor.putInt(getString(R.string.sundarGutkaFontSize), i).commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public class GurbaniListAdapter extends RecyclerView.Adapter<GurbaniListAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sundargutkabanilist, null, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.baniNumber.setText(""+(position+1));
            holder.baniNameGurmukhi.setText(BaniNames[position]);
            holder.baniNameEnglish.setText(BaniNamesEnglish[position]);

            if(position==sundarGutkaLastBaniIndex){
                holder.lastReadToken.setVisibility(View.VISIBLE);
            } else holder.lastReadToken.setVisibility(View.INVISIBLE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadBaniIndex = baniListRV.getChildAdapterPosition(view);
                    sundarGutkaLastBaniIndex = loadBaniIndex;
                    sundarGutkaCurrentBani = BaniNames[loadBaniIndex];
                    sundarGutkaBtnsView.setVisibility(View.GONE);
                    emptyMarginView.setVisibility(View.VISIBLE);
                    nitnemHeading.setVisibility(View.INVISIBLE);
                    baniHeading.setText(BaniNames[position]);
                    baniHeading.setVisibility(View.VISIBLE);
                    gurbaniViewAdapter = new GurbaniViewAdapter();
                    gurbaniRV.setAdapter(gurbaniViewAdapter);
                    gurbaniRV.setVisibility(View.VISIBLE);
                    baniListRV.setVisibility(View.GONE);
                    sundarGutkaDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    settingsBtn.setVisibility(View.VISIBLE);
                    sundarGuktaGoToBtn.setVisibility(View.VISIBLE);

                    GoToAdapter goToAdapter = new GoToAdapter();
                    gotoRV.setAdapter(goToAdapter);

                    if(sundarGutkaCurrentBani.equals(sundarGutkaLastBani)){
                        gurbaniRV.smoothScrollToPosition(sundarGutkaLastPosition);
                    }

                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    indexCheckInQuickList = baniListRV.getChildAdapterPosition(view);
                    if(quickListArray.contains(indexCheckInQuickList)){
                        itemViewOptionsSave.setText("Remove From Quick List");
                    } else {
                        itemViewOptionsSave.setText("Add to Quick List");
                    }
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
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return BaniNames.length;
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            TextView baniNumber;
            TextView baniNameGurmukhi;
            TextView baniNameEnglish;
            TextView lastReadToken;
            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                baniNumber = itemView.findViewById(R.id.baniNumber);
                baniNameGurmukhi = itemView.findViewById(R.id.baniNameGurmukhi);
                baniNameEnglish = itemView.findViewById(R.id.baniNameEnglish);
                lastReadToken = itemView.findViewById(R.id.lastReadToken);
            }
        }
    }

    public class GurbaniQuickListAdapter extends RecyclerView.Adapter<GurbaniQuickListAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sundargutkabanilist, null, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            holder.baniNumber.setText(""+(position+1));
            holder.baniNameGurmukhi.setText(BaniNames[quickListArray.get(position)]);
            holder.baniNameEnglish.setText(BaniNamesEnglish[quickListArray.get(position)]);

            if(quickListArray.get(position)==sundarGutkaLastBaniIndex){
                holder.lastReadToken.setVisibility(View.VISIBLE);
            } else holder.lastReadToken.setVisibility(View.INVISIBLE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = quickBaniListRV.getChildAdapterPosition(view);
                    loadBaniIndex = quickListArray.get(pos);
                    sundarGutkaLastBaniIndex = loadBaniIndex;
                    sundarGutkaCurrentBani = BaniNames[loadBaniIndex];
                    sundarGutkaBtnsView.setVisibility(View.GONE);
                    emptyMarginView.setVisibility(View.VISIBLE);
                    nitnemHeading.setVisibility(View.INVISIBLE);
                    baniHeading.setText(BaniNames[loadBaniIndex]);
                    baniHeading.setVisibility(View.VISIBLE);
                    gurbaniViewAdapter = new GurbaniViewAdapter();
                    gurbaniRV.setAdapter(gurbaniViewAdapter);
                    gurbaniRV.setVisibility(View.VISIBLE);
                    baniListRV.setVisibility(View.GONE);
                    sundarGutkaDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    settingsBtn.setVisibility(View.VISIBLE);
                    sundarGuktaGoToBtn.setVisibility(View.VISIBLE);

                    GoToAdapter goToAdapter = new GoToAdapter();
                    gotoRV.setAdapter(goToAdapter);

                    if(sundarGutkaCurrentBani.equals(sundarGutkaLastBani)){
                        gurbaniRV.smoothScrollToPosition(sundarGutkaLastPosition);
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    indexCheckInQuickList = quickListArray.get(quickBaniListRV.getChildAdapterPosition(view));
                    itemViewOptionsSave.setText("Remove From Quick List");
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
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return quickListArray.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            TextView baniNumber;
            TextView baniNameGurmukhi;
            TextView baniNameEnglish;
            TextView lastReadToken;
            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                baniNumber = itemView.findViewById(R.id.baniNumber);
                baniNameGurmukhi = itemView.findViewById(R.id.baniNameGurmukhi);
                baniNameEnglish = itemView.findViewById(R.id.baniNameEnglish);
                lastReadToken = itemView.findViewById(R.id.lastReadToken);
            }
        }
    }

    public class GurbaniViewAdapter extends RecyclerView.Adapter<GurbaniViewAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(gurbaniLayout, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            if(HeadingArrayList.get(loadBaniIndex).get(position)){
                holder.sundarGutkaHeadingText.setText(GurbaniArrayList.get(loadBaniIndex).get(position));
                holder.sundarGutkaHeadingText.setVisibility(View.VISIBLE);
                holder.sundarGutkaBaniText.setVisibility(View.GONE);
            } else {
                holder.sundarGutkaBaniText.setText(GurbaniArrayList.get(loadBaniIndex).get(position));
                holder.sundarGutkaBaniText.setVisibility(View.VISIBLE);
                holder.sundarGutkaHeadingText.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return GurbaniArrayList.get(loadBaniIndex).size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            TextView sundarGutkaHeadingText;
            TextView sundarGutkaBaniText;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                sundarGutkaHeadingText = itemView.findViewById(R.id.sundarGutkaHeadingText);
                sundarGutkaBaniText = itemView.findViewById(R.id.sundarGutkaBaniText);

                sundarGutkaHeadingText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sundarGutkaFontSize);
                sundarGutkaBaniText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sundarGutkaFontSize);
            }
        }
    }

    public class GoToAdapter extends RecyclerView.Adapter<GoToAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_goto_layout, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            holder.gotoBani.setText(JumpToLabelsArrayList.get(loadBaniIndex).get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = gotoRV.getChildAdapterPosition(view);
                    gurbaniRV.smoothScrollToPosition(JumpToIndexArrayList.get(loadBaniIndex).get(pos));
                    sundarGutkaDrawer.closeDrawer(GravityCompat.END);
                }
            });
        }

        @Override
        public int getItemCount() {
            return JumpToIndexArrayList.get(loadBaniIndex).size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            TextView gotoBani;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                gotoBani = itemView.findViewById(R.id.gotoBani);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if(sundarGutkaDrawer.isDrawerOpen(GravityCompat.END)){
            sundarGutkaDrawer.closeDrawer(GravityCompat.END);
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

        if(itemViewOptionsView.getVisibility()==View.VISIBLE){
            itemViewOptionsMenu.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(itemViewOptionsMenu.getHeight()).alpha(0.0f).setDuration(220);
            itemViewBackgroundHandlerView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).alpha(0.0f).setDuration(220);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    itemViewOptionsView.setVisibility(View.INVISIBLE);
                    itemViewBackgroundHandlerView.setAlpha(1.0f);
                }
            }, 220);
            return;
        }

        if(gurbaniRV.getVisibility()==View.VISIBLE){
            baniHeading.setVisibility(View.INVISIBLE);
            sundarGutkaBtnsView.setVisibility(View.VISIBLE);
            emptyMarginView.setVisibility(View.GONE);
            nitnemHeading.setVisibility(View.VISIBLE);
            gurbaniViewAdapter = null;
            settingsBtn.setVisibility(View.INVISIBLE);
            sundarGutkaDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            sundarGuktaGoToBtn.setVisibility(View.INVISIBLE);
            if(quickListBtn.isSelected()){
                quickBaniListRV.setVisibility(View.VISIBLE);
            } else baniListRV.setVisibility(View.VISIBLE);
            gurbaniRV.setVisibility(View.GONE);

            sundarGutkaLastBani = sundarGutkaCurrentBani;
            sundarGutkaLastPosition = linearLayoutManager2.findFirstCompletelyVisibleItemPosition();

            gurbaniListAdapter.notifyDataSetChanged();
            if(gurbaniQuickListAdapter!=null){
                gurbaniQuickListAdapter.notifyDataSetChanged();
            }

            mEditor.putString(getString(R.string.sundarGutkaLastBani), sundarGutkaLastBani).commit();
            mEditor.putInt(getString(R.string.sundarGutkaLastPosition), sundarGutkaLastPosition).commit();
            mEditor.putInt(getString(R.string.sundarGutkaLastBaniIndex), sundarGutkaLastBaniIndex).commit();

            return;
        }
        else{
            finish();
        }
    }

    void updateFontSize(){
        if(gurbaniViewAdapter!=null){
            gurbaniRV.setAdapter(null);
            gurbaniViewAdapter.notifyDataSetChanged();
            gurbaniRV.setAdapter(gurbaniViewAdapter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sundarGutkaLastBani = sundarGutkaCurrentBani;
        sundarGutkaLastPosition = linearLayoutManager2.findFirstCompletelyVisibleItemPosition();

        mEditor.putString(getString(R.string.sundarGutkaLastBani), sundarGutkaLastBani).commit();
        mEditor.putInt(getString(R.string.sundarGutkaLastPosition), sundarGutkaLastPosition).commit();
        mEditor.putInt(getString(R.string.sundarGutkaLastBaniIndex), sundarGutkaLastBaniIndex).commit();
    }

    public void setSelectedFontStyleBtn(){
        slimFontStyleBtn.setSelected(false);
        regularFontStyleBtn.setSelected(false);
        mediumFontStyleBtn.setSelected(false);
        boldFontStyleBtn.setSelected(false);

        if (slimFont){
            slimFontStyleBtn.setSelected(true);
            gurbaniLayout = R.layout.list_gurbani_layout_slim;
        } else if(regularFont){
            regularFontStyleBtn.setSelected(true);
            gurbaniLayout = R.layout.list_gurbani_layout;
        } else if(mediumFont){
            mediumFontStyleBtn.setSelected(true);
            gurbaniLayout = R.layout.list_gurbani_layout_medium;
        }
        else {
            boldFontStyleBtn.setSelected(true);
            gurbaniLayout = R.layout.list_gurbani_layout_bold;
        }

        if(gurbaniViewAdapter!=null){
            gurbaniRV.setAdapter(null);
            gurbaniViewAdapter.notifyDataSetChanged();
            gurbaniRV.setAdapter(gurbaniViewAdapter);
        }
    }
}

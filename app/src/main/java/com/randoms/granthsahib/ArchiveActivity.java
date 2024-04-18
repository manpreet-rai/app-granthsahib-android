package com.randoms.granthsahib;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class ArchiveActivity extends AppCompatActivity {
    int tintActive, tintInactive, cancelColorActive, cancelColorInactive, mode;
    String link, date;

    View archiveHukamView, datePickerArea, internetErrorView, datePickerView, settingsMenuView, settingsMenuHandlerView, settingsAreaView;
    TextView gurmukhiHeading, gurmukhiContent, punjabiViakhia, punjabiContent, englishHeading, englishContent, angNo, datePicker, infoLabel, selectDateText;
    Button internetErrorBackBtn, cancelNumberPickersBtn, okayNumberPickersBtn, fontSizeMinus5Btn, fontSizeMinus3Btn, fontSizeBtn, fontSizePlus3Btn, fontSizePlus7Btn, fontSizePlus11Btn, fontSizePlus15Btn, showViakhiaBtn, showEnglishBtn;
    ImageButton archiveBack, settingsBtn, closeSettingsViewBtn;
    NumberPicker dayPicker, monthPicker, yearPicker;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    int hukamnamaFontSize, hukamnamaViakhiaVisibility, hukamnamaEnglishVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = this.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.AppTheme);

        setTheme(mode);
        setContentView(R.layout.activity_archive);

        datePicker = findViewById(R.id.datePicker);
        selectDateText = findViewById(R.id.selectDateText);
        internetErrorView = findViewById(R.id.internetErrorView);
        infoLabel = findViewById(R.id.infoLabel);
        internetErrorBackBtn = findViewById(R.id.internetErrorBackBtn);
        datePickerView = findViewById(R.id.datePickerView);
        dayPicker = findViewById(R.id.dayPicker);
        monthPicker = findViewById(R.id.monthPicker);
        yearPicker = findViewById(R.id.yearPicker);
        cancelNumberPickersBtn = findViewById(R.id.cancelNumberPickersBtn);
        okayNumberPickersBtn = findViewById(R.id.okayNumberPickersBtn);

        archiveBack = findViewById(R.id.archiveBack);

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
        showViakhiaBtn = findViewById(R.id.showViakhiaBtn);
        showEnglishBtn = findViewById(R.id.showEnglishBtn);

        archiveHukamView = findViewById(R.id.archiveHukamView);
        datePickerArea = findViewById(R.id.datePickerArea);
        gurmukhiHeading = findViewById(R.id.gurmukhiHeading);
        gurmukhiContent = findViewById(R.id.gurmukhiContent);
        punjabiViakhia = findViewById(R.id.punjabiViakhia);
        punjabiContent = findViewById(R.id.punjabiContent);
        englishHeading = findViewById(R.id.englishHeading);
        englishContent = findViewById(R.id.englishContent);
        angNo = findViewById(R.id.angNo);

        hukamnamaFontSize = mPreferences.getInt(getString(R.string.archiveFontSize), 19);
        updateFontSize();
        setSelectedFontBtn();

        hukamnamaViakhiaVisibility = mPreferences.getInt(getString(R.string.archiveViakhiaVisibility), 0);
        hukamnamaEnglishVisibility = mPreferences.getInt(getString(R.string.archiveEnglishVisibility), 0);

        if(hukamnamaViakhiaVisibility==0){
            showViakhiaBtn.setSelected(true);
            punjabiViakhia.setVisibility(View.VISIBLE);
            punjabiContent.setVisibility(View.VISIBLE);
        } else {
            showViakhiaBtn.setSelected(false);
            punjabiViakhia.setVisibility(View.GONE);
            punjabiContent.setVisibility(View.GONE);
        }

        if(hukamnamaEnglishVisibility==0){
            showEnglishBtn.setSelected(true);
            englishHeading.setVisibility(View.VISIBLE);
            englishContent.setVisibility(View.VISIBLE);
        }else {
            showEnglishBtn.setSelected(false);
            englishHeading.setVisibility(View.GONE);
            englishContent.setVisibility(View.GONE);
        }

        if(mode==R.style.AppTheme){
            tintActive = Color.parseColor("#333333");
            tintInactive = Color.parseColor("#8E8E8E");
            cancelColorActive = Color.parseColor("#C02F1D");
            cancelColorInactive = Color.parseColor("#8E8E8E");
        } else {
            tintActive = Color.parseColor("#DAE1EA");
            tintInactive = Color.parseColor("#4B647C");
            cancelColorActive = Color.parseColor("#DAE1EA");
            cancelColorInactive = Color.parseColor("#4B647C");
        }

        String [] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov","Dec"};
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setValue(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setDisplayedValues(monthNames);
        monthPicker.setValue(Calendar.getInstance().get(Calendar.MONTH)+1);
        yearPicker.setMinValue(2002);
        yearPicker.setMaxValue(Calendar.getInstance().get(Calendar.YEAR));
        yearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));

        internetErrorBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        archiveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        datePickerArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoLabel.setVisibility(View.INVISIBLE);
                internetErrorView.setVisibility(View.INVISIBLE);
                datePickerView.setVisibility(View.VISIBLE);
            }
        });
        cancelNumberPickersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (archiveHukamView.getVisibility() == View.INVISIBLE) {
                    finish();
                } else{
                    datePickerView.setVisibility(View.INVISIBLE);
                }
            }
        });
        okayNumberPickersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()){
                    date = ""+yearPicker.getValue()+"/"+monthPicker.getValue()+"/"+dayPicker.getValue();
                    link = "https://api.gurbaninow.com/v2/hukamnama/"+date;
                    archiveHukamView.setVisibility(View.INVISIBLE);
                    infoLabel.setText("Loading... Please Wait!!!");
                    infoLabel.setTextColor(tintActive);
                    infoLabel.setVisibility(View.VISIBLE);
                    datePicker.setText("Loading...");
                    selectDateText.setText("Please Wait");
                    datePickerView.setVisibility(View.INVISIBLE);
                    new FetchDatedHukam().execute();
                }else{
                    if(archiveHukamView.getVisibility()==View.VISIBLE){
                        archiveHukamView.setVisibility(View.VISIBLE);
                    }
                    internetErrorView.setVisibility(View.VISIBLE);
                    datePickerView.setVisibility(View.INVISIBLE);
                }
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
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.archiveFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizeMinus3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 16;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.archiveFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 19;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.archiveFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizePlus3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 22;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.archiveFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizePlus7Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 26;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.archiveFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizePlus11Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 30;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.archiveFontSize), hukamnamaFontSize).commit();
            }
        });
        fontSizePlus15Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hukamnamaFontSize = 34;
                updateFontSize();
                deselectAllFontBtns();
                mEditor.putInt(getString(R.string.archiveFontSize), hukamnamaFontSize).commit();
            }
        });

        showViakhiaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hukamnamaViakhiaVisibility==0){
                    showViakhiaBtn.setSelected(false);
                    hukamnamaViakhiaVisibility=8;
                    punjabiViakhia.setVisibility(View.GONE);
                    punjabiContent.setVisibility(View.GONE);
                    mEditor.putInt(getString(R.string.archiveViakhiaVisibility), 8).commit();
                } else {
                    showViakhiaBtn.setSelected(true);
                    hukamnamaViakhiaVisibility=0;
                    punjabiViakhia.setVisibility(View.VISIBLE);
                    punjabiContent.setVisibility(View.VISIBLE);
                    mEditor.putInt(getString(R.string.archiveViakhiaVisibility), 0).commit();
                }
            }
        });
        showEnglishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hukamnamaEnglishVisibility==0){
                    showEnglishBtn.setSelected(false);
                    hukamnamaEnglishVisibility=8;
                    englishHeading.setVisibility(View.GONE);
                    englishContent.setVisibility(View.GONE);
                    mEditor.putInt(getString(R.string.archiveEnglishVisibility), 8).commit();
                } else {
                    showEnglishBtn.setSelected(true);
                    hukamnamaEnglishVisibility=0;
                    englishHeading.setVisibility(View.VISIBLE);
                    englishContent.setVisibility(View.VISIBLE);
                    mEditor.putInt(getString(R.string.archiveEnglishVisibility), 0).commit();
                }
            }
        });
    }

    public class FetchDatedHukam extends AsyncTask<Void, Void, Void> {
        Boolean error = false;
        String jsonData = "";
        ArrayList<String> hukamFetch = new ArrayList<String>();
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                hukamFetch.clear();
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if(httpURLConnection.getResponseCode()!=200){
                    error = true;
                    hukamFetch = null;
                    return null;
                }
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
                if(jsonObject.get("hukamnamainfo").equals(JSONObject.NULL)){
                    error = true;
                    hukamFetch = null;
                    //this.cancel(true);
                    return null;
                }
                dateObject = (JSONObject) jsonObject.get("hukamnamainfo");
                hukamFetch.add("Ang : "+dateObject.get("pageno"));

                JSONArray hukamnamaArray = (JSONArray) jsonObject.get("hukamnama");
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
                error = true;
                hukamFetch = null;
            } catch (IOException e) {
                error = true;
                hukamFetch = null;
            } catch (JSONException e) {
                error = true;
                hukamFetch = null;
            }
            return null;
    }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(hukamFetch == null){
                datePicker.setText("Try Again Later");
                selectDateText.setText("Change Date");
                infoLabel.setText("Error Fetching Data, Please Try Again Later");
                infoLabel.setTextColor(cancelColorActive);
            }
            else {
                punjabiViakhia.setVisibility(hukamnamaViakhiaVisibility);
                punjabiContent.setVisibility(hukamnamaViakhiaVisibility);
                englishHeading.setVisibility(hukamnamaEnglishVisibility);
                englishContent.setVisibility(hukamnamaEnglishVisibility);

                gurmukhiHeading.setText(hukamFetch.get(2));
                gurmukhiContent.setText(hukamFetch.get(4));
                punjabiContent.setText(hukamFetch.get(6));
                englishHeading.setText(hukamFetch.get(3));
                englishContent.setText(hukamFetch.get(5));
                angNo.setText(hukamFetch.get(1));
                infoLabel.setVisibility(View.INVISIBLE);
                archiveHukamView.setVisibility(View.VISIBLE);
                datePicker.setText(hukamFetch.get(0));
                selectDateText.setText("Change Date");
            }
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return  (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
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
        if (datePickerView.getVisibility() == View.VISIBLE && archiveHukamView.getVisibility() == View.VISIBLE) {
            datePickerView.setVisibility(View.INVISIBLE);
        } else {
            finish();
        }
    }

    void updateFontSize(){
        gurmukhiHeading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize+2);
        gurmukhiContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        punjabiViakhia.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        punjabiContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        englishHeading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize);
        englishContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hukamnamaFontSize-1);
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
}

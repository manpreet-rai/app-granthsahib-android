package com.randoms.granthsahib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class EnglishSearch extends AppCompatActivity {
    int mode, tintActive, tintInactive, cancelColorActive, cancelColorInactive;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    View englishSearchArea, keyboardTouchHandlerView;
    EditText englishSearchBox;
    ImageButton englishBarGoBtn;
    ImageView englishSearchBoxIcon;
    Button englishCancelBtn;
    RecyclerView englishSearchResultsRV;
    TextView warningMessage;

    ArrayList<String> typedFirstLetters, replacementColumn0, replacementColumn1, replacementColumn2;
    ArrayList<Integer> firstLetterChoices;
    ArrayList<ArrayList<String>> combinedList;

    Cursor gurmukhiResultsCursor;
    ListAdapter2 gurmukhiResultAdapter;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.AppTheme);

        setTheme(mode);
        setContentView(R.layout.activity_english_search);

        englishSearchArea = findViewById(R.id.englishSearchArea);
        englishSearchBox = findViewById(R.id.englishSearchBox);
        englishSearchBoxIcon = findViewById(R.id.englishSearchBoxIcon);
        englishBarGoBtn = findViewById(R.id.englishBarGoBtn);
        englishCancelBtn = findViewById(R.id.englishCancelBtn);
        englishSearchResultsRV = findViewById(R.id.englishSearchResultsRV);
        keyboardTouchHandlerView = findViewById(R.id.keyboardTouchHandlerView);
        warningMessage = findViewById(R.id.warningMessage);

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

        linearLayoutManager = new LinearLayoutManager(EnglishSearch.this);
        englishSearchResultsRV.setLayoutManager(linearLayoutManager);

        typedFirstLetters = new ArrayList<String>(Arrays.asList("u", "oo", "o", "a", "e", "i", "s", "h", "k", "kh", "g", "gh", "ch", "sh", "j", "jh", "y", "t", "th", "d", "dh", "n", "p", "ph", "f", "b", "bh", "m", "r", "l", "v", "w", "c", "q", "x", "z"));
        firstLetterChoices = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        replacementColumn0 = new ArrayList<String>(Arrays.asList("a", "a", "E", "A", "e", "e", "s", "h", "k", "K", "g", "G", "c", "s", "j", "J", "\\", "t", "T", "f", "F", "x", "p", "P", "P", "b", "B","m", "r", "l", "v", "v", "k", "k", "z", "z"));
        replacementColumn1 = new ArrayList<String>(Arrays.asList("", "", "", "", "", "", "", "", "", "", "|", "", "C", "C", "", "", "X", "q", "Q", "d", "D", "n", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        replacementColumn2 = new ArrayList<String>(Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "V", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

        combinedList = new ArrayList<>();
        combinedList.add(replacementColumn0);
        combinedList.add(replacementColumn1);
        combinedList.add(replacementColumn2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                englishSearchBox.requestFocus();
                keyboardTouchHandlerView.setVisibility(View.VISIBLE);
                englishSearchBoxIcon.setColorFilter(tintActive);
                englishCancelBtn.setTextColor(cancelColorActive);
                englishBarGoBtn.setColorFilter(cancelColorActive);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(englishSearchBox, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 220);

        englishSearchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                englishSearchBox.requestFocus();
                keyboardTouchHandlerView.setVisibility(View.VISIBLE);
                englishSearchBoxIcon.setColorFilter(tintActive);
                englishCancelBtn.setTextColor(cancelColorActive);
                englishBarGoBtn.setColorFilter(cancelColorActive);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(englishSearchBox, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        keyboardTouchHandlerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboardTouchHandlerView.setVisibility(View.INVISIBLE);
                englishSearchBoxIcon.setColorFilter(tintInactive);
                englishCancelBtn.setTextColor(cancelColorInactive);
                englishBarGoBtn.setColorFilter(cancelColorInactive);
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        englishSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                englishSearchBox.requestFocus();
                keyboardTouchHandlerView.setVisibility(View.VISIBLE);
                englishSearchBoxIcon.setColorFilter(tintActive);
                englishCancelBtn.setTextColor(cancelColorActive);
                englishBarGoBtn.setColorFilter(cancelColorActive);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(englishSearchBox, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        englishSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    englishBarGoBtn.setVisibility(View.INVISIBLE);
                } else englishBarGoBtn.setVisibility(View.VISIBLE);
            }
        });

        englishBarGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                keyboardTouchHandlerView.setVisibility(View.INVISIBLE);
                englishSearchResultsRV.setVisibility(View.INVISIBLE);
                englishSearchResultsRV.setAdapter(null);
                warningMessage.setVisibility(View.INVISIBLE);
                goBtnAction();
            }
        });

        englishSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    keyboardTouchHandlerView.setVisibility(View.INVISIBLE);
                    englishSearchResultsRV.setVisibility(View.INVISIBLE);
                    englishSearchResultsRV.setAdapter(null);
                    warningMessage.setVisibility(View.INVISIBLE);
                    goBtnAction();
                }
                return true;
            }
        });


        englishCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void goBtnAction(){
        DatabaseHelper mHelper = new DatabaseHelper(EnglishSearch.this);
        SQLiteDatabase db = mHelper.openDatabase();

        String searchString = englishSearchBox.getText().toString().toLowerCase().replaceAll("[^A-Za-z\\s]+", "").trim();
        String[] words = searchString.split("\\s+");
        if(words.length<4){
            warningMessage.setVisibility(View.VISIBLE);
            return;
        }
        if(words.length>20){
            words = Arrays.copyOfRange(words, 0, 20);
        }

        warningMessage.setVisibility(View.INVISIBLE);

        ArrayList<String> wordsToSearch = new ArrayList<>();
        ArrayList<Character> containsH = new ArrayList<>(Arrays.asList('k', 'g', 'c', 's', 'j', 't', 'd', 'p', 'b'));

        for(String str : words){
            if(str.length()>1 && str.charAt(0)=='o' && str.charAt(1)=='o'){
                wordsToSearch.add("oo");
            } else if(str.length()>1 && containsH.contains(str.charAt(0)) && str.charAt(1)=='h'){
                wordsToSearch.add(""+ str.charAt(0)+"h");
            }
            else wordsToSearch.add(""+str.charAt(0));
        }

        int initialChoices = firstLetterChoices.get(typedFirstLetters.indexOf(wordsToSearch.get(0)));

        ArrayList<String> queryArray = new ArrayList<>();
        for(int i = 0; i<initialChoices;i++){
            queryArray.add("");
        }

        for(int j = 0; j<wordsToSearch.size();j++){
            ArrayList<String> tempArrayList = new ArrayList<String>();

            int newChoices = firstLetterChoices.get(typedFirstLetters.indexOf(wordsToSearch.get(j)));

            try{
                for(int innerchoice = 0; innerchoice < newChoices; innerchoice++){
                    for(int queryElement = 0; queryElement<queryArray.size();queryElement++){
                        tempArrayList.add(queryArray.get(queryElement)+combinedList.get(innerchoice).get(typedFirstLetters.indexOf(wordsToSearch.get(j))));
                    }
                }
                queryArray.clear();
                queryArray = tempArrayList;
            }
            catch (Exception e){
                return;
            }

        }

        String [] QueryList = new String[queryArray.size()];
        StringBuilder QueryBuilder = new StringBuilder();

        for(int i = 0; i<queryArray.size();i++){
            QueryList[i] = "%"+queryArray.get(i)+"%";
            if(i==0){
                QueryBuilder.append(" FIRST_LETTERS LIKE ?");
            } else{
                QueryBuilder.append(" OR FIRST_LETTERS LIKE ?");
            }
        }

        gurmukhiResultsCursor = db.rawQuery("SELECT * FROM Shabad_ID WHERE"+QueryBuilder.toString(), QueryList);

        if(!gurmukhiResultsCursor.moveToNext()){
            Toast.makeText(EnglishSearch.this, "No Results Found", Toast.LENGTH_LONG).show();
            return;
        }

        gurmukhiResultAdapter = new ListAdapter2();
        englishSearchResultsRV.setAdapter(gurmukhiResultAdapter);
        englishSearchResultsRV.setVisibility(View.VISIBLE);
        db.close();
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
            gurmukhiResultsCursor.moveToPosition(position);

            holder.banitxt.setText(gurmukhiResultsCursor.getString(5));
            holder.sourceInfotxt.setText(gurmukhiResultsCursor.getString(20));
            if(gurmukhiResultsCursor.getString(8).contentEquals("SGGS")){
                holder.angIDtxt.setText("AMg "+gurmukhiResultsCursor.getInt(1));
            } else holder.angIDtxt.setText("");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gurmukhiResultsCursor.moveToPosition(englishSearchResultsRV.getChildAdapterPosition(view));
                    Intent intent = new Intent(EnglishSearch.this, ShabadActivity.class);
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
}

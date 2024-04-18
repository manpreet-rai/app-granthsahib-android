package com.randoms.granthsahib;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "prefetch.cache";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDatabase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if(Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
        copyDataBase();
        this.openDatabase();
    }

    public void updateDataBase() throws IOException{
        if (mNeedUpdate){
            File dbFile = new File(DB_PATH+DB_NAME);
            if (dbFile.exists()){
                dbFile.delete();
            }
            copyDataBase();
            mNeedUpdate = false;
        }
    }
    private boolean checkDataBase(){
        File dbFile = new File(DB_PATH+DB_NAME);
        return dbFile.exists();
    }
    private void copyDataBase(){
        if(!checkDataBase()){
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            }
            catch (IOException mIOException){
                throw new Error("Error Copying Database");
            }
        }
    }

    private void copyDBFile() throws IOException{
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(DB_PATH+DB_NAME);
        byte [] buffer = new byte[1024];
        int length;
        while ((length = mInput.read(buffer))>0){
            mOutput.write(buffer, 0, length);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public SQLiteDatabase openDatabase() throws SQLException{
        mDatabase = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDatabase;
    }
    @Override
    public synchronized void close(){
        if(mDatabase !=null){
            mDatabase.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //if (i1>i){
        //    mNeedUpdate = true;
        //}
    }
}

package it.unige.dibris.baddroids.db;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.unige.dibris.baddroids.App;
import it.unige.dibris.baddroids.db.PermInvokeContract.PermissionEntry;
import it.unige.dibris.baddroids.db.PermInvokeContract.MethodInvocationEntry;
import it.unige.dibris.baddroids.engine.MethodInvocation;

public class PermInvokeDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PermInvoke.db";


    public PermInvokeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PermInvokeDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private static final String SQL_CREATE_PERMISSION =
            "CREATE TABLE " + PermissionEntry.TABLE_NAME + " (" +
                    PermissionEntry._ID + " INTEGER PRIMARY KEY," +
                    PermissionEntry.COLUMN_NAME_PERNAME + " TEXT)";

    private static final String SQL_DELETE_PERMISSION =
            "DROP TABLE IF EXISTS " + PermissionEntry.TABLE_NAME;

    private static final String SQL_CREATE_INVOKE =
            "CREATE TABLE " + MethodInvocationEntry.TABLE_NAME + " (" +
                    MethodInvocationEntry._ID + " INTEGER PRIMARY KEY," +
                    MethodInvocationEntry.COLUMN_NAME_CLASS_METHOD + " TEXT)";

    private static final String SQL_DELETE_INVOKE =
            "DROP TABLE IF EXISTS " + MethodInvocationEntry.TABLE_NAME;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PERMISSION);
        db.execSQL(SQL_CREATE_INVOKE);
        AssetManager assetManager = App.getContext().getAssets();
        try {
            populateDbWithPermissions(db, assetManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_PERMISSION);
        db.execSQL(SQL_DELETE_INVOKE);
        onCreate(db);
    }

    private void populateDbWithPermissions(SQLiteDatabase db, AssetManager assetManager) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open("perMapping.txt")));
        String line = null;
        while((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                ContentValues cv = new ContentValues();
                cv.put(PermissionEntry._ID, parts[0]);
                cv.put(PermissionEntry.COLUMN_NAME_PERNAME, parts[1]);
                long newRowId = db.insert(PermissionEntry.TABLE_NAME, null, cv);
                Log.d("",""+newRowId);
            }
        }
    }
}

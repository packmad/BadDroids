package it.unige.dibris.baddroids.db;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.unige.dibris.baddroids.App;
import it.unige.dibris.baddroids.db.PermInvokeContract.PermissionEntry;
import it.unige.dibris.baddroids.db.PermInvokeContract.MethodInvocationEntry;
import it.unige.dibris.baddroids.engine.MethodInvocation;

public class PermInvokeDbHelper extends SQLiteOpenHelper {
    private static final String TAG = PermInvokeDbHelper.class.getCanonicalName();

    private static final String DATABASE_NAME = "PermInvoke.db";
    private static PermInvokeDbHelper mInstance = null;
    private static final int DATABASE_VERSION = 2;
    private static SQLiteDatabase readableDatabase;


    public static PermInvokeDbHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new PermInvokeDbHelper(ctx.getApplicationContext());
        }
        if (readableDatabase == null || !readableDatabase.isOpen())
            readableDatabase = mInstance.getReadableDatabase();
        return mInstance;
    }


    private PermInvokeDbHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private PermInvokeDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private static final String SQL_CREATE_PERMISSION =
            "CREATE TABLE " + PermissionEntry.TABLE_NAME + " (" +
                    PermissionEntry._ID + " INTEGER PRIMARY KEY," +
                    PermissionEntry.COLUMN_NAME_PERNAME + " TEXT," +
                    PermissionEntry.COLUMN_NAME_PERWEIGHT + " REAL)";

    private static final String SQL_CREATE_PERMISSION_INDEX =
            "CREATE INDEX permission_index ON " + PermissionEntry.TABLE_NAME + " (" +
                    PermissionEntry.COLUMN_NAME_PERNAME + ")";

    private static final String SQL_DELETE_PERMISSION =
            "DROP TABLE IF EXISTS " + PermissionEntry.TABLE_NAME;

    private static final String SQL_CREATE_INVOKE =
            "CREATE TABLE " + MethodInvocationEntry.TABLE_NAME + " (" +
                    MethodInvocationEntry._ID + " INTEGER PRIMARY KEY," +
                    MethodInvocationEntry.COLUMN_NAME_CLASS_METHOD + " TEXT," +
                    MethodInvocationEntry.COLUMN_NAME_INVWEIGHT + " REAL)";

    private static final String SQL_CREATE_INVOKE_INDEX =
            "CREATE INDEX invoke_index ON " + MethodInvocationEntry.TABLE_NAME + " (" +
                    MethodInvocationEntry.COLUMN_NAME_CLASS_METHOD + ")";

    private static final String SQL_DELETE_INVOKE =
            "DROP TABLE IF EXISTS " + MethodInvocationEntry.TABLE_NAME;


    /*
    https://stackoverflow.com/questions/1983979/insertion-of-data-after-creating-index-on-empty-table-or-creating-unique-index-a
    Insert your data first, then create your index.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w(TAG, ">>> Start db creation");
        db.execSQL(SQL_CREATE_PERMISSION);
        db.execSQL(SQL_CREATE_INVOKE);
        AssetManager assetManager = App.getContext().getAssets();
        try {
            populateDbWithPermissions(db, assetManager);
            db.execSQL(SQL_CREATE_PERMISSION_INDEX);

            populateDbWithInvokes(db, assetManager);
            db.execSQL(SQL_CREATE_INVOKE_INDEX);
            //db.close(); illegal state?
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.w(TAG, "<<< End db creation");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

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
            if (parts.length == 3) {
                ContentValues cv = new ContentValues();
                cv.put(PermissionEntry._ID, parts[0]);
                cv.put(PermissionEntry.COLUMN_NAME_PERNAME, parts[1]);
                cv.put(PermissionEntry.COLUMN_NAME_PERWEIGHT, parts[2]);
                if (db.insert(PermissionEntry.TABLE_NAME, null, cv) == -1) {
                    Log.e(TAG, parts[0] + ' ' + parts[1]);
                }
            }
            else {
                Log.e(TAG, "Can't split: " + line);
            }
        }
    }


    private void populateDbWithInvokes(SQLiteDatabase db, AssetManager assetManager) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open("invMapping.txt")));
        String line = null;
        while((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                ContentValues cv = new ContentValues();
                cv.put(MethodInvocationEntry._ID, parts[0]);
                cv.put(MethodInvocationEntry.COLUMN_NAME_CLASS_METHOD, parts[1]);
                cv.put(MethodInvocationEntry.COLUMN_NAME_INVWEIGHT, parts[2]);
                if (db.insert(MethodInvocationEntry.TABLE_NAME, null, cv) == -1) {
                    Log.e(TAG, parts[0] + ' ' + parts[1]);
                }
            }
            else {
                Log.e(TAG, "Can't split: " + line);
            }
        }
    }


    private double getWeightFrom(String searchName, String id, String column, String table) {
        String[] projection = {id};
        String selection = column + " = ?";
        String[] selectionArgs = { searchName };
        Cursor cursor = readableDatabase.query(
                table,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        double res = -1.0;
        if (cursor.getCount() != 1) {
            Log.w(TAG, String.format("cursor.count=%d with searchName=%s", cursor.getCount(), searchName));
        }
        else if (cursor.moveToNext()) {
            res = cursor.getDouble(cursor.getColumnIndexOrThrow(id));
        }
        cursor.close();
        return res;
    }


    public double getWeightFromInvoke(String invocation) {
        return getWeightFrom(invocation,
                MethodInvocationEntry.COLUMN_NAME_INVWEIGHT,
                MethodInvocationEntry.COLUMN_NAME_CLASS_METHOD,
                MethodInvocationEntry.TABLE_NAME);
    }


    public double getWeightromPermission(String pername) {
        return getWeightFrom(pername,
                PermissionEntry.COLUMN_NAME_PERWEIGHT,
                PermissionEntry.COLUMN_NAME_PERNAME,
                PermissionEntry.TABLE_NAME);
    }

}

package org.altervista.pierluigilaviano.notaspese.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.sql.Date;

import static org.altervista.pierluigilaviano.notaspese.helper.Constants.*;


/**
 * Created by Pj94 on 08/01/2018.
 */

public class DBManager {
    final static String TAG = "DBManager";
    private DBHelper dbHelper;

    public DBManager(Context ctx) {
        dbHelper = new DBHelper(ctx);
    }

    public boolean store(Date data, String descr, double mo) {
        SQLiteDatabase db = getDbHelper().getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(C_DATA, String.valueOf(data));
        cv.put(C_DESCR, descr);
        cv.put(C_MOVIMENTO, mo);
        return (db.insert(TABLE_NAME, null, cv) != -1) ? true : false;

    }

   public boolean insert(Date data, String descrizione, double importo) {
        return store(data, descrizione, importo);
    }

    public void doQuery(String s) {
        SQLiteDatabase db = getDbHelper().getWritableDatabase();
        db.execSQL(s);

    }
    public Cursor query() {
        Cursor cursor = null;

        try {
            SQLiteDatabase db = getDbHelper().getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        } catch(SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }

        return cursor;
    }

    private DBHelper getDbHelper() {
        return dbHelper;
    }
}
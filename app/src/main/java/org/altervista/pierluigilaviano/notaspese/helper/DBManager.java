package org.altervista.pierluigilaviano.notaspese.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import static org.altervista.pierluigilaviano.notaspese.helper.Constants.C_DATA;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.C_DESCR;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.C_MOVIMENTO;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.TABLE_NAME;

/**
 * Gestore del database
 */
public class DBManager {
    private final static String TAG = "DBManager";
    private DBHelper dbHelper;

    /**
     * Costruttore; prende in ingresso il contesto
     * @param ctx the application's context
     */
    public DBManager(Context ctx) {
        dbHelper = new DBHelper(ctx);
    }

    private boolean store(long data, String descr, double mo) {
        SQLiteDatabase db = getDbHelper().getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(C_DATA, data);
        cv.put(C_DESCR, descr);
        cv.put(C_MOVIMENTO, mo);
        return db.insert(TABLE_NAME, null, cv) != -1;
    }

    /**
     * Prova ad effettuare l'inserimento nel db
     * @param mo the movimendooo
     * @return eddaje
     */
    public boolean insert(Movimento mo) {
        return store(mo.data, mo.descrizione, mo.importo);
    }

    /**
     * Esegue una query sql (non neutralizzata)
     * @param s the query
     */
    public void doQuery(String s) {
        SQLiteDatabase db = getDbHelper().getWritableDatabase();
        db.execSQL(s);
    }

    /**
     * Restituisce il risultato di una query
     * @return the result of the query
     */
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
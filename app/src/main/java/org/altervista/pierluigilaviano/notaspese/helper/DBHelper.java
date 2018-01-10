package org.altervista.pierluigilaviano.notaspese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static org.altervista.pierluigilaviano.notaspese.helper.Constants.C_DATA;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.C_DESCR;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.C_MOVIMENTO;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.DB_NAME;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.TABLE_NAME;

/**
 * Generazione del db
 */
public class DBHelper extends SQLiteOpenHelper {

    DBHelper(Context ctx) {
        super(ctx, DB_NAME, null, 1);
    }

    /**
     * Creazione della struttura del db
     * @param db the global db instance
     */
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_DATA + " INTEGER DEFAULT NULL," +    //watchout this
                C_DESCR + " VARCHAR(50) DEFAULT NULL," +
                C_MOVIMENTO + " REAL DEFAULT NULL" +
                ");";
        db.execSQL(query);
    }

    /**
     * Utilizzato al cambiamento di versione del db
     * @param db the db
     * @param oldVersion the old version
     * @param newVersion the new version
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
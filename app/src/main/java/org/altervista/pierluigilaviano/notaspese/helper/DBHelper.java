package org.altervista.pierluigilaviano.notaspese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static org.altervista.pierluigilaviano.notaspese.helper.Constants.*;
/**
 * Created by Pj94 on 08/01/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context ctx) {
        super(ctx, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_DATA + " INTEGER DEFAULT NULL," +    //watchout this
                C_DESCR + " VARCHAR(50) DEFAULT NULL," +
                C_MOVIMENTO + " REAL DEFAULT NULL" +
                ");";
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
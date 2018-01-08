package org.altervista.pierluigilaviano.notaspese;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.altervista.pierluigilaviano.notaspese.helper.DBManager;
import org.altervista.pierluigilaviano.notaspese.helper.Movimento;
import org.altervista.pierluigilaviano.notaspese.helper.MovimentoAdapter;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;

import static org.altervista.pierluigilaviano.notaspese.helper.Constants.*;

public class MainActivity extends AppCompatActivity {
    public static DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBManager(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AggiuntaMovimento.class));
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                delete();
                return true;
            }
        });
    }

    private void updateListView() {
        ListView lwList = findViewById(R.id.lvMovimenti);
        MovimentoAdapter adapter = null;
        Cursor crs = db.query();
        ArrayList<Movimento> movimenti = new ArrayList<>();
        if (crs != null && crs.getCount() != 0) {
            while (crs.moveToNext()) {
                long data = crs.getLong(crs.getColumnIndex(C_DATA));
                String descrizione = crs.getString(crs.getColumnIndex(C_DESCR));
                double importo = crs.getDouble(crs.getColumnIndex(C_MOVIMENTO));

                movimenti.add(Movimento.getInstance(data, descrizione, importo));
            }
            adapter = new MovimentoAdapter(getBaseContext(), movimenti);
        }

        if (adapter != null) {
            lwList.setAdapter(adapter);
        } else {
            lwList.setAdapter(null);
        }
    }

    private void delete() {
        boolean exists = false;
        String[] lista = getApplicationContext().databaseList();
        for (int i = 0; i < lista.length && exists == false; i++) {
            Log.i("LISTADB", lista.toString());
            if (lista[i].equals(DB_NAME))
                exists = true;
        }
        if (exists) {
            db.doQuery("DELETE FROM " + TABLE_NAME);
            getBaseContext().deleteDatabase(DB_NAME);
            updateListView();
        }
    }

    protected void onResume() {
        super.onResume();
        updateListView();
    }

    /**
     * Utilizzare il menu per gestire l'ordinamento dei movimenti (feature)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
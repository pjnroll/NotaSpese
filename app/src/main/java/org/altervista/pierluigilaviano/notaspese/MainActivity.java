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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.altervista.pierluigilaviano.notaspese.helper.Constants.*;

public class MainActivity extends AppCompatActivity {
    public static DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBManager(this);

        FloatingActionButton fab = findViewById(R.id.fab);
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

        List<Movimento> movimenti = getMovimenti();

        if (movimenti != null && movimenti.size() > 0) {
            adapter = new MovimentoAdapter(getBaseContext(), movimenti);
        }

        lwList.setAdapter(adapter);
    }

    private List<Movimento> getMovimenti() {
        Cursor crs = db.query();
        List<Movimento> movimenti = new ArrayList<>();
        if (crs != null && crs.getCount() != 0) {
            while (crs.moveToNext()) {
                long data = crs.getLong(crs.getColumnIndex(C_DATA));
                String descrizione = crs.getString(crs.getColumnIndex(C_DESCR));
                double importo = crs.getDouble(crs.getColumnIndex(C_MOVIMENTO));

                movimenti.add(Movimento.getInstance(data, descrizione, importo));
            }
        }

        return movimenti;
    }

    private void delete() {
        boolean exists = false;
        String[] lista = getApplicationContext().databaseList();
        for (int i = 0; i < lista.length && !exists; i++) {
            Log.i("LISTADB", Arrays.toString(lista));
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

    private void ordinaPerData() {
//        ordinato = true;
        ListView lwList = findViewById(R.id.lvMovimenti);

        Set<Movimento> ordinati = new TreeSet<>(getMovimenti());
        List<Movimento> movListOrdinati = new ArrayList<>();
        movListOrdinati.addAll(ordinati);
        MovimentoAdapter movOrdinati;

        if (movListOrdinati.size() > 0) {
            movOrdinati = new MovimentoAdapter(getBaseContext(), movListOrdinati);
            lwList.setAdapter(movOrdinati);
        } else {
            updateListView();
        }
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
        if (id == R.id.action_sort) {
            ordinaPerData();
        }
        return super.onOptionsItemSelected(item);
    }
}
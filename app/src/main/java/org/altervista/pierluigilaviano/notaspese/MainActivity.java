package org.altervista.pierluigilaviano.notaspese;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.altervista.pierluigilaviano.notaspese.helper.DBManager;
import org.altervista.pierluigilaviano.notaspese.helper.Movimento;
import org.altervista.pierluigilaviano.notaspese.helper.MovimentoAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.altervista.pierluigilaviano.notaspese.helper.Constants.SORTED_BY_DATE;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.idx_sort_by_date;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.idx_sort_default;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.C_DATA;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.C_DESCR;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.C_MOVIMENTO;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.DB_NAME;
import static org.altervista.pierluigilaviano.notaspese.helper.Constants.TABLE_NAME;

/**
 * Damn Kiuwan
 */
public class MainActivity extends AppCompatActivity {
    /**
     * The global db istance
     */
    public static DBManager db;

    private Menu mMenu;
    boolean sortedByDate;
    private ListView mLwList;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getPreferences(MODE_PRIVATE);
        sortedByDate = sp.getBoolean(SORTED_BY_DATE, false);

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

        mLwList = findViewById(R.id.lvMovimenti);
        mLwList.setOnItemLongClickListener(getDeleteItemListener());
    }

    protected void onPause() {
        sp.edit().putBoolean(SORTED_BY_DATE, sortedByDate).apply();

        super.onPause();
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
            ordinaPerInserimento();
        }
    }

    protected void onResume() {
        super.onResume();
        if (sortedByDate) {
            ordinaPerData();
        } else {
            ordinaPerInserimento();
        }
    }

    private void ordinaPerInserimento() {
        MovimentoAdapter adapter = null;

        List<Movimento> movimenti = getMovimenti();

        if (movimenti != null && movimenti.size() > 0) {
            adapter = new MovimentoAdapter(getBaseContext(), movimenti);
        }

        mLwList.setAdapter(adapter);
    }

    private void ordinaPerData() {
        List<Movimento> movList = getMovimenti();
        Set<Movimento> ordinati = new TreeSet<>();
        ordinati.addAll(movList);
        for (Movimento m : ordinati) {
            Log.i("MOVIMENTO->", m.toString());
        }
        List<Movimento> movListOrdinati = new ArrayList<>();
        movListOrdinati.addAll(ordinati);
        MovimentoAdapter movOrdinati;

        if (movListOrdinati.size() > 0) {
            movOrdinati = new MovimentoAdapter(getBaseContext(), movListOrdinati);
            mLwList.setAdapter(movOrdinati);
        } else {
            ordinaPerInserimento();
        }

    }

    /**
     * Utilizzare il menu per gestire l'ordinamento dei movimenti (feature)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (sortedByDate) {
            menu.getItem(idx_sort_by_date).setEnabled(false);
            ordinaPerData();
        } else {
            menu.getItem(idx_sort_default).setEnabled(false);
            ordinaPerInserimento();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_date || id == R.id.action_sort_default) {
            if (id == R.id.action_sort_by_date) {
                ordinaPerData();
            } else {
                ordinaPerInserimento();
            }
            mMenu.getItem(idx_sort_by_date).setEnabled(sortedByDate);
            mMenu.getItem(idx_sort_default).setEnabled(!sortedByDate);
            sortedByDate = !sortedByDate;
            sp.edit().putBoolean(SORTED_BY_DATE, sortedByDate).apply();
        }
        return super.onOptionsItemSelected(item);
    }


    private AdapterView.OnItemLongClickListener getDeleteItemListener () {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(getApplicationContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_delete, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()) {
                            case ("Cancella"):
                                deleteItem(adapterView, i);
                                break;

                            default:
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();

                return true;
            }
        };
    }

    private void deleteItem(AdapterView<?> adapterView, int i) {
        Movimento toDel;
        toDel = ((MovimentoAdapter) adapterView.getAdapter()).getItem(i);
        if (doDeleteQuery(toDel)) {
            Toast.makeText(getBaseContext(), "Elemento rimosso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Elemento non rimosso", Toast.LENGTH_SHORT).show();
        }
        onResume();
    }

    private boolean doDeleteQuery(Movimento m) {
        long toDelDate = m.data;
        String toDelDescr = m.descrizione;
        double toDelImp = m.importo;

        String whereClause = C_DATA + "=? and " + C_DESCR + "=? and " + C_MOVIMENTO + "=?";
        String[] whereArgs = {String.valueOf(toDelDate), toDelDescr, String.valueOf(toDelImp)};

        return db.delete(TABLE_NAME, whereClause, whereArgs) > 0;
    }
}
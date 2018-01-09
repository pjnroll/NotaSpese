package org.altervista.pierluigilaviano.notaspese;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.altervista.pierluigilaviano.notaspese.helper.DBManager;
import org.altervista.pierluigilaviano.notaspese.helper.Movimento;
import org.altervista.pierluigilaviano.notaspese.helper.Utils;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.altervista.pierluigilaviano.notaspese.MainActivity.db;

/**
 * Activity per aggiungere un nuovo Movimento
 */
public class AggiuntaMovimento extends AppCompatActivity {
    private EditText mTxtAggiuntaData;
    private EditText mTxtAggiuntaImporto;
    private EditText mTxtAggiuntaDescrizione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_movimento);

        db = new DBManager(this);

        mTxtAggiuntaImporto = findViewById(R.id.txtAggiuntaImporto);
        mTxtAggiuntaDescrizione = findViewById(R.id.txtAggiuntaDescrizione);
        Button mBtnAggiungi = findViewById(R.id.btnAggiungi);
        mBtnAggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTxtAggiuntaImporto.getText().toString().isEmpty() && !mTxtAggiuntaDescrizione.getText().toString().isEmpty()) {
                    aggiungiMovimento();
                }
            }
        });
    }

    /**
     * Aggiunge un movimento al database
     */
    public void aggiungiMovimento() {
        mTxtAggiuntaData = findViewById(R.id.txtAggiuntaData);
        long data;
        if (!mTxtAggiuntaData.getText().toString().isEmpty()) {
            String[] dataArray;
            String dataStringa = mTxtAggiuntaData.getText().toString();
            dataArray = dataStringa.split("/");
            data = new GregorianCalendar(Integer.parseInt(dataArray[0]), (Integer.parseInt(dataArray[1]) - 1), Integer.parseInt(dataArray[2])).getTime().getTime();
        } else {
            data = new Date().getTime();
        }
        if (data > new GregorianCalendar().getTimeInMillis()) {
            Utils.hideKeyboard(getBaseContext(), getCurrentFocus());
            Snackbar.make(getCurrentFocus(), "Impossibile inserire date future", Snackbar.LENGTH_LONG).show();
            return;
        }
        String descrizione = mTxtAggiuntaDescrizione.getText().toString();
        double importo = Double.parseDouble(mTxtAggiuntaImporto.getText().toString());

        db.insert(Movimento.getInstance(data, descrizione, importo));
        finish();
    }
}
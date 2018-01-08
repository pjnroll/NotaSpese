package org.altervista.pierluigilaviano.notaspese;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.altervista.pierluigilaviano.notaspese.helper.DBManager;
import org.altervista.pierluigilaviano.notaspese.helper.Movimento;

import java.util.Date;

import static org.altervista.pierluigilaviano.notaspese.MainActivity.db;

public class AggiuntaMovimento extends AppCompatActivity {
    private EditText mTxtAggiuntaImporto;
    private EditText mTxtAggiuntaDescrizione;
    private Button mBtnAggiungi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_movimento);

        db = new DBManager(this);

        mTxtAggiuntaImporto = findViewById(R.id.txtAggiuntaImporto);
        mTxtAggiuntaDescrizione = findViewById(R.id.txtAggiuntaDescrizione);
        mBtnAggiungi = findViewById(R.id.btnAggiungi);
        mBtnAggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTxtAggiuntaImporto.getText().toString().isEmpty() && !mTxtAggiuntaDescrizione.getText().toString().isEmpty()) {
                    aggiungiMovimento();
                }
            }
        });
    }

    public void aggiungiMovimento() {
        long data = new Date().getTime();
        String descrizione = mTxtAggiuntaDescrizione.getText().toString();
        double importo = Double.parseDouble(mTxtAggiuntaImporto.getText().toString());

        db.insert(Movimento.getInstance(data, descrizione, importo));
        finish();
    }
}

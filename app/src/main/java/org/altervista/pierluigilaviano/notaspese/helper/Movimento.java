package org.altervista.pierluigilaviano.notaspese.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import java.sql.Date;

/**
 * Un Movimento è una entrata o uscita di denaro
 */
public class Movimento implements Comparable<Movimento> {
    /**
     * Data espressa in millisecondi
     */
    public long data;
    String descrizione;
    double importo;

    private Movimento(long data, String descrizione, double importo) {
        this.data = data;
        this.descrizione = descrizione;
        this.importo = importo;
    }

    /**
     * Static factory method per ottenere un'istanza di Movimento
     * @param data
     * @param descrizione
     * @param importo
     * @return
     */
    public static Movimento getInstance(long data, String descrizione, double importo) {
        return new Movimento(data, descrizione, importo);
    }

    @Override
    public String toString() {
        return descrizione + " " + new Date(data).toString();
    }

    @Override
    public int compareTo(@NonNull Movimento movimento) {
        return (new Date(this.data)).compareTo(new Date(movimento.data));
    }
}
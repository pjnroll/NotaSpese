package org.altervista.pierluigilaviano.notaspese.helper;

import android.support.annotation.NonNull;

import java.sql.Date;

/**
 * Un Movimento è una entrata o uscita di denaro
 */
public class Movimento implements Comparable<Movimento> {
    /**
     * Data espressa in millisecondi
     */
    public long data;
    public String descrizione;
    public double importo;

    private Movimento(long data, String descrizione, double importo) {
        this.data = data;
        this.descrizione = descrizione;
        this.importo = importo;
    }

    /**
     * Static factory method per ottenere un'istanza di Movimento
     * @param data the date in milliseconds
     * @param descrizione the description
     * @param importo the moneyyyyyyyyyyyyyyy
     * @return the Movimendoooo
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Movimento movimento = (Movimento) o;

        return data == movimento.data && Double.compare(movimento.importo, importo) == 0 && (descrizione != null ? descrizione.equals(movimento.descrizione) : movimento.descrizione == null);
    }
}
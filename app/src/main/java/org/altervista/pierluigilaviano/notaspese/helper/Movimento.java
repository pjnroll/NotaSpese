package org.altervista.pierluigilaviano.notaspese.helper;

import java.sql.Date;

/**
 * Created by Pj94 on 08/01/2018.
 */

public class Movimento {
    public long data;
    public String descrizione;
    public double importo;

    private Movimento(long data, String descrizione, double importo) {
        this.data = data;
        this.descrizione = descrizione;
        this.importo = importo;
    }

    public static Movimento getInstance(long data, String descrizione, double importo) {
        return new Movimento(data, descrizione, importo);
    }
}

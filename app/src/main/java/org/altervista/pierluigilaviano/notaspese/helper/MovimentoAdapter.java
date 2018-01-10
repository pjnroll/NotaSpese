package org.altervista.pierluigilaviano.notaspese.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.altervista.pierluigilaviano.notaspese.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter per gestire i movimenti
 */
public class MovimentoAdapter extends BaseAdapter {
    private Context ctx;

    private List<Movimento> movimenti;

    public MovimentoAdapter(Context ctx, List<Movimento> movimenti) {
        this.ctx = ctx;
        this.movimenti = new ArrayList<>(movimenti);
    }

    @Override
    public int getCount() {
        return movimenti.size();
    }

    @Override
    public Movimento getItem(int i) {
        return movimenti.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        view = inflater.inflate(R.layout.item_list, null);
        TextView mTxtDescr = view.findViewById(R.id.txtDescr);
        TextView mTxtData = view.findViewById(R.id.txtData);
        TextView mTxtMovimento = view.findViewById(R.id.txtMovimento);
        mTxtDescr.setText(movimenti.get(i).descrizione);
        mTxtData.setText(new Date(movimenti.get(i).data).toString());
        mTxtMovimento.setText(String.valueOf(movimenti.get(i).importo) + " " + ctx.getString(R.string.euro));
        return view;
    }
}
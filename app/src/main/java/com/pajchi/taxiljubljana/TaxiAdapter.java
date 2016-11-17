package com.pajchi.taxiljubljana;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Matic on 3.11.2016.
 */

public class TaxiAdapter extends ArrayAdapter<Taxi> {

    private Context context;
    private ArrayList<Taxi> taxis;

    /**
     * Constructor.
     *
     * @param context   Context in which it is called.
     * @param taxis     {@link ArrayList} of {@link Taxi} objects to bind to a
     * {@link android.widget.ListView}.
     */
    public TaxiAdapter(Context context, ArrayList<Taxi> taxis) {
        super(context, R.layout.item_taxi, taxis);
        this.context = context;
        this.taxis = taxis;
    }

    /**
     * Binds a {@link Taxi} to a {@link android.widget.ListView} item.
     *
     * @param position       Position of the {@link Taxi} in the {@link ArrayList}.
     * @param convertView    The {@link View} to bind the {@link Taxi} to.
     * @param parent         Parent {@link ViewGroup} of {@link View} convertView.
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView;

        if (convertView == null) {
            rowView = inflater.inflate(R.layout.item_taxi, parent, false);
        } else {
            rowView = convertView;
        }

        final TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
        TextView tvRandom10Km = (TextView) rowView.findViewById(R.id.tvRandom10Km);

        final Taxi taxi = taxis.get(position);

        tvName.setText(taxi.getName());
        tvRandom10Km.setText(taxi.getRandom10Km());

        return rowView;
    }

    public ArrayList<Taxi> getTaxis() {
        return taxis;
    }
}

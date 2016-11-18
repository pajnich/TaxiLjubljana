package com.pajchi.taxiljubljana;

/**
 * Created by Matic on 7.11.2016.
 */

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Connects a database query for all taxis' data with a ListView displaying that data.
 */
public class TaxisCursorAdapter extends CursorAdapter {

    /**
     * Constructor that always enables auto-requery.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @deprecated This option is discouraged, as it results in Cursor queries
     * being performed on the application's UI thread and thus can cause poor
     * responsiveness or even Application Not Responding errors.  As an alternative,
     * use {@link LoaderManager} with a {@link CursorLoader}.
     */
    public TaxisCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_taxi, parent, false);
    }

    /**
     * Binds an existing view to the data pointed to by cursor.
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvEstimatedPrice = (TextView) view.findViewById(R.id.tvEstimatedPrice);

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract
                .Taxis.COLUMN_NAME_NAME));
        String randomKm = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract
                .Taxis.COLUMN_NAME_RANDOM_KM));
        String startFee = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Taxis
                .COLUMN_NAME_START_FEE));

        String estimatedPrice = String.valueOf(
                Math.round(
                        MainActivity.priceStringToDouble(randomKm)
                                * (MainActivity.priceStringToDouble(MainActivity.distance)


                        / 1000)
                        + MainActivity.priceStringToDouble(startFee)
                )
        ) + " €";

        // Populate fields with extracted properties
        tvName.setText(name);
        tvEstimatedPrice.setText(estimatedPrice);

    }
}

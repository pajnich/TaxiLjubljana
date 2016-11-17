package com.pajchi.taxiljubljana;

/**
 * Created by Matic on 7.11.2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Contains methods for database operations.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Taxis.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.Taxis.DELETE_TABLE);
        onCreate(db);
    }

    /**
     * Inserts new given taxi into given database.
     *
     * @param taxi      The {@link Taxi} to be inserted.
     * @param db        The database for the taxi to be inserted into.
     */
    public void insertNewTaxi(Taxi taxi, SQLiteDatabase db){

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Taxis.COLUMN_NAME_CITY, taxi.getCity());
        values.put(DatabaseContract.Taxis.COLUMN_NAME_NAME, taxi.getName());
        values.put(DatabaseContract.Taxis.COLUMN_NAME_PHONE, taxi.getPhone());
        values.put(DatabaseContract.Taxis.COLUMN_NAME_START_FEE, taxi.getStartFee());
        values.put(DatabaseContract.Taxis.COLUMN_NAME_REGULAR_KM, taxi.getRegularKm());
        values.put(DatabaseContract.Taxis.COLUMN_NAME_CONTRACT_KM, taxi.getContractKm());
        values.put(DatabaseContract.Taxis.COLUMN_NAME_RANDOM_KM, taxi.getRandomKm());
        values.put(DatabaseContract.Taxis.COLUMN_NAME_WAITING_HOUR, taxi.getWaitingHour());
        values.put(DatabaseContract.Taxis.COLUMN_NAME_REGULAR_10_KM, taxi.getRegular10Km());
        values.put(DatabaseContract.Taxis.COLUMN_NAME_RANDOM_10_KM, taxi.getRandom10Km());

        long newRowId = db.insert(DatabaseContract.Taxis.TABLE_NAME, null, values);
        Log.d("DATABASE_INSERT", taxi.getName() + " inserted under id " + Long.toString(newRowId) + "" +
                ".");
    }

    /**
     * Helper function that parses a given table into a string
     * and returns it for easy printing. The string consists of
     * the table name and then each row is iterated through with
     * column_name: value pairs printed out.
     *
     * @param db the database to get the table from
     * @param tableName the the name of the table to parse
     * @return the table tableName as a string
     */
    public String getTableAsString(SQLiteDatabase db, String tableName) {
        Log.d("DATABASE", "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        allRows.close();

        return tableString;
    }

    /**
     * Gets all taxis from the database.
     *
     * @param db    Database to query.
     * @return      Cursor with results.
     */
    public Cursor getAllTaxis(SQLiteDatabase db) {

        return db.rawQuery(
                "SELECT * FROM " + DatabaseContract.Taxis.TABLE_NAME,
                null
        );
    }

    /**
     * Deletes all rows from given table.
     *
     * @param tableName    Name of table with rows to be deleted.
     * @param db           Name of database containing given table.
     */
    public void deleteAllRows(String tableName, SQLiteDatabase db){

        db.delete(tableName, null, null);
    }
}

package com.pajchi.taxiljubljana;

/**
 * Created by Matic on 7.11.2016.
 */

import android.provider.BaseColumns;

/**
 * Represents the database blueprint.
 * Contains public constants for use with database operations.
 */
public class DatabaseContract {

    public static final int     DATABASE_VERSION    = 1;
    public static final String  DATABASE_NAME       = "database.db";
    private static final String TEXT_TYPE           = " TEXT";
    private static final String COMMA_SEP           = ",";

    // private constructor to prevent anyone from accidentally instantiating this class
    private DatabaseContract(){}

    // tables
    public static abstract class Taxis implements BaseColumns {

        public static final String TABLE_NAME = "taxis";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_START_FEE = "startFee";
        public static final String COLUMN_NAME_REGULAR_KM = "regularKm";
        public static final String COLUMN_NAME_CONTRACT_KM = "contractKm";
        public static final String COLUMN_NAME_RANDOM_KM = "randomKm";
        public static final String COLUMN_NAME_WAITING_HOUR = "waitingHour";
        public static final String COLUMN_NAME_REGULAR_10_KM = "regular10Km";
        public static final String COLUMN_NAME_RANDOM_10_KM = "random10Km";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_START_FEE + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_REGULAR_KM + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_CONTRACT_KM + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_RANDOM_KM + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_WAITING_HOUR + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_REGULAR_10_KM + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_RANDOM_10_KM + TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

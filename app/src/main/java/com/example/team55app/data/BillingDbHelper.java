package com.example.team55app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BillingDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BillsDB.db";
    private static final int VERSION = 1;

    public BillingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + BillingContract.BillingEntry.PRIMARY_TABLE_NAME + " (" +
                BillingContract.BillingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BillingContract.BillingEntry.PRIMARY_COLUMN_NAME + " TEXT NOT NULL, " +
                BillingContract.BillingEntry.PRIMARY_COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                BillingContract.BillingEntry.PRIMARY_COLUMN_DATE + " TEXT NOT NULL, " +
                BillingContract.BillingEntry.PRIMARY_COLUMN_STATUS + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void createBillTable(SQLiteDatabase db, String billId) {

        final String CREATE_TABLE = "CREATE TABLE " + BillingContract.BillingCustomerEntry.SECONDARY_TABLE_NAME + billId + " (" +
                BillingContract.BillingCustomerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BillingContract.BillingCustomerEntry.SECONDARY_COLUMN_ITEM_DESCRIPTION + " TEXT NOT NULL, " +
                BillingContract.BillingCustomerEntry.SECONDARY_COLUMN_FINAL_PRICE + " REAL NOT NULL, " +
                BillingContract.BillingCustomerEntry.SECONDARY_COLUMN_QUANTITY + " INTEGER NOT NULL); ";

        db.execSQL(CREATE_TABLE);

    }

    public static void dropBillTable(SQLiteDatabase db, String billId) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + BillingContract.BillingCustomerEntry.SECONDARY_TABLE_NAME + billId;
        db.execSQL(DROP_TABLE);
    }

}

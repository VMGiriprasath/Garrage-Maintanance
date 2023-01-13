package com.example.team55app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.team55app.ItemActivity;

public class BillingContentProvider extends ContentProvider {
    public static final int BILLS = 100;
    public static final int BILL_WITH_ID = 101;
    public static final int BILL_WITH_ID_WITH_ID = 102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BillingContract.AUTHORITY, BillingContract.PATH_BILLS, BILLS);
        uriMatcher.addURI(BillingContract.AUTHORITY, BillingContract.PATH_BILLS + "/#", BILL_WITH_ID);
        uriMatcher.addURI(BillingContract.AUTHORITY, BillingContract.PATH_BILLS + "/#/#", BILL_WITH_ID_WITH_ID);
        return uriMatcher;
    }

    private BillingDbHelper mBillingDbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mBillingDbHelper = new BillingDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mBillingDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case BILLS:
                retCursor = db.query(
                        BillingContract.BillingEntry.PRIMARY_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            case BILL_WITH_ID:
                retCursor = db.query(
                        BillingContract.BillingCustomerEntry.SECONDARY_TABLE_NAME + uri.getLastPathSegment(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                retCursor.setNotificationUri(getContext().getContentResolver(),
                        BillingContract.BASE_CONTENT_URI.buildUpon().appendPath(BillingContract.BillingCustomerEntry.SECONDARY_TABLE_NAME + uri.getLastPathSegment()).build());
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return retCursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mBillingDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case BILLS:
                long id = db.insert(BillingContract.BillingEntry.PRIMARY_TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(BillingContract.BillingEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into : " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mBillingDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case BILL_WITH_ID:
                if (ItemActivity.addingMoreItems == false) {
                    BillingDbHelper.createBillTable(db, uri.getLastPathSegment());
                }
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(BillingContract.BillingCustomerEntry.SECONDARY_TABLE_NAME + uri.getLastPathSegment(), null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(BillingContract.BASE_CONTENT_URI.buildUpon().appendPath(BillingContract.BillingCustomerEntry.SECONDARY_TABLE_NAME + uri.getLastPathSegment()).build(), null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mBillingDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        switch (match) {
            case BILL_WITH_ID:
                try {
                    db.beginTransaction();

                    BillingDbHelper.dropBillTable(db, uri.getLastPathSegment());
                    rowsDeleted = db.delete(
                            BillingContract.BillingEntry.PRIMARY_TABLE_NAME,
                            BillingContract.BillingEntry._ID + "=" + uri.getLastPathSegment(),
                            selectionArgs
                    );

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsDeleted > 0) {
                    getContext().getContentResolver().notifyChange(BillingContract.BillingEntry.CONTENT_URI, null);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mBillingDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;
        switch (match) {
            case BILL_WITH_ID:
                rowsUpdated = db.update(BillingContract.BillingEntry.PRIMARY_TABLE_NAME, values, selection, selectionArgs);
                if (rowsUpdated > 0) {
                    getContext().getContentResolver().notifyChange(BillingContract.BillingEntry.CONTENT_URI, null);
                }
                break;
            case BILL_WITH_ID_WITH_ID:
                rowsUpdated = db.update(
                        BillingContract.BillingCustomerEntry.SECONDARY_TABLE_NAME + uri.getPathSegments().get(1),
                        values,
                        BillingContract.BillingCustomerEntry._ID + "=" + uri.getLastPathSegment(),
                        null
                );
                if (rowsUpdated > 0) {
                    getContext().getContentResolver().notifyChange(BillingContract.BASE_CONTENT_URI.buildUpon().appendPath(BillingContract.BillingCustomerEntry.SECONDARY_TABLE_NAME + uri.getPathSegments().get(1)).build(), null);
                    getContext().getContentResolver().notifyChange(BillingContract.BillingEntry.CONTENT_URI, null);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return rowsUpdated;
    }
}

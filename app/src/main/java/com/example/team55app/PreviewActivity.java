package com.example.team55app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.team55app.data.BillingContract;
import com.example.team55app.databinding.ActivityPreviewBinding;
import com.example.team55app.utils.NumberToWord;

public class PreviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER_ID = 44;
    private static final int ACTION_MARK_AS_PAID_ID = 400;
    private static final int ACTION_DELETE_BILL_ID = 401;
    private static final int ACTION_ADD_MORE_ITEMS_ID = 402;
    private ActivityPreviewBinding binding;
    static final String ADDING_MORE_ITEMS = "adding-more-items-to-bill";
    static final String EDITING_ITEM = "editing-existing-item";

    private static String billId;
    private static String billStatus;
    private String phoneNumber;
    private String customerName;

    private static TextView totalAmountTv;
    private static TextView totalAmountInWordsTv;

    private static String inr;
    private PreviewAdapter adapter;

    private int itemCount;
    public static final String ITEM_COUNT_KEY = "items-count-in-bill";

    private static Intent getDetailIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        getDetailIntent = getIntent();
        billId = getDetailIntent.getStringExtra(BillingContract.BillingEntry._ID);
        if (getDetailIntent.hasExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_STATUS)) {
            billStatus = getDetailIntent.getStringExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_STATUS);
        }

        if (billStatus.equals(BillingContract.BILL_STATUS_PAID)) {
            binding.toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00C853")));
        } else {
            binding.toolbar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        }

        if (getDetailIntent.hasExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_NAME)) {
            customerName = getDetailIntent.getStringExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_NAME);
            binding.toolbar.setTitle(customerName);
        }

        if (getDetailIntent.hasExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_PHONE_NUMBER)) {
            phoneNumber = getDetailIntent.getStringExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_PHONE_NUMBER);
        }

        binding.detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.detailRecyclerView.setHasFixedSize(true);
        adapter = new PreviewAdapter(this);
        binding.detailRecyclerView.setAdapter(adapter);


        totalAmountTv = (TextView) findViewById(R.id.total_amount_after_tax_value);
        totalAmountInWordsTv = (TextView) findViewById(R.id.total_amount_in_words_value);

        inr = "₹" + " ";

        getSupportLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);


    }

    public static void printTotalDetails( float totalAmount) {

        totalAmountTv.setText(inr + String.format("%.2f", totalAmount));
        totalAmountInWordsTv.setText("Rupees. " + NumberToWord.getNumberInWords(String.valueOf((int) totalAmount)));
    }

    private void addMoreItems() {
        Intent addIntent = new Intent(this, ItemActivity.class);
        addIntent.putExtra(ADDING_MORE_ITEMS, true);
        addIntent.putExtra(BillingContract.BillingEntry._ID, billId);
        addIntent.putExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_NAME, customerName);
        addIntent.putExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_PHONE_NUMBER, phoneNumber);
        startActivity(addIntent);
    }
    public static void editItem(Context mContext, int id, String itemDescription, float finalPrice, int quantity) {

    }
    public static void changeBillStatus() {
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(
                this,
                BillingContract.BillingEntry.CONTENT_URI.buildUpon().appendPath(billId).build(),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        itemCount = data.getCount();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
        itemCount = 0;
    }
}
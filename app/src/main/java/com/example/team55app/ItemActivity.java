package com.example.team55app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.team55app.data.BillingContract;
import com.example.team55app.databinding.ActivityItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemActivity extends AppCompatActivity {
    private ActivityItemBinding binding;
    public static boolean addingMoreItems = false;
    List<ContentValues> cvList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.finishbtn.setVisibility(View.GONE);

        //toolbar
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //onclick add bill
        binding.addmorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.itemdescription.getText().toString().length() == 0) {
                    binding.itemdescription.setText("NA");
                }

                if (binding.quantity.getText().toString().length() == 0 || binding.quantity.equals("0")) {
                    binding.quantity.setText("1");
                    return;
                }
                if (binding.finalprice.getText().toString().length() == 0) {
                    Toast.makeText(ItemActivity.this, "Enter Selling Price", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put(BillingContract.BillingCustomerEntry.SECONDARY_COLUMN_ITEM_DESCRIPTION, binding.itemdescription.getText().toString());
                    cv.put(BillingContract.BillingCustomerEntry.SECONDARY_COLUMN_FINAL_PRICE, Integer.parseInt(binding.finalprice.getText().toString()));
                    cv.put(BillingContract.BillingCustomerEntry.SECONDARY_COLUMN_QUANTITY, Integer.parseInt(binding.quantity.getText().toString()));
                    cvList.add(cv);
                    Toast.makeText(ItemActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();

                    binding.itemdescription.setText("");
                    binding.finalprice.setText("");
                    binding.quantity.setText("");
                    binding.finishbtn.setVisibility(View.VISIBLE);

                    binding.itemdescription.requestFocus();


                }
            }
        });


    }


    public void finishaddingitem(View view) {
        if (binding.finalprice.getText().toString().length() != 0) {
            Toast.makeText(this, "Add Item to Bill to Finish", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!getIntent().hasExtra(PreviewActivity.ADDING_MORE_ITEMS)) {
            Intent intent = getIntent();
            String customerName = intent.getStringExtra(DetailsActivity.ADD_CUSTOMER_NAME_KEY);
            String phoneNumber = intent.getStringExtra(DetailsActivity.ADD_CUSTOMER_PHONE_KEY);

            String billDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String billStatus = BillingContract.BILL_STATUS_UNPAID;

            ContentValues contentValues = new ContentValues();
            contentValues.put(BillingContract.BillingEntry.PRIMARY_COLUMN_NAME, customerName);
            contentValues.put(BillingContract.BillingEntry.PRIMARY_COLUMN_PHONE_NUMBER, phoneNumber);
            contentValues.put(BillingContract.BillingEntry.PRIMARY_COLUMN_DATE, billDate);
            contentValues.put(BillingContract.BillingEntry.PRIMARY_COLUMN_STATUS, billStatus);

            Uri idUri = getContentResolver().insert(BillingContract.BillingEntry.CONTENT_URI, contentValues);

            // Inserting item details in secondary table
            String id = idUri.getLastPathSegment();
            getContentResolver().bulkInsert(BillingContract.BillingEntry.CONTENT_URI.buildUpon().appendPath(id).build(),
                    cvList.toArray(new ContentValues[cvList.size()]));

            // Opening detail activity
            Intent detailIntent = new Intent(this, PreviewActivity.class);

            detailIntent.putExtra(BillingContract.BillingEntry._ID, id);
            detailIntent.putExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_NAME, customerName);
            detailIntent.putExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_PHONE_NUMBER, phoneNumber);
            detailIntent.putExtra(BillingContract.BillingEntry.PRIMARY_COLUMN_STATUS, BillingContract.BILL_STATUS_UNPAID);

            startActivity(detailIntent);

            finish();
        } else {
            addingMoreItems = true;

            String id = getIntent().getStringExtra(BillingContract.BillingEntry._ID);
            getContentResolver().bulkInsert(BillingContract.BillingEntry.CONTENT_URI.buildUpon().appendPath(id).build(),
                    cvList.toArray(new ContentValues[cvList.size()]));

            ContentValues contentValues = new ContentValues();
            contentValues.put(BillingContract.BillingEntry.PRIMARY_COLUMN_STATUS, BillingContract.BILL_STATUS_UNPAID);
            getContentResolver().update(
                    BillingContract.BillingEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build(),
                    contentValues,
                    BillingContract.BillingEntry._ID + "=" + id,
                    null
            );
            PreviewActivity.changeBillStatus();

            finish();
        }

    }


}

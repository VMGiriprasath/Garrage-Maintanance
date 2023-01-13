package com.example.team55app.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class BillingContract {
    public static final String AUTHORITY = "com.example.team55app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_BILLS = "billsMain";

    public static final String BILL_STATUS_PAID = "paid";
    public static final String BILL_STATUS_UNPAID = "unpaid";

    public static final class BillingEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BILLS).build();

        public static final String PRIMARY_TABLE_NAME = "billsMain";
        public static final String PRIMARY_COLUMN_NAME = "customerName";
        public static final String PRIMARY_COLUMN_PHONE_NUMBER = "phoneNumber";
        public static final String PRIMARY_COLUMN_DATE = "billDate";
        public static final String PRIMARY_COLUMN_STATUS = "billStatus";

    }
    public static final class BillingCustomerEntry implements BaseColumns {

        public static final String SECONDARY_TABLE_NAME = "bill";
        public static final String SECONDARY_COLUMN_ITEM_DESCRIPTION = "item";
        public static final String SECONDARY_COLUMN_FINAL_PRICE = "finalPrice";
        public static final String SECONDARY_COLUMN_QUANTITY = "quantity";

    }
}

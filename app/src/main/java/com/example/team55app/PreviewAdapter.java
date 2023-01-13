package com.example.team55app;

import android.content.Context;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team55app.data.BillingContract;
import com.example.team55app.utils.PriceUtils;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PreviewHolder> {
    private Cursor mCursor;
    float totalAmount = 0f;
    private Context mContext;


    public PreviewAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public PreviewAdapter.PreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_item_detail_layout, parent, false);
        return new PreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {

            int idValue = mCursor.getInt(mCursor.getColumnIndexOrThrow(BillingContract.BillingCustomerEntry._ID));
            String itemDescriptionValue = mCursor.getString(mCursor.getColumnIndexOrThrow(BillingContract.BillingCustomerEntry.SECONDARY_COLUMN_ITEM_DESCRIPTION));
            float finalPriceValue = mCursor.getFloat(mCursor.getColumnIndexOrThrow(BillingContract.BillingCustomerEntry.SECONDARY_COLUMN_FINAL_PRICE));
            int quantityValue = mCursor.getInt(mCursor.getColumnIndexOrThrow(BillingContract.BillingCustomerEntry.SECONDARY_COLUMN_QUANTITY));

            PriceUtils priceUtils = new PriceUtils(finalPriceValue, quantityValue);
            float rateValue = priceUtils.getRate();


            holder.itemDescription.setText(itemDescriptionValue);
            holder.sno.setText(String.valueOf(idValue));
            holder.finalPrice.setText(String.format("%.2f", finalPriceValue));
            holder.qty.setText(String.valueOf(quantityValue));
            holder.rate.setText(String.format("%.2f", rateValue));

            holder.itemView.setTag(R.id.bill_edit_id, idValue);
            holder.itemView.setTag(R.id.bill_edit_item_description, itemDescriptionValue);
            holder.itemView.setTag(R.id.bill_edit_final_price, finalPriceValue);
            holder.itemView.setTag(R.id.bill_edit_quantity, quantityValue);


            totalAmount += (finalPriceValue * quantityValue);

            if (position == getItemCount() - 1) {
                PreviewActivity.printTotalDetails(totalAmount);
            }

        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;

        totalAmount = 0f;

        notifyDataSetChanged();
    }

    public class PreviewHolder extends RecyclerView.ViewHolder {
        TextView itemDescription;
        TextView sno, finalPrice, qty, rate;

        public PreviewHolder(@NonNull View itemView) {
            super(itemView);

            itemDescription = (TextView) itemView.findViewById(R.id.detail_item);
            sno = (TextView) itemView.findViewById(R.id.detail_sno);
            finalPrice = (TextView) itemView.findViewById(R.id.detail_final_price);
            qty = (TextView) itemView.findViewById(R.id.detail_quantity);
            rate = (TextView) itemView.findViewById(R.id.detail_rate);
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    MenuItem editItem = contextMenu.add(Menu.NONE, 1, 1, "Edit");
                    editItem.setOnMenuItemClickListener(onEditItemMenu);
                }
            });


        }


        private MenuItem.OnMenuItemClickListener onEditItemMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = (int) itemView.getTag(R.id.bill_edit_id);
                String itemDescription = (String) itemView.getTag(R.id.bill_edit_item_description);
                float finalPrice = (float) itemView.getTag(R.id.bill_edit_final_price);
                int quantity = (int) itemView.getTag(R.id.bill_edit_quantity);
                PreviewActivity.editItem(mContext, id, itemDescription, finalPrice, quantity);

                return true;
            }
        };

    }
}

package com.example.android.inventoryapp.adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.InventoryContract.ProductEntry;

import java.text.NumberFormat;

public class InventoryAdapter extends CursorAdapter {

    public InventoryAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView productNameTextView = view.findViewById(R.id.list_item_product_name_text_view);
        TextView productPriceTextView = view.findViewById(R.id.list_item_product_price_text_view);
        final TextView productQuantityTextView = view.findViewById(R.id.list_item_quantity_text_view);

        final int position = cursor.getPosition();
        final String productName = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        Double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        final Integer productQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));

        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();
        priceFormat.setMaximumFractionDigits(2);

        productNameTextView.setText(productName);
        productPriceTextView.setText(String.valueOf(priceFormat.format(productPrice)));
        productQuantityTextView.setText(String.valueOf(productQuantity));

        Button sellButton = view.findViewById(R.id.sell_button);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = productQuantity;

                if (productQuantity > 0) {
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, --quantity);

                    int numberOfRowsUpdated = context.getContentResolver().update(ContentUris.withAppendedId(ProductEntry.CONTENT_URI, getItemId(position)), values, null, null);

                    if (numberOfRowsUpdated == 1) {
                        Toast.makeText(context, context.getString(R.string.item_sold_message, productName), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.no_more_items_message), Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}

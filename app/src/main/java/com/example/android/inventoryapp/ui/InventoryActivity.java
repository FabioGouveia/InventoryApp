package com.example.android.inventoryapp.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.InventoryDbHelper;

import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_PRICE;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_QUANTITY;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.TABLE_NAME;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry._ID;

public class InventoryActivity extends AppCompatActivity {

    private InventoryDbHelper dbHelper;
    private TextView queryResultTextView;

    private int dummyNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        dbHelper = new InventoryDbHelper(this);

        queryResultTextView = findViewById(R.id.query_result_text_view);

        Button insertDummyDataButton = findViewById(R.id.add_dummy_data_button);
        insertDummyDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        queryData();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    private void insertData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, getString(R.string.dummy_product_name, dummyNumber));
        values.put(COLUMN_PRODUCT_PRICE, (dummyNumber * 3));
        values.put(COLUMN_PRODUCT_QUANTITY, (dummyNumber * 10));
        values.put(COLUMN_PRODUCT_SUPPLIER_NAME, getString(R.string.dummy_product_supplier_name));
        values.put(COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, getString(R.string.dummy_product_supplier_phone_number));

        long insertedRowId = db.insert(TABLE_NAME, null, values);

        if (insertedRowId == -1) {
            Toast.makeText(this, "An error occurred with data insertion", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
            dummyNumber++;
            queryData();
        }
    }

    private void queryData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_QUANTITY,
                COLUMN_PRODUCT_SUPPLIER_NAME,
                COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };

        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);

        if (cursor != null) {

            try {
                int idColumnIndex = cursor.getColumnIndex(_ID);
                int nameColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
                int priceColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_PRICE);
                int quantityColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY);
                int supplierNameColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_SUPPLIER_NAME);
                int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

                StringBuilder resultString = new StringBuilder();

                String line;
                while (cursor.moveToNext()) {

                    line = cursor.getInt(idColumnIndex) + " : [" +
                            cursor.getString(nameColumnIndex) + ", " +
                            cursor.getDouble(priceColumnIndex) + ", " +
                            cursor.getInt(quantityColumnIndex) + "]\n" +
                            cursor.getString(supplierNameColumnIndex) + "/" +
                            cursor.getString(supplierPhoneNumberColumnIndex) + "\n\n";

                    resultString.append(line);
                }

                if (resultString.toString().isEmpty()) {
                    queryResultTextView.setText(getString(R.string.no_data_to_show));
                } else {
                    queryResultTextView.setText(resultString.toString());
                }

            } finally {
                cursor.close();
            }

        } else {
            queryResultTextView.setText(getString(R.string.error_while_fetching_data));
        }
    }
}

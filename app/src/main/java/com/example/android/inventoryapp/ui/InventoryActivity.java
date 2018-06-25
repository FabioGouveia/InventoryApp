package com.example.android.inventoryapp.ui;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.adapters.InventoryAdapter;
import com.example.android.inventoryapp.data.InventoryContract;

import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.*;

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 0;

    private InventoryAdapter inventoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        final float displayDensity = getResources().getDisplayMetrics().density;

        FloatingActionButton addInventoryFAB = findViewById(R.id.add_inventory_fab);
        addInventoryFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryActivity.this, ProductDetailsActivity.class));
                overridePendingTransition(R.anim.enter_from_right_animation, R.anim.exit_to_left_animation);
            }
        });

        ListView inventoryList = findViewById(R.id.inventory_list_view);

        inventoryAdapter = new InventoryAdapter(this, null);

        inventoryList.setAdapter(inventoryAdapter);

        int listDynamicFooterPadding = getResources().getInteger(R.integer.inventory_list_dynamic_footer_padding);
        int footerViewHeightInPixels = (int) (listDynamicFooterPadding * displayDensity + 0.5f);

        View inventoryListFooterView = new View(this);
        inventoryListFooterView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, footerViewHeightInPixels));

        inventoryList.addFooterView(inventoryListFooterView);

        inventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

                Intent productDetailIntent = new Intent(InventoryActivity.this, ProductDetailsActivity.class);
                productDetailIntent.setData(ContentUris.withAppendedId(InventoryContract.ProductEntry.CONTENT_URI, id));

                startActivity(productDetailIntent);
                overridePendingTransition(R.anim.enter_from_right_animation, R.anim.exit_to_left_animation);
            }
        });

        View emptyView = findViewById(R.id.empty_view);
        inventoryList.setEmptyView(emptyView);

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {

        switch (loaderID) {
            case INVENTORY_LOADER:


                String[] projection = {
                        BaseColumns._ID,
                        COLUMN_PRODUCT_NAME,
                        COLUMN_PRODUCT_PRICE,
                        COLUMN_PRODUCT_QUANTITY,
                        COLUMN_PRODUCT_SUPPLIER_NAME,
                        COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
                };

                return new CursorLoader(this, CONTENT_URI, projection, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        inventoryAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        inventoryAdapter.swapCursor(null);
    }
}

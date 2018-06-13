package com.example.android.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class InventoryContract {

    static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp.data";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_PRODUCT = "product";
    static final String PATH_PRODUCT_ID = "product_id";

    private InventoryContract() {
    }

    public static final class ProductEntry implements BaseColumns {

    }
}

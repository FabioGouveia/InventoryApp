package com.example.android.inventoryapp.data;

import android.net.Uri;

public class InventoryContract {

    static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp.data";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_PRODUCTS = "products";
    static final String PATH_PRODUCTS_ID = "products_id";
}

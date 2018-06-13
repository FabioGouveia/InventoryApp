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

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);
        public static final Uri CONTENT_URI_ID = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT_ID);

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_PRICE = "product_price";
        public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "product_supplier_name";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "product_supplier_phone_number";
        static final String TABLE_NAME = "products";
    }
}

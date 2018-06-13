package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public class InventoryContract {

    private InventoryContract() {
    }

    public static final class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "products";
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_PRICE = "product_price";
        public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "product_supplier_name";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "product_supplier_phone_number";
    }
}

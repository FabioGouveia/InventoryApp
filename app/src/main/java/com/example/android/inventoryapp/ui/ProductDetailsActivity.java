package com.example.android.inventoryapp.ui;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.InventoryContract.ProductEntry;
import com.example.android.inventoryapp.model.TouchableInputEditText;

import java.text.NumberFormat;

import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_PRICE;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_QUANTITY;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME;
import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String PRODUCT_EDIT_MODE_KEY = "product_edit_mode_key";

    private static final int PRODUCT_LOADER = 1;

    private static final double PRICE_RATE = 0.05d;
    private static final double MAXIMUM_PRICE = 1000000d;

    private Uri currentProductUri;
    private boolean editMode;
    private boolean productHasChanged;
    private double price;
    private int quantity;

    private TextInputLayout productNameInputLayout;
    private TextInputLayout supplierNameInputLayout;
    private TextInputLayout supplierPhoneNumberInputLayout;
    private TouchableInputEditText productNameInputEditText;
    private TouchableInputEditText supplierNameInputEditText;
    private TouchableInputEditText supplierPhoneNumberInputEditText;

    private TextView nameTextView;
    private TextView priceTextView;
    private TextView quantityTextView;
    private TextView supplierNameTextView;
    private TextView supplierPhoneNumberTextView;
    private TextView quantityCounterTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productNameInputLayout = findViewById(R.id.product_name_text_input_layout);
        supplierNameInputLayout = findViewById(R.id.supplier_name_text_input_layout);
        supplierPhoneNumberInputLayout = findViewById(R.id.supplier_phone_number_text_input_layout);
        productNameInputEditText = findViewById(R.id.product_name_text_input_edit_text);
        supplierNameInputEditText = findViewById(R.id.supplier_name_text_input_edit_text);
        supplierPhoneNumberInputEditText = findViewById(R.id.supplier_phone_number_text_input_edit_text);

        nameTextView = findViewById(R.id.product_name_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        quantityTextView = findViewById(R.id.quantity_text_view);
        TextView supplierNameLabelTextView = findViewById(R.id.supplier_name_label);
        supplierNameTextView = findViewById(R.id.supplier_name_text_view);
        TextView supplierPhoneNumberLabelTextView = findViewById(R.id.supplier_phone_number_label);
        supplierPhoneNumberTextView = findViewById(R.id.supplier_phone_number_text_view);
        quantityCounterTextView = findViewById(R.id.quantity_counter_text_view);

        Button reducePriceButton = findViewById(R.id.price_minus_button);
        Button raisePriceButton = findViewById(R.id.price_plus_button);
        Button reduceQuantityButton = findViewById(R.id.quantity_minus_button);
        Button raiseQuantityButton = findViewById(R.id.quantity_plus_button);
        TextView callSupplierTextView = findViewById(R.id.call_supplier_text_view);

        reducePriceButton.setOnClickListener(this);
        raisePriceButton.setOnClickListener(this);
        reduceQuantityButton.setOnClickListener(this);
        raiseQuantityButton.setOnClickListener(this);
        callSupplierTextView.setOnClickListener(this);

        productNameInputEditText.setOnTouchListener(this);
        supplierNameInputEditText.setOnTouchListener(this);
        supplierPhoneNumberInputEditText.setOnTouchListener(this);

        Intent intent = getIntent();
        currentProductUri = intent.getData();


        ActionBar actionBar = getSupportActionBar();


        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            if (currentProductUri == null) {
                actionBar.setTitle("Add " + actionBar.getTitle());
            } else {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    editMode = bundle.getBoolean(PRODUCT_EDIT_MODE_KEY);
                }

                if (editMode) {
                    actionBar.setTitle("Edit Product");
                } else {
                    actionBar.setTitle("Product Details");
                }
            }
        }

        if (currentProductUri != null) {
            if (!editMode) {
                productNameInputLayout.setVisibility(View.GONE);
                reducePriceButton.setVisibility(View.GONE);
                raisePriceButton.setVisibility(View.GONE);
                supplierNameInputLayout.setVisibility(View.GONE);
                supplierPhoneNumberInputLayout.setVisibility(View.GONE);

                nameTextView.setVisibility(View.VISIBLE);
                supplierNameLabelTextView.setVisibility(View.VISIBLE);
                supplierNameTextView.setVisibility(View.VISIBLE);
                supplierPhoneNumberLabelTextView.setVisibility(View.VISIBLE);
                supplierPhoneNumberTextView.setVisibility(View.VISIBLE);
                quantityCounterTextView.setVisibility(View.GONE);
                callSupplierTextView.setVisibility(View.VISIBLE);
            }

            getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        } else {
            showPrice(0);
            showQuantity(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem saveItem = menu.findItem(R.id.save_menu_item);
        MenuItem editItem = menu.findItem(R.id.edit_menu_item);
        MenuItem deleteItem = menu.findItem(R.id.delete_menu_item);

        if (currentProductUri != null && !editMode) {
            saveItem.setVisible(false);
            editItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (!productHasChanged) {
                    goToTheInventoryActivity();
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goToTheInventoryActivity();
                    }
                };

                showUnsavedChangesDialog(discardButtonClickListener);

                return true;
            case R.id.save_menu_item:
                saveProduct();
                break;
            case R.id.edit_menu_item:
                Intent editProductIntent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                editProductIntent.putExtra(PRODUCT_EDIT_MODE_KEY, true);
                editProductIntent.setData(currentProductUri);

                startActivity(editProductIntent);
                overridePendingTransition(R.anim.enter_from_top_animation, R.anim.exit_to_bottom_animation);
                break;
            case R.id.delete_menu_item:
                showDeleteConfirmationDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.price_minus_button:
                reducePrice();
                break;
            case R.id.price_plus_button:
                augmentPrice();
                break;
            case R.id.quantity_minus_button:
                reduceQuantity();
                break;
            case R.id.quantity_plus_button:
                augmentQuantity();
                break;
            case R.id.call_supplier_text_view:
                actionCall();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                productHasChanged = true;
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
        }
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {

        String[] projection = {
                BaseColumns._ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_QUANTITY,
                COLUMN_PRODUCT_SUPPLIER_NAME,
                COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };

        return new CursorLoader(this, currentProductUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {

            int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int productPriceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            String productName = cursor.getString(productNameColumnIndex);
            double productPrice = cursor.getDouble(productPriceColumnIndex);
            int productQuantity = cursor.getInt(productQuantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);

            if (editMode) {
                productNameInputEditText.setText(productName);
                supplierNameInputEditText.setText(supplierName);
                supplierPhoneNumberInputEditText.setText(supplierPhoneNumber);
            } else {
                nameTextView.setText(productName);
                supplierNameTextView.setText(supplierName);
                supplierPhoneNumberTextView.setText(supplierPhoneNumber);
            }

            price = productPrice;
            quantity = productQuantity;

            showPrice(productPrice);
            showQuantity(productQuantity);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (editMode) {
            productNameInputEditText.setText("");
            supplierNameInputEditText.setText("");
            supplierPhoneNumberInputEditText.setText("");
        } else {
            nameTextView.setText("");
            supplierNameTextView.setText("");
            supplierPhoneNumberTextView.setText("");
        }

        price = 0;
        quantity = 0;

        showPrice(0);
        showQuantity(0);
    }

    @Override
    public void onBackPressed() {

        if (!productHasChanged) {
            goToTheInventoryActivity();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void saveProduct() {

        String productName = productNameInputEditText.getText().toString();
        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();
        Double productPrice = Double.parseDouble(priceTextView.getText().toString().replaceAll(" ", "").replaceAll("[%s" + priceFormat.getCurrency().getSymbol() + "\\s]", "").replaceAll("[,]", "."));
        Integer productQuantity = Integer.parseInt(quantityTextView.getText().toString());
        String supplierName = supplierNameInputEditText.getText().toString();
        String supplierPhoneNumber = supplierPhoneNumberInputEditText.getText().toString();

        boolean savingError = verifyContentBeforeSaving(productName, supplierName, supplierPhoneNumber);

        if (!savingError) {
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
            values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
            values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
            values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

            if (editMode) {
                int rowsAffected = getContentResolver().update(currentProductUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.product_update_failed_message, productName), Toast.LENGTH_SHORT).show();
                } else {
                    goToTheInventoryActivity();
                    Toast.makeText(this, getString(R.string.product_update_successful_message, productName), Toast.LENGTH_SHORT).show();
                }
            } else {
                Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

                if (ContentUris.parseId(uri) != -1) {
                    goToTheInventoryActivity();
                    Toast.makeText(this, getString(R.string.product_inserted_successfully_message, productName), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.product_inserted_failed_message, productName), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void deleteProduct() {

        if (currentProductUri != null) {

            String productName = productNameInputEditText.getText().toString();

            int rowsDeleted = getContentResolver().delete(currentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.product_deleted_failed_message, productName), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.product_deleted_successfully_message, productName), Toast.LENGTH_SHORT).show();
            }

            goToTheInventoryActivity();
        }
    }

    private boolean verifyContentBeforeSaving(String productName, String supplierName, String supplierPhoneNumber) {

        boolean error = false;

        productNameInputLayout.setErrorEnabled(true);
        supplierNameInputLayout.setErrorEnabled(true);
        supplierPhoneNumberInputLayout.setErrorEnabled(true);

        if (TextUtils.isEmpty(productName)) {
            error = true;
            productNameInputLayout.setError(getString(R.string.error_product_name_empty));
            Toast.makeText(this, R.string.error_product_name_empty, Toast.LENGTH_SHORT).show();
        } else {
            productNameInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(supplierName)) {
            error = true;
            supplierNameInputLayout.setError(getString(R.string.error_supplier_name_empty));
            Toast.makeText(this, R.string.error_supplier_name_empty, Toast.LENGTH_SHORT).show();
        } else {
            supplierNameInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(supplierPhoneNumber)) {
            error = true;
            supplierPhoneNumberInputLayout.setError(getString(R.string.error_supplier_phone_number_empty));
            Toast.makeText(this, R.string.error_supplier_phone_number_empty, Toast.LENGTH_SHORT).show();
        } else {
            supplierPhoneNumberInputLayout.setError(null);
        }

        return error;
    }

    private void augmentPrice() {
        if (price < MAXIMUM_PRICE) {

            price += PRICE_RATE;

            if (currentProductUri != null) {
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);

                int rowsAffected = getContentResolver().update(currentProductUri, values, null, null);

                if (rowsAffected == 1) {
                    Toast.makeText(this, R.string.price_augmented, Toast.LENGTH_SHORT).show();
                }
            }
        }

        showPrice(price);
    }

    private void reducePrice() {
        if (price > 0) {

            price -= PRICE_RATE;

            if (currentProductUri != null) {
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);

                int rowsAffected = getContentResolver().update(currentProductUri, values, null, null);

                if (rowsAffected == 1) {
                    Toast.makeText(this, R.string.price_reduced, Toast.LENGTH_SHORT).show();
                }
            }
        }

        showPrice(price);
    }

    private void augmentQuantity() {
        if (quantity < 1000) {

            quantity++;

            if (currentProductUri != null) {
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

                int rowsAffected = getContentResolver().update(currentProductUri, values, null, null);

                if (rowsAffected == 1) {
                    Toast.makeText(this, R.string.quantity_augmented, Toast.LENGTH_SHORT).show();
                }
            }
        }

        showQuantity(quantity);
    }

    private void reduceQuantity() {
        if (quantity > 0) {

            quantity--;

            if (currentProductUri != null) {
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

                int rowsAffected = getContentResolver().update(currentProductUri, values, null, null);

                if (rowsAffected == 1) {
                    Toast.makeText(this, R.string.quantity_reduced, Toast.LENGTH_SHORT).show();
                }
            }
        }

        showQuantity(quantity);
    }

    private void showPrice(double price) {
        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();
        priceFormat.setMaximumFractionDigits(2);
        priceTextView.setText(priceFormat.format(price));
    }

    private void showQuantity(int quantity) {
        quantityTextView.setText(String.valueOf(quantity));
        quantityCounterTextView.setText(String.valueOf(quantity));
    }

    private void actionCall() {

        String supplierPhoneNumber = supplierPhoneNumberTextView.getText().toString();
        Uri supplierPhoneNumberUri = Uri.fromParts("tel", supplierPhoneNumber, null);

        Intent callImplicitIntent = new Intent(Intent.ACTION_DIAL, supplierPhoneNumberUri);

        if (callImplicitIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callImplicitIntent);
        } else {
            Toast.makeText(this, "No application available to handle call action", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_unsaved_changes_msg);
        builder.setPositiveButton(R.string.dialog_discard_button, discardButtonClickListener);
        builder.setNegativeButton(R.string.dialog_keep_editing_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_delete_message);
        builder.setPositiveButton(R.string.dialog_delete_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void goToTheInventoryActivity() {
        startActivity(new Intent(this, InventoryActivity.class));
        overridePendingTransition(R.anim.enter_from_left_animation, R.anim.exit_to_right_animation);
    }
}

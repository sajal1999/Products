package com.example.android.products;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.products.data.ProductContract;

import org.w3c.dom.Text;

public class EditorActivity
        extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PET_LOADER = 0;
    private Uri mCurrentProductUri;
    private EditText mProductNameEditText;
    private EditText mProductPriceEditText;
    private TextView mQuantityNumberText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierNumberEditText;

    private boolean mPetHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;

            return false;
        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_product));


        } else {

            setTitle(getString(R.string.editor_activity_title_edit_product));
            getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);

        }

        mProductNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mProductPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierNumberEditText = (EditText) findViewById(R.id.edit_supplier_number);
        mQuantityNumberText = (TextView) findViewById(R.id.quantityText);
        mProductNameEditText.setOnTouchListener(mTouchListener);
        mProductPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierNumberEditText.setOnTouchListener(mTouchListener);
        mQuantityNumberText.setText("" + supplierQuantity);
    }

    int supplierQuantity = 0;

    public void quantityDisplay(int supplierQuantity) {

        TextView quantityText = (TextView) findViewById(R.id.quantityText);
        if (supplierQuantity >= 0) {
            quantityText.setText("" + supplierQuantity);
        } else {
        }
    }

    public void plusOne(View view) {
        supplierQuantity = supplierQuantity + 1;
        quantityDisplay(supplierQuantity);
    }

    public void minusOne(View view) {
        supplierQuantity = supplierQuantity - 1;
        quantityDisplay(supplierQuantity);

    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteProduct();
            }

        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }


    private void saveProduct() {
        String productNameString = mProductNameEditText.getText().toString().trim();
        String quantityString = mQuantityNumberText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String productPrice = mProductPriceEditText.getText().toString().trim();
        String supplierNumber = mSupplierNumberEditText.getText().toString().trim();

        if (mCurrentProductUri == null &&
                TextUtils.isEmpty(productNameString) || TextUtils.isEmpty(quantityString) ||
                TextUtils.isEmpty(supplierNameString) || TextUtils.isEmpty(productPrice) || TextUtils.isEmpty(supplierNumber)) {
            Toast.makeText(EditorActivity.this,
                    "Please fill in the details", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productNameString);
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, quantityString);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierNumber);

        int Price = 0;
        int supplierQuantity = 0;

        if (!TextUtils.isEmpty(productPrice) && !TextUtils.isEmpty(quantityString)) {

            Price = Integer.parseInt(productPrice);
            supplierQuantity = Integer.parseInt(quantityString);

        }

        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, quantityString);
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, productPrice);


        if (mCurrentProductUri == null) {

            Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }


        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                finish();
                return true;

            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;

            case R.id.action_delete_all_entries:
                deleteAllProducts();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                if (!mPetHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;

                }

                DialogInterface.OnClickListener discardButtonClickListener =

                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }

                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRICE,
                ProductContract.ProductEntry.COLUMN_QUANTITY,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from products database");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            final int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
            int quantityIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);
            int supplierNameIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneNumberIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            String name = cursor.getString(nameColumnIndex);
            String supplierName = cursor.getString(supplierNameIndex);
            int price = cursor.getInt(priceColumnIndex);
            final int quantity = cursor.getInt(quantityIndex);
            final int suppliernumber = cursor.getInt(supplierPhoneNumberIndex);
            mProductNameEditText.setText(name);
            mSupplierNameEditText.setText(supplierName);
            mQuantityNumberText.setText(Integer.toString(quantity));
            mProductPriceEditText.setText(Integer.toString(price));
            mSupplierNumberEditText.setText(Integer.toString(suppliernumber));

            Button phoneButton = findViewById(R.id.phone_button);
            phoneButton.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    String phone = String.valueOf(suppliernumber);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }

            });
            Button productDecreaseButton = findViewById(R.id.subtractButton);
            productDecreaseButton.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    decreaseCount(idColumnIndex, quantity);
                }

            });


            Button productIncreaseButton = findViewById(R.id.addButton);
            productIncreaseButton.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    increaseCount(idColumnIndex, quantity);
                }

            });
        }
    }

    public void decreaseCount(int productID, int productQuantity) {

        productQuantity = productQuantity - 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, getString(R.string.quantity_change_msg), Toast.LENGTH_SHORT).show();
            Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");
        } else {
            Toast.makeText(this, getString(R.string.quantity_finish_msg), Toast.LENGTH_SHORT).show();
        }

    }

    public void increaseCount(int productID, int productQuantity) {
        productQuantity = productQuantity + 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, getString(R.string.quantity_change_msg), Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");

        }

    }

    private void updateProduct(int productQuantity) {
        Log.d("message", "updateProduct at ViewActivity");
        if (mCurrentProductUri == null) {
            return;

        }

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, productQuantity);
        if (mCurrentProductUri == null) {
            Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mProductNameEditText.setText("");
        mProductPriceEditText.setText("");
        mQuantityNumberText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierNumberEditText.setText("");
    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
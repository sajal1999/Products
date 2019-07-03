package com.example.android.products;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.products.data.ProductContract;
import com.example.android.products.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private ProductDbHelper mDbHelper;
    private static final int PET_LOADER =0;
    private int quantity;
    ProductCursorAdapter mCursorAdapter;
    private TextView mQuantityNumberText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override


            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);

            }
        });

        ListView petListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);
        mCursorAdapter = new ProductCursorAdapter(this,null);
        petListView.setAdapter(mCursorAdapter);
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI,id);
                intent.setData(currentPetUri);
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(PET_LOADER,null,this);

    }
    private void insertProduct() {

        ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Headphones");
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, 4500);
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, 5);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, "Sanju");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, 761071997);

        Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);


    }


    public void productSaleCount(int productID, int productQuantity) {

        productQuantity = productQuantity - 1;

        if (productQuantity >= 0) {

            ContentValues values = new ContentValues();

            values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, productQuantity);

            Uri updateUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, productID);

            int rowsAffected = getContentResolver().update(updateUri, values, null, null);

            Toast.makeText(this, "Quantity was change", Toast.LENGTH_SHORT).show();



            Log.d("Log msg", "rowsAffected " + rowsAffected + " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");

        } else {

            Toast.makeText(this, "Product was finish :( , buy another Product", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertProduct();
                return true;
            case R.id.action_delete_all_entries:
            deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {ProductContract.ProductEntry._ID, ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, ProductContract.ProductEntry.COLUMN_PRICE, ProductContract.ProductEntry.COLUMN_QUANTITY};
        return new CursorLoader(this, ProductContract.ProductEntry.CONTENT_URI,projection,null,null,null);
    }

    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}

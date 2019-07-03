package com.example.android.products;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.products.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {

        super(context, c, 0 /* flags */);

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        Button shopButton = (Button) view.findViewById(R.id.shop);


        final int columnIdIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int breedColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);


        // Read the pet attributes from the Cursor for the current pet
        final String productID = cursor.getString(columnIdIndex);
        String productName = cursor.getString(nameColumnIndex);
        String price = cursor.getString(breedColumnIndex);
        final String quantity = cursor.getString(quantityColumnIndex);

        shopButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                MainActivity Activity = (MainActivity) context;

                Activity.productSaleCount(Integer.valueOf(productID), Integer.valueOf(quantity));

            }

        });

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(price)) {
            price = context.getString(R.string.unknown_breed);
        }

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(productName);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);
    }
}
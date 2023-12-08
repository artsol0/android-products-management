package com.artsolo.goods;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ProductsStock.db";
    private static final int DATABASE_VERSION = 1;

    // Products table
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_SPECIAL = "special";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_PHOTO = "image";

    // Sizes table
    private static final String TABLE_SIZES = "sizes";
    private static final String COLUMN_SIZE_ID = "size_id";
    private static final String COLUMN_PROD_ID_FK = "product_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AMOUNT = "amount";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStock = "CREATE TABLE " + TABLE_PRODUCTS +
                " (" + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SPECIAL + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_PHOTO + " BLOB);";

        String createTableSizes = "CREATE TABLE " + TABLE_SIZES + "("
                + COLUMN_SIZE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PROD_ID_FK + " INTEGER, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_AMOUNT + " INTEGER, "
                + "FOREIGN KEY (" + COLUMN_PROD_ID_FK + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")"
                + ")";

        db.execSQL(createTableStock);
        db.execSQL(createTableSizes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIZES);
        onCreate(db);
    }

    public void addNewProduct(String special, String title, double price, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SPECIAL, special);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_PHOTO, imageBytes);

        long result = db.insert(TABLE_PRODUCTS, null, cv);

        if (result == -1) {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.success_product_add, Toast.LENGTH_LONG).show();
        }
    }

    public void updateProduct(int productId, String special, String title, double price, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SPECIAL, special);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_PHOTO, imageBytes);

        long result = db.update(TABLE_PRODUCTS, cv, "product_id=?", new String[] {String.valueOf(productId)});

        if (result == -1) {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.updated, Toast.LENGTH_LONG).show();
        }
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long deleteSizesResult = db.delete(TABLE_SIZES, "product_id=?", new String[]{String.valueOf(productId)});
        long deleteProductResult = db.delete(TABLE_PRODUCTS, "product_id=?", new String[] {String.valueOf(productId)});
        if (deleteSizesResult == -1 && deleteProductResult == -1) {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.deleted, Toast.LENGTH_LONG).show();
        }
    }

    public Cursor readAllProducts() {
        String query = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void addNewSize(int productId, String sizeName, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PROD_ID_FK, productId);
        cv.put(COLUMN_NAME, sizeName);
        cv.put(COLUMN_AMOUNT, amount);

        long result = db.insert(TABLE_SIZES, null, cv);

        if (result == -1) {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.success_product_add, Toast.LENGTH_LONG).show();
        }
    }

    public void changeSizeAmount(int sizeId, int newAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_AMOUNT, newAmount);

        long result = db.update(TABLE_SIZES, cv, "size_id=?", new String[] {String.valueOf(sizeId)});
        if (result == -1) {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show();
        }
    }

    public void deleteSize(int sizeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_SIZES, "size_id=?", new String[] {String.valueOf(sizeId)});
        if (result == -1) {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.deleted, Toast.LENGTH_LONG).show();
        }
    }

    public Cursor getAllSizes(int productId) {
        String query = "SELECT * FROM " + TABLE_SIZES + " WHERE product_id=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[] {String.valueOf(productId)});
        }
        return cursor;
    }
}

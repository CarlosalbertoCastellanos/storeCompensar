package com.compensar.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "shoop.db";
    private static final int DATABASE_VERSION = 2;

    // Tabla de Productos
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_PRICE = "price";

    // Tabla del Carrito
    private static final String TABLE_CART = "cart";
    private static final String COLUMN_CART_ID = "cart_id";
    private static final String COLUMN_CART_PRODUCT_ID = "product_id";
    private static final String COLUMN_CART_PRODUCT_NAME = "product_name";
    private static final String COLUMN_CART_PRODUCT_PRICE = "product_price";
    private static final String COLUMN_CART_QUANTITY = "quantity";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS +
                " (" + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PRODUCT_PRICE + " INTEGER);";

        String createCartTable = "CREATE TABLE " + TABLE_CART +
                " (" + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CART_PRODUCT_ID + " INTEGER, " +
                COLUMN_CART_PRODUCT_NAME + " TEXT, " +
                COLUMN_CART_PRODUCT_PRICE + " INTEGER, " +
                COLUMN_CART_QUANTITY + " INTEGER DEFAULT 1, " +
                "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + "));";

        db.execSQL(createProductsTable);
        db.execSQL(createCartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Métodos para manejar Productos
    public void deleteProduct(String row_id) {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + "=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Fallo al eliminar el producto", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
        }
    }

    public long addProduct(String nameProduct, int price) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCT_NAME, nameProduct);
        cv.put(COLUMN_PRODUCT_PRICE, price);
        long result = db.insert(TABLE_PRODUCTS, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Error al añadir producto", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Producto añadido exitosamente", Toast.LENGTH_SHORT).show();
        }
        return result;
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

    public void updateProduct(String row_id, String newNameProduct, String newPriceProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCT_NAME, newNameProduct);
        cv.put(COLUMN_PRODUCT_PRICE, Integer.parseInt(newPriceProduct));
        long result = db.update(TABLE_PRODUCTS, cv, COLUMN_PRODUCT_ID + "=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Fallo al actualizar el producto", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();
        }
    }

    // Métodos para manejar el Carrito
    public long addToCart(String productId, String productName, int productPrice) {
        SQLiteDatabase db = getWritableDatabase();

        // Verificar si el producto ya está en el carrito
        Cursor cursor = db.query(TABLE_CART, null, COLUMN_CART_PRODUCT_ID + "=?",
                new String[]{productId}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // Producto ya existe en el carrito, actualizar la cantidad
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CART_QUANTITY, currentQuantity + 1);
            long result = db.update(TABLE_CART, cv, COLUMN_CART_PRODUCT_ID + "=?", new String[]{productId});
            cursor.close();
            return result;
        } else {
            // Producto no existe en el carrito, añadirlo
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CART_PRODUCT_ID, productId);
            cv.put(COLUMN_CART_PRODUCT_NAME, productName);
            cv.put(COLUMN_CART_PRODUCT_PRICE, productPrice);
            cv.put(COLUMN_CART_QUANTITY, 1);
            long result = db.insert(TABLE_CART, null, cv);
            if (cursor != null) cursor.close();
            return result;
        }
    }

    public Cursor readAllCartItems() {
        String query = "SELECT * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void removeFromCart(String cartId) {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{cartId});
        if (result == -1) {
            Toast.makeText(context, "Fallo al eliminar del carrito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCartQuantity(String cartId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CART_QUANTITY, newQuantity);
        long result = db.update(TABLE_CART, cv, COLUMN_CART_ID + "=?", new String[]{cartId});
        if (result == -1) {
            Toast.makeText(context, "Fallo al actualizar la cantidad", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Cantidad actualizada", Toast.LENGTH_SHORT).show();
        }
    }
}

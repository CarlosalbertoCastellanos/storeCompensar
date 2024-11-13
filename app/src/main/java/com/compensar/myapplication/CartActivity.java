package com.compensar.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private Button checkoutButton;
    private Database db;
    private ArrayList<String> cartIds, cartProductIds, cartProductNames, cartProductPrices, cartQuantities;
    private CustomAdapterCart adapterCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Carrito de Compras");

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        checkoutButton = findViewById(R.id.checkoutButton);

        db = new Database(this);
        cartIds = new ArrayList<>();
        cartProductIds = new ArrayList<>();
        cartProductNames = new ArrayList<>();
        cartProductPrices = new ArrayList<>();
        cartQuantities = new ArrayList<>();

        storeCartDataInArrays();

        adapterCart = new CustomAdapterCart(this, this, cartIds, cartProductIds, cartProductNames, cartProductPrices, cartQuantities);
        cartRecyclerView.setAdapter(adapterCart);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkoutButton.setOnClickListener(v -> {
            // Aquí puedes implementar la lógica de checkout
            Toast.makeText(CartActivity.this, "Checkout no implementado", Toast.LENGTH_SHORT).show();
        });
    }

    void storeCartDataInArrays() {
        // Limpiar las listas antes de llenarlas
        cartIds.clear();
        cartProductIds.clear();
        cartProductNames.clear();
        cartProductPrices.clear();
        cartQuantities.clear();

        // Obtener los datos del carrito desde la base de datos
        Cursor cursor = db.readAllCartItems();

        if (cursor != null) {
            if (cursor.moveToFirst()) { // Mover el cursor a la primera fila
                do {
                    // Obtener los datos de cada columna por nombre
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("cart_id"));
                    String productId = cursor.getString(cursor.getColumnIndexOrThrow("product_id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow("product_price"));
                    String quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));

                    // Añadir los datos a las listas correspondientes
                    cartIds.add(id);
                    cartProductIds.add(productId);
                    cartProductNames.add(name);
                    cartProductPrices.add(price);
                    cartQuantities.add(quantity);
                } while (cursor.moveToNext()); // Mover a la siguiente fila
            } else {
                // El cursor está vacío
                Toast.makeText(this, "No hay datos en el carrito", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el cursor para liberar recursos
            cursor.close();
        } else {
            // El cursor es nulo
            Toast.makeText(this, "Error al obtener datos del carrito", Toast.LENGTH_SHORT).show();
        }
    }
}

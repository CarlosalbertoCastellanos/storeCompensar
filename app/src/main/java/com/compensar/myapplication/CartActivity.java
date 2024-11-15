package com.compensar.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";

    private RecyclerView cartRecyclerView;
    private Button checkoutButton;
    private TextView totalPriceTextView;
    private CustomAdapterCart customAdapterCart;
    private ArrayList<String> cartIds, cartProductIds, cartProductNames, cartProductPrices, cartQuantities;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Inicializar vistas
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        checkoutButton = findViewById(R.id.checkoutButton);
        totalPriceTextView = findViewById(R.id.totalPrice);

        cartIds = new ArrayList<>();
        cartProductIds = new ArrayList<>();
        cartProductNames = new ArrayList<>();
        cartProductPrices = new ArrayList<>();
        cartQuantities = new ArrayList<>();

        db = new Database(this);

        loadCartData();

        customAdapterCart = new CustomAdapterCart(this, this, cartIds, cartProductIds, cartProductNames, cartProductPrices, cartQuantities);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(customAdapterCart);

        updateTotalPrice();

        // Configurar botón de compra
        checkoutButton.setOnClickListener(v -> realizarCompra());
    }
    private void loadCartData() {
        cartIds.clear();
        cartProductIds.clear();
        cartProductNames.clear();
        cartProductPrices.clear();
        cartQuantities.clear();

        Cursor cursor = db.readAllCartItems();
        if (cursor != null && cursor.moveToFirst()) {
            Log.d(TAG, "Productos en el carrito:");
            do {
                String cartId = cursor.getString(cursor.getColumnIndexOrThrow("cart_id"));
                String productId = cursor.getString(cursor.getColumnIndexOrThrow("product_id"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                String productPrice = cursor.getString(cursor.getColumnIndexOrThrow("product_price"));
                String quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));

                Log.d(TAG, "Cart ID: " + cartId + ", Product ID: " + productId + ", Name: " + productName + ", Price: " + productPrice + ", Quantity: " + quantity);

                cartIds.add(cartId);
                cartProductIds.add(productId);
                cartProductNames.add(productName);
                cartProductPrices.add(productPrice);
                cartQuantities.add(quantity);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Log.d(TAG, "El carrito está vacío.");
        }

        // Notificar al adaptador que los datos han cambiado
        if (customAdapterCart != null) {
            customAdapterCart.notifyDataSetChanged();
        }
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (int i = 0; i < cartProductPrices.size(); i++) {
            try {
                double price = Double.parseDouble(cartProductPrices.get(i));
                int quantity = Integer.parseInt(cartQuantities.get(i));
                total += price * quantity;
            } catch (NumberFormatException e) {
                // Manejar el error de parseo
                Toast.makeText(this, "Error al calcular el total", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al parsear precio o cantidad", e);
                return;
            }
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
        Log.d(TAG, "Total del carrito: $" + total);
    }

    // Método para manejar la compra
    private void realizarCompra() {
        if (cartIds.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Implementa aquí la lógica para procesar la compra
        // Por ejemplo, enviar los datos al servidor, procesar el pago, etc.

        Toast.makeText(this, "Compra realizada con éxito.", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Compra realizada exitosamente.");

        for (String cartId : new ArrayList<>(cartIds)) {
            db.removeFromCart(cartId);
        }
        cartIds.clear();
        cartProductIds.clear();
        cartProductNames.clear();
        cartProductPrices.clear();
        cartQuantities.clear();
        customAdapterCart.notifyDataSetChanged();

        // Actualizar el total
        updateTotalPrice();
    }
}

package com.compensar.myapplication;

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

    void storeCartDataInArrays(){
        cartIds.clear();
        cartProductIds.clear();
        cartProductNames.clear();
        cartProductPrices.clear();
        cartQuantities.clear();

        // Obtener los datos del carrito desde la base de datos
        db.readAllCartItems().moveToFirst();
        while (db.readAllCartItems().moveToNext()) {
            cartIds.add(db.readAllCartItems().getString(0));
            cartProductIds.add(db.readAllCartItems().getString(1));
            cartProductNames.add(db.readAllCartItems().getString(2));
            cartProductPrices.add(db.readAllCartItems().getString(3));
            cartQuantities.add(db.readAllCartItems().getString(4));
        }
    }
}

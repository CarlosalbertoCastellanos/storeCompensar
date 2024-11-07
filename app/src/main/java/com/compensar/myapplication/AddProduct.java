package com.compensar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AddProduct extends AppCompatActivity {

    EditText nameProduct, priceProduct;
    Button buttonAddProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        nameProduct=findViewById(R.id.name_product);
        priceProduct=findViewById(R.id.price_product);
        buttonAddProduct=findViewById(R.id.btn_add_product);

        buttonAddProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                Database myDB= new Database(AddProduct.this);
                String name = nameProduct.getText().toString().trim();
                String priceStr = priceProduct.getText().toString().trim();

                if (!name.isEmpty() && !priceStr.isEmpty()) {
                    int price = Integer.parseInt(priceStr);
                    myDB.addProduct(name, price);
                    Toast.makeText(AddProduct.this, "Product added", Toast.LENGTH_SHORT).show();
                    nameProduct.setText("");
                    priceProduct.setText("");
                    startActivity(new Intent(AddProduct.this, Home.class));

                } else {
                    Toast.makeText(AddProduct.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
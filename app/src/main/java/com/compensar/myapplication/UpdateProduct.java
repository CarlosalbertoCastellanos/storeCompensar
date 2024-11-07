package com.compensar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateProduct extends AppCompatActivity {

    EditText  name_input, price_input;
    Button update_button;

    String id, name, price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_product);
        name_input=findViewById(R.id.edit_name_product);
        price_input=findViewById(R.id.edit_price_product);
        update_button=findViewById(R.id.btn_edit_product);
        getIntentData();
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName=name_input.getText().toString();
                String newPrice=price_input.getText().toString();

                Database db=new Database(UpdateProduct.this);
                db.updateProduct(id, newName, newPrice);
                startActivity(new Intent(UpdateProduct.this, Home.class));
            }
        });

    }
    void getIntentData(){
        if (getIntent().hasExtra("id")&&getIntent().hasExtra("name")&&getIntent().hasExtra("price")){
            id=getIntent().getStringExtra("id");
            name=getIntent().getStringExtra("name");
            price=getIntent().getStringExtra("price");
            name_input.setText(name);
            price_input.setText(price);
        }
        else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT);
        }
    }
}
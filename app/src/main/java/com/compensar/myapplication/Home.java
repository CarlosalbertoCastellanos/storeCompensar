package com.compensar.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private FloatingActionButton addNewProduct;
    private RecyclerView recyclerView;

    Database db;
    ArrayList<String> nameProducts, priceProducts, idProducts;
    CustomAdapterProducts customerAdapterProducts;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        addNewProduct = findViewById(R.id.floatingActionButton);
        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, AddProduct.class));
            }
        });
        db = new Database(Home.this);
        nameProducts=new ArrayList<>();
        priceProducts=new ArrayList<>();
        idProducts=new ArrayList<>();
        storeDataInArray();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapterProducts adapter= new CustomAdapterProducts(Home.this,this, idProducts, nameProducts, priceProducts);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            recreate();
        }
    }
    void storeDataInArray(){
        Cursor cursor = db.readAllProducts();
        int sizeData=cursor.getCount();
        if (cursor==null || sizeData==0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT);
        }else{
            while (cursor.moveToNext()){
                String idProduct = cursor.getString(0);
                String nameProduct= cursor.getString(1);
                String priceProduct= cursor.getString(2);
                idProducts.add(idProduct);
                nameProducts.add(nameProduct);
                priceProducts.add(priceProduct);
            }
        }
    }
}
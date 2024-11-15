package com.compensar.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateProduct extends AppCompatActivity {

    private EditText editTextName, editTextPrice;
    private Button buttonUpdate;
    private Database db;
    private String id, currentName, currentPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product); // Asegúrate de tener este layout

        // Inicializar vistas
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // Inicializar base de datos
        db = new Database(this);

        // Obtener datos del producto a actualizar (posiblemente pasados por Intent)
        // Aquí asumimos que recibes los datos a través de un Intent
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            currentName = getIntent().getStringExtra("name");
            currentPrice = getIntent().getStringExtra("price");

            // Establecer los datos actuales en los EditTexts
            editTextName.setText(currentName);
            editTextPrice.setText(currentPrice);
        }

        // Configurar el botón de actualización
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = editTextName.getText().toString().trim();
                String newPriceString = editTextPrice.getText().toString().trim();

                // Validar que el nombre no esté vacío
                if (newName.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "El nombre no puede estar vacío.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar que el precio no esté vacío
                if (newPriceString.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "El precio no puede estar vacío.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double newPrice;

                // Intentar convertir el String a double
                try {
                    newPrice = Double.parseDouble(newPriceString);
                } catch (NumberFormatException e) {
                    Toast.makeText(UpdateProduct.this, "Por favor, ingresa un precio válido.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Llamar al método para actualizar el producto
                db.updateProduct(id, newName, newPrice);

                // Opcional: Finalizar la actividad o actualizar la UI
                Toast.makeText(UpdateProduct.this, "Producto actualizado exitosamente.", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad y regresa a la anterior
            }
        });
    }
}

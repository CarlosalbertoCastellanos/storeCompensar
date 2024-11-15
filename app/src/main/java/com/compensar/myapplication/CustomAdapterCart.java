package com.compensar.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterCart extends RecyclerView.Adapter<CustomAdapterCart.MyViewHolder> {

    private static final String TAG = "CustomAdapterCart";

    private Context context;
    private Activity activity;
    private ArrayList<String> cartIds, cartProductIds, cartProductNames, cartProductPrices, cartQuantities;
    private Database db;

    public CustomAdapterCart(Activity activity, Context context, ArrayList<String> cartIds, ArrayList<String> cartProductIds,
                             ArrayList<String> cartProductNames, ArrayList<String> cartProductPrices,
                             ArrayList<String> cartQuantities) {
        this.activity = activity;
        this.context = context;
        this.cartIds = cartIds;
        this.cartProductIds = cartProductIds;
        this.cartProductNames = cartProductNames;
        this.cartProductPrices = cartProductPrices;
        this.cartQuantities = cartQuantities;
        this.db = new Database(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "Inflando el layout para el ítem del carrito.");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d(TAG, "Vinculando datos al ítem en posición: " + position);
        holder.cartId.setText(cartIds.get(position));
        holder.cartProductName.setText(cartProductNames.get(position));
        holder.cartProductPrice.setText("$" + cartProductPrices.get(position));
        holder.cartQuantity.setText("Cantidad: " + cartQuantities.get(position));

        // Aumentar Cantidad
        holder.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int currentQuantity = Integer.parseInt(cartQuantities.get(position));
                    currentQuantity += 1;
                    cartQuantities.set(position, String.valueOf(currentQuantity));
                    holder.cartQuantity.setText("Cantidad: " + currentQuantity);

                    // Actualizar la base de datos
                    String cartId = cartIds.get(position);
                    db.updateCartQuantity(cartId, currentQuantity);

                    notifyItemChanged(position);
                    Log.d(TAG, "Cantidad aumentada para el producto en posición: " + position);
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Error al actualizar la cantidad", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error al parsear la cantidad", e);
                }
            }
        });

        // Disminuir Cantidad
        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int currentQuantity = Integer.parseInt(cartQuantities.get(position));
                    if (currentQuantity > 1) {
                        currentQuantity -= 1;
                        cartQuantities.set(position, String.valueOf(currentQuantity));
                        holder.cartQuantity.setText("Cantidad: " + currentQuantity);

                        // Actualizar la base de datos
                        String cartId = cartIds.get(position);
                        db.updateCartQuantity(cartId, currentQuantity);

                        notifyItemChanged(position);
                        Log.d(TAG, "Cantidad disminuida para el producto en posición: " + position);
                    } else {
                        // Si la cantidad es 1, eliminar el producto del carrito
                        removeProduct(position);
                        Log.d(TAG, "Producto eliminado del carrito en posición: " + position + " porque la cantidad es 1");
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Error al actualizar la cantidad", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error al parsear la cantidad", e);
                }
            }
        });

        // Eliminar del Carrito
        holder.removeButton.setOnClickListener(v -> {
            String cartId = cartIds.get(position);
            db.removeFromCart(cartId);
            removeProduct(position);
            Toast.makeText(context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Producto eliminado del carrito en posición: " + position);
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Número de ítems en el adaptador: " + cartIds.size());
        return cartIds.size();
    }

    private void removeProduct(int position) {
        Log.d(TAG, "Removiendo producto en posición: " + position);
        cartIds.remove(position);
        cartProductIds.remove(position);
        cartProductNames.remove(position);
        cartProductPrices.remove(position);
        cartQuantities.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartIds.size());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cartId, cartProductName, cartProductPrice, cartQuantity;
        ImageButton removeButton, increaseButton, decreaseButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cartId = itemView.findViewById(R.id.cart_id);
            cartProductName = itemView.findViewById(R.id.cart_product_name);
            cartProductPrice = itemView.findViewById(R.id.cart_product_price);
            cartQuantity = itemView.findViewById(R.id.cart_quantity);
            removeButton = itemView.findViewById(R.id.removeFromCartButton);
            increaseButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseButton = itemView.findViewById(R.id.decreaseQuantityButton);
        }
    }
}

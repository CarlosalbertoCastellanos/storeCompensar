package com.compensar.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    private Context context;
    private Activity activity;
    private ArrayList<String> cartIds, cartProductIds, cartProductNames, cartProductPrices, cartQuantities;
    private Database db;

    // Constructor para inicializar los datos
    CustomAdapterCart(Activity activity, Context context, ArrayList<String> cartIds, ArrayList<String> cartProductIds,
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
    public CustomAdapterCart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterCart.MyViewHolder holder, int position) {
        holder.cartId.setText(cartIds.get(position));
        holder.cartProductName.setText(cartProductNames.get(position));
        holder.cartProductPrice.setText("$" + cartProductPrices.get(position));
        holder.cartQuantity.setText("Cantidad: " + cartQuantities.get(position));

        holder.removeButton.setOnClickListener(v -> {
            String cartId = cartIds.get(position);
            db.removeFromCart(cartId);
            cartIds.remove(position);
            cartProductIds.remove(position);
            cartProductNames.remove(position);
            cartProductPrices.remove(position);
            cartQuantities.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartIds.size());
            Toast.makeText(context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return cartIds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cartId, cartProductName, cartProductPrice, cartQuantity;
        ImageButton removeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cartId = itemView.findViewById(R.id.cart_id);
            cartProductName = itemView.findViewById(R.id.cart_product_name);
            cartProductPrice = itemView.findViewById(R.id.cart_product_price);
            cartQuantity = itemView.findViewById(R.id.cart_quantity);
            removeButton = itemView.findViewById(R.id.removeFromCartButton);
        }
    }
}

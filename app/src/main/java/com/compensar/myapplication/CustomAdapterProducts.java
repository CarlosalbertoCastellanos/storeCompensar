package com.compensar.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterProducts extends RecyclerView.Adapter<CustomAdapterProducts.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> idProduct, nameProduct, priceProduct;

    // Constructor to initialize the data
    CustomAdapterProducts( Activity activity, Context context, ArrayList<String> idProduct, ArrayList<String> nameProduct, ArrayList<String> priceProduct) {
        this.activity=activity;
        this.context = context;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflating the layout for each row
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Binding data to the view elements
        holder.textId.setText(idProduct.get(position));
        holder.textTitle.setText(nameProduct.get(position));
        holder.textPrice.setText(priceProduct.get(position));

        holder.editButton.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();  // Get the correct position
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, UpdateProduct.class);
                intent.putExtra("id", idProduct.get(adapterPosition));
                intent.putExtra("name", nameProduct.get(adapterPosition));
                intent.putExtra("price", priceProduct.get(adapterPosition));
                activity.startActivityForResult(intent, 1);
            }
        });

        // Delete button setup
        holder.deleteButton.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();  // Get the correct position for deletion
            if (adapterPosition != RecyclerView.NO_POSITION) {
                removeProduct(adapterPosition);  // Remove the item from the list
            }
        });
    }

    @Override
    public int getItemCount() {
        return idProduct.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textId, textTitle, textPrice;
        ConstraintLayout mainLayout;
        ImageButton deleteButton, editButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.id_product);
            textTitle = itemView.findViewById(R.id.name_product);
            textPrice = itemView.findViewById(R.id.price_product);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            deleteButton = itemView.findViewById(R.id.deleteProduct);
            editButton = itemView.findViewById(R.id.editProduct);
        }
    }

    // Method to remove a product from the list
    private void removeProduct(int position) {
        Database db = new Database(context);
        db.deleteProduct(idProduct.get(position));

        idProduct.remove(position);
        nameProduct.remove(position);
        priceProduct.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, idProduct.size());
    }
}

package com.andorid.fudbox.view.mainscreen.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.R;
import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.DishOrder;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Cart cart;

    public void setDishes(Cart cart) {
        this.cart = cart;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cart != null && position < cart.getDishes().size()) {
            DishOrder dishOrder = cart.getDishes().get(position);
            holder.bind(dishOrder);
        }
    }

    @Override
    public int getItemCount() {
        return cart != null ? cart.getDishes().size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dishNameTextView;
        private final TextView quantityTextView;
        private final TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishNameTextView = itemView.findViewById(R.id.dishNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);

        }

        public void bind(DishOrder dishOrder) {
            dishNameTextView.setText(dishOrder.getDish().getName());
            quantityTextView.setText(String.valueOf(dishOrder.getQuantity()));
            priceTextView.setText(String.valueOf(dishOrder.getDish().getPrice()));
        }
    }
}


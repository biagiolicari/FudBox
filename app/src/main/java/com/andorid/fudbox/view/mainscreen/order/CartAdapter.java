package com.andorid.fudbox.view.mainscreen.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.databinding.ItemCartDishBinding;
import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.DishOrder;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private Cart cart;
    private OnRemoveDishClickListener listener;

    public CartAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setDishes(Cart cart) {
        this.cart = cart;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartDishBinding itemCartDishBinding = ItemCartDishBinding.inflate(inflater, parent, false);
        return new ViewHolder(itemCartDishBinding);
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

    public void setOnRemoveDishClickListener(OnRemoveDishClickListener listener) {
        this.listener = listener;
    }

    public interface OnRemoveDishClickListener {
        void onRemoveDishClick(DishOrder dishOrder);
        void onDecrementDishClick(DishOrder dishOrder);
        void onIncrementDishClick(DishOrder dishOrder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemCartDishBinding binding;

        public ViewHolder(@NonNull ItemCartDishBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set click listener for the "Add to Cart" button
            binding.trashButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    DishOrder dishOrder = cart.getDishes().get(position);
                    if (listener != null) {
                        listener.onRemoveDishClick(dishOrder);

                    }
                }
            });

            binding.plusButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    DishOrder dishOrder = cart.getDishes().get(position);
                    if (listener != null) {
                        listener.onIncrementDishClick(dishOrder);

                    }
                }
            });

            binding.minusButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    DishOrder dishOrder = cart.getDishes().get(position);
                    if (listener != null) {
                        listener.onDecrementDishClick(dishOrder);

                    }
                }
            });
        }

        public void bind(DishOrder dishOrder) {
            binding.itemNameTextView.setText(dishOrder.getDish().getName());
            binding.itemDescriptionTextView.setText(dishOrder.getDish().getDescription());
            binding.quantityEditText.setText(String.valueOf(dishOrder.getQuantity()));
            binding.priceTextView.setText(String.valueOf(dishOrder.getDish().getPrice()));
        }
    }
}


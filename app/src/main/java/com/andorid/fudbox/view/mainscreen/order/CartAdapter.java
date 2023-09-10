package com.andorid.fudbox.view.mainscreen.order;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.ItemCartDishBinding;
import com.andorid.fudbox.databinding.ItemMenuBinding;
import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.view.mainscreen.home.menu.MenuAdapter;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Cart cart;
    private final LayoutInflater inflater;
    private OnRemoveDishClickListener onRemoveDishClick;

    public CartAdapter(Context context){
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

    public interface OnRemoveDishClickListener {
        void onRemoveDishClick(DishOrder dishOrder);
    }

    public void setOnRemoveDishClickListener(OnRemoveDishClickListener listener) {
        this.onRemoveDishClick = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemCartDishBinding binding;

        public ViewHolder(@NonNull ItemCartDishBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set click listener for the "Add to Cart" button
            binding.imageButton2.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    DishOrder dishOrder = cart.getDishes().get(position);
                    if (onRemoveDishClick != null) {
                        onRemoveDishClick.onRemoveDishClick(dishOrder);

                    }
                }
            });

        }

        public void bind(DishOrder dishOrder) {
            binding.itemNameTextView.setText(dishOrder.getDish().getName());
            binding.itemDescriptionTextView.setText(dishOrder.getDish().getDescription());
            binding.quantityTextInputLayout.getEditText().setText(String.valueOf(dishOrder.getQuantity()));
            binding.priceTextView.setText(dishOrder.getDish().getPrice().toString());
        }
    }
}


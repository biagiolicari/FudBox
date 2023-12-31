package com.andorid.fudbox.view.mainscreen.home.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.databinding.ItemMenuBinding;
import com.andorid.fudbox.model.Dish;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    private final LayoutInflater inflater;
    private List<Dish> menuItems = new ArrayList<>();

    private OnAddToCartClickListener addToCartClickListener;
    private final NumberFormat currencyFormatter;

    public MenuAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        currencyFormatter.setCurrency(Currency.getInstance("EUR"));
    }

    public void setOnAddToCartClickListener(OnAddToCartClickListener listener) {
        this.addToCartClickListener = listener;
    }

    public void setMenuItems(List<Dish> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMenuBinding binding = ItemMenuBinding.inflate(inflater, parent, false);
        return new MenuHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        Dish menuItem = menuItems.get(position);
        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public interface OnAddToCartClickListener {
        void onAddToCartClick(Dish dish, int quantity);
    }

    public class MenuHolder extends RecyclerView.ViewHolder {
        private static final String EURO_SYMBOL = "€";
        private final ItemMenuBinding binding;

        public MenuHolder(@NonNull ItemMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set click listener for the "Add to Cart" button
            binding.addToCartButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Dish menuItem = menuItems.get(position);
                    if (addToCartClickListener != null) {
                        int quantity = Integer.parseInt(binding.quantityTextInputLayout.getEditText().getText().toString());
                        addToCartClickListener.onAddToCartClick(menuItem, quantity);
                    }
                }
            });
        }

        public void bind(Dish menuItem) {
            binding.itemNameTextView.setText(menuItem.getName());
            binding.itemDescriptionTextView.setText(menuItem.getDescription());
            binding.itemPriceTextView.setText(currencyFormatter.format(menuItem.getPrice()));
        }
    }
}

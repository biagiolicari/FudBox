package com.andorid.fudbox.view.mainscreen.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.databinding.ItemRecentOrderBinding;
import com.andorid.fudbox.model.Cart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserRecentOrderAdapter extends RecyclerView.Adapter<UserRecentOrderAdapter.UserRecentOrderHolder> {
    private final LayoutInflater inflater;
    private final NumberFormat currencyFormatter;
    private List<Cart> carts = new ArrayList<>();

    public UserRecentOrderAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

    }

    public void setOrders(List<Cart> carts) {
        this.carts = carts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserRecentOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecentOrderBinding binding = ItemRecentOrderBinding.inflate(inflater, parent, false);
        return new UserRecentOrderHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecentOrderHolder holder, int position) {
        Cart cart = carts.get(position);
        holder.bind(cart);
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    class UserRecentOrderHolder extends RecyclerView.ViewHolder {
        private final ItemRecentOrderBinding binding;

        public UserRecentOrderHolder(@NonNull ItemRecentOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Cart cart) {
            binding.numberOfDishOrdered.setText(String.format("Order Composed by: %d dishes.", cart.getNumberOfDishOrdered()));
            binding.restaurantAddress.setText(cart.getRestaurant().getAddress());
            binding.restaurantItemTitle.setText(cart.getRestaurant().getName());
            binding.totalPriceOrder.setText(currencyFormatter.format(cart.getTotalPrice()));
            binding.orderDate.setText(cart.getOrderDate());
        }
    }
}

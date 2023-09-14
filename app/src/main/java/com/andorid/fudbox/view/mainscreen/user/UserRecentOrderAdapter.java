package com.andorid.fudbox.view.mainscreen.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.databinding.ItemRecentOrderBinding;
import com.andorid.fudbox.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class UserRecentOrderAdapter extends RecyclerView.Adapter<UserRecentOrderAdapter.UserRecentOrderHolder> {
    private final LayoutInflater inflater;
    private final NumberFormat currencyFormatter;
    private List<Order> carts = new ArrayList<>();

    public UserRecentOrderAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        currencyFormatter.setCurrency(Currency.getInstance("EUR"));

    }

    public void setOrders(List<Order> carts) {
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
        Order order = carts.get(position);
        holder.bind(order);
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

        public void bind(Order order) {
            binding.numberOfDishOrdered.setText(String.format("Order Composed by: %d dishes.", order.getNumberOfDishOrdered()));
            binding.restaurantAddress.setText(order.getRestaurantAddress());
            binding.restaurantItemTitle.setText(order.getRestaurantName());
            binding.totalPriceOrder.setText(currencyFormatter.format(order.getTotalCostOfOrder()));
            binding.orderDate.setText(order.getDate());
        }
    }
}

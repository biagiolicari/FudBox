package com.andorid.fudbox.view.mainscreen.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.databinding.ItemRecentOrderBinding;
import com.andorid.fudbox.model.Order;

import java.util.ArrayList;
import java.util.List;

public class UserRecentOrderAdapter extends RecyclerView.Adapter<UserRecentOrderAdapter.UserRecentOrderHolder> {
    private final LayoutInflater inflater;
    private List<Order> orders = new ArrayList<>();

    public UserRecentOrderAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setOrders(List<Order> orders){
        this.orders = orders;
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
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class UserRecentOrderHolder extends RecyclerView.ViewHolder{
        private final ItemRecentOrderBinding binding;
        private StringBuilder numberOfDishText = new StringBuilder("Order Composed by: ");
        private static final String EURO_SYMBOL = "€";

        public UserRecentOrderHolder(@NonNull ItemRecentOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Order order) {
            binding.numberOfDishOrdered.setText(numberOfDishText.append(order.getNumberOfDishOrdered()).append(" dishes."));
            binding.restaurantAddress.setText(order.getRestaurant().getAddress());
            binding.restaurantItemTitle.setText(order.getRestaurant().getName());
            binding.totalPriceOrder.setText(EURO_SYMBOL + " " + order.getTotalPrice());
            binding.orderDate.setText(order.getOrderDate());
        }
    }
}

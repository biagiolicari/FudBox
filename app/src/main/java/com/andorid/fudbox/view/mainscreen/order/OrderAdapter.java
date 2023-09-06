package com.andorid.fudbox.view.mainscreen.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.R;
import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.model.Order;
import com.andorid.fudbox.view.mainscreen.home.menu.MenuAdapter;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Order order;
    private OnConfirmOrderListener confirmOrderListener;

    public void setOnAddToCartClickListener(OnConfirmOrderListener listener) {
        this.confirmOrderListener = listener;
    }

    public interface OnConfirmOrderListener {
        void onConfirmOrderListener(Order order);
    }

    public void setDishes(Order order) {
        this.order = order;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (order != null && position < order.getDishes().size()) {
            DishOrder dishOrder = order.getDishes().get(position);
            holder.bind(dishOrder);
        }
    }

    @Override
    public int getItemCount() {
        return order != null ? order.getDishes().size() : 0;
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


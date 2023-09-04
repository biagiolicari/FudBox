package com.andorid.fudbox.view.mainscreen.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.databinding.ItemRestaurantBinding;
import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.model.restaurant.RestaurantFeature;
import com.andorid.fudbox.utils.LatLngDistanceCalculator;
import com.andorid.fudbox.utils.RatingGenerator;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class RestaurantSearchResultAdapter extends RecyclerView.Adapter<RestaurantSearchResultAdapter.RestaurantSearchResultHolder> {
    private final LayoutInflater inflater;
    private List<Restaurant> restaurants = new ArrayList<>();
    private LatLng position;

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Restaurant restaurant);
    }



    public RestaurantSearchResultAdapter(Context context, OnItemClickListener listener, LatLng position) {
        inflater = LayoutInflater.from(context);
        this.position = position;
        this.itemClickListener = listener;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantSearchResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(inflater, parent, false);
        return new RestaurantSearchResultHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantSearchResultHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.bind(restaurant);
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(restaurant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    class RestaurantSearchResultHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantBinding binding;

        public RestaurantSearchResultHolder(ItemRestaurantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Restaurant restaurant) {
            String restaurantTitle = restaurant.getName() == null ? "Not Available" : restaurant.getName();
            String restaurantAddress = restaurant.getAddress() == null ? "Not Available" : restaurant.getAddress();

            binding.restaurantItemTitle.setText(restaurantTitle);
            binding.restaurantAddress.setText(restaurantAddress);

            LatLng restaurantPosition = new LatLng(restaurant.getLat(), restaurant.getLng());
            binding.restaurantDistance.setText(LatLngDistanceCalculator.calculateDistance(position, restaurantPosition));

            binding.restaurantRating.setRating(RatingGenerator.generateRandomRating());


            // You can similarly bind other views here using binding.
        }
    }
}


/**
 * public class RestaurantSearchResultAdapter extends RecyclerView.Adapter<RestaurantSearchResultAdapter.RestaurantSearchResultHolder> {
 * private List<RestaurantFeature> restaurantFeatureList = new ArrayList<>();
 * <p>
 * public void setRestaurantFeatureList(List<RestaurantFeature> restaurantFeatureList) {
 * this.restaurantFeatureList = restaurantFeatureList;
 * notifyDataSetChanged();
 * }
 *
 * @NonNull
 * @Override public RestaurantSearchResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
 * View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
 * return new RestaurantSearchResultHolder(itemView);
 * }
 * @Override public void onBindViewHolder(@NonNull RestaurantSearchResultHolder holder, int position) {
 * RestaurantFeature restaurantFeature = restaurantFeatureList.get(position);
 * <p>
 * holder.restaurantName.setText(restaurantFeature.getProperties().getName());
 * holder.restaurantAddress.setText(restaurantFeature.getProperties().getAddressLine2());
 * <p>
 * }
 * @Override public int getItemCount() {
 * return restaurantFeatureList.size();
 * }
 * <p>
 * <p>
 * class RestaurantSearchResultHolder extends RecyclerView.ViewHolder {
 * private final TextView restaurantName;
 * private final TextView restaurantAddress;
 * private final TextView restaurantPhoneNumber;
 * private final ImageView restaurantIMG;
 * <p>
 * public RestaurantSearchResultHolder(@NonNull View itemView) {
 * super(itemView);
 * <p>
 * restaurantName = itemView.findViewById(R.id.restaurant_item_title);
 * restaurantAddress = itemView.findViewById(R.id.restaurant_address);
 * restaurantPhoneNumber = itemView.findViewById(R.id.restaurant_phone_number);
 * restaurantIMG = itemView.findViewById(R.id.restaurant_item_small);
 * }
 * }
 * }
 **/
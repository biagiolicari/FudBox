package com.andorid.fudbox.view.mainscreen.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.FragmentHomePageBinding;
import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.model.restaurant.RestaurantFeature;
import com.andorid.fudbox.viewmodel.mainscreen.home.RestaurantViewModel;
import com.google.android.gms.maps.model.LatLng;


public class HomeFragment extends Fragment implements RestaurantSearchResultAdapter.OnItemClickListener {
    private LatLng latLngData;
    private RestaurantViewModel viewModel;
    private RestaurantSearchResultAdapter adapter;
    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel and observe LiveData
        viewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        Bundle args = getArguments();
        if (args != null && !args.isEmpty()) {
            Double latitude = args.getDouble("latitude");
            Double longitude = args.getDouble("longitude");
            latLngData = new LatLng(latitude, longitude);
        }
        // Initialize ViewModel and observe LiveData
        viewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        FragmentHomePageBinding binding = FragmentHomePageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize RecyclerView and Adapter
        recyclerView = binding.fragmentRestaurantSearchResultsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RestaurantSearchResultAdapter(requireContext(), this, latLngData);
        recyclerView.setAdapter(adapter);

        //search restaurants
        viewModel.searchRestaurants(latLngData);

        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurantFeatures -> {
            adapter.setRestaurants(restaurantFeatures);
        });

        return view;
    }

    @Override
    public void onItemClick(Restaurant restaurant) {
        Bundle menuBundle = new Bundle();
        //menuBundle.putString("restaurant", restaurant.getName());
        menuBundle.putSerializable("restaurant", restaurant);
        NavController navController = Navigation.findNavController(requireView());
        // Navigate to the MenuFragment using the action defined in the navigation graph
        navController.navigate(R.id.action_restaurantFragment_to_menuFragment, menuBundle);
    }
}

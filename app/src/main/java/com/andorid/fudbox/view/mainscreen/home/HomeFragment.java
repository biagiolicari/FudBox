package com.andorid.fudbox.view.mainscreen.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.FragmentHomePageBinding;
import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.viewmodel.mainscreen.home.RestaurantViewModel;
import com.andorid.fudbox.viewmodel.mainscreen.shared.SharedLatLng;
import com.andorid.fudbox.viewmodel.mainscreen.shared.SharedRestaurantViewModel;
import com.rejowan.cutetoast.CuteToast;


public class HomeFragment extends Fragment implements RestaurantSearchResultAdapter.OnItemClickListener {
    private RestaurantViewModel viewModel;
    private RestaurantSearchResultAdapter adapter;
    private RecyclerView recyclerView;
    private FragmentHomePageBinding binding;
    private ProgressBar progressBar;
    private SharedLatLng sharedLatLng;
    private SharedRestaurantViewModel sharedRestaurantViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        viewModel.init();
        sharedLatLng = new ViewModelProvider(requireActivity()).get(SharedLatLng.class);
        sharedRestaurantViewModel = new ViewModelProvider(requireActivity()).get(SharedRestaurantViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and Adapter
        recyclerView = binding.fragmentRestaurantSearchResultsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sharedLatLng.getLatLngMutableLiveData().observe(getViewLifecycleOwner(), latLng -> {
            adapter = new RestaurantSearchResultAdapter(requireContext(), this, latLng);
            recyclerView.setAdapter(adapter);
            //search restaurants
            viewModel.searchRestaurants(latLng);
        });


        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurant -> {
            switch (restaurant.status){
                case SUCCESS:
                    adapter.setRestaurants(restaurant.data);
                    hideLoadingProgressBar();
                    break;
                case ERROR:
                    hideLoadingProgressBar();
                    CuteToast.ct(requireContext(), restaurant.message, CuteToast.LENGTH_LONG, CuteToast.CONFUSE).show();
            }
        });

        progressBar = binding.loadingProgressBar;
        showLoadingProgressBar();
    }

    @Override
    public void onItemClick(Restaurant restaurant) {
        Bundle menuBundle = new Bundle();
        menuBundle.putSerializable("restaurant", restaurant);
        sharedRestaurantViewModel.setRestaurantToLiveData(restaurant);
        NavController navController = Navigation.findNavController(requireView());
        // Navigate to the MenuFragment using the action defined in the navigation graph
        navController.navigate(R.id.action_restaurantFragment_to_menuFragment, menuBundle);
    }

    private void showLoadingProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}

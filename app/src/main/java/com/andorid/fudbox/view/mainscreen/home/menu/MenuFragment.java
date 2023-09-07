package com.andorid.fudbox.view.mainscreen.home.menu;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.databinding.FragmentMenuBinding;
import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.viewmodel.mainscreen.home.menu.DishOrderViewModel;
import com.andorid.fudbox.viewmodel.mainscreen.home.menu.MenuViewModel;
import com.andorid.fudbox.viewmodel.mainscreen.order.OrderViewModel;
import com.rejowan.cutetoast.CuteToast;

import java.util.List;

public class MenuFragment extends Fragment implements MenuAdapter.OnAddToCartClickListener {
    private final static String RESTAURANT_ARG = "restaurant";
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private MenuViewModel menuViewModel;
    private DishOrderViewModel dishOrderViewModel;
    private OrderViewModel orderViewModel;
    private LiveData<List<Dish>> dishesLiveData;
    private ProgressBar progressBar;
    private FragmentMenuBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        dishOrderViewModel = new ViewModelProvider(this).get(DishOrderViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        menuViewModel.init();
        dishOrderViewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Restaurant restaurantBundle = getRestaurantBundle();
            binding.restaurantNameTextView.setText(restaurantBundle.getName());
        }

        recyclerView = binding.menuRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        menuAdapter = new MenuAdapter(requireContext());
        recyclerView.setAdapter(menuAdapter);

        dishesLiveData = menuViewModel.getDishes();

        menuViewModel.getDishes().observe(getViewLifecycleOwner(), dishes -> {
            menuAdapter.setMenuItems(dishes);
            hideLoadingProgressBar();
        });

        menuAdapter.setOnAddToCartClickListener(this);

        menuViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                CuteToast.ct(requireContext(), errorMessage, CuteToast.LENGTH_SHORT, CuteToast.CONFUSE, true).show();
                menuViewModel.clearErrorMessage(); // Optionally clear the error message in the ViewModel
            }
        });

        progressBar = binding.loadingProgressBar;
        showLoadingProgressBar();

        return view;
    }

    @Nullable
    private Restaurant getRestaurantBundle() {
        Restaurant restaurantBundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            restaurantBundle = getArguments().getSerializable(RESTAURANT_ARG, Restaurant.class);
        }
        return restaurantBundle;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Imposta il comportamento del pulsante indietro
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBackToHomeFragment();
            }
        });

        // Add the observer for dish quantities
        dishOrderViewModel.getDishOrderLiveData().observe(getViewLifecycleOwner(), dishQuantity -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && dishQuantity != null) {
                Restaurant restaurant = getArguments().getSerializable("restaurant", Restaurant.class);
                orderViewModel.buildOrder(dishQuantity, restaurant);
            }
        });
    }

    public void navigateBackToHomeFragment() {
        // Navigate back to the previous fragment (HomeFragment)
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onAddToCartClick(Dish dish, int quantity) {
        dishOrderViewModel.addItemToCart(dish, quantity);
    }

    private void showLoadingProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}

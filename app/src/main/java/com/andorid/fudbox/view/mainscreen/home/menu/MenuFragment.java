package com.andorid.fudbox.view.mainscreen.home.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.databinding.FragmentMenuBinding;
import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.Restaurant;
import com.andorid.fudbox.viewmodel.mainscreen.home.menu.DishOrderViewModel;
import com.andorid.fudbox.viewmodel.mainscreen.home.menu.MenuViewModel;
import com.andorid.fudbox.viewmodel.mainscreen.order.CartViewModel;
import com.andorid.fudbox.viewmodel.mainscreen.shared.SharedRestaurantViewModel;
import com.rejowan.cutetoast.CuteToast;

public class MenuFragment extends Fragment implements MenuAdapter.OnAddToCartClickListener {
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private MenuViewModel menuViewModel;
    private DishOrderViewModel dishOrderViewModel;
    private CartViewModel cartViewModel;
    private ProgressBar progressBar;
    private FragmentMenuBinding binding;
    private SharedRestaurantViewModel sharedRestaurantViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        dishOrderViewModel = new ViewModelProvider(this).get(DishOrderViewModel.class);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        sharedRestaurantViewModel = new ViewModelProvider(requireActivity()).get(SharedRestaurantViewModel.class);
        menuViewModel.init();
        dishOrderViewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sharedRestaurantViewModel.getRestaurantMutableLiveData()
                .observe(getViewLifecycleOwner(), restaurant -> binding.restaurantNameTextView.setText(restaurant.getName()));

        recyclerView = binding.menuRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        menuAdapter = new MenuAdapter(requireContext());
        recyclerView.setAdapter(menuAdapter);

        menuViewModel.fetchDishes().observe(getViewLifecycleOwner(), dishData -> {
            switch (dishData.status){
                case SUCCESS:
                    menuAdapter.setMenuItems(dishData.data);
                    hideLoadingProgressBar();
                    break;
                case ERROR:
                    CuteToast.ct(requireContext(), dishData.message, CuteToast.LENGTH_LONG, CuteToast.SAD).show();
            }

        });

        menuAdapter.setOnAddToCartClickListener(this);

        progressBar = binding.loadingProgressBar;
        showLoadingProgressBar();

        return view;
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
            if (sharedRestaurantViewModel.getRestaurantMutableLiveData().getValue() != null) {
                Restaurant restaurant = sharedRestaurantViewModel.getRestaurantMutableLiveData().getValue();
                cartViewModel.buildCart(dishQuantity, restaurant);
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

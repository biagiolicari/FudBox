package com.andorid.fudbox.view.mainscreen.home.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.andorid.fudbox.model.DishQuantity;
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
        FragmentMenuBinding binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.restaurantNameTextView.setText(getArguments().getString(RESTAURANT_ARG));

        recyclerView = binding.menuRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        menuAdapter = new MenuAdapter(requireContext());
        recyclerView.setAdapter(menuAdapter);

        // Create a list of menu items (you can fetch this from a data source)
        dishesLiveData = menuViewModel.getDishes();

        menuViewModel.getDishes().observe(getViewLifecycleOwner(), dishes -> {
            menuAdapter.setMenuItems(dishes);
        });

        menuAdapter.setOnAddToCartClickListener(this);

        menuViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                CuteToast.ct(requireContext(), errorMessage, CuteToast.LENGTH_SHORT, CuteToast.CONFUSE, true).show();
                menuViewModel.clearErrorMessage(); // Optionally clear the error message in the ViewModel
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Imposta il comportamento del pulsante indietro
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Torna al fragment precedente quando premi il pulsante indietro
                NavHostFragment.findNavController(MenuFragment.this)
                        .navigateUp(); // o .navigate(R.id.action_fragmentB_to_fragmentA) se vuoi esplicitamente usare un'azione
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        orderViewModel.buildOrder(dishOrderViewModel.getDishOrderLiveData().getValue(), getArguments().getString(RESTAURANT_ARG));
    }


    @Override
    public void onAddToCartClick(Dish dish, int quantity) {
        dishOrderViewModel.addToCart(dish, quantity);
    }
}

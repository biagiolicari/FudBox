package com.andorid.fudbox.view.mainscreen.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.FragmentCartBinding;
import com.andorid.fudbox.model.DishOrder;
import com.andorid.fudbox.viewmodel.mainscreen.order.CartViewModel;
import com.rejowan.cutetoast.CuteToast;

public class CartFragment extends Fragment implements CartAdapter.OnRemoveDishClickListener {
    private CartViewModel cartViewModel;
    private RecyclerView orderRecyclerView;
    private CartAdapter cartAdapter;
    private FragmentCartBinding binding;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize the RecyclerView and its adapter
        orderRecyclerView = binding.orderRecyclerView;
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartAdapter = new CartAdapter(requireContext());
        orderRecyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnRemoveDishClickListener(this);
        // Observe the order data from the ViewModel and update the adapter
        cartViewModel.getOrderLiveData().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                cartAdapter.setDishes(order);
                binding.totalPriceTextView.setText(order.getTotalPrice().toString());
                binding.completeOrderButton.setVisibility(View.VISIBLE);
                binding.priceLayout.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBack();
            }
        });

        binding.completeOrderButton.setOnClickListener(l -> {
            if (cartViewModel.getOrderLiveData().getValue() != null) {
                Log.wtf("CLICK", "TO SET");
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_order_to_orderaddress);
            } else {
                CuteToast.ct(getContext(), "Please, order something before confirm.", CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
            }
        });
    }

    public void navigateBack() {
        // Navigate back to the previous fragment
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onRemoveDishClick(DishOrder dishOrder) {
        cartViewModel.removeDishQuantityFromCart(dishOrder);
    }

    @Override
    public void onDecrementDishClick(DishOrder dishOrder) {
        cartViewModel.decrementDishQuantityFromCart(dishOrder);
    }

    @Override
    public void onIncrementDishClick(DishOrder dishOrder) {
        cartViewModel.incrementDishQuantityFromCart(dishOrder);
    }
}
package com.andorid.fudbox.view.mainscreen.order;

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
import com.andorid.fudbox.databinding.FragmentCartBinding;
import com.andorid.fudbox.viewmodel.mainscreen.order.CartViewModel;
import com.rejowan.cutetoast.CuteToast;

public class CartFragment extends Fragment {
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
        cartAdapter = new CartAdapter();
        orderRecyclerView.setAdapter(cartAdapter);

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
    public void onStart() {
        super.onStart();
        binding.completeOrderButton.setOnClickListener(l -> {
            if (cartViewModel.getOrderLiveData().getValue() != null) {
                Log.wtf("CLICK", "TO SET");
                //orderViewModel.uploadToFireStore();
                NavController navController = Navigation.findNavController(requireView());
                // Navigate to the MenuFragment using the action defined in the navigation graph
                navController.navigate(R.id.action_order_to_orderaddress);
            } else {
                CuteToast.ct(getContext(), "Please, order something before confirm.", CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
            }
        });

    }
}
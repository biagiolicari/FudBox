package com.andorid.fudbox.view.mainscreen.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.FragmentOrderBinding;
import com.andorid.fudbox.viewmodel.mainscreen.order.OrderViewModel;
import com.rejowan.cutetoast.CuteToast;

public class OrderFragment extends Fragment {
    private OrderViewModel orderViewModel;
    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private FragmentOrderBinding binding;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize the RecyclerView and its adapter
        orderRecyclerView = binding.orderRecyclerView;
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderAdapter = new OrderAdapter();
        orderRecyclerView.setAdapter(orderAdapter);

        // Observe the order data from the ViewModel and update the adapter
        orderViewModel.getOrderLiveData().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                orderAdapter.setDishes(order);
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
            if(orderViewModel.getOrderLiveData().getValue() != null){
                Log.wtf("CLICK", "TO SET");
                orderViewModel.uploadToFireStore();
                orderViewModel.clearOrder();
            } else {
                CuteToast.ct(getContext(), "Please, order something before confirm.", CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
            }
        });

    }
}
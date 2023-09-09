package com.andorid.fudbox.view.mainscreen.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andorid.fudbox.databinding.FragmentOrderAddressBinding;
import com.andorid.fudbox.databinding.FragmentOrderPaymentBinding;
import com.andorid.fudbox.viewmodel.mainscreen.order.OrderViewModel;

public class OrderPaymentFragment extends Fragment {
    private OrderViewModel orderViewModel;
    private FragmentOrderPaymentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

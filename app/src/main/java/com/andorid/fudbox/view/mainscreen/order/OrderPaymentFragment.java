package com.andorid.fudbox.view.mainscreen.order;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.FragmentOrderPaymentBinding;
import com.andorid.fudbox.viewmodel.mainscreen.order.OrderViewModel;
import com.rejowan.cutetoast.CuteToast;

public class OrderPaymentFragment extends Fragment {
    private OrderViewModel orderViewModel;
    private FragmentOrderPaymentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        orderViewModel.init();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.payButton.setOnClickListener(l -> {
            if(isPaymentDetailsValid()) {
                paymentCompleted();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isPaymentDetailsValid(){
        return checkCardNumberText() && checkNameAndSurname() && checkCVCAndExpiration();
    }

    private boolean checkCardNumberText(){
        if(TextUtils.isEmpty(binding.cardNumber.getText())){
            binding.cardNumberLayout.setError(getString(R.string.empty_field));
            binding.cardNumberLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            return false;
        }else {
            binding.cardNumberLayout.setError(null);
            return true;
        }
    }

    private boolean checkNameAndSurname(){
        if(TextUtils.isEmpty(binding.cardNumber.getText()) || TextUtils.isEmpty(binding.surname.getText())){
            binding.nameLayout.setError(getString(R.string.empty_field));
            binding.surnameLayout.setError(getString(R.string.empty_field));
            binding.nameLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            binding.surnameLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            return false;
        }else {
            binding.nameLayout.setError(null);
            binding.surnameLayout.setError(null);
            return true;
        }
    }

    private boolean checkCVCAndExpiration(){
        if(TextUtils.isEmpty(binding.cvc.getText())|| TextUtils.isEmpty(binding.cardExpiration.getText())){
            binding.cvcLayout.setError(getString(R.string.empty_field));
            binding.expirationLayout.setError(getString(R.string.empty_field));
            binding.cvcLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            binding.expirationLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            return false;
        }else {
            binding.expirationLayout.setError(null);
            binding.cvcLayout.setError(null);
            return true;
        }
    }

    private void paymentCompleted() {
        orderViewModel.uploadOrderToFirestore();
        CuteToast.ct(requireContext(), getString(R.string.order_completed), CuteToast.LENGTH_SHORT, CuteToast.HAPPY, true).show();
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_return_to_home);
    }
}

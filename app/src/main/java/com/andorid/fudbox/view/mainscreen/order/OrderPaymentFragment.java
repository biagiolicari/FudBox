package com.andorid.fudbox.view.mainscreen.order;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.FragmentOrderPaymentBinding;
import com.andorid.fudbox.utils.PaymentsUtil;
import com.andorid.fudbox.viewmodel.mainscreen.order.CartViewModel;
import com.andorid.fudbox.viewmodel.mainscreen.order.CheckoutViewModel;
import com.andorid.fudbox.viewmodel.mainscreen.order.OrderViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.button.ButtonOptions;
import com.google.android.gms.wallet.button.PayButton;
import com.rejowan.cutetoast.CuteToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class OrderPaymentFragment extends Fragment {
    private OrderViewModel orderViewModel;
    private CartViewModel cartViewModel;
    private FragmentOrderPaymentBinding binding;
    private CheckoutViewModel checkoutViewModel;
    private PayButton googlePayButton;

    ActivityResultLauncher<IntentSenderRequest> resolvePaymentForResult = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                switch (result.getResultCode()) {
                    case Activity.RESULT_OK:
                        Intent resultData = result.getData();
                        if (resultData != null) {
                            PaymentData paymentData = PaymentData.getFromIntent(result.getData());
                            if (paymentData != null) {
                                handlePaymentCompleted();
                            }
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        CuteToast.ct(requireContext(), getString(R.string.payment_deleted), CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
                        break;
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        checkoutViewModel = new ViewModelProvider(requireActivity()).get(CheckoutViewModel.class);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        orderViewModel.init();
        checkoutViewModel.canUseGooglePay.observe(this, this::setGooglePayAvailable);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        googlePayButton = binding.googlePayButton;
        try {
            googlePayButton.initialize(
                    ButtonOptions.newBuilder()
                            .setAllowedPaymentMethods(PaymentsUtil.getAllowedPaymentMethods().toString()).build()
            );
            googlePayButton.setOnClickListener(this::requestPayment);
        } catch (JSONException e) {
            Log.wtf("PAYG", e.getMessage());
            googlePayButton.setVisibility(View.INVISIBLE);
        }

        binding.payButton.setOnClickListener(l -> {
            if(isPaymentDetailsValid()) {
                handlePaymentCompleted();
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

    private void handlePaymentCompleted() {
        orderViewModel.uploadOrderToFirestore();
        cartViewModel.clearCart();
        CuteToast.ct(requireContext(), getString(R.string.order_completed), CuteToast.LENGTH_SHORT, CuteToast.HAPPY, true).show();
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_return_to_home);
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            CuteToast.ct(requireContext(), getString(R.string.google_pay_status_unavailable), CuteToast.LENGTH_LONG, CuteToast.CONFUSE).show();
        }
    }


    public void requestPayment(View view) {
        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);
        // This price is not displayed to the user.
        final Task<PaymentData> task = checkoutViewModel.getLoadPaymentDataTask(orderViewModel.getTotalOrderPrice());

        task.addOnCompleteListener(completedTask -> {
            if (completedTask.isSuccessful()) {
                handlePaymentCompleted();
            } else {
                Exception exception = completedTask.getException();
                if (exception instanceof ResolvableApiException) {
                    PendingIntent resolution = ((ResolvableApiException) exception).getResolution();
                    resolvePaymentForResult.launch(new IntentSenderRequest.Builder(resolution).build());

                } else if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    handleError(apiException.getStatusCode(), apiException.getMessage());

                } else {
                    handleError(CommonStatusCodes.INTERNAL_ERROR, "Unexpected non API" +
                            " exception when trying to deliver the task result to an activity!");
                }
            }
            // Re-enables the Google Pay payment button.
            googlePayButton.setClickable(true);
        });
    }

    private void handleError(int statusCode, @Nullable String message) {
        Log.e("loadPaymentData failed",
                String.format(Locale.getDefault(), "Error code: %d, Message: %s", statusCode, message));
    }

}

package com.andorid.fudbox.view.authentication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.FragmentAuthBinding;
import com.andorid.fudbox.viewmodel.authentication.AuthViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.rejowan.cutetoast.CuteToast;

public class AuthFragment extends Fragment {

    private Button loginButton;
    private Button signUpButton;
    private TextInputLayout emailInputText;
    private TextInputLayout passwordInputText;
    private AuthViewModel authViewModel;

    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    private void navigateToHomeActivity() {
        Navigation.findNavController(requireView()).navigate(R.id.action_to_home_activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            /**
            if (firebaseUser != null) {
                navigateToHomeActivity();
            }
             **/
            switch (firebaseUser.status){
                case SUCCESS:
                    navigateToHomeActivity();
                    break;
                case ERROR:
                    CuteToast.ct(requireContext(), firebaseUser.message, CuteToast.LENGTH_LONG, CuteToast.SAD).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAuthBinding binding = FragmentAuthBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        emailInputText = binding.fragmentLoginregisterEmail;
        passwordInputText = binding.fragmentLoginregisterPassword;
        loginButton = binding.fragmentLoginregisterLogin;
        signUpButton = binding.fragmentLoginregisterRegister;

        loginButton.setOnClickListener(viewLogin -> {
            String email = emailInputText.getEditText().getText().toString();
            String password = passwordInputText.getEditText().getText().toString();
            if (checkEmailAndPasswordText(email, password)) {
                authViewModel.login(email, password);
            } else {
                CuteToast.ct(getContext(), getString(R.string.email_password_not_inserted), CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
            }
        });

        signUpButton.setOnClickListener(viewSignUp -> {
            String email = emailInputText.getEditText().getText().toString();
            String password = passwordInputText.getEditText().getText().toString();

            if (checkEmailAndPasswordText(email, password)) {
                authViewModel.register(email, password);
            } else {
                CuteToast.ct(getContext(), getString(R.string.email_password_not_inserted), CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
            }
        });

        return view;
    }

    private boolean checkEmailAndPasswordText(String email, String password){
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);
    }


}
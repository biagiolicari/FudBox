package com.andorid.fudbox.view.mainscreen.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.andorid.fudbox.MainActivity;
import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.FragmentUserBinding;
import com.andorid.fudbox.viewmodel.authentication.LoggedInViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.rejowan.cutetoast.CuteToast;

import org.jetbrains.annotations.Nullable;

public class UserFragment extends Fragment {
    private TextView loggedInUserTextView;
    private Button logOutButton;
    private Button recentOrdersButton;
    private LoggedInViewModel loggedInViewModel;
    private FragmentUserBinding binding;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews();
        setUpViewModel();
        setClickListeners();
    }

    private void initializeViews() {
        loggedInUserTextView = binding.fragmentLoggedinUser;
        logOutButton = binding.fragmentLogoutButton;
        recentOrdersButton = binding.recentOrdersButton;
    }

    private void setUpViewModel() {
        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);
        loggedInViewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> updateUI(firebaseUser));
    }

    private void setClickListeners() {
        recentOrdersButton.setOnClickListener(view -> navigateToRecentOrders());
        logOutButton.setOnClickListener(view -> logoutUser());
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            loggedInUserTextView.setText(getString(R.string.logged_in_user, firebaseUser.getEmail()));
            logOutButton.setEnabled(true);
        } else {
            logOutButton.setEnabled(false);
        }
    }

    private void navigateToRecentOrders() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_user_to_recent_order);
    }

    private void logoutUser() {
        loggedInViewModel.logOut();
        CuteToast.ct(getContext(), "User Logged Out", CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
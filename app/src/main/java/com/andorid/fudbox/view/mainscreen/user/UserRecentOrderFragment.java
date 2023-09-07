package com.andorid.fudbox.view.mainscreen.user;

import android.os.Bundle;
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

import com.andorid.fudbox.databinding.FragmentRecentOrdersBinding;
import com.andorid.fudbox.model.Order;
import com.andorid.fudbox.viewmodel.mainscreen.user.UserViewModel;

import java.util.List;

public class UserRecentOrderFragment extends Fragment {
        private LiveData<List<Order>> recentOrdersLiveData;
        private UserViewModel userViewModel;
        private RecyclerView recyclerView;
        private UserRecentOrderAdapter userRecentOrderAdapter;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
                userViewModel.init();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                FragmentRecentOrdersBinding binding = FragmentRecentOrdersBinding.inflate(inflater, container, false);
                View view = binding.getRoot();

                recyclerView = binding.recentOrdersRecyclerView;
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                userRecentOrderAdapter = new UserRecentOrderAdapter(requireContext());
                recyclerView.setAdapter(userRecentOrderAdapter);

                recentOrdersLiveData = userViewModel.getRecentOrdersLiveData();

                userViewModel.getRecentOrdersLiveData().observe(getViewLifecycleOwner(), dishes -> {
                        userRecentOrderAdapter.setOrders(dishes);
                });

/**
                menuViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
                        if (errorMessage != null) {
                                CuteToast.ct(requireContext(), errorMessage, CuteToast.LENGTH_SHORT, CuteToast.CONFUSE, true).show();
                                menuViewModel.clearErrorMessage(); // Optionally clear the error message in the ViewModel
                        }
                });
**/
                return view;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                //Imposta il comportamento del pulsante indietro
                requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                                navigateBackToUserFragment();
                        }
                });
        }

        public void navigateBackToUserFragment() {
                // Navigate back to the previous fragment (HomeFragment)
                NavHostFragment.findNavController(this).popBackStack();
        }


}

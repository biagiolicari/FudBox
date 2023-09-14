package com.andorid.fudbox.view.mainscreen.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.fudbox.databinding.FragmentRecentOrdersBinding;
import com.andorid.fudbox.viewmodel.mainscreen.user.UserViewModel;
import com.rejowan.cutetoast.CuteToast;

public class UserRecentOrderFragment extends Fragment {
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private UserRecentOrderAdapter userRecentOrderAdapter;
    private ProgressBar loadingProgressBar;
    private FragmentRecentOrdersBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecentOrdersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.recentOrdersRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        userRecentOrderAdapter = new UserRecentOrderAdapter(requireContext());
        recyclerView.setAdapter(userRecentOrderAdapter);

        userViewModel.getRecentOrdersLiveData().observe(getViewLifecycleOwner(), orderData -> {
            switch (orderData.status) {
                case SUCCESS:
                    userRecentOrderAdapter.setOrders(orderData.data);
                    hideLoadingProgressBar();
                    break;
                case ERROR:
                    CuteToast.ct(requireContext(), orderData.message, CuteToast.LENGTH_LONG, CuteToast.SAD).show();
                    break;
            }
        });

        loadingProgressBar = binding.loadingProgressBar;
        showLoadingProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
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

    private void showLoadingProgressBar() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingProgressBar() {
        loadingProgressBar.setVisibility(View.GONE);
    }
}

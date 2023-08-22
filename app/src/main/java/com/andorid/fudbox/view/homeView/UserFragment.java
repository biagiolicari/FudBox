package com.andorid.fudbox.view.homeView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.andorid.fudbox.MainActivity;
import com.andorid.fudbox.R;
import com.andorid.fudbox.viewmodel.loginViemModel.LoggedInViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.rejowan.cutetoast.CuteToast;

public class UserFragment extends Fragment {


    private TextView loggedInUserTextView;
    private Button logOutButton;
    private LoggedInViewModel loggedInViewModel;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loggedInUserTextView = view.findViewById(R.id.fragment_loggedin_user);
        logOutButton = view.findViewById(R.id.fragment_logout_button);

        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);
        loggedInViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    loggedInUserTextView.setText("Hi, " + firebaseUser.getEmail());
                    logOutButton.setEnabled(true);
                } else {
                    logOutButton.setEnabled(false);
                }
            }
        });
        logOutButton.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser() {
        loggedInViewModel.logOut();
        CuteToast.ct(getContext(), "User Logged Out", CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
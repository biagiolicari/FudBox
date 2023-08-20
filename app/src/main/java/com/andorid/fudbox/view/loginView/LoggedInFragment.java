package com.andorid.fudbox.view.loginView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.andorid.fudbox.R;
import com.andorid.fudbox.viewmodel.loginViemModel.LoggedInViewModel;
import com.andorid.fudbox.viewmodel.loginViemModel.LoginRegisterViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.rejowan.cutetoast.CuteToast;

public class LoggedInFragment extends Fragment {
    private TextView loggedInUserTextView;
    private Button logOutButton;

    private LoggedInViewModel loggedInViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);

        loggedInViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
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
        loggedInViewModel.getLoggedOutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if (loggedOut) {
                    //Toast.makeText(getContext(), "User Logged Out", Toast.LENGTH_SHORT).show();
                    CuteToast.ct(getContext(), "User Logged Out", CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
                    Navigation.findNavController(getView()).navigate(R.id.action_loggedInFragment_to_loginRegisterFragment);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loggedin, container, false);

        loggedInUserTextView = view.findViewById(R.id.fragment_loggedin_loggedInUser);
        logOutButton = view.findViewById(R.id.fragment_loggedin_logOut);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggedInViewModel.logOut();
            }
        });

        return view;
    }
    public LoggedInFragment() {
        // Required empty public constructor
    }
}



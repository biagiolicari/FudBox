package com.andorid.fudbox.view.loginView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.andorid.fudbox.R;
import com.andorid.fudbox.viewmodel.loginViemModel.LoginRegisterViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.rejowan.cutetoast.CuteToast;

public class LoginRegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Button loginButton;
    private Button signUpButton;
    private TextInputLayout emailInputText;
    private TextInputLayout passwordInputText;
    private LoginRegisterViewModel loginRegisterViewModel;

    public LoginRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
    }

    private void navigateToHomeActivity(){
        Navigation.findNavController(requireView()).navigate(R.id.action_to_home_activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("SONO IN START", "EGOLO");
        if(loginRegisterViewModel.getUserLiveData().getValue() == null){
            Log.i("USER", "NULLLLLLL");
        } else {
            Log.i("USERLIVE", loginRegisterViewModel.getUserLiveData().getValue().toString());
        }
            loginRegisterViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
                @Override
                public void onChanged(FirebaseUser firebaseUser) {
                    if (firebaseUser != null) {
                        navigateToHomeActivity();
                    }
                }
            });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loginregister, container, false);

        emailInputText = view.findViewById(R.id.fragment_loginregister_email);
        passwordInputText = view.findViewById(R.id.fragment_loginregister_password);
        loginButton = view.findViewById(R.id.fragment_loginregister_login);
        signUpButton = view.findViewById(R.id.fragment_loginregister_register);

        loginButton.setOnClickListener(viewLogin -> {
            String email = emailInputText.getEditText().getText().toString();
            String password = passwordInputText.getEditText().getText().toString();
            Log.i("EMAIL: ", emailInputText.getEditText().getText().toString());

            if (email.length() > 0 && password.length() > 0) {
                loginRegisterViewModel.login(email, password);
            } else {
                //Toast.makeText(getContext(), "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                CuteToast.ct(getContext(), "Email Address and Password Must Be Entered", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();

            }
        });

        signUpButton.setOnClickListener(viewSignUp -> {
            String email = emailInputText.getEditText().getText().toString();
            String password = passwordInputText.getEditText().getText().toString();

            if (email.length() > 0 && password.length() > 0) {
                loginRegisterViewModel.register(email, password);
            } else {
                //Toast.makeText(getContext(), "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                CuteToast.ct(getContext(), "Email Address and Password Must Be Entered", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();

            }
        });

        return view;
    }


}
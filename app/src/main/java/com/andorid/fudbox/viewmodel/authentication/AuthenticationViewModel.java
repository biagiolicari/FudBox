package com.andorid.fudbox.viewmodel.authentication;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.repository.authentication.AuthenticationRepo;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationViewModel extends ViewModel {
    private final AuthenticationRepo authRepository;
    private final LiveData<FirebaseUser> userLiveData;

    public AuthenticationViewModel() {
        authRepository = new AuthenticationRepo();
        userLiveData = authRepository.getUserLiveData();
    }

    public void login(String email, String password) {
        authRepository.login(email, password);
    }

    public void register(String email, String password) {
        authRepository.register(email, password);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}

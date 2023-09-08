package com.andorid.fudbox.viewmodel.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.repository.authentication.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final LiveData<FirebaseUser> userLiveData;

    public AuthViewModel() {
        authRepository = new AuthRepository();
        userLiveData = authRepository.getUserLiveData();
    }

    public void login(String email, String password) {
        authRepository.signIn(email, password);
    }

    public void register(String email, String password) {
        authRepository.signUP(email, password);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}

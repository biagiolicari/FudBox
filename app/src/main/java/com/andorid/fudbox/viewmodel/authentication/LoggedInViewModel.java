package com.andorid.fudbox.viewmodel.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.repository.authentication.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final LiveData<FirebaseUser> userLiveData;
    private final LiveData<Boolean> loggedOutLiveData;

    public LoggedInViewModel() {
        authRepository = new AuthRepository();
        userLiveData = authRepository.getUserLiveData();
        loggedOutLiveData = authRepository.getLoggedOutLiveData();
    }

    public void logOut() {
        authRepository.signOut();
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

}

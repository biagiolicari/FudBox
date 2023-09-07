package com.andorid.fudbox.viewmodel.authentication;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.repository.authentication.AuthenticationRepo;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInViewModel extends ViewModel {
    private final AuthenticationRepo authRepository;
    private final LiveData<FirebaseUser> userLiveData;
    private final LiveData<Boolean> loggedOutLiveData;

    public LoggedInViewModel() {
        authRepository = new AuthenticationRepo();
        userLiveData = authRepository.getUserLiveData();
        loggedOutLiveData = authRepository.getLoggedOutLiveData();
    }

    public void logOut() {
        authRepository.logOut();
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

}

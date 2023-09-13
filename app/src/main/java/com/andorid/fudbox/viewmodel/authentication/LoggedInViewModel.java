package com.andorid.fudbox.viewmodel.authentication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.repository.authentication.AuthRepository;
import com.andorid.fudbox.utils.Resource;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final LiveData<Resource<FirebaseUser>> userLiveData;
    private final LiveData<Boolean> loggedOutLiveData;

    public LoggedInViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        userLiveData = authRepository.getUserLiveData();
        loggedOutLiveData = authRepository.getLoggedOutLiveData();
    }

    public void logOut() {
        authRepository.signOut();
    }

    public LiveData<Resource<FirebaseUser>> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

}

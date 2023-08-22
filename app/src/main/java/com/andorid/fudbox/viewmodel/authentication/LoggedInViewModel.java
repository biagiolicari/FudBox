package com.andorid.fudbox.viewmodel.authentication;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.andorid.fudbox.repository.authentication.AuthenticationRepo;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInViewModel extends AndroidViewModel {
    private final AuthenticationRepo authRepository;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;

    public LoggedInViewModel(@NonNull Application application) {
        super(application);

        authRepository = new AuthenticationRepo(application);
        userLiveData = authRepository.getUserLiveData();
        loggedOutLiveData = authRepository.getLoggedOutLiveData();
    }

    public void logOut() {
        authRepository.logOut();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

}

package com.andorid.fudbox.viewmodel.authentication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.repository.authentication.AuthRepository;
import com.andorid.fudbox.utils.Resource;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final LiveData<Resource<FirebaseUser>> userLiveData;

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        userLiveData = authRepository.getUserLiveData();
    }

    public void login(String email, String password) {
        authRepository.signIn(email, password);
    }

    public void register(String email, String password) {
        authRepository.signUP(email, password);
    }

    public void resetPassword(String email){authRepository.resetPassword(email);}

    public LiveData<Resource<FirebaseUser>> getUserLiveData() {
        return userLiveData;
    }
}

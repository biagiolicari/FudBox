package com.andorid.fudbox.viewmodel.authentication;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.andorid.fudbox.repository.loginRepo.LoginRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {
    private final LoginRepository authRepository;
    private final MutableLiveData<FirebaseUser> userLiveData;

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
        authRepository = new LoginRepository(application);
        userLiveData = authRepository.getUserLiveData();
    }

    public void login(String email, String password) {
        authRepository.login(email, password);
    }

    public void register(String email, String password) {
        authRepository.register(email, password);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}

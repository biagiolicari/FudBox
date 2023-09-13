package com.andorid.fudbox.repository.authentication;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {

    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;
    private final MutableLiveData<String> errorMessageLiveData;
    private final Application application;

    public AuthRepository(Application application) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.application = application;
        this.errorMessageLiveData = new MutableLiveData<>();
        checkCurrentUser();
    }

    public MutableLiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            postUser(currentUser);
            postLoggedOut(false);
        }
    }

    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        checkCurrentUser();
                    } else {
                        handleAuthFailure(task.getException());
                    }
                });
    }

    public void signUP(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        checkCurrentUser();
                    } else {
                        handleAuthFailure(task.getException());
                    }
                });
    }

    public void signOut() {
        firebaseAuth.signOut();
        postLoggedOut(true);
    }

    private void handleAuthFailure(Exception exception) {
        this.errorMessageLiveData.setValue(exception.getMessage());
    }

    private void postUser(FirebaseUser user) {
        userLiveData.postValue(user);
    }

    private void postLoggedOut(boolean loggedOut) {
        loggedOutLiveData.postValue(loggedOut);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}

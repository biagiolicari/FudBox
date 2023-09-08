package com.andorid.fudbox.repository.authentication;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;

public class AuthRepository {

    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;
    private final Handler mainHandler;

    public AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.mainHandler = new Handler(Looper.getMainLooper());
        checkCurrentUser();
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
                .addOnCompleteListener(mainHandler::post, task -> {
                    if (task.isSuccessful()) {
                        checkCurrentUser();
                    } else {
                        handleAuthFailure(task.getException());
                    }
                });
    }

    public void signUP(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mainHandler::post, task -> {
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
        // Handle authentication failure, e.g., display error messages.
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

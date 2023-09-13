package com.andorid.fudbox.repository.authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.utils.Resource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {

    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<Resource<FirebaseUser>> userLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;
    private final Application application;

    public AuthRepository(Application application) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.application = application;
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            userLiveData.setValue(Resource.success(currentUser));
            setLoggedOut(false);
        }
    }

    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(application.getMainExecutor(), data -> {
                    userLiveData.setValue(Resource.success(firebaseAuth.getCurrentUser()));
                    loggedOutLiveData.setValue(false);
        })
                .addOnFailureListener(e -> {
                    userLiveData.setValue(Resource.error(e.getLocalizedMessage().toString(), null));
                    loggedOutLiveData.setValue(true);
                });
    }

    public void signUP(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).
                addOnSuccessListener(application.getMainExecutor(), data -> {
                    userLiveData.setValue(Resource.success(firebaseAuth.getCurrentUser()));
                    loggedOutLiveData.setValue(false);
                })
                .addOnFailureListener(e -> {
                    userLiveData.setValue(Resource.error(e.getLocalizedMessage().toString(), null));
                    loggedOutLiveData.setValue(true);
                });
    }

    public void signOut() {
        firebaseAuth.signOut();
        setLoggedOut(true);
    }


    private void setLoggedOut(boolean loggedOut) {
        loggedOutLiveData.postValue(loggedOut);
    }

    public LiveData<Resource<FirebaseUser>> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}

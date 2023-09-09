package com.andorid.fudbox.viewmodel.mainscreen.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.repository.mainscreen.user.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {
    private UserRepository repository;

    public LiveData<List<Cart>> getRecentOrdersLiveData() {
        return recentOrdersLiveData;
    }

    private LiveData<List<Cart>> recentOrdersLiveData;

    public UserViewModel(){

    }

    public void init(){
        repository = new UserRepository();
        if(repository.getRecentOrderLiveData().getValue() == null) {
            repository.fetchOrdersFromFirestore();
            recentOrdersLiveData = repository.getRecentOrderLiveData();
        }
    }


}

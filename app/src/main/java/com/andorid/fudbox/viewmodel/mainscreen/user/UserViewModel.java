package com.andorid.fudbox.viewmodel.mainscreen.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.model.Order;
import com.andorid.fudbox.repository.mainscreen.user.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {
    private UserRepository repository;
    private LiveData<List<Order>> recentOrdersLiveData;

    public UserViewModel() {

    }

    public LiveData<List<Order>> getRecentOrdersLiveData() {
        return recentOrdersLiveData;
    }

    public void init() {
        repository = new UserRepository();
        if (repository.getRecentOrderLiveData().getValue() == null) {
            repository.fetchOrdersFromFirestore();
            recentOrdersLiveData = repository.getRecentOrderLiveData();
        }
    }


}

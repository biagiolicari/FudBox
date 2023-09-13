package com.andorid.fudbox.viewmodel.mainscreen.home.menu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishType;
import com.andorid.fudbox.repository.mainscreen.home.menu.DishRepository;
import com.andorid.fudbox.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends AndroidViewModel {
    private DishRepository dishRepository;
    private LiveData<Resource<List<Dish>>> dishesLiveData;

    public MenuViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        dishRepository = new DishRepository();
        dishesLiveData = dishRepository.getDishListLiveData();
    }

    public LiveData<Resource<List<Dish>>> getDishes() {
        return dishesLiveData;
    }

    public LiveData<Resource<List<Dish>>> fetchDishes(){
        dishRepository.getAllDishes();
        return dishRepository.getDishListLiveData();
    }

}

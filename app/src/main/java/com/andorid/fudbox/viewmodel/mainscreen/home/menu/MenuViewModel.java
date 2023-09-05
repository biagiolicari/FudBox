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

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends AndroidViewModel {
    private DishRepository dishRepository;
    private LiveData<List<Dish>> dishesLiveData;
    private MutableLiveData<String> errorLiveData;

    public MenuViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void init() {
        dishRepository = new DishRepository();
        dishesLiveData = dishRepository.getAllDishes();
        errorLiveData = dishRepository.getErrorLiveData();
    }

    public LiveData<List<Dish>> getDishes() {
        return dishesLiveData;
    }

    public LiveData<List<Dish>> getDishesByType(DishType type) {
        return Transformations.map(dishesLiveData, dishes -> filterDishesByType(dishes, type));
    }

    private List<Dish> filterDishesByType(List<Dish> dishes, DishType type) {
        List<Dish> filteredDishes = new ArrayList<>();
        for (Dish dish : dishes) {
            if (dish.getType().equals(type)) {
                filteredDishes.add(dish);
            }
        }
        return filteredDishes;
    }

    public void clearErrorMessage(){
        this.errorLiveData.setValue(null);
    }
}
package com.andorid.fudbox.viewmodel.mainscreen.home.menu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishQuantity;
import com.andorid.fudbox.model.DishType;
import com.andorid.fudbox.repository.mainscreen.home.menu.DishQuantityRepository;
import com.andorid.fudbox.repository.mainscreen.home.menu.DishRepository;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends AndroidViewModel {
    private DishRepository dishRepository;
    private LiveData<List<Dish>> dishesLiveData;
    public MenuViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        dishRepository = new DishRepository();
        dishesLiveData = dishRepository.getAllDishes();
    }

    public LiveData<List<Dish>> getDishes(){
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
}

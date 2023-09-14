package com.andorid.fudbox.repository.mainscreen.home.menu;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishType;
import com.andorid.fudbox.utils.Resource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class DishRepository {
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String COLLECTION = "menu_appetizer";
    private final FirebaseFirestore firestore;
    private final MutableLiveData<Resource<List<Dish>>> dishListLiveData = new MutableLiveData<>();

    public DishRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<Resource<List<Dish>>> getDishListLiveData() {
        return dishListLiveData;
    }

    public void getAllDishes() {
        firestore.collection(COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Dish> dishList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        dishList.add(this.createDishFromDocumentSnapshot(documentSnapshot));
                    }
                    dishListLiveData.setValue(Resource.success(dishList));
                })
                .addOnFailureListener(e -> dishListLiveData.setValue(Resource.error(e.getMessage(), null)));
    }

    private Dish createDishFromDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        return new Dish.Builder()
                .setDescription(documentSnapshot.getString(DESCRIPTION))
                .setPrice(documentSnapshot.getDouble(PRICE))
                .setName(documentSnapshot.getString(NAME))
                .setType(DishType.valueOf(documentSnapshot.getString(TYPE)))
                .build();
    }

    private Dish getRandomDish(List<Dish> dishes) {
        Random random = new Random();
        int randomIndex = random.nextInt(dishes.size());
        return dishes.get(randomIndex);
    }

    private int generateRandomIndexNumber(List itemList){
        // Generate a random index between 1 and list.size()
        return new Random().nextInt(itemList.size());
    }

}

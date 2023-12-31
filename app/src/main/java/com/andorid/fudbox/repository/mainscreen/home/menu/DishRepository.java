package com.andorid.fudbox.repository.mainscreen.home.menu;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishType;
import com.andorid.fudbox.utils.Resource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
                    List<Dish> completeDishListFromFirestore = new ArrayList<>();

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        completeDishListFromFirestore.add(this.createDishFromDocumentSnapshot(documentSnapshot));
                    }

                    List<Dish> tmp = completeDishListFromFirestore.stream()
                            .collect(Collectors.groupingBy(Dish::getType))
                            .values()
                            .stream()
                            .flatMap(sublist -> pickRandom(sublist).stream())
                            .collect(Collectors.toList());

                    Collections.sort(tmp, Comparator.comparing(Dish::getType));
                    dishListLiveData.setValue(Resource.success(tmp, null));
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

    private static <E> List<E> pickRandom(List<E> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        Random random = new Random();
        int n = random.nextInt(list.size()) + 1;

        return random.ints(0, list.size()).distinct().limit(n)
                .mapToObj(list::get)
                .collect(Collectors.toList());
    }

}

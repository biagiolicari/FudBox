package com.andorid.fudbox.repository.mainscreen.home.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishType;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DishRepository {
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String COLLECTION = "menu_appetizer";
    private final FirebaseFirestore firestore;
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public DishRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<List<Dish>> getAllDishes() {
        MutableLiveData<List<Dish>> dishMutableLiveData = new MutableLiveData<>();
        firestore.collection(COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Dish> dishList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        dishList.add(this.createDishFromDocumentSnapshot(documentSnapshot));
                    }
                    dishMutableLiveData.setValue(dishList);
                })
                .addOnFailureListener(e -> errorLiveData.setValue(e.getMessage()));
        return dishMutableLiveData;
    }

    private Dish createDishFromDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        return new Dish.Builder()
                .setDescription(documentSnapshot.getString(DESCRIPTION))
                .setPrice(documentSnapshot.getDouble(PRICE))
                .setName(documentSnapshot.getString(NAME))
                .setType(DishType.valueOf(documentSnapshot.getString(TYPE)))
                .build();
    }

}

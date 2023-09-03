package com.andorid.fudbox.repository.mainscreen.home.menu;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Dish;
import com.andorid.fudbox.model.DishType;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DishRepository {
    private static final String DESCRIPTION  = "description";
    private static final String PRICE  = "price";
    private static final String NAME  = "name";
    private static final String TYPE  = "type";
    private FirebaseFirestore firestore;
    private static final String COLLECTION  = "menu_appetizer";
    public DishRepository(){
        firestore = FirebaseFirestore.getInstance();
    }

    public LiveData<List<Dish>> getAllDishes(){
        MutableLiveData<List<Dish>> dishMutableLiveData = new MutableLiveData<>();
        firestore.collection(COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Dish> dishList = new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Log.wtf("DISH", documentSnapshot.getData().toString());
                        dishList.add(this.createDishFromDocumentSnapshot(documentSnapshot));
                        Log.wtf("FORMATTED", this.createDishFromDocumentSnapshot(documentSnapshot).toString());
                    }
                    dishMutableLiveData.setValue(dishList);
                })
                .addOnFailureListener(e -> Log.e("FIRESTORE", "Failure to fetch data"));
        return dishMutableLiveData;
    }

    private Dish createDishFromDocumentSnapshot(DocumentSnapshot documentSnapshot){
        return new Dish.Builder()
                .setDescription(documentSnapshot.getString(DESCRIPTION))
                .setPrice(documentSnapshot.getDouble(PRICE))
                .setName(documentSnapshot.getString(NAME))
                .setType(DishType.valueOf(documentSnapshot.getString(TYPE)))
                .build();
    }


}

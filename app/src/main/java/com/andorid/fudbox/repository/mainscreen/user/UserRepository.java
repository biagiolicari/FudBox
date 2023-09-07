package com.andorid.fudbox.repository.mainscreen.user;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.andorid.fudbox.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private FirebaseFirestore firestore;
    private MutableLiveData<List<Order>> ordersLiveData;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;

    public UserRepository(){

    }

    public void init(){
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        ordersLiveData = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();

        if(firebaseAuth.getCurrentUser() != null){
            userLiveData.setValue(firebaseAuth.getCurrentUser());
        }
    }

    public void fetchOrdersFromFirestore() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            firestore.collection("orders")
                    .whereEqualTo("userUID", uid)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Order> orders = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Convert Firestore document to your Order object
                            Order order = document.toObject(Order.class);
                            orders.add(order);
                        }

                        // Now, 'orders' contains the list of orders for the current user.
                    })
                    .addOnFailureListener(e -> {
                        Log.wtf("FIREBASE", e.getMessage());
                    });
        }
    }

}

package com.andorid.fudbox.viewmodel.mainscreen.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;


public class SharedLatLng extends ViewModel {
    private MutableLiveData<LatLng> latLngMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<LatLng> getLatLngMutableLiveData() {
        return latLngMutableLiveData;
    }

    public void setLatLngMutableLiveData(LatLng latLng) {
        this.latLngMutableLiveData.setValue(latLng);
    }
}

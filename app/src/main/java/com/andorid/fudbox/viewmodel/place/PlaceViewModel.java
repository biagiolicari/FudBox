package com.andorid.fudbox.viewmodel.place;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.type.LatLng;

public class PlaceViewModel extends ViewModel {

    private MutableLiveData<String> addressMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<LatLng> latLngMutableLiveData = new MutableLiveData<>();

    public PlaceViewModel() {
    }

    public MutableLiveData<String> getAddressMutableLiveData() {
        return addressMutableLiveData;
    }

    public void setAddressMutableLiveData(MutableLiveData<String> addressMutableLiveData) {
        this.addressMutableLiveData = addressMutableLiveData;
    }

    public MutableLiveData<LatLng> getLatLngMutableLiveData() {
        return latLngMutableLiveData;
    }

    public void setLatLngMutableLiveData(MutableLiveData<LatLng> latLngMutableLiveData) {
        this.latLngMutableLiveData = latLngMutableLiveData;
    }

    public LiveData<String> getAddress() {
        return addressMutableLiveData;
    }

    public void setAddress(String newAddress) {
        addressMutableLiveData.setValue(newAddress);
    }
}

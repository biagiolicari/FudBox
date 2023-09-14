package com.andorid.fudbox.view.place;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.ActivityPlaceBinding;
import com.andorid.fudbox.view.mainscreen.MainScreenActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.rejowan.cutetoast.CuteToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Activity for using Place Autocomplete to assist filling out an address form.
 */
@SuppressWarnings("FieldCanBeLocal")
public class PlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "ADDRESS_AUTOCOMPLETE";
    private static final String MAP_FRAGMENT_TAG = "MAP";
    private static final String COUNTRY = "IT";
    private LatLng coordinates;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Marker marker;
    private PlacesClient placesClient;
    private View mapPanel;
    private ActivityPlaceBinding binding;
    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        // Write a method to read the address components from the Place
                        // and populate the form with the address components
                        Log.d(TAG, place.getAddressComponents().toString());
                        getAddressFromAutocomplete(place);
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
            });
    View.OnClickListener startAutocompleteIntentListener = view -> {
        view.setOnClickListener(null);
        startAutocompleteIntent();
    };
    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (Boolean.TRUE.equals(isGranted.get(Manifest.permission.ACCESS_FINE_LOCATION))
                        && Boolean.TRUE.equals(isGranted.get(ACCESS_WIFI_STATE))) {
                    findCurrentPlaceWithPermissions();
                } else {
                    // Fallback behavior if user denies permission
                    Log.d(TAG, "User denied permission");
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        binding.autocompleteAddress.setOnClickListener(startAutocompleteIntentListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPlacesClient();
        setupAutocompleteAddress();
        setupMyLastKnownLocationButton();
        setupSetAddressButton();
    }

    private void initView() {
        Places.initialize(getApplicationContext(), getString(R.string.google_key));
        binding = ActivityPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initPlacesClient() {
        placesClient = Places.createClient(this);
    }

    private void setupAutocompleteAddress() {
        binding.autocompleteAddress.setOnClickListener(startAutocompleteIntentListener);
    }

    private void setupMyLastKnownLocationButton() {
        binding.geolocalizeButton.setOnClickListener(l -> checkLocationPermissions());
    }

    private void setupSetAddressButton() {
        binding.autocompleteSaveButton.setOnClickListener(v -> {
            if(coordinates != null) {
                setAddressAndStartActivity();
            }else {
                CuteToast.ct(this, getString(R.string.no_address), CuteToast.LENGTH_SHORT, CuteToast.ERROR).show();
        }

        });
    }

    private void startAutocompleteIntent() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG, Place.Field.VIEWPORT);

        // Build the autocomplete intent with field, country, and type filters applied
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountries(Collections.singletonList(COUNTRY))
                .setTypesFilter(new ArrayList<String>() {{
                    add(TypeFilter.ADDRESS.toString().toLowerCase());
                }})
                .build(this);
        startAutocomplete.launch(intent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        try {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15f));
        marker = map.addMarker(new MarkerOptions().position(coordinates));
    }

    private void getAddressFromAutocomplete(Place place) {
        binding.autocompleteAddress.clearFocus();
        AddressComponents components = place.getAddressComponents();
        StringBuilder addressBuilder = new StringBuilder();

        if (components != null) {
            for (AddressComponent component : components.asList()) {
                String type = component.getTypes().get(0);
                switch (type) {
                    case "route":
                        addressBuilder.append(component.getName()).append(", ");
                        break;

                    case "locality":
                        addressBuilder.append(component.getName()).append(" ");
                        break;

                    case "postal_code_suffix": {
                        addressBuilder.append(component.getName()).append(". ");
                        break;
                    }

                    case "administrative_area_level_1": {
                        addressBuilder.append(component.getName()).append(", ");
                        break;
                    }

                    case "country":
                        addressBuilder.append(component.getName());
                        break;
                }
            }
        }
        // Add a map for visual confirmation of the address
        showMap(place.getLatLng());
        binding.autocompleteAddress.setText(addressBuilder);
    }

    private void showMap(LatLng latLng) {
        coordinates = latLng;

        // It isn't possible to set a fragment's id programmatically so we set a tag instead and
        // search for it using that.
        mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);

        // We only create a fragment if it doesn't already exist.
        if (mapFragment == null) {
            mapPanel = ((ViewStub) findViewById(R.id.stub_map)).inflate();
            GoogleMapOptions mapOptions = new GoogleMapOptions();
            mapOptions.mapToolbarEnabled(false);

            // To programmatically add the map, we first create a SupportMapFragment.
            mapFragment = SupportMapFragment.newInstance(mapOptions);

            // Then we add it using a FragmentTransaction.
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.confirmation_map, mapFragment, MAP_FRAGMENT_TAG)
                    .commit();
            mapFragment.getMapAsync(this);
        } else {
            updateMap(coordinates);
        }

        if (binding.cardView.getVisibility() == View.GONE)
            binding.cardView.setVisibility(View.VISIBLE);
    }

    private void updateMap(LatLng latLng) {
        marker.setPosition(latLng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
        if (mapPanel.getVisibility() == View.GONE) {
            mapPanel.setVisibility(View.VISIBLE);
        }
    }

    private void setAddressAndStartActivity() {
        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.putExtra("lat", coordinates.latitude);
        intent.putExtra("lng", coordinates.longitude);
        CuteToast.ct(this, getString(R.string.set_address), CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            findCurrentPlaceWithPermissions();
        } else {
            requestPermissionLauncher.launch(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE});
        }
    }

    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    private void findCurrentPlaceWithPermissions() {
        setLoading(true);
        FindCurrentPlaceRequest currentPlaceRequest =
                FindCurrentPlaceRequest.newInstance(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID));

        Task<FindCurrentPlaceResponse> currentPlaceTask =
                placesClient.findCurrentPlace(currentPlaceRequest);

        currentPlaceTask.addOnSuccessListener(
                (response) -> {
                    Place place = response.getPlaceLikelihoods().get(0).getPlace();
                    // Access the latitude and longitude
                    double latitude = place.getLatLng().latitude;
                    double longitude = place.getLatLng().longitude;
                    Log.w("LAT LONG", latitude + "    " + longitude);
                    coordinates = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    showMap(coordinates);
                });

        currentPlaceTask.addOnFailureListener(
                (exception) -> {
                    CuteToast.ct(getApplicationContext(), exception.getMessage(), CuteToast.LENGTH_LONG, CuteToast.ERROR).show();
                });

        currentPlaceTask.addOnCompleteListener(task -> setLoading(false));
    }

    private void setLoading(boolean loading) {
        binding.loading.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }
}



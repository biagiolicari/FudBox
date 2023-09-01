package com.andorid.fudbox.view.place;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.ActivityPlaceBinding;
import com.andorid.fudbox.view.mainscreen.MainScreenActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
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
    private static final String COUNTRY="IT";
    private LatLng coordinates;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Marker marker;
    private PlacesClient placesClient;
    private View mapPanel;
    private String address;
    private ActivityPlaceBinding binding;
    // [START maps_solutions_android_autocomplete_define]
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
    // [END maps_solutions_android_autocomplete_define]
    View.OnClickListener startAutocompleteIntentListener = view -> {
        view.setOnClickListener(null);
        startAutocompleteIntent();
    };    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getGeoLocalizationData();
                } else {
                    // Fallback behavior if user denies permission
                    Log.d(TAG, "User denied permission");
                    CuteToast.ct(this, "User denied permission", CuteToast.LENGTH_SHORT, CuteToast.CONFUSE, true).show();

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
        setupGeolocalizationButton();
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

    private void setupGeolocalizationButton() {
        Button geolocalizationButton = findViewById(R.id.geolocalize_button);
        geolocalizationButton.setOnClickListener(l -> getGeoLocalizationData());
    }

    private void setupSetAddressButton() {
        Button saveButton = findViewById(R.id.autocomplete_save_button);
        saveButton.setOnClickListener(v -> setAddressAndStartActivity());
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
            // Customise the styling of the base map using a JSON object defined
            // in a string resource.
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
        this.address = addressBuilder.toString();
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
        //intent.putExtra("address", this.address);
        intent.putExtra("lat", Double.toString(coordinates.latitude));
        intent.putExtra("lng", Double.toString(coordinates.longitude));
        CuteToast.ct(this, "Address successfully set", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
        // Start the new activity
        startActivity(intent);

    }

    private Boolean checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                    ACCESS_FINE_LOCATION);
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    @SuppressLint("MissingPermission")
    private void getGeoLocalizationData() {
        if (checkLocationPermissions()) {
            FusedLocationProviderClient fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location == null) {
                            return;
                        }
                        coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                        showMap(coordinates);

                    });
        }

    }


}


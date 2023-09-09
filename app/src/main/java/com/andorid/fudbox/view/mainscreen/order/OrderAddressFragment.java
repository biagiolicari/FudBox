package com.andorid.fudbox.view.mainscreen.order;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.andorid.fudbox.R;
import com.andorid.fudbox.databinding.FragmentOrderAddressBinding;
import com.andorid.fudbox.model.Cart;
import com.andorid.fudbox.viewmodel.mainscreen.order.CartViewModel;
import com.andorid.fudbox.viewmodel.mainscreen.order.OrderViewModel;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderAddressFragment extends Fragment {
    private static final String TAG = "ADDRESS_AUTOCOMPLETE";
    private FragmentOrderAddressBinding binding;
    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        Log.d(TAG, "Place: " + place.getAddressComponents());
                        fillInAddress(place);
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
            });
    View.OnClickListener startAutocompleteIntentListener = view -> startAutocompleteIntent();
    private OrderViewModel orderViewModel;
    private CartViewModel cartViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        orderViewModel.init();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderAddressBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        // Attach an Autocomplete intent to the Address 1 EditText field
        binding.autocompleteAddress1.setOnClickListener(startAutocompleteIntentListener);
        // Submit
        Button saveButton = binding.autocompleteSaveButton;
        saveButton.setEnabled(Boolean.FALSE);
        saveButton.setOnClickListener(v -> saveForm());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBack();
            }
        });
    }

    private void startAutocompleteIntent() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG, Place.Field.VIEWPORT);

        // Build the autocomplete intent with field, country, and type filters applied
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountries(Collections.singletonList("IT"))
                .setTypesFilter(new ArrayList<String>() {{
                    add(TypeFilter.ADDRESS.toString().toLowerCase());
                }})
                .build(requireContext());
        startAutocomplete.launch(intent);
    }

    private void fillInAddress(Place place) {
        AddressComponents components = place.getAddressComponents();
        StringBuilder address1 = new StringBuilder();
        StringBuilder postcode = new StringBuilder();

        // Get each component of the address from the place details,
        // and then fill-in the corresponding field on the form.
        // Possible AddressComponent types are documented at https://goo.gle/32SJPM1
        if (components != null) {
            for (AddressComponent component : components.asList()) {
                String type = component.getTypes().get(0);
                switch (type) {
                    case "street_number": {
                        binding.autocompleteAddress2.setText(component.getName());
                        break;
                    }

                    case "route": {
                        address1.append(component.getShortName());
                        break;
                    }

                    case "postal_code": {
                        postcode.insert(0, component.getName());
                        break;
                    }

                    case "postal_code_suffix": {
                        postcode.append("-").append(component.getName());
                        break;
                    }

                    case "locality":
                        binding.autocompleteCity.setText(component.getName());
                        break;

                    case "administrative_area_level_1": {
                        binding.autocompleteState.setText(component.getShortName());
                        break;
                    }

                    case "country":
                        binding.autocompleteCountry.setText(component.getName());
                        break;
                }
            }
        }

        binding.autocompleteAddress1.setText(address1.toString());
        binding.autocompletePostal.setText(postcode.toString());

        if (binding.autocompleteAddress2.getText().toString().isEmpty()) {
            binding.autocompleteAddress2Layout.requestFocus();
            binding.autocompleteAddress2Layout.setError(getString(R.string.empty_field));
            binding.autocompleteAddress2Layout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            binding.autocompleteSaveButton.setEnabled(Boolean.FALSE);
        } else {
            binding.autocompleteAddress2Layout.setError(null);
            binding.autocompleteSaveButton.setEnabled(Boolean.TRUE);
        }
    }

    private void saveForm() {
        String addressBuilder = binding.autocompleteAddress1.getText().toString() +
                binding.autocompleteAddress2.getText().toString();
        Cart cart = cartViewModel.getOrderLiveData().getValue();
        orderViewModel.buildOrder(cart, addressBuilder);
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_order_payment);
    }

    public void navigateBack() {
        // Navigate back to the previous fragment
        NavHostFragment.findNavController(this).popBackStack();
    }
}

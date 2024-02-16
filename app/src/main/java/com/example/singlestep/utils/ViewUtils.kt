package com.example.singlestep.utils

import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

fun setupPlacesAutocompleteFragment(
    fragment: AutocompleteSupportFragment,
    hint: String,
    placeSelectedListener: (Place) -> Unit,
    clearButtonClickedListener: () -> Unit
) {
    with(fragment) {
        setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        setTypesFilter(listOf(PlaceTypes.CITIES))
        setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                placeSelectedListener(place)
            }

            override fun onError(status: Status) {
                Log.i("PlacesAPI", "An error occurred: $status")
            }
        })
        getAutocompleteFragmentClearButton(this)?.setOnClickListener {
            this.setText("")
            it.visibility = View.GONE
            clearButtonClickedListener()
        }
        getAutocompleteFragmentEditText(this).hint = hint
    }
}

fun getAutocompleteFragmentClearButton(fragment: AutocompleteSupportFragment): View? {
    return fragment.view?.findViewById<View>(com.google.android.libraries.places.R.id.places_autocomplete_clear_button)
}

fun getAutocompleteFragmentEditText(fragment: AutocompleteSupportFragment): EditText {
    return fragment.view?.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input) as EditText
}
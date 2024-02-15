package com.example.singlestep.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.singlestep.R
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

fun setupPlacesAutocompleteFragment(
    context: Context,
    fragment: AutocompleteSupportFragment,
    prompt: String,
    icon: Int,
    background: Int,
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
                Log.i("TAG", "An error occurred: $status")
            }
        })
        view?.background = ContextCompat.getDrawable(context, background)
        getAutocompleteFragmentSearchButton(this)?.visibility = View.GONE
        getAutocompleteFragmentClearButton(this)?.setOnClickListener {
            this.setText("")
            it.visibility = View.GONE
            clearButtonClickedListener()
        }

        val autocompleteEditText = getAutocompleteFragmentEditText(this)
        setupPlacesAutocompleteEditText(context, autocompleteEditText, prompt, icon)
    }
}

fun setupPlacesAutocompleteEditText(
    context: Context, editText: EditText, prompt: String, icon: Int
) {
    with(editText) {
        val dps = resources.displayMetrics.density
        val padding = (dps * 16).toInt()
        hint = prompt
        textSize = 16f
        compoundDrawablePadding = (dps * 8).toInt()
        setHintTextColor(ContextCompat.getColor(context, R.color.faded_grey))
        setPadding(padding, padding, padding, padding)
        setCompoundDrawablesRelativeWithIntrinsicBounds(icon, 0, 0, 0)
    }
}

fun getAutocompleteFragmentSearchButton(fragment: AutocompleteSupportFragment): View? {
    return fragment.view?.findViewById<View>(com.google.android.libraries.places.R.id.places_autocomplete_search_button)
}

fun getAutocompleteFragmentClearButton(fragment: AutocompleteSupportFragment): View? {
    return fragment.view?.findViewById<View>(com.google.android.libraries.places.R.id.places_autocomplete_clear_button)
}

fun getAutocompleteFragmentEditText(fragment: AutocompleteSupportFragment): EditText {
    return fragment.view?.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input) as EditText
}
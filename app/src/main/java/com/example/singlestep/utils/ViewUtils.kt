package com.example.singlestep.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import com.example.singlestep.R
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomnavigation.BottomNavigationView

fun setupPlacesAutocompleteFragment(
    fragment: AutocompleteSupportFragment,
    hint: String,
    placeSelectedListener: (Place) -> Unit,
    clearButtonClickedListener: () -> Unit
) {
    with(fragment) {
        setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
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
        this.setHint(hint)
    }
}

fun getAutocompleteFragmentClearButton(fragment: AutocompleteSupportFragment): View? {
    return fragment.view?.findViewById<View>(com.google.android.libraries.places.R.id.places_autocomplete_clear_button)
}

fun startGoogleMapsNavigation(context: Context, lat: Double, long: Double) {
    val navigationIntentUri = Uri.parse("google.navigation:q=$lat,$long")
    val mapIntent = Intent(Intent.ACTION_VIEW, navigationIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    context.startActivity(mapIntent)
}

fun startWebView(context: Context, link: String?) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setData(Uri.parse(link))
    context.startActivity(intent)
}

fun hideBottomNavigationBar(activity: Activity?) {
    activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility =
        View.GONE
}

fun showBottomNavigationBar(activity: Activity?) {
    activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility =
        View.VISIBLE
}
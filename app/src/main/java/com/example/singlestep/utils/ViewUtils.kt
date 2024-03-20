package com.example.singlestep.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
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
        setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.PHOTO_METADATAS
            )
        )
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
    return fragment.view?.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_clear_button)
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

fun convertToTitleCase(input: String): String {
    return input.lowercase().split(" ")
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}

fun getRemoveTripOnClickListener(
    context: Context,
    onPositiveButtonClicked: () -> Unit
): View.OnClickListener {
    return View.OnClickListener {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Remove from My Trips")
            .setMessage("Are you sure you want to continue? This action cannot be undone.")

        builder.setPositiveButton("Yes") { dialog, _ ->
            onPositiveButtonClicked()
            Toast.makeText(
                context,
                "Successfully removed from My Trips",
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
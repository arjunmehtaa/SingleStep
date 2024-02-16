package com.example.singlestep.ui.common.autocomplete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.singlestep.R
import com.google.android.libraries.places.widget.AutocompleteSupportFragment

class SourceAutocompleteFragment : AutocompleteSupportFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_autocomplete_source, container, false)
    }
}
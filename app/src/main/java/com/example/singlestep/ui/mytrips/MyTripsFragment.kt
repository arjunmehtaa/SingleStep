package com.example.singlestep.ui.mytrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentMyTripsBinding

class MyTripsFragment : Fragment() {

    private val viewModel: MyTripsViewModel by viewModels()
    private lateinit var binding: FragmentMyTripsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTripsBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        with(binding) {
            subtitleTextView.text = getString(R.string.hello_world)
        }
    }
}
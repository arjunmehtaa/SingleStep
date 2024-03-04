package com.example.singlestep.ui.mytrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentMyTripsBinding
import com.example.singlestep.ui.common.adapters.SavedTripAdapter

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
            var tripAdapter = SavedTripAdapter()
            myTripsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            myTripsRecyclerView.adapter = tripAdapter
            var placeholders = listOf<String>("1", "2", "3", "4", "5", "6", "7")
            tripAdapter.submitList(placeholders)
        }
    }
}
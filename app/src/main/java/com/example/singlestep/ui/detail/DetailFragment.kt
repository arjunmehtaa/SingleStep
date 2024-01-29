package com.example.singlestep.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentDetailBinding
import com.example.singlestep.model.Item

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        arguments?.let { bundle ->
            setupViews(DetailFragmentArgs.fromBundle(bundle).item)
        }
        return binding.root
    }

    private fun setupViews(item: Item) {
        with(binding) {
            subtitle.text = getString(R.string.item_details, item.id)
            backButton.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }
}
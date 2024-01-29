package com.example.singlestep.ui.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlestep.databinding.FragmentResultBinding
import com.example.singlestep.model.Item
import com.example.singlestep.ui.common.ItemAdapter
import com.example.singlestep.utils.Result

class ResultFragment : Fragment() {

    private val viewModel: ResultViewModel by viewModels()
    private lateinit var binding: FragmentResultBinding
    private lateinit var adapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        setupObservers()
        setupViews()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.itemList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> onLoadingSuccess(result.value)
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            adapter = ItemAdapter {
                val action = ResultFragmentDirections.actionResultFragmentToDetailFragment(it)
                findNavController(requireView()).navigate(action)
            }
            resultRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            resultRecyclerView.adapter = adapter
        }
    }

    private fun onLoading() {
        Log.i("Loading", "onLoading()")
    }

    private fun onLoadingFailure() {
        Log.i("Loading Failed", "onLoadingFailure()")
    }

    private fun onLoadingSuccess(items: List<Item>) {
        adapter.submitList(items)
    }
}
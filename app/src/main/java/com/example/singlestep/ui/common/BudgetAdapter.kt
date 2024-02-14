package com.example.singlestep.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.singlestep.R
import com.example.singlestep.databinding.ItemBudgetBinding
import com.example.singlestep.model.Budget
import java.text.NumberFormat

class BudgetAdapter : ListAdapter<Budget, BudgetAdapter.BudgetViewHolder>(REPO_COMPARATOR) {

    inner class BudgetViewHolder(private val binding: ItemBudgetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(budget: Budget) {
            with(binding) {
                budgetTextView.text = root.context.getString(
                    R.string.budget_text,
                    NumberFormat.getIntegerInstance().format(budget.value)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Budget>() {
            override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean =
                oldItem.value == newItem.value

            override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean =
                oldItem == newItem
        }
    }
}
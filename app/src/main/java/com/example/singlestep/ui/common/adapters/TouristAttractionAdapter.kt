package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.domain.resources.Activity
import com.example.singlestep.R
import com.example.singlestep.databinding.ItemTouristAttractionBinding
import com.example.singlestep.utils.startGoogleMapsNavigation
import com.example.singlestep.utils.startWebView
import org.jsoup.Jsoup

class TouristAttractionAdapter(
    val viewLessClickListener: (Int) -> Unit
) : ListAdapter<Activity, TouristAttractionAdapter.TouristAttractionViewHolder>(REPO_COMPARATOR) {

    private lateinit var touristAttractionImageAdapter: TouristAttractionImageAdapter

    inner class TouristAttractionViewHolder(private val binding: ItemTouristAttractionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var expanded = false

        fun bind(activity: Activity) {
            with(binding) {

                nameTextView.text = activity.name

                if (!activity.rating.isNullOrBlank()) {
                    ratingTextView.text = activity.rating
                    ratingTextView.visibility = View.VISIBLE
                } else {
                    ratingTextView.visibility = View.GONE
                }

                if (!activity.description.isNullOrBlank()) {
                    descriptionTextView.text = Jsoup.parse(activity.description?.trim()).wholeText()
                    descriptionTextView.visibility = View.VISIBLE
                } else {
                    descriptionTextView.visibility = View.GONE
                }

                descriptionTextView.viewTreeObserver.addOnGlobalLayoutListener(object :
                    OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (descriptionTextView.lineCount > 4) {
                            viewMoreTextView.text = root.context.getString(R.string.view_more)
                            viewMoreTextView.visibility = View.VISIBLE
                            descriptionTextView.maxLines = 4
                            expanded = false
                        } else {
                            viewMoreTextView.visibility = View.GONE
                        }
                        descriptionTextView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })

                viewMoreTextView.setOnClickListener {
                    if (expanded) {
                        descriptionTextView.maxLines = 4
                        viewMoreTextView.text = root.context.getString(R.string.view_more)
                        viewLessClickListener(adapterPosition)
                        expanded = false
                    } else {
                        descriptionTextView.maxLines = 100
                        viewMoreTextView.text = root.context.getString(R.string.view_less)
                        expanded = true
                    }
                }

                if (!activity.pictures.isNullOrEmpty()) {
                    photosTextView.visibility = View.VISIBLE
                    touristAttractionImageAdapter = TouristAttractionImageAdapter()
                    photosRecyclerView.apply {
                        visibility = View.VISIBLE
                        layoutManager = LinearLayoutManager(
                            binding.root.context, LinearLayoutManager.HORIZONTAL, false
                        )
                        adapter = touristAttractionImageAdapter
                    }
                    touristAttractionImageAdapter.submitList(activity.pictures)
                } else {
                    photosTextView.visibility = View.GONE
                    photosRecyclerView.visibility = View.GONE
                }

                if (activity.geoCode != null) {
                    getDirectionsButton.visibility = View.VISIBLE
                    getDirectionsButton.setOnClickListener {
                        startGoogleMapsNavigation(
                            root.context, activity.geoCode!!.latitude, activity.geoCode!!.longitude
                        )
                    }
                } else {
                    getDirectionsButton.visibility = View.GONE
                }

                if (!activity.bookingLink.isNullOrBlank()) {
                    bookButton.visibility = View.VISIBLE
                    bookButton.setOnClickListener {
                        startWebView(root.context, activity.bookingLink)
                    }
                } else {
                    bookButton.visibility = View.GONE
                }

                if (adapterPosition == itemCount - 1) {
                    divider.visibility = View.GONE
                } else {
                    divider.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TouristAttractionViewHolder {
        val binding =
            ItemTouristAttractionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TouristAttractionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TouristAttractionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Activity>() {
            override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean =
                oldItem == newItem
        }
    }
}
package com.chordcapo.helper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chordcapo.helper.data.CapoResult
import com.chordcapo.helper.databinding.ItemCapoPositionBinding

class CapoPositionAdapter(
    private val onPositionClick: (CapoResult) -> Unit
) : ListAdapter<CapoResult, CapoPositionAdapter.PositionViewHolder>(PositionDiffCallback()) {

    private var bestBarreCount: Int = Int.MAX_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val binding = ItemCapoPositionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PositionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<CapoResult>?) {
        bestBarreCount = list?.minOfOrNull { it.barreCount } ?: Int.MAX_VALUE
        super.submitList(list)
    }

    inner class PositionViewHolder(
        private val binding: ItemCapoPositionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: CapoResult) {
            binding.tvCapoFret.text = result.capoFret.toString()
            binding.tvBarreCount.text = "${result.barreCount} of ${result.mappings.size}"
            
            // Show best indicator for optimal positions
            binding.tvBestIndicator.visibility = if (result.barreCount == bestBarreCount) {
                View.VISIBLE
            } else {
                View.GONE
            }
            
            binding.root.setOnClickListener {
                onPositionClick(result)
            }
        }
    }

    private class PositionDiffCallback : DiffUtil.ItemCallback<CapoResult>() {
        override fun areItemsTheSame(oldItem: CapoResult, newItem: CapoResult): Boolean {
            return oldItem.capoFret == newItem.capoFret
        }

        override fun areContentsTheSame(oldItem: CapoResult, newItem: CapoResult): Boolean {
            return oldItem == newItem
        }
    }
}
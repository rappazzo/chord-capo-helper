package com.chordcapo.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chordcapo.helper.R
import com.chordcapo.helper.data.ChordMapping
import com.chordcapo.helper.databinding.ItemChordMappingBinding

class ChordMappingAdapter : ListAdapter<ChordMapping, ChordMappingAdapter.MappingViewHolder>(MappingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MappingViewHolder {
        val binding = ItemChordMappingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MappingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MappingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MappingViewHolder(
        private val binding: ItemChordMappingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mapping: ChordMapping) {
            binding.tvOriginalChord.text = formatChordName(mapping.originalChord)
            
            val shapeText = if (mapping.isBarreChord) {
                "${mapping.rootPositionShape} Shape (Barre) ⚠️"
            } else {
                "${mapping.rootPositionShape} Shape (Open)"
            }
            
            binding.tvMappedChord.text = shapeText
            
            // Color code barre chords
            val textColor = if (mapping.isBarreChord) {
                ContextCompat.getColor(binding.root.context, R.color.barre_warning)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.text_primary)
            }
            binding.tvMappedChord.setTextColor(textColor)
        }

        private fun formatChordName(chord: String): String {
            return when {
                chord.endsWith("m7") -> "${chord.dropLast(2)} Minor 7th"
                chord.endsWith("maj7") -> "${chord.dropLast(4)} Major 7th"
                chord.endsWith("7") -> "${chord.dropLast(1)} 7th"
                chord.endsWith("m") -> "${chord.dropLast(1)} Minor"
                else -> "$chord Major"
            }
        }
    }

    private class MappingDiffCallback : DiffUtil.ItemCallback<ChordMapping>() {
        override fun areItemsTheSame(oldItem: ChordMapping, newItem: ChordMapping): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChordMapping, newItem: ChordMapping): Boolean {
            return oldItem == newItem
        }
    }
}
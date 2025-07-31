package com.chordcapo.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chordcapo.helper.databinding.ItemChordBinding

class ChordListAdapter(
    private val onRemoveChord: (String) -> Unit
) : ListAdapter<String, ChordListAdapter.ChordViewHolder>(ChordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChordViewHolder {
        val binding = ItemChordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChordViewHolder(
        private val binding: ItemChordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chord: String) {
            binding.tvChordName.text = formatChordName(chord)
            binding.btnRemoveChord.setOnClickListener {
                onRemoveChord(chord)
            }
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

    private class ChordDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
package com.chordcapo.helper

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chordcapo.helper.adapter.ChordListAdapter
import com.chordcapo.helper.databinding.ActivityMainBinding
import com.chordcapo.helper.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var chordAdapter: ChordListAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        chordAdapter = ChordListAdapter { chord ->
            viewModel.removeChord(chord)
        }
        
        binding.rvChordList.apply {
            adapter = chordAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
    
    private fun setupClickListeners() {
        binding.btnAddChord.setOnClickListener {
            val input = binding.etChordInput.text.toString().trim()
            if (input.isNotEmpty()) {
                viewModel.addChords(input)
                binding.etChordInput.text.clear()
            }
        }
        
        binding.btnClearAll.setOnClickListener {
            viewModel.clearAllChords()
        }
        
        binding.btnFindOptimalCapo.setOnClickListener {
            viewModel.findOptimalCapo()
        }
    }
    
    private fun observeViewModel() {
        viewModel.chordList.observe(this) { chords ->
            chordAdapter.submitList(chords)
            binding.btnFindOptimalCapo.isEnabled = chords.isNotEmpty()
        }
        
        viewModel.capoResults.observe(this) { results ->
            if (results.isNotEmpty()) {
                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra("CHORD_LIST", ArrayList(viewModel.chordList.value ?: emptyList()))
                startActivity(intent)
            }
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnFindOptimalCapo.isEnabled = !isLoading && !viewModel.chordList.value.isNullOrEmpty()
            binding.btnFindOptimalCapo.text = if (isLoading) "Calculating..." else getString(R.string.find_optimal_capo)
        }
        
        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
}
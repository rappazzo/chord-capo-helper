package com.chordcapo.helper

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chordcapo.helper.adapter.ChordMappingAdapter
import com.chordcapo.helper.databinding.ActivityResultsBinding
import com.chordcapo.helper.viewmodel.ResultsViewModel

class ResultsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityResultsBinding
    private val viewModel: ResultsViewModel by viewModels()
    private lateinit var mappingAdapter: ChordMappingAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val chordList = intent.getStringArrayListExtra("CHORD_LIST") ?: emptyList()
        
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        
        viewModel.calculateResults(chordList)
    }
    
    private fun setupRecyclerView() {
        mappingAdapter = ChordMappingAdapter()
        
        binding.rvChordMappings.apply {
            adapter = mappingAdapter
            layoutManager = LinearLayoutManager(this@ResultsActivity)
        }
    }
    
    private fun setupClickListeners() {
        binding.btnTryAgain.setOnClickListener {
            finish()
        }
        
        binding.btnSaveProgression.setOnClickListener {
            Toast.makeText(this, "Save functionality coming soon!", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnViewAllPositions.setOnClickListener {
            val intent = Intent(this, AllPositionsActivity::class.java)
            intent.putExtra("CHORD_LIST", ArrayList(viewModel.currentChordList))
            startActivity(intent)
        }
    }
    
    private fun observeViewModel() {
        viewModel.optimalResult.observe(this) { result ->
            result?.let {
                binding.tvOptimalCapo.text = getString(R.string.optimal_capo, it.capoFret)
                binding.tvBarreCount.text = getString(R.string.barre_chords_count, it.barreCount, it.mappings.size)
                binding.tvMappingHeader.text = getString(R.string.original_with_capo, it.capoFret)
                
                mappingAdapter.submitList(it.mappings)
            }
        }
        
        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
}
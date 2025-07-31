package com.chordcapo.helper

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chordcapo.helper.adapter.CapoPositionAdapter
import com.chordcapo.helper.databinding.ActivityAllPositionsBinding
import com.chordcapo.helper.viewmodel.ResultsViewModel

class AllPositionsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAllPositionsBinding
    private val viewModel: ResultsViewModel by viewModels()
    private lateinit var positionAdapter: CapoPositionAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllPositionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val chordList = intent.getStringArrayListExtra("CHORD_LIST") ?: emptyList()
        
        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        
        viewModel.calculateResults(chordList)
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupRecyclerView() {
        positionAdapter = CapoPositionAdapter { result ->
            // Show detailed view for this position
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("CHORD_LIST", ArrayList(viewModel.currentChordList))
            intent.putExtra("SELECTED_CAPO", result.capoFret)
            startActivity(intent)
        }
        
        binding.rvAllPositions.apply {
            adapter = positionAdapter
            layoutManager = LinearLayoutManager(this@AllPositionsActivity)
        }
    }
    
    private fun observeViewModel() {
        viewModel.allResults.observe(this) { results ->
            positionAdapter.submitList(results)
        }
        
        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
}
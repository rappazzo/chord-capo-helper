package com.chordcapo.helper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chordcapo.helper.data.ChordDatabase
import com.chordcapo.helper.data.CapoResult
import com.chordcapo.helper.repository.ChordRepository
import kotlinx.coroutines.launch

class ResultsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ChordRepository
    
    private val _optimalResult = MutableLiveData<CapoResult?>()
    val optimalResult: LiveData<CapoResult?> = _optimalResult
    
    private val _allResults = MutableLiveData<List<CapoResult>>()
    val allResults: LiveData<List<CapoResult>> = _allResults
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    var currentChordList: List<String> = emptyList()
        private set
    
    init {
        val database = ChordDatabase.getDatabase(application, viewModelScope)
        repository = ChordRepository(database.chordMappingDao())
    }
    
    fun calculateResults(chordList: List<String>) {
        currentChordList = chordList
        
        viewModelScope.launch {
            try {
                val results = repository.findOptimalCapo(chordList)
                _allResults.value = results
                _optimalResult.value = results.firstOrNull()
            } catch (e: Exception) {
                _errorMessage.value = "Error calculating results: ${e.message}"
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
package com.chordcapo.helper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chordcapo.helper.data.ChordDatabase
import com.chordcapo.helper.data.CapoResult
import com.chordcapo.helper.repository.ChordRepository
import com.chordcapo.helper.utils.ChordParser
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ChordRepository
    
    private val _chordList = MutableLiveData<List<String>>(emptyList())
    val chordList: LiveData<List<String>> = _chordList
    
    private val _capoResults = MutableLiveData<List<CapoResult>>()
    val capoResults: LiveData<List<CapoResult>> = _capoResults
    
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    init {
        val database = ChordDatabase.getDatabase(application, viewModelScope)
        repository = ChordRepository(database.chordMappingDao())
    }
    
    fun addChords(input: String) {
        val newChords = ChordParser.parseChordInput(input)
        val validChords = newChords.filter { ChordParser.isValidChordFormat(it) }
        
        if (validChords.size != newChords.size) {
            _errorMessage.value = "Some chords were invalid and skipped"
        }
        
        val currentList = _chordList.value ?: emptyList()
        val updatedList = (currentList + validChords).distinct()
        _chordList.value = updatedList
    }
    
    fun removeChord(chord: String) {
        val currentList = _chordList.value ?: emptyList()
        _chordList.value = currentList.filter { it != chord }
    }
    
    fun clearAllChords() {
        _chordList.value = emptyList()
        _capoResults.value = emptyList()
    }
    
    fun findOptimalCapo() {
        val chords = _chordList.value
        if (chords.isNullOrEmpty()) {
            _errorMessage.value = "Please add some chords first"
            return
        }
        
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val results = repository.findOptimalCapo(chords)
                _capoResults.value = results
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error calculating capo positions: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
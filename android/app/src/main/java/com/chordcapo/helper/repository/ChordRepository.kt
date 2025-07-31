package com.chordcapo.helper.repository

import com.chordcapo.helper.data.ChordMapping
import com.chordcapo.helper.data.ChordMappingDao
import com.chordcapo.helper.data.CapoResult

class ChordRepository(private val chordMappingDao: ChordMappingDao) {

    suspend fun findOptimalCapo(chordList: List<String>): List<CapoResult> {
        val results = mutableListOf<CapoResult>()
        
        for (capoFret in 0..12) {
            var barreCount = 0
            val mappings = mutableListOf<ChordMapping>()
            
            for (chord in chordList) {
                val mapping = chordMappingDao.getChordMapping(chord, capoFret)
                if (mapping != null) {
                    if (mapping.isBarreChord) {
                        barreCount++
                    }
                    mappings.add(mapping)
                } else {
                    // If no mapping found, create a default barre chord mapping
                    val defaultMapping = ChordMapping(
                        originalChord = chord,
                        capoFret = capoFret,
                        rootPositionShape = "E",
                        isBarreChord = true,
                        chordName = chord
                    )
                    barreCount++
                    mappings.add(defaultMapping)
                }
            }
            
            results.add(CapoResult(capoFret, barreCount, mappings))
        }
        
        // Sort by fewest barre chords, then by lowest fret position
        return results.sortedWith(compareBy<CapoResult> { it.barreCount }.thenBy { it.capoFret })
    }
    
    suspend fun getSupportedChords(): List<String> {
        return chordMappingDao.getAllSupportedChords()
    }
    
    suspend fun validateChord(chord: String): Boolean {
        val supportedChords = getSupportedChords()
        return supportedChords.contains(chord)
    }
}
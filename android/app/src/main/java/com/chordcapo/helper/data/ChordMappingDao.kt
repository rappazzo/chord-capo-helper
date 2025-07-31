package com.chordcapo.helper.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ChordMappingDao : ChordDataPopulator {
    @Query("SELECT * FROM chord_mappings WHERE originalChord = :chord AND capoFret = :capoFret")
    suspend fun getChordMapping(chord: String, capoFret: Int): ChordMapping?
    
    @Query("SELECT * FROM chord_mappings WHERE originalChord = :chord")
    suspend fun getAllMappingsForChord(chord: String): List<ChordMapping>
    
    @Query("SELECT DISTINCT originalChord FROM chord_mappings ORDER BY originalChord")
    suspend fun getAllSupportedChords(): List<String>
}
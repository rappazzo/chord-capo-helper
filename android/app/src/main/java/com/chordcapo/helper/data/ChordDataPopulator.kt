package com.chordcapo.helper.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ChordDataPopulator {
    @Insert
    suspend fun insertChordMappings(mappings: List<ChordMapping>)
}

object ChordMappingData {
    fun getInitialChordMappings(): List<ChordMapping> {
        val mappings = mutableListOf<ChordMapping>()
        
        // Major chords
        val majorChords = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
        
        majorChords.forEach { chord ->
            mappings.addAll(generateMajorChordMappings(chord))
        }
        
        // Minor chords
        majorChords.forEach { chord ->
            mappings.addAll(generateMinorChordMappings("${chord}m"))
        }
        
        // Seventh chords
        majorChords.forEach { chord ->
            mappings.addAll(generateSeventhChordMappings("${chord}7"))
        }
        
        return mappings
    }
    
    private fun generateMajorChordMappings(chord: String): List<ChordMapping> {
        val mappings = mutableListOf<ChordMapping>()
        val rootNote = chord.replace("#", "").replace("b", "")
        
        for (capo in 0..12) {
            val (shape, isBarre) = when ((getChordIndex(chord) + capo) % 12) {
                0 -> "C" to false  // C shape
                1 -> "C" to true   // C# barre
                2 -> "D" to false  // D shape
                3 -> "D" to true   // D# barre
                4 -> "E" to false  // E shape
                5 -> "F" to true   // F barre
                6 -> "F" to true   // F# barre
                7 -> "G" to false  // G shape
                8 -> "G" to true   // G# barre
                9 -> "A" to false  // A shape
                10 -> "A" to true  // A# barre
                11 -> "A" to true  // B barre
                else -> "E" to true
            }
            
            mappings.add(ChordMapping(
                originalChord = chord,
                capoFret = capo,
                rootPositionShape = shape,
                isBarreChord = isBarre,
                chordName = getTransposedChord(chord, capo)
            ))
        }
        
        return mappings
    }
    
    private fun generateMinorChordMappings(chord: String): List<ChordMapping> {
        val mappings = mutableListOf<ChordMapping>()
        val baseChord = chord.replace("m", "")
        
        for (capo in 0..12) {
            val (shape, isBarre) = when ((getChordIndex(baseChord) + capo) % 12) {
                0 -> "C" to false  // Cm shape
                1 -> "C" to true   // C#m barre
                2 -> "D" to false  // Dm shape
                3 -> "D" to true   // D#m barre
                4 -> "E" to false  // Em shape
                5 -> "F" to true   // Fm barre
                6 -> "F" to true   // F#m barre
                7 -> "G" to false  // Gm shape
                8 -> "G" to true   // G#m barre
                9 -> "A" to false  // Am shape
                10 -> "A" to true  // A#m barre
                11 -> "A" to true  // Bm barre
                else -> "E" to true
            }
            
            mappings.add(ChordMapping(
                originalChord = chord,
                capoFret = capo,
                rootPositionShape = shape,
                isBarreChord = isBarre,
                chordName = getTransposedChord(chord, capo)
            ))
        }
        
        return mappings
    }
    
    private fun generateSeventhChordMappings(chord: String): List<ChordMapping> {
        val mappings = mutableListOf<ChordMapping>()
        val baseChord = chord.replace("7", "")
        
        for (capo in 0..12) {
            // Most 7th chords require barre positions
            val (shape, isBarre) = when ((getChordIndex(baseChord) + capo) % 12) {
                4 -> "E" to false  // E7 open
                9 -> "A" to false  // A7 open
                2 -> "D" to false  // D7 open
                7 -> "G" to false  // G7 open
                0 -> "C" to false  // C7 open
                else -> "E" to true // Most others are barre
            }
            
            mappings.add(ChordMapping(
                originalChord = chord,
                capoFret = capo,
                rootPositionShape = shape,
                isBarreChord = isBarre,
                chordName = getTransposedChord(chord, capo)
            ))
        }
        
        return mappings
    }
    
    private fun getChordIndex(chord: String): Int {
        return when (chord) {
            "C" -> 0
            "C#" -> 1
            "D" -> 2
            "D#" -> 3
            "E" -> 4
            "F" -> 5
            "F#" -> 6
            "G" -> 7
            "G#" -> 8
            "A" -> 9
            "A#" -> 10
            "B" -> 11
            else -> 0
        }
    }
    
    private fun getTransposedChord(originalChord: String, capoFret: Int): String {
        val chordNames = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
        val baseChord = originalChord.replace(Regex("[m7maj]+"), "")
        val suffix = originalChord.replace(baseChord, "")
        val baseIndex = getChordIndex(baseChord)
        val newIndex = (baseIndex + capoFret) % 12
        return chordNames[newIndex] + suffix
    }
}
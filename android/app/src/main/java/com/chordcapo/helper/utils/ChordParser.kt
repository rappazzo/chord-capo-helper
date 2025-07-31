package com.chordcapo.helper.utils

object ChordParser {
    
    fun parseChordInput(input: String): List<String> {
        if (input.isBlank()) return emptyList()
        
        // Support multiple input formats: "C Am F G", "C, Am, F, G", "C-Am-F-G"
        val delimiters = Regex("[,\\s-]+")
        return input.trim()
            .split(delimiters)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { normalizeChord(it) }
    }
    
    private fun normalizeChord(chord: String): String {
        // Convert flat notation to sharp and normalize chord names
        return chord.replace("♭", "b")
            .replace("Bb", "A#")
            .replace("Db", "C#")
            .replace("Eb", "D#")
            .replace("Gb", "F#")
            .replace("Ab", "G#")
    }
    
    fun isValidChordFormat(chord: String): Boolean {
        // Basic chord format validation
        val chordPattern = Regex("^[A-G][#b]?(m|maj7|m7|7)?$")
        return chordPattern.matches(chord)
    }
}
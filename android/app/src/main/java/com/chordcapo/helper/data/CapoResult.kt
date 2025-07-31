package com.chordcapo.helper.data

data class CapoResult(
    val capoFret: Int,
    val barreCount: Int,
    val mappings: List<ChordMapping>
)

data class ChordInput(
    val originalChord: String,
    val displayName: String = originalChord
)
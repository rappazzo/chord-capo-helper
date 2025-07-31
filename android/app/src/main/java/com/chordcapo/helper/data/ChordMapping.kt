package com.chordcapo.helper.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chord_mappings")
data class ChordMapping(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val originalChord: String,
    val capoFret: Int,
    val rootPositionShape: String,
    val isBarreChord: Boolean,
    val chordName: String
)
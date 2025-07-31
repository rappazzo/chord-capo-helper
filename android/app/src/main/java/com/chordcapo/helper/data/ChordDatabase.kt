package com.chordcapo.helper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [ChordMapping::class],
    version = 1,
    exportSchema = false
)
abstract class ChordDatabase : RoomDatabase() {
    abstract fun chordMappingDao(): ChordMappingDao

    companion object {
        @Volatile
        private var INSTANCE: ChordDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ChordDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChordDatabase::class.java,
                    "chord_database"
                ).addCallback(ChordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class ChordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.chordMappingDao())
                }
            }
        }

        suspend fun populateDatabase(chordMappingDao: ChordMappingDao) {
            val initialMappings = ChordMappingData.getInitialChordMappings()
            // Insert mappings in batches to avoid memory issues
            initialMappings.chunked(100).forEach { batch ->
                (chordMappingDao as ChordDataPopulator).insertChordMappings(batch)
            }
        }
    }
}
# Guitar Capo Chord Optimizer - Android App

A native Android application that helps guitarists find the optimal capo position to minimize barre chords when playing a given set of chords.

## Features

- **Chord Input**: Enter chord progressions in multiple formats (space, comma, or dash separated)
- **Capo Optimization**: Calculates the best capo position to minimize barre chords
- **Chord Shape Display**: Shows which guitar chord shapes (E, A, D, G, C, F) to use
- **All Positions View**: Compare all possible capo positions
- **Offline Database**: Pre-populated with comprehensive chord mappings

## Supported Chord Types

- Major chords (C, D, E, F, G, A, B)
- Minor chords (Cm, Dm, Em, Fm, Gm, Am, Bm)
- Seventh chords (C7, D7, E7, F7, G7, A7, B7)
- Minor seventh chords (Cm7, Dm7, Em7, Fm7, Gm7, Am7, Bm7)
- Major seventh chords (Cmaj7, Dmaj7, Emaj7, Fmaj7, Gmaj7, Amaj7, Bmaj7)

## Technical Stack

- **Language**: Kotlin
- **Architecture**: MVVM with LiveData
- **Database**: Room (SQLite)
- **UI**: View Binding with Material Design
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Project Structure

```
android/
├── app/
│   ├── src/main/java/com/chordcapo/helper/
│   │   ├── data/           # Room database entities and DAOs
│   │   ├── repository/     # Data repository layer
│   │   ├── viewmodel/      # ViewModels for UI logic
│   │   ├── adapter/        # RecyclerView adapters
│   │   ├── utils/          # Utility classes
│   │   ├── MainActivity.kt
│   │   ├── ResultsActivity.kt
│   │   └── AllPositionsActivity.kt
│   └── src/main/res/       # Android resources
├── build.gradle
└── README.md
```

## Building the App

1. Open the `android` folder in Android Studio
2. Sync the project with Gradle files
3. Run the app on an emulator or physical device

## Algorithm

The app uses a comprehensive chord mapping database that stores:
- Original chord name
- Capo fret position (0-12)
- Root position shape (E, A, D, G, C, F based on CAGED system)
- Whether the chord requires a barre
- Transposed chord name

The optimization algorithm:
1. For each capo position (0-12 frets)
2. Look up the chord shape for each input chord
3. Count how many require barre chords
4. Sort results by fewest barre chords, then by lowest fret position

## Future Enhancements

- Visual chord diagrams
- Save/load chord progressions
- Popular song database
- Audio playback
- Chord theory educational content
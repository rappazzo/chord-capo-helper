# Guitar Capo Chord Optimizer - Mobile App Specification

## 1. Project Overview

### 1.1 Purpose
A cross-platform mobile application that helps guitarists find the optimal capo position to minimize barre chords when playing a given set of chords.

### 1.2 Target Platforms
- iOS (iPhone/iPad)
- Android (Phone/Tablet)

### 1.3 Core Functionality
- Store a comprehensive chord-capo mapping table
- Accept user input of chord progressions
- Calculate optimal capo position to minimize barre chords
- Display chord shape mappings and recommendations

## 2. Technical Requirements

### 2.1 Development Framework
- **Recommended**: React Native or Flutter for cross-platform development
- **Alternative**: Native development (Swift/Kotlin) if platform-specific optimizations are required

### 2.2 Data Storage
- Local SQLite database for chord mapping table
- User preferences stored in device storage
- No cloud storage required (offline-first app)

### 2.3 Performance Requirements
- App launch time: < 3 seconds
- Chord calculation response: < 1 second
- Smooth scrolling and responsive UI
- Support for devices with 2GB+ RAM

## 3. Data Structure

### 3.1 Chord Mapping Table Schema
```sql
CREATE TABLE chord_mappings (
    id INTEGER PRIMARY KEY,
    original_chord VARCHAR(10) NOT NULL,
    capo_fret INTEGER NOT NULL CHECK (capo_fret BETWEEN 0 AND 12),
    root_position_shape CHAR(1) NOT NULL CHECK (root_position_shape IN ('E','A','D','G','C','F')),
    is_barre_chord BOOLEAN NOT NULL,
    chord_name VARCHAR(10) NOT NULL,
    INDEX idx_chord_capo (original_chord, capo_fret)
);
```

### 3.2 Chord Shape Categories
- **E Shape**: Chords based on open E major/minor patterns
- **A Shape**: Chords based on open A major/minor patterns  
- **D Shape**: Chords based on open D major/minor patterns
- **G Shape**: Chords based on open G major/minor patterns
- **C Shape**: Chords based on open C major/minor patterns
- **F Shape**: Chords based on F major pattern (typically barre)

### 3.3 Supported Chord Types
- Major chords (C, D, E, F, G, A, B)
- Minor chords (Cm, Dm, Em, Fm, Gm, Am, Bm)
- Seventh chords (C7, D7, E7, F7, G7, A7, B7)
- Minor seventh chords (Cm7, Dm7, Em7, Fm7, Gm7, Am7, Bm7)
- Major seventh chords (Cmaj7, Dmaj7, Emaj7, Fmaj7, Gmaj7, Amaj7, Bmaj7)

## 4. User Interface Specification

### 4.1 Main Screen Layout
```
┌─────────────────────────────────┐
│ [≡] Guitar Capo Optimizer   [?] │
├─────────────────────────────────┤
│                                 │
│ Enter Chords:                   │
│ ┌─────────────────────────────┐ │
│ │ C  Am  F  G                 │ │
│ └─────────────────────────────┘ │
│ [Add Chord] [Clear All]         │
│                                 │
│ ┌─────────────────────────────┐ │
│ │      FIND OPTIMAL CAPO      │ │
│ └─────────────────────────────┘ │
│                                 │
│ Current Chord List:             │
│ • C Major                       │
│ • A Minor                       │
│ • F Major                       │
│ • G Major                       │
│                                 │
└─────────────────────────────────┘
```

### 4.2 Results Screen Layout
```
┌─────────────────────────────────┐
│ [←] Capo Recommendation         │
├─────────────────────────────────┤
│                                 │
│ 🎯 OPTIMAL: Capo on Fret 3      │
│    Barre Chords: 1 of 4         │
│                                 │
│ Original → With Capo Fret 3     │
│ ────────────────────────────────│
│ C Major  → A Shape (Open)       │
│ A Minor  → F# Shape (Open)      │
│ F Major  → D Shape (Open)       │
│ G Major  → E Shape (Barre) ⚠️   │
│                                 │
│ ┌─────────────────────────────┐ │
│ │     VIEW ALL POSITIONS      │ │
│ └─────────────────────────────┘ │
│                                 │
│ [Try Again] [Save Progression]  │
│                                 │
└─────────────────────────────────┘
```

### 4.3 All Positions View
```
┌─────────────────────────────────┐
│ [←] All Capo Positions          │
├─────────────────────────────────┤
│                                 │
│ Capo Fret | Barre Chords        │
│ ──────────┼──────────────────   │
│    0      │ 2 of 4 ⭐️           │
│    1      │ 3 of 4              │
│    2      │ 2 of 4              │
│  → 3      │ 1 of 4 🎯 BEST      │
│    4      │ 2 of 4              │
│    5      │ 3 of 4              │
│   ...     │ ...                 │
│                                 │
│ [Tap row for details]           │
│                                 │
└─────────────────────────────────┘
```

## 5. Core Algorithm

### 5.1 Capo Optimization Logic
```javascript
function findOptimalCapo(chordList) {
    let results = [];
    
    for (let capoFret = 0; capoFret <= 12; capoFret++) {
        let barreCount = 0;
        let mappings = [];
        
        for (let chord of chordList) {
            let mapping = getChordMapping(chord, capoFret);
            if (mapping.is_barre_chord) {
                barreCount++;
            }
            mappings.push(mapping);
        }
        
        results.push({
            capoFret: capoFret,
            barreCount: barreCount,
            mappings: mappings
        });
    }
    
    // Sort by fewest barre chords, then by lowest fret position
    results.sort((a, b) => {
        if (a.barreCount !== b.barreCount) {
            return a.barreCount - b.barreCount;
        }
        return a.capoFret - b.capoFret;
    });
    
    return results;
}
```

### 5.2 Chord Input Processing
- Support multiple input formats: "C Am F G", "C, Am, F, G", "C-Am-F-G"
- Auto-complete chord suggestions
- Validate chord names against supported chord database
- Handle both sharp (#) and flat (♭ or b) notation

## 6. Features & Functionality

### 6.1 Core Features
- ✅ Chord input with validation and auto-complete
- ✅ Capo position optimization algorithm
- ✅ Visual chord shape identification (E, A, D, G, C, F)
- ✅ Barre chord indication
- ✅ Results sorted by optimal capo position
- ✅ Detailed mapping display (original chord → capo chord)

### 6.2 Enhanced Features
- 📚 Save and load chord progressions
- 🎵 Popular song chord progressions database
- 📊 Statistics (most common chord shapes, preferred capo positions)
- 🔄 Transpose chord progressions to different keys
- 📱 Share chord progressions with other users

### 6.3 Future Enhancements
- 🎸 Visual chord diagrams
- 🎵 Audio playback of chords
- 📖 Chord theory educational content
- 🎼 Integration with songbook apps
- ☁️ Cloud sync for saved progressions

## 7. User Experience Flow

### 7.1 Primary User Journey
1. User opens app
2. User enters chord progression (manually or from saved list)
3. User taps "Find Optimal Capo"
4. App calculates and displays recommended capo position
5. User views detailed chord mappings
6. User optionally saves progression for future use

### 7.2 Input Methods
- **Manual Entry**: Type chord names in text field
- **Chord Picker**: Visual chord selector with categories
- **Voice Input**: "Add C major, A minor, F major, G major"
- **Import**: Load from saved progressions or popular songs

## 8. Data Population Strategy

### 8.1 Initial Chord Database
The app will ship with a pre-populated database containing:
- Common chord shapes for all 12 chromatic positions
- Barre chord classifications based on standard guitar fingerings
- Root position shape mappings following CAGED system principles

### 8.2 Database Population Example
```sql
-- Example entries for C Major chord across capo positions
INSERT INTO chord_mappings VALUES 
(1, 'C', 0, 'C', FALSE, 'C'),      -- Open C shape
(2, 'C', 1, 'B', TRUE, 'C#'),     -- Barre at 1st fret
(3, 'C', 2, 'A', FALSE, 'D'),     -- A shape at 2nd fret
(4, 'C', 3, 'G', FALSE, 'D#'),    -- G shape at 3rd fret
(5, 'C', 5, 'E', TRUE, 'F');      -- E shape barre at 5th fret
```

## 9. Technical Implementation Notes

### 9.1 Performance Considerations
- Pre-calculate all chord mappings and store in local database
- Use indexing on frequently queried columns (chord name, capo fret)
- Implement result caching for recently calculated progressions
- Lazy loading for chord progression history

### 9.2 Cross-Platform Considerations
- Consistent UI/UX across iOS and Android
- Platform-specific design guidelines (Material Design for Android, Human Interface Guidelines for iOS)
- Device-specific optimizations (tablet layouts, keyboard handling)
- Accessibility support (VoiceOver, TalkBack, high contrast mode)

### 9.3 Error Handling
- Invalid chord name input validation
- Graceful handling of empty chord lists
- Network connectivity not required (offline-first design)
- App state preservation during interruptions

## 10. Success Metrics

### 10.1 User Engagement
- Daily active users
- Session length
- Chord progressions calculated per session
- Feature adoption rates (save progressions, view all positions)

### 10.2 App Performance
- App launch time < 3 seconds
- Calculation completion time < 1 second
- Crash-free sessions > 99.5%
- User retention rate > 60% after 30 days

## 11. Development Timeline Estimate

### Phase 1 (MVP - 6-8 weeks)
- Core chord database creation
- Basic UI implementation
- Capo optimization algorithm
- Chord input and results display

### Phase 2 (Enhanced Features - 4-6 weeks)
- Save/load progressions
- All positions view
- UI/UX polish and testing
- Platform-specific optimizations

### Phase 3 (Advanced Features - 6-8 weeks)
- Popular songs database
- Chord diagrams
- Audio features
- Cloud sync capabilities
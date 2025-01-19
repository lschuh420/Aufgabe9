package com.example.aufgabe9.database.dataclass

enum class Priority(val level: Int) {
    HIGH(1),   // Höchste Priorität
    MEDIUM(2), // Mittlere Priorität
    LOW(3);    // Niedrigste Priorität

    companion object {
        // Funktion zum Konvertieren eines Int-Werts in eine Priority
        fun fromLevel(level: Int): Priority {
            return values().find { it.level == level }
                ?: throw IllegalArgumentException("Unbekannter Prioritätslevel: $level")
        }
    }
}

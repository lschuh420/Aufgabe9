ToDo App
Die ToDo-App bietet eine einfache und intuitive Möglichkeit, Aufgaben zu verwalten. Sie wurde mit Jetpack Compose in Kotlin entwickelt und bietet moderne Funktionen wie Status- und Prioritätsmanagement, CRUD-Operationen und Filter.

Installationsanleitung 🚀
Voraussetzungen
Entwicklungsumgebung: Android Studio (Version 2022.2.1 oder höher empfohlen)
Java-Version: Java 11 oder neuer
Android-Gerät oder Emulator: API-Level 21 (Android 5.0) oder höher
Schritte zur Installation
Repository klonen: Lade das Projekt herunter, indem du das folgende Kommando ausführst:

bash
Kopieren
Bearbeiten
git clone https://github.com/username/todo-app.git
cd todo-app
Projekt in Android Studio importieren:

Öffne Android Studio.
Wähle File > Open und navigiere zum heruntergeladenen Ordner todo-app.
Abhängigkeiten synchronisieren:

Android Studio sollte automatisch die benötigten Abhängigkeiten synchronisieren.
Falls nicht, klicke auf Sync Now, wenn du dazu aufgefordert wirst.
Emulator einrichten (optional):

Erstelle einen Android-Emulator in Android Studio (API-Level ≥ 21).
App ausführen:

Wähle ein Zielgerät oder einen Emulator aus.
Klicke auf Run (Grüner Play-Button oben in Android Studio).
App installieren:

Die App wird automatisch auf dem Zielgerät installiert und gestartet.
Funktionsbeschreibung ✨
Hauptfunktionen
ToDos anzeigen: Aufgaben werden nach ihrem Status (offen oder erledigt) gefiltert und angezeigt.
Neue Aufgaben erstellen: Aufgaben mit Titel, Beschreibung, Priorität und Fälligkeitsdatum hinzufügen.
Aufgaben bearbeiten: Titel, Beschreibung, Priorität oder Fälligkeitsdatum ändern.
Aufgaben löschen: Aufgaben können vollständig entfernt werden.
Status ändern: Aufgaben können als erledigt oder offen markiert werden.
Filteroptionen: Wechsle zwischen offenen und erledigten Aufgaben.
Modernes Design: Benutzerfreundliche Oberfläche mit klarer Struktur.
Zusatzfunktionen
Langklick-Funktion: Öffnet einen Dialog zum Bearbeiten von Aufgaben.
Checkbox: Ändert den Status einer Aufgabe mit nur einem Klick.
ToDo-Prioritäten: Jede Aufgabe kann mit einer Priorität (Hoch, Mittel, Niedrig) versehen werden, die optisch hervorgehoben wird.
Verwendete Technologien 🛠️
Programmiersprache und Framework
Kotlin: Moderne Programmiersprache für Android-Entwicklung.
Jetpack Compose: UI-Toolkit für deklaratives und reaktives App-Design.
Datenmanagement
SQLite: Lokale Datenbank für die persistenten Daten.
TodoListController: Enthält die CRUD-Logik (Create, Read, Update, Delete).
UI-Komponenten
Material 3: Für modernes, intuitives und ästhetisches UI-Design.
LazyColumn: Für effizientes Rendern von Listen.
AlertDialog: Zur Bearbeitung und Bestätigung von Aktionen.
Entwicklungswerkzeuge
Android Studio: IDE für Android-Entwicklung.
Live Templates: Beschleunigen die Code-Generierung.
Bekannte Probleme 🐞
UI-Flackern bei schnellem Scrollen:

Wenn die Liste sehr groß ist, kann es beim Scrollen zu geringem Flackern kommen.
Lösung: Implementiere LazyColumn mit optimierten key-Werten.
Datenbank-Fehler bei leerem Eingabefeld:

Wenn ein ToDo ohne Titel gespeichert wird, kann ein Fehler auftreten.
Lösung: Validierung hinzufügen, um leere Eingaben zu verhindern.
Dialog-Überlagerung auf kleinen Bildschirmen:

Auf Geräten mit kleinen Bildschirmen kann der Bearbeitungsdialog unübersichtlich werden.
Lösung: Scrollable-Layout im Dialog verwenden.
Keine Cloud-Synchronisierung:

Derzeit können ToDos nur lokal gespeichert werden.
Mögliche Erweiterung: Firebase oder ein REST-Backend hinzufügen.

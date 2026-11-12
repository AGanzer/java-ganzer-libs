## Kodierungsregeln
- Code nach den offiziellen Java Coding Conventions erstellen.
- Alle Bezeichner in US-Englisch.
- Verwende aussagekräftige Bezeichner.
- Schreibe verständlichen, wartbaren Code.
- Bevorzuge kleine Methoden mit klarer Verantwortung.
- Vermeide Code-Duplikate.
- Vermeide unnötige Abstraktionen.
- Einzelne Zeilen in Block-Anwendungen (`for`, `while`, `if` usw.) nicht in Blickklammern setzen, mit zwei Ausnahmen:
  - Bei verschachtelten Blöcken
  - Wenn entweder der `if`-Block oder einer der `else if`-Blöcke oder der `else`-Block geklammert werden muss, werden auch alle anderen Blöcker in dieser Anweisung geklammert.
  - Halte dich dabei an die derzeitigen Formatierungen, wie sie z.B. in  `de.ganzer.swing.controls` und  `de.ganzer.swing.dlgfw` vorhanden sind.
- Methoden/Konstruktur-Parameter werden bei der Deklaration wie beim Aufruf hinter die öffnende Klammer geschrieben. Erst, wenn die Zeile länger als 120 Zeichen wird, bleibt der erste Parameter hinter der Klammer unhd alle weiteren beginnen in der nächsten Zeile. Dazu können die Formatierungsregeln verwendet werden, wie sie in IntelliJ angegeben sind.
- Bei Unklarheiten nachfragen und ebenfalls fragen, ob die Antwort in den Richtlinien nachgetragen werden soll.

## Testregeln
- Erstelle Tests für neue Funktionalität.
- Ergänze Regressionstests bei Fehlerbehebungen.
- Führe die Tests nach Änderungen aus.
- Behebe durch Änderungen verursachte Testfehler.

## Arbeitsweise
- Analysiere zunächst die Auswirkungen der Änderung.
- Erstelle bei umfangreichen Aufgaben zunächst einen Plan.
- Setze Änderungen schrittweise um.
- Prüfe die Änderungen anschließend auf Seiteneffekte.
- Führe Build und Tests aus.
- Gib eine Zusammenfassung der Änderungen und Prüfergebnisse aus.

##  Prioritäten
- Bestehende Projektarchitektur hat Vorrang.
- Projektspezifische Regeln haben Vorrang vor allgemeinen Coding-Empfehlungen.
- Bei widersprüchlichen oder unklaren Vorgaben frage vor größeren Änderungen nach.

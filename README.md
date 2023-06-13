# SchedulerBSSProjekt

Funktionsweise der grafischen Oberfläche zur Eingabe von Prozessen:

Eingabefelder für neue Prozesse: Es gibt zwei Textfelder, eines für die Eingabe der Prozessdauer und eines für die Eingabe der Prozessankunftszeit. 
Der Benutzer kann Integer-Werte in diese Felder eingeben, um einen Prozess zu definieren.

"Weiter" Button: Wenn der Benutzer auf diesen Button klickt, wird der eingegebene Prozess zur Arbeitsliste hinzugefügt.

"Zurück" Button: Wenn der Benutzer auf diesen Button klickt, wird der zuletzt eingegebene Prozess aus der Arbeitsliste entfernt (noch nicht optimal implementiert, funktioniert nicht immer, feature aus Zeitgründen nicht fertiggestellt)

"Erzeuge zufällige Prozesse" Button: Wenn der Benutzer auf diesen Button klickt, öffnet sich ein weiteres Eingabefenster zum Festlegen der gewünschten Anzahl an Prozessen, der maximalen Ankunftszeit und der maximalen Bearbeitungsdauer. 

Buttons für Scheduling-Algorithmen: Es gibt mehrere Buttons, welche die verschiedenen Scheduling-Algorithmen repräsentieren, darunter FCFS, Shortest Job First (SJF), Round Robin und Preemptive Shortest Job First (pSJF). Der Benutzer kann einen dieser Algorithmen auswählen, indem er auf den entsprechenden Button klickt. Anschließend kann die Umschaltzeit und bei Bedarf das Quantum angegeben werden. Die Ergebnisse werden tabellarisch angezeigt.

"Run All" Button: Nach Eingabe der Umschaltzeit und des Zeitquantums werden alle Scheduling-Algorithmen sequenziell ausgeführt, und die Ergebnisse werden tabellarisch angezeigt. Zusätzlich wird in der Tabelle die prozentuale Abweichung vom jeweiligen Bestwert der entsprechenden Spalte angezeigt.

Zusätzlich zur tabellarischen Auswertung werden in der Konsole noch die eingegeben Prozesse nach Ankunftszeit aufsteigend sortiert mit Angaben zur Startzeit, Endzeit, Reaktionszeit, Wartezeit und Verweilzeit ausgegeben.

# How To

## Bedienung

1. Beim Starten der Applikation müssen als erstes die Abmessungen des Simulationsraumes definiert werden.
2. Die Anzahl der Personen und wenn gewünscht deren Gewichtung, respektive Spawn-Wahrscheinlichkeit definieren.
3. Spawn- und Goal-Area(s) und Obstacles erstellen. 
    Diese können mit dem Scroll-Rad vergrössert oder verkleinert werden. 
    Wenn man die Alt-Taste drückt und scrollt, dreht sich das Objekt.
    Ursprünglich war die Drehung die Shift-Taste geplant, aber bei der Logitech Maus wird diese verwendet um die Y-Achse zu invertieren.
4. Objekte können mit dem Rechts-Klick wieder gelöscht werden.
5. Objekte können mit der linken Maustaste gegriffen und verschoben werden.
6. Objekte können mit den goldenen Punkten in ihrer Form verändert werden.
7. Objekte können nur am Anfang und zwischen Reset und Spawn Zuständen.
8. Personen können beliebig verschoben werden, solange die Simulation nicht läuft.
9. Wenn eine Gewichtung der Personen festgelegt wird, muss diese in der Summe exakt 100 ergeben, die Simulation lässt sich sonst nicht starten.
    Der Benutzer wird mit einem Warnhinweis darauf aufmerksam gemacht.
10. Die Simulation muss mindestens ein Spawn- und ein Goal-Area beinhalten, sonst erscheint ein Warnhinweis.
11. Die Simulation kann mit Start und Pause gestoppt und wieder aufgenommen werden.
12. Die Statistiken auf der rechten Seite sind nur im gestoppten Zustand aufrufbar.
   Hierbei muss die Simulation bereits einmal gestartet wroden sein.

## Konfiguration

1. Konfigurationseinstellungen können über das GUI (grösse des Simulationsraumes) oder im simulation.properties im src/ Ordner definiert werden.
   Hier können beispielsweise auch die Kanten ein- und ausgeschalten werden.
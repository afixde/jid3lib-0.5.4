@echo off
rem /P E:\Ordner               : Pfad auf dem die Suche gestartet werden soll
rem /S                         : Bitte mit allen Unterordnern
rem /M *.*                     : Suchmaske - hier alle Dateien (ausser denen ohne Dateiendung)
rem /D -8                      : Letztes Änderungsdatum älter als 8 Tage zum heutigen Datum
rem /C "cmd /c del /q @path"   : Befehl der mit diesen Dateien ausgeführt werden soll (hier löschen)

set CURDIR=%1

if x%CURDIR% == "x" set CURDIR=.\

cd %CURDIR%
if not exist _lat goto EXIT
echo cleaning up %CURDIR% ...
del *.mp3
move /y _lat\* .
rmdir _lat

:EXIT

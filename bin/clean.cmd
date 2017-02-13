@echo on
rem /P E:\Ordner               : Pfad auf dem die Suche gestartet werden soll
rem /S                         : Bitte mit allen Unterordnern
rem /M *.*                     : Suchmaske - hier alle Dateien (ausser denen ohne Dateiendung)
rem /D -8                      : Letztes �nderungsdatum �lter als 8 Tage zum heutigen Datum
rem /C "cmd /c del /q @path"   : Befehl der mit diesen Dateien ausgef�hrt werden soll (hier l�schen)

set CMDPATH=%~dp0

set CURDIR=%1

if x%CURDIR% == x set CURDIR=.\

forfiles /p %CURDIR% /c "cmd /c IF @ISDIR==TRUE %CMDPATH%\cleanlat @path"

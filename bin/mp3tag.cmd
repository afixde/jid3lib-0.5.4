@echo on
rem # start-up script for MP3 tagger
rem # usage: mp3.cmd [- options] mp3file
rem #
                                                              

set grp=%1                               
set inst=%2                              
if x%grp%==x set grp=mfc
if x%inst%==x set inst=%COMPUTERNAME%     

REM # ----------------------------------------------------------------------------

setlocal
rem if x%PRJ_HOME%==x set PRJ_HOME=D:\Java\EclipseWorkspace\jid3lib-0.5.4
if x%PRJ_HOME%==x set PRJ_HOME=F:\Java\workspace\jid3lib-0.5.4\
set LIB_CP=%LIB_CP%;%PRJ_NAME%\dist\jid3lib-0.5.4.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\aelfred-1.2.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\emma.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\emma_ant.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\jaxp-1.2.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\jdom-1.0b8.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\jsch-0.1.25.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\junit.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\log4j-1.2.6.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\oro-2.0.6.jar
set LIB_CP=%LIB_CP%;%PRJ_NAME%\lib\sax-2.0.1.jar


REM # ----------------------------------------------------------------------------

rem # setup class path
set cp=%PRJ_HOME%;%PRJ_HOME%\classes;%LIB_CP%
echo java -Xms64M -Xmx128M -classpath %cp%  -Dwms.terminal=%TERMINAL% de.siemens.sd.it.lcmbase.gui.frame.LCMApplication %grp% %inst% -f %prop%  -s
java   -Xms64M -Xmx128M -classpath %cp%  fix.mp3.demo.unicode.MP3 %*

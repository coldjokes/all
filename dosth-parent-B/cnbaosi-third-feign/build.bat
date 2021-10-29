@echo OFF
set out=..\resource\
call mvn install
xcopy target\*.jar %out%
xcopy target\lib\* %out%\lib\ /y /d 


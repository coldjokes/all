@echo OFF
set out=..\resource\
xcopy target\*.jar %out%
xcopy target\lib\* %out%\lib\ /y /d 


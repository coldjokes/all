@echo OFF

set out=..\resource\

cd dosth-eureka-server
xcopy target\*.jar %out% /y /d /i
xcopy target\lib\* %out%\lib\ /y /d /i

cd ..\dosth-admin
xcopy target\*.jar %out% /y /d /i
xcopy target\lib\* %out%\lib\ /y /d /i

cd ..\dosth-tool
xcopy target\*.jar %out% /y /d /i
xcopy target\lib\* %out%\lib\ /y /d /i

cd ..\dosth-toolcabinet
xcopy target\*.jar %out% /y /d /i
xcopy target\lib\* %out%\lib\ /y /d /i

xcopy ..\source\config\application-config.yml %out% /y /i

pause
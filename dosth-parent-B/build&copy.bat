@echo OFF

set out=..\resource\

cd dosth-eureka-server
call mvn clean install
xcopy target\*.jar %out% /y /d /i
xcopy target\lib\* %out%\lib\ /y /d /i

cd ..\dosth-admin
call mvn clean install
xcopy target\*.jar %out% /y /d /i
xcopy target\lib\* %out%\lib\ /y /d /i

cd ..\dosth-tool
call mvn clean install
xcopy target\*.jar %out% /y /d /i
xcopy target\lib\* %out%\lib\ /y /d /i

cd ..\dosth-toolcabinet
call mvn clean install
xcopy target\*.jar %out% /y /d /i
xcopy target\lib\* %out%\lib\ /y /d /i

xcopy ..\source\config\application-config.yml %out% /y /i

pause
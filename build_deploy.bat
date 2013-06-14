call mvn clean install -DskipTests
call copy target\saiku-reporting-core-trunk-SNAPSHOT.jar c:\TargetPlatforms\biserver-ce-4.8.0-stable\pentaho-solutions\system\saiku-reporting\lib\ /Y
rem call c:\TargetPlatforms\biserver-ce-4.5-stable\start-pentaho-debug.bat
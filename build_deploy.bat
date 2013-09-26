call mvn clean install -DskipTests
call copy target\saiku-reporting-core-trunk-SNAPSHOT.jar c:\TargetPlatforms\biserver-ce-4.8.0-stable\pentaho-solutions\system\saiku-reporting\lib\ /Y
call copy target\saiku-reporting-core-trunk-SNAPSHOT.jar c:\TargetPlatforms\biserver-ce-4.8.0-stable\tomcat\webapps\pentaho\WEB-INF\lib\ /Y


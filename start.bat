rem java -jar -Dmail.username=xxx@163.com ^
rem -Dmail.password=email_password    ^
rem -Dmail.from=xxx@163.com ^
rem -Dmail.to=yyy@163.com   ^
rem -Dbackup.enable=true target/baishun.jar

echo %cd%
set MAIN_CLASS=org.springframework.boot.loader.JarLauncher
set JAVA_HOME=F:\jdk\jdk_jre\jre1.8.0_66
set JAVA_CMD=%JAVA_HOME%\bin\java
set JAVA_OPTS=-Dmail.username=xxx@163.com ^
				-Dmail.password=email_password    ^
				-Dmail.from=xxx@163.com ^
				-Dmail.to=yyy@163.com   ^
				-Dbackup.enable=true
rem %JAVA_CMD% -cp .;target\baishun.jar;%JAVA_HOME%\ %JAVA_OPTS% %MAIN_CLASS%
%JAVA_CMD% -cp .;G:\baishun\BaiShun\target\baishun.jar;%JAVA_HOME%\ %JAVA_OPTS% %MAIN_CLASS%
rem pause
mvn clean package spring-boot:repackage -Dmaven.test.skip=true
java -jar -Dmail.username=xxx@163.com \
-Dmail.password=email_password    \
-Dmail.from=xxx@163.com \
-Dmail.to=yyy@163.com   \
-Dbackup.enable=true target/baishun.jar
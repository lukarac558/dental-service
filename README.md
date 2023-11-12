# dental-service


To run keycloak server run [docker-compose](docker-compose.yml)<br>
It should import configuration and create 3 [accounts](auth-service/ACCOUNTS.md)<br>
<br>
Swagger: http://localhost:8080/swagger-ui.html
<br>
To push to repository mvn clean package jib:build
<br>
To use backend through docker add new line with <strong>127.0.0.1 auth-service</strong> in [hosts](C:\Windows\System32\drivers\etc\hosts) and change issuer uri in frontend config<br>
To run gateway locally you need to import auth-service certificate to jdk<br>
keytool -import -trustcacerts -cacerts -storepass changeit -noprompt -alias auth-service.pem -file cert\auth-service.crt.pem
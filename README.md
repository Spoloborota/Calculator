# Calculator
Test task for Aim Consulting.

Application based on spring-boot.<br>
To run it, build war via **mvn package** and put to the tomacat webapp directory.<br>
Or just execute **mvn spring-boot:run**.

Swagger UI for service testing:<br>
http://localhost:8080/swagger-ui.html

H2 database console:<br>
http://localhost:8080/console<br>
JDBC URL to use: jdbc:h2:mem:calculationsdb

Original task definition is [here](https://github.com/Spoloborota/Calculator/blob/master/Original_Task_Definition.docx).

Updated task:<br>
1. Передача результата выполнения запроса через CompleatableFuture.<br>
2. Поднятие результирующего сервиса в докер-контенере.<br>
3. Написать тесты, которые бы проверяли всю логику работы сервиса от контроллера до DAO слоя с реальной базой данных без моков. Также необходимо настроить maven, чтобы он во время сборки проекта с профилем it в фазе pre-integration поднимал всё необходимое, запускал тесты с постфиксом IT или IntegrationTest. В фазе post-integration все поднятые сервисы должны быть остановлены.<br>

For run integration test in docker starting postgresql and calculator service in docker use profile "integration-test". E.g.:<br>
**mvn clean verify -P integration-test**

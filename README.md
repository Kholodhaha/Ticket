!!!Для запуска программы на Linux OS используйте команду : "java -jar target/TicketAnalyzer-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/resources/tickets.json", где "target/TicketAnalyzer-1.0-SNAPSHOT-jar-with-
dependencies.jar": это путь к JAR-файлу, который вы хотите запустить. В данном случае, это JAR-файл, созданный Maven с помощью maven-shade-plugin, содержащий все необходимые зависимости; "src/main/resources/tickets.json": это аргумент, передаваемый вашему Java-приложению. Обычно, если вы используете флаг -jar, все аргументы после JAR-файла передаются программе, которая указана в манифесте JAR-файла.

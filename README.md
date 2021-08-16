# EmployeeSalaryManagement

Following are the assumptions taken while building the application
1. Currently only CSV files are needed
2. System should have java 8 installed.
3. System should have maven installed.
4. System should have internet connection and allowed to download dependencies from internet.
5. Authentication/Authorization is not required.
6. Encryption via HTTPS not necessary
7. CSV column names are case sensitive
8. id and login are case sensitive

#Design Decisions
1. Application is build in such a way that in future it can be extended for reading from Excel or other formats.
2. Three tier architecture is followed where the business logic is implemented in the service layer.

#Building the application
The project is a maven project, so it can be build by using the maven standard package goal

mvn package

#running the application
the application can be run by the command
java -jar employeesalarymanagement-0.0.1-SNAPSHOT.jar

or by maven 

mvn spring-boot:run

#How the solution can be improved?
1. By using the project lombok for generating the getters/setters, constructors, equals, hashcode methods and the logger creation
2. By Implementing automatic integration tests.
3. some query can be replaced by Spring Data JPA
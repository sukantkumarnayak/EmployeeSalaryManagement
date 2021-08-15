# EmployeeSalaryManagement

Following are the assumptions taken while building the application
1. Currently only CSV files are needed
2. System should have java 8 installed.
3. System should have maven installed.
4. System should have internet connection and allowed to download dependencies from internet.
5. Authentication/Authorization is not required.
6. Encryption via HTTPS not necessary

Design Decisions
1. Application is build in such a way that in future it can be extended for reading from Excel or other formats.
2. Three tier architecture is followed where the business logic is implemented in the service layer.

running the application

1.java -jar employeesalarymanagement-0.0.1-SNAPSHOT.jar
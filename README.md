Spring boot REST API application

Subject area: Case and Investigator(many-to-one relation)

Used technologies: Java, Spring Boot, Liquibase, MockMvc, Maven

Database: PostgreSQL

Principles of work: There are Liquibase script that creates schemas in database and filling the investigator table. 

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/3497621e-a713-4f30-967a-f6c06f174502)

Case tables are being filled from JSON file, that user uploads. (Was tested on dataFile_14.json).

CaseController Endpoints:

1. Create a Case
   
URL: /api/cases

Method: POST

Request Body: CaseDto

Description: Creates a new case using the provided details in the CaseDto object. Returns the created case with an HTTP status of 201 (Created).

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/982e366a-9887-4286-b1ea-c625699bbaf0)

2. Get a Case by ID

URL: /api/cases/{id}

Method: GET

Path Variable: id (Long)

Description: Retrieves a specific case by its ID. Returns the case if found, otherwise returns a 404 (Not Found).

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/ae035d69-5cd5-42d7-9111-32e8986bae10)

3. Update a Case

URL: /api/cases/{id}

Method: PUT

Path Variable: id (Long)

Request Body: CaseDto

Description: Updates the details of an existing case using the provided CaseDto. Returns the updated case.

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/66d13367-2c15-45ef-8c75-f7c81860631d)


4. Import Cases
   
URL: /api/cases/upload

Method: POST

Request Param: file (MultipartFile)

Description: Imports cases from a JSON file. Returns the result of the import operation.

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/11ffbde5-303e-4531-bfc7-1117b354fae8)


5. Delete a Case
    
URL: /api/cases/{id}

Method: DELETE

Path Variable: id (Long)

Description: Deletes a specific case by its ID. Returns a 204 (No Content) status on successful deletion.

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/cca19242-dd7d-4207-bfaa-5d196120fa4a)

6. Get Cases List
    
URL: /api/cases/_list

Method: POST

Request Body: { "filters": { ... }, "page": 1, "size": 10 }

Description: Retrieves a paginated list of cases based on the provided filters. Returns the list of cases and the total number of pages.

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/7800110b-8b18-464d-8b7b-784fb989060f)

7. Generate Cases Report
    
URL: /api/cases/_report

Method: POST

Request Body: { "filters": { ... } }

Description: Generates a CSV report of cases based on the provided filters. Returns the CSV file as a byte stream for download.

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/6d039690-661c-43db-9340-48a51b26cdf7)

InvestigatorController Endpoints

1. Create an Investigator

URL: /api/investigators

Method: POST

Request Body: Investigator

Description: Creates a new investigator using the provided details. Returns the created investigator with an HTTP status of 201 (Created).

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/ec4d4c7b-83d2-446e-864d-bad9b37ff032)

2. Get All Investigators
   
URL: /api/investigators

Method: GET

Description: Retrieves a paginated list of all investigators. Returns the list of investigators.

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/d52b785b-8bbb-46b9-84ee-ab7b913be1a8)

3. Update an Investigator

URL: /api/investigators/{id}

Method: PUT

Path Variable: id (Long)

Request Body: Investigator

Description: Updates the details of an existing investigator using the provided information. Returns the updated investigator.

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/a315279f-5ece-481c-b74d-5126344a77cd)

4. Delete an Investigator

URL: /api/investigators/{id}

Method: DELETE

Path Variable: id (Long)

Description: Deletes a specific investigator by its ID. Returns a 204 (No Content) status on successful deletion.

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/ca6c685e-afb9-4d42-997d-9ae7784b4d98)

5. Get an Investigator by ID
    
URL: /api/investigators/{id}

Method: GET

Path Variable: id (Long)

Description: Retrieves a specific investigator by their ID. Returns the investigator if found, otherwise returns a 404 (Not Found).

![image](https://github.com/victoriadobryden/Spring-Boot-REST-API-app/assets/65075884/282a4e71-ff60-47cf-9f8a-d96423018310)

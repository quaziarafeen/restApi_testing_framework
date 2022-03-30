# Rest API testing using Rest-Assured Java Framework
- This is repository of basic REST API testing framework
- The public API used for this example is - https://www.purgomalum.com
- This framework is developed using
    - **Rest-Assured** library for Rest API testing
    - BDD
    - Java
    - Maven

- Reporting is by **Allure**


# Required software
Java JDK 11+\
Maven installed and in your classpath\
API https://www.purgomalum.com

# How to execute the tests
We can follow two different steps:

Approach 1 - Download and Import project in IntelliJ
1. Open IntelliJ
2. Import project
3. Build Project
4. Run Maven Clean, Install and Test

Approach 2 - Using Zip and command line
1. Download Zip
2. Unzipped project
3. Go to project location using terminal
4. run commands \
   a) mvn clean\
   b) mvn test


# Generating the test report
This project is using Allure Report to automatically generate test report.
Command line to generate it in two ways:
1. mvn allure:serve: To open report in browser
2. mvn allure:report: To generate project report at target/site/allure-maven-plugin folder

Sample Report

<img width="1427" alt="Screenshot 2021-05-10 at 00 42 22" src="https://user-images.githubusercontent.com/43905401/117590739-b184a300-b128-11eb-9be9-a2f0983eaeb0.png">

# Inside API Testing Framework:
src/test/java\
This folder contains 4 packages
1. common
2. runners
3. stepDef
4. utilities

src/test/resources
1. features
2. allure.properties

## Below are instructions are how to create Rest API test framework using Rest Assured Java library

	• Maven Dependency to add,
        // rest assured
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>4.2.0</version>
            <scope>test</scope>
        </dependency>

        // BDD Cucumber
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>5.2.0</version>
        </dependency>

	• TestRunner.java file to run the BDD scenarios
	   -------
       @CucumberOptions(
         features = "src/test/resources/features"
         ,glue={"stepDef"}
         ,tags={"@FunctionTests"},
         plugin = { "pretty",
                 "html:target/cucumber/cucumber-html-report",
                 "json:target/cucumber/cucumber-json-report.json"
         },
        monochrome = true)
        @RunWith(Cucumber.class)
        public class TestRunner {}
	   -------

	• RestAssured.baseURI = BASE_URL; // to specify the basic URL of the API

	• Basic building blocks of Rest Assured - given, when, then
		
	• To get the response body:
		-------
		@Test
		public void getResponse() {
			this.response = request.when().get(this.url);
		}
		-------

	• To specify particular header value when sending the request
		RequestSpecification request = given();
		request.header("key", "value");
				
	• To validate response content type,
		○ response.then().contentType(ContentType.XML);
	
	• To validate a particular item from the response body,
		○ If equalTo is not getting detected, add this import 'import static org.hamcrest.Matchers.equalTo;'
		○ response.then().assertThat().body("expected", equalTo("actual"));	

	• Validating status code to check whether the service is working or not, before every test run, using BACKGROUND in BDD feature file.
		-------
		@Given("Purgomalum service is working")
		public void validate_service_is_working() {
		    RestAssured.baseURI = BASE_URL;
		    RequestSpecification request = given();
		    request.header(Constants.CONTENT_TYPE, "application/json");
		    int getStatusCode = response.statusCode();
		    Assert.assertEquals(getStatusCode, 200);
		}
		-------

	• To set Query Parameters:
		-------
		public void setQueryParameter(String key, String value) {
		    if (null != this.request) {
		        request.queryParam(key, value);
		    } else {
		       this.request = given().param(key, value);
		    }
		}
		-------

	• Log.java class to capture Logs at INFO, ERROR level:
        Log.INFO("Info Message");
        Log.ERROR("Error Message);


## Pros and Cons of Rest API Framework Approach

	**Pros**
	1. Helps to find bug at early stage.
    2. Takes less time to validate the functionality.
    3. Usage of BDD, which enables non-techinal person (example BA/PO), to contribute to the test case coverage.
    4. Resusablity of code, as same step defintion can be used across various scenarios.
    5. Allure reporting, which provides detailed and user friendly report to the business users.

	**Cons**
	1. Cannot figure out, how user friendly the application is.
    2. Cannot figure out, how quickly a page is getting loaded/rendered.
    3. Token based authentication is not implemented. Example- JWT.
    4. Jenkins integration is not implemeneted in the framework.
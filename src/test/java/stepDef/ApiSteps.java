package stepDef;

import common.Base;
import common.Constants;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import utilities.Log;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiSteps extends Base {

    @Given("Purgomalum service is working")
    public void validate_service_is_working() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = given();
        request.header(Constants.CONTENT_TYPE, "application/json");
        int getStatusCode = response.statusCode();
        Assert.assertEquals(getStatusCode, 200);
    }

    @Given("I have selected file type as {string}")
    public void i_have_selected_file_type_as(String fileType) {
        this.url = BASE_URL + "/service/" + fileType;
    }

    @When("I send request with data input string {string}")
    public void send_request_with_data_input_string(String inputText) {
        setQueryParameter(Constants.TEXT, inputText);
        getResponse();
    }

    @Then("I see {string} with {string} response")
    @Then("I see {string} response with entered {string} text")
    public void received_response(String fileType, String data) {
        response.then()
                .assertThat().statusCode(HttpStatus.SC_OK);
        switch (fileType) {
            case Constants.XML:
                response.then()
                        .assertThat().contentType(ContentType.XML)
                        .assertThat().rootPath("PurgoMalum").body(Constants.RESULT, equalTo(data));
                Log.INFO("XML response verified");
                break;
            case Constants.JSON:
                response.then()
                        .assertThat().contentType(ContentType.JSON)
                        .assertThat().body(Constants.RESULT, equalTo(data));
                Log.INFO("JSON response verified");
                break;
            case Constants.PLAIN:
                response.then()
                        .assertThat().contentType(ContentType.TEXT)
                        .assertThat().body(equalTo(data));
                Log.INFO("PLAIN response verified");
                break;
            default:
                Assert.assertFalse("unexpected response received", true);
                Log.ERROR("unexpected filetype received-" + fileType);
        }
    }

    @When("I send {string} with profanity request")
    public void send_request_with_profanity(String inputText) {
        this.url = BASE_URL + "/service/containsprofanity";
        setQueryParameter(Constants.TEXT, inputText);
        getResponse();
    }

    @Then("I see response as per profanity {string}")
    public void received_isProfanityTrueOrFalse(String isTextProfanity) {
        response.then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .assertThat().body(equalTo(isTextProfanity));
        Log.INFO("profanity response verified");
    }

    @When("I send request with text {string}")
    public void send_request_with_text(String inputText) {
        setQueryParameter(Constants.TEXT, inputText);
    }

    @When("I add {string} to profanity list")
    public void i_add_to_profanity_list_using(String text) {
        setQueryParameter(Constants.ADD, text);
        getResponse();
    }

    @When("I add {string} with exceed limit text {string} to profanity list")
    public void i_replaced_text_to_profanity_list_using(String add_text, String replaced_text) {
        setQueryParameter(Constants.ADD, add_text);
        setQueryParameter(Constants.FILL_TEXT, replaced_text);
        getResponse();
    }

    @Then("I see {string} with error {string} response")
    public void received_error_response(String fileType, String errorMsg) {
        response.then()
                .assertThat().statusCode(HttpStatus.SC_OK);
        switch (fileType) {
            case Constants.XML:
                response.then()
                        .assertThat().contentType(ContentType.XML)
                        .assertThat().rootPath("PurgoMalum").body(Constants.ERROR, equalTo(errorMsg));
                Log.INFO("Received error response in XML filetype");
                break;
            case Constants.JSON:
                response.then()
                        .assertThat().contentType(ContentType.JSON)
                        .assertThat().body(Constants.ERROR, equalTo(errorMsg));
                Log.INFO("Received error response in JSON filetype");
                break;
            case Constants.PLAIN:
                response.then()
                        .assertThat().contentType(ContentType.TEXT)
                        .assertThat().body(equalTo(errorMsg));
                Log.INFO("Received error response in PLAIN filetype");
                break;
            default:
                Assert.assertFalse("unexpected error response received", true);
                Log.ERROR("unexpected error response received-" + fileType);
        }
    }

}
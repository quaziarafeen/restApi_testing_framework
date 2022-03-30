package common;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Base {
    public Response response = null;
    public String url;
    public RequestSpecification request;
    public static final String BASE_URL = "https://www.purgomalum.com";


    public void getResponse() {
        this.response = request.when().get(this.url);
    }

    public void setQueryParameter(String key, String value) {
        if (null != this.request) {
            request.queryParam(key, value);
        } else {
            this.request = given().param(key, value);
        }
    }

}

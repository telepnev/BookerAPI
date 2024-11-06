package core.clients;

import core.sittings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class APIClient {
    final private String baseUrl;
    private String token;

    public APIClient() {
        this.baseUrl = determineBaseUrl();
    }

    // Определение базового URL на основе файла конфигурации
    private String determineBaseUrl() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";

        Properties properties = new Properties();
        try (InputStream input =
                     getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found: "
                        + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load configuration file: " + configFileName, e);
        }
        return properties.getProperty("baseUrl");
    }

    // Настройка базовых параметров HTTP - запросов
    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .filter(addAuthFilter());
    }

    // Получение token
    public void createToken(String username, String password) {
        String requestBody = String.format("{ \"username\": \"%s\",\"password\": \"%s\" }", username,  password);

        Response response = getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.AUTH.getPath())
                .then()
                .statusCode(200)
                .extract().response();

        // измлекаем токен из ответа
        token = response.jsonPath().getString("token");
    }

    private Filter addAuthFilter() {
        return (FilterableRequestSpecification requestSpec,
                FilterableResponseSpecification responseSpec, FilterContext ctx) -> {
            if (token != null) {
                requestSpec.header("Cookie", "token=" + token);
            }
            return ctx.next(requestSpec, responseSpec); // Продолжает выполнение
        };
    }


    // Get запрос на endpoint /ping
    public Response ping() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.PING.getPath())       // Enum для ендпоинта ping
                .then()
                .statusCode(201)
                .extract().response();
    }

    // Get запрос на endpoint /booking
    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath())       // Enum для ендпоинта booking
                .then()
                .statusCode(200)
                .extract().response();
    }

    public Response getBookingById(int bookingId) {
        return getRequestSpec()
                .pathParam("id", bookingId)
                .when()
                .get(ApiEndpoints.BOOKING.getPath() +"/{id}")       // Enum для ендпоинта booking
                .then()
                .extract().response();
    }

    public Response deleteBooking(int bookingId) {
        return getRequestSpec()
                .pathParam("id", bookingId)
                .when()
                .delete(ApiEndpoints.BOOKING.getPath() +"/{id}")
                .then()
                .log().all()
                .extract().response();
    }
}

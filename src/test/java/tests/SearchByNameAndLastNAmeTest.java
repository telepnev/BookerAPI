package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.CreatedBooking;
import core.models.NewBooking;
import helpers.HelperBooking;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Story("Booking")
public class SearchByNameAndLastNAmeTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking;
    private NewBooking newBooking;
    private HelperBooking helperBooking;
    private int newBookingId;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        newBooking = newBooking = helperBooking.createNewRandomBooking();

        String requestBody = objectMapper.writeValueAsString(newBooking);
        Response response = apiClient.createBooking(requestBody);
        String responseBody = response.asString();
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);
        newBookingId = createdBooking.getBookingid();
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    @Feature("Поиск бронирования")
    @Owner("telepneves")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Поиск бронирования по ФИО")
    public void positiveSearchByNameAndLastNameTest() throws JsonProcessingException {
        Response response = apiClient.searchByNameAndLastName(newBooking.getFirstname(), newBooking.getLastname());

        String responseBody = response.getBody().asString();
        List<CreatedBooking> bookings = objectMapper.readValue(responseBody,
                new TypeReference<List<CreatedBooking>>() {
                });

        assertThat(bookings).isNotEmpty();
        assertThat(response.statusCode()).isEqualTo(200);

        for (CreatedBooking booking : bookings) {
            Assertions.assertEquals(newBookingId, booking.getBookingid());
        }
    }


    @Test
    @Feature("Поиск бронирования")
    @Owner("telepneves")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Поиск бронирования по не существующему ФИО")
    public void negativeSearchByNameAndLastNameTest() throws JsonProcessingException {
        Response response = apiClient.searchByNameAndLastName("newBookingOne.getFirstname()", "newBookingOne.getLastname()");

        String responseBody = response.getBody().asString();
        List<CreatedBooking> bookings = objectMapper.readValue(responseBody,
                new TypeReference<List<CreatedBooking>>() {
                });

        assertThat(bookings).isEmpty();
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @Feature("Поиск бронирования")
    @Owner("telepneves")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Поиск бронирования с пустыми полями ФИО")
    public void negativeSearchWithOutNameLastNameTest() throws JsonProcessingException {
        Response response = apiClient.searchByNameAndLastName(" ", " ");

        String responseBody = response.getBody().asString();
        List<CreatedBooking> bookings = objectMapper.readValue(responseBody,
                new TypeReference<List<CreatedBooking>>() {
                });

        assertThat(bookings).isEmpty();

    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(newBookingId);

        assertThat(apiClient.getBookingById(createdBooking.getBookingid()).getStatusCode())
                .isEqualTo(404);
    }

}

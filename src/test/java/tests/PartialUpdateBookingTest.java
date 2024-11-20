package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.CreatedBooking;
import core.models.NewBooking;
import helpers.HelperBooking;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Story("Booking")
public class PartialUpdateBookingTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking;
    private NewBooking newBooking;
    private NewBooking updateBooking;
    private Random rand;
    private int newBookingId;
    private HelperBooking helperBooking;


    @BeforeEach
    public void setUp() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin", "password123");

        newBooking = helperBooking.createNewRandomBooking();

        String requestBody = objectMapper.writeValueAsString(newBooking);
        Response response = apiClient.createBooking(requestBody);
        String responseBody = response.asString();
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);
        newBookingId = createdBooking.getBookingid();
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    @Feature("Обновление бронирования")
    @Owner("telepneves")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Частичное обновление бронирования")
    public void positivePartialUpdateInfoBookingTest() throws JsonProcessingException {
        String updateName = "updateEvgen";
        String updateLastName = "updateTelepnev";

        updateBooking = NewBooking.builder()
                .firstname(updateName)
                .lastname(updateLastName)
                .build();

        Response responseBody = apiClient.partialUpdateBooking(newBookingId, updateBooking);
        String requestBody = responseBody.getBody().asString();
        Booking updateBookingInfo = objectMapper.readValue(requestBody, Booking.class);

        assertThat(responseBody.getStatusCode()).isEqualTo(200);

        assertThat(newBooking.getFirstname()).isNotEqualTo(updateBookingInfo.getFirstname());
        assertThat(newBooking.getLastname()).isNotEqualTo(updateBookingInfo.getLastname());
        assertThat(newBooking.getTotalprice()).isEqualTo(updateBookingInfo.getTotalprice());
        assertThat(newBooking.getBookingdates().getCheckin())
                .isEqualTo(updateBookingInfo.getBookingdates().getCheckin());
        assertThat(newBooking.getBookingdates().getCheckout())
                .isEqualTo(updateBookingInfo.getBookingdates().getCheckout());
        assertThat(newBooking.getTotalprice()).isEqualTo(updateBookingInfo.getTotalprice());
        assertThat(newBooking.getAdditionalneeds()).isEqualTo(updateBookingInfo.getAdditionalneeds());
    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(newBookingId);

        assertThat(apiClient.getBookingById(createdBooking.getBookingid()).getStatusCode())
                .isEqualTo(404);
    }
}

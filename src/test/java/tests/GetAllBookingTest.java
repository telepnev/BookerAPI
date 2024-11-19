package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Bookingdates;
import core.models.CreatedBooking;
import core.models.NewBooking;
import helpers.HelperBooking;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Story("Создание Бронирования")
public class GetAllBookingTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking;
    private NewBooking newBooking;
    private HelperBooking helperBooking;
    private Bookingdates dates;
    private int newBookingId;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        newBooking = helperBooking.createNewRandomBooking();

        String requestBody = objectMapper.writeValueAsString(newBooking);
        Response response = apiClient.createBooking(requestBody);
        String responseBody = response.asString();
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);
        newBookingId = createdBooking.getBookingid();

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    @Feature("Список бронирования")
    @Owner("telepneves")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получение списка бронирования")
    public void getAllBookingTest() throws JsonProcessingException {
        Response response = apiClient.getBooking();
        String responseBody = response.getBody().asString();
        List<CreatedBooking> bookings = objectMapper.readValue(responseBody,
                new TypeReference<List<CreatedBooking>>() {
                });

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(bookings).isNotEmpty();
        assertThat(createdBooking.getBookingid()).isNotNull();
        for (CreatedBooking booking : bookings) {
            assertThat(booking.getBookingid()).isGreaterThan(0);
        }
    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(newBookingId);

        assertThat(apiClient.getBookingById(createdBooking.getBookingid()).getStatusCode())
                .isEqualTo(404);
    }
}

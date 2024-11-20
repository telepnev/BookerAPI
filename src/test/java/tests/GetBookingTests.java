package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.CreatedBooking;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Story("Booking")
public class GetBookingTests {

    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Feature("Список бронирования")
    @Owner("telepneves")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получение списка бронирования")
    public void testGetBooking() throws JsonProcessingException {
        Response response = apiClient.getBooking();

        //десериализуем тело ответа в список обьектов
        String responseBody = response.getBody().asString();
        List<CreatedBooking> bookings = objectMapper.readValue(responseBody,
                new TypeReference<List<CreatedBooking>>() {
                });

        assertThat(bookings).isNotEmpty();
        assertThat(response.statusCode()).isEqualTo(200);

        for (CreatedBooking booking : bookings) {
            assertThat(booking.getBookingid()).isGreaterThan(0);
        }

    }
}

package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTests {

    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetBooking() throws JsonProcessingException {
        Response response = apiClient.getBooking();

        //десериализуем тело ответа в список обьектов
        String responseBody = response.getBody().asString();
        List<Booking> bookings = objectMapper.readValue(responseBody,
                new TypeReference<List<Booking>>() {});

        assertThat(bookings).isNotEmpty();
        assertThat(response.statusCode()).isEqualTo(200);

        for (Booking booking : bookings) {
            assertThat(booking.getBookingid()).isGreaterThan(0);
        }

    }
}

package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.CreatedBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingByIdTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin", "password123");
    }

    @Test
    public void testRandomDeleteBookingById() throws JsonProcessingException {
        List<Integer> listBookingId = new ArrayList<>();
        Random rand = new Random();
        int value = rand.nextInt(10);

        Response response = apiClient.getBooking();
        String responseBody = response.getBody().asString();
        List<CreatedBooking> bookings = objectMapper.readValue(responseBody,
                new TypeReference<List<CreatedBooking>>() {
                });

        for (CreatedBooking booking : bookings) {
            listBookingId.add(booking.getBookingid());
        }

        int geRandomBookingId = listBookingId.get(value);

        if (geRandomBookingId != 0) {
            // Delete by ID
            Response deleteResponse = apiClient.deleteBooking(geRandomBookingId);
            assertThat(deleteResponse.getStatusCode()).isEqualTo(201);
            assertThat(deleteResponse.asString()).isEqualTo("Created");
        } else {
            System.out.format("Id %d not found", geRandomBookingId);
        }

        // Checking not found booking by ID
        Response responseGetBookingByID = apiClient.getBookingById(geRandomBookingId);
        assertThat(responseGetBookingByID.getStatusCode()).isEqualTo(404);
    }
}

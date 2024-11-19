package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Bookingdates;
import core.models.CreatedBooking;
import core.models.NewBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchByNameAndLastNAmeTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking;
    private NewBooking newBooking;
    private int newBookingId;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();

        Random rand = new Random();
        int value = rand.nextInt(1000);

        newBooking = NewBooking.builder()
                .firstname("Evgen" + value)
                .lastname("Telepnev" + value)
                .totalprice(value)
                .depositpaid(false)
                .bookingdates(Bookingdates.builder()
                        .checkin("2025-01-01")
                        .checkout("2025-01-01")
                        .build())
                .additionalneeds("Beer and fish")
                .build();

        String requestBody = objectMapper.writeValueAsString(newBooking);
        Response response = apiClient.createBooking(requestBody);
        String responseBody = response.asString();
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);
        newBookingId = createdBooking.getBookingid();
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
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

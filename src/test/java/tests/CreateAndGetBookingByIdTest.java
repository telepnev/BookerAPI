package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.Bookingdates;
import core.models.CreatedBooking;
import core.models.NewBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateAndGetBookingByIdTest {
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
                        .checkin("2018-01-01")
                        .checkout("2019-01-01")
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
    public void positiveGetBookingByIdTest() throws JsonProcessingException {
        Response responseBody = apiClient.getBookingById(newBookingId);
        String requestBody = responseBody.getBody().asString();
        Booking bookingInfo = objectMapper.readValue(requestBody, Booking.class);

        assertThat(responseBody.getStatusCode()).isEqualTo(200);
        assertThat(newBooking.getFirstname()).isEqualTo(bookingInfo.getFirstname());
        assertThat(newBooking.getLastname()).isEqualTo(bookingInfo.getLastname());
        assertThat(newBooking.getTotalprice()).isEqualTo(bookingInfo.getTotalprice());
        assertThat(newBooking.getBookingdates()).isEqualTo(bookingInfo.getBookingdates());
        assertThat(newBooking.getAdditionalneeds()).isEqualTo(bookingInfo.getAdditionalneeds());

    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(createdBooking.getBookingid());

        assertThat(apiClient.getBookingById(createdBooking.getBookingid()).getStatusCode())
                .isEqualTo(404);
    }
}

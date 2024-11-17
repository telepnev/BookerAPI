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

public class PartialUpdateBookingTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking;
    private NewBooking newBooking;
    private NewBooking updateBooking;
    private Random rand;
    private int newBookingId;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin", "password123");

        rand = new Random();
        int value = rand.nextInt(1000);

        newBooking = NewBooking.builder()
                .firstname("Evgen" + value)
                .lastname("Telepnev" + value)
                .totalprice(10000)
                .depositpaid(false)
                .bookingdates(Bookingdates.builder()
                        .checkin("2024-11-28")
                        .checkout("2024-11-30")
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
        apiClient.deleteBooking(createdBooking.getBookingid());

        assertThat(apiClient.getBookingById(createdBooking.getBookingid()).getStatusCode())
                .isEqualTo(404);
    }
}

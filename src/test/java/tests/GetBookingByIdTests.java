package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingByIdTests {

    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetBookingByIdNotEmpty() throws JsonProcessingException {
        Random rand = new Random();
        int value = rand.nextInt(100);

        Response response = apiClient.getBookingById(value);
        //десериализуем тело ответа в список обьектов
        String responseBody = response.getBody().asString();
        Booking bookingsById = objectMapper.readValue(responseBody, Booking.class);

        // пока такой тест, дороботать после создания Booking
        assertThat(bookingsById.getFirstname()).isNotEmpty();
        assertThat(bookingsById.getLastname()).isNotEmpty();
        assertThat(bookingsById.getTotalprice()).isNotNull();
        assertThat(bookingsById.getDepositpaid()).isNotNull();
        assertThat(bookingsById.getBookingdates()).isNotNull();
        assertThat(bookingsById.getAdditionalneeds()).isNotEmpty();
        assertThat(bookingsById.getAdditionalneeds()).isNotEmpty();

    }
}
